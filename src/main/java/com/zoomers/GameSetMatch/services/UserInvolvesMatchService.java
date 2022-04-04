package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.controller.Match.ResponseBody.MatchDetailsForCalendar;
import com.zoomers.GameSetMatch.controller.Match.ResponseBody.UsersMatchInfo;
import com.zoomers.GameSetMatch.entity.EnumsForColumns.MatchResult;
import com.zoomers.GameSetMatch.entity.Match;
import com.zoomers.GameSetMatch.entity.Tournament;
import com.zoomers.GameSetMatch.entity.UserInvolvesMatch;
import com.zoomers.GameSetMatch.entity.UserRegistersTournament;
import com.zoomers.GameSetMatch.repository.*;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.enumerations.MatchBy;
import com.zoomers.GameSetMatch.scheduler.enumerations.PlayerStatus;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentFormat;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentSeries;
import com.zoomers.GameSetMatch.scheduler.exceptions.ScheduleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserInvolvesMatchService {
    @Autowired
    UserInvolvesMatchRepository userInvolvesMatchRepository;
    @Autowired
    MatchRepository matchRepository;
    @Autowired
    TournamentRepository tournamentRepository;
    @Autowired
    RoundRepository roundRepository;

    @Autowired
    UserRegistersTournamentRepository userRegistersTournamentRepository;

    @Transactional
    public void updateMatchResults(int matchID, int userID, int result) throws EntityNotFoundException, ScheduleException {
        // there is a match for each user, set the result to be the same in both i.e tie, player1 or player2 was the winner
        List<UserInvolvesMatch> matches = userInvolvesMatchRepository.getUserInvolvesMatchByMatchID(matchID);

        if (matches.size() == 0) {
            throw new EntityNotFoundException("Match not found in our records. Unable to update Match Results");
        }
        for (UserInvolvesMatch match : matches) {
            // tie
            if (result == MatchResult.TIE.getResult() || result == MatchResult.PENDING.getResult()) {
                match.setResults(result);
            } else {
                int opponentsResult = result == MatchResult.WIN.getResult() ? MatchResult.LOSS.getResult() : MatchResult.WIN.getResult();
                match.setResults(match.getUserID() == userID ? result : opponentsResult);
            }
        }
        userInvolvesMatchRepository.saveAll(matches);
    }

    public void updateToNewPlayerStatus(Tournament t, int userID, int roundID) throws ScheduleException {

        TournamentSeries series = TournamentSeries.values()[t.getSeries()];
        TournamentFormat format = TournamentFormat.values()[t.getFormat()];

        List<UserInvolvesMatch> matches = userInvolvesMatchRepository.getUsersMatchesForRound(roundID, userID);

        if (matches.size() == 0) {
            throw new EntityNotFoundException("Match not found in our records. Unable to update Player Status");
        }

        int matchesToWin = (int) Math.ceil(series.getNumberOfGames() / 2.0);
        int matchesWon = 0;
        int matchesLost = 0;

        for (UserInvolvesMatch m : matches) {
            if (m.getResults() == MatchResult.WIN.getResult()) {
                matchesWon++;
            } else if (m.getResults() == MatchResult.LOSS.getResult()) {
                matchesLost++;
            }
        }

        // don't update till series is over for now
        if (matchesLost + matchesWon != series.getNumberOfGames()) {
            return;
        }

        switch (format) {
            case ROUND_ROBIN:
                updateForRoundRobin(t, userID);
                break;
            case DOUBLE_KNOCKOUT:
                setDoubleKnockoutStatus(matchesWon >= matchesToWin, userID, t.getTournamentID());
                break;
            case SINGLE_BRACKET:
            case SINGLE_KNOCKOUT:
                PlayerStatus uStat = matchesWon >= matchesToWin ? PlayerStatus.SAFE : PlayerStatus.ELIMINATED;
                updatePlayerStatus(t.getTournamentID(), userID, uStat);
                break;

        }
    }

    public void setDoubleKnockoutStatus(boolean winner, int userID, int tournamentID) {
        // no need to update status if they won
        if (winner) {
            return;
        }

        List<UserRegistersTournament> uList = userRegistersTournamentRepository.getTournamentRegistrationForUser(userID, tournamentID);
        if (uList.size() != 1) {
            throw new EntityNotFoundException("User registration not found in our records. Unable to update Player Status");
        }
        UserRegistersTournament u = uList.get(0);
        PlayerStatus original = u.getPlayerStatus();

        switch (original) {
            case SAFE:
                u.setPlayerStatus(PlayerStatus.ONE_LOSS);
                break;
            case ONE_LOSS:
            case ELIMINATED:
                u.setPlayerStatus(PlayerStatus.ELIMINATED);
                break;
        }
    }

    public void updateForRoundRobin(Tournament t, int userID) throws ScheduleException {
        List<Registrant> registrants = userRegistersTournamentRepository.getSchedulerRegistrantsByTournamentID(t.getTournamentID());
        Set<Integer> registrantIDs = registrants.stream().map(Registrant::getID).collect(Collectors.toSet());
        registrants = registrants.stream().filter(r -> r.getID() == userID).collect(Collectors.toList());

        for (Registrant r : registrants) {
            r.setPlayersToPlay(new LinkedHashSet<>(registrantIDs));
            r.initCurrentStatus(
                    TournamentFormat.ROUND_ROBIN,
                    MatchBy.values()[t.getMatchBy()],
                    t.getTournamentID()
            );

            if (r.getPlayersToPlay().size() == 0) {
                updatePlayerStatus(t.getTournamentID(), r.getID(), PlayerStatus.ELIMINATED);
            }
        }
    }

    private void updatePlayerStatus(int tournamentID, int userID, PlayerStatus newStatus) {
        List<UserRegistersTournament> uList = userRegistersTournamentRepository.getTournamentRegistrationForUser(userID, tournamentID);
        if (uList.size() != 1) {
            throw new EntityNotFoundException("User registration not found in our records. Unable to update Player Status");
        }

        uList.get(0).setPlayerStatus(newStatus);
        userRegistersTournamentRepository.save(uList.get(0));
    }

    public List<MatchDetailsForCalendar> getMatchesByRoundForCalendar(int roundID) {
        List<Match> matches = matchRepository.getMatchesByRound(roundID);

        List<MatchDetailsForCalendar> returnList = new ArrayList<>();
        for (Match m : matches) {
            List<UsersMatchInfo> usersMatch = userInvolvesMatchRepository.getUsersMatchInfoForCalendar(m.getMatchID());
            MatchDetailsForCalendar matchDetailsForCalendar = new MatchDetailsForCalendar(m.getMatchID(),
                    m.getStartTime(), m.getEndTime(), m.getRoundID(), m.getMatchStatus(), m.getUserID_1(), m.getUserID_2());
            matchDetailsForCalendar.setParticipants(usersMatch);

            returnList.add(matchDetailsForCalendar);

        }
        return returnList;
    }

    public List<Integer> findMatchesForRoundWithPendingResults(int roundID) {
        return userInvolvesMatchRepository.getPendingMatches(roundID, MatchResult.PENDING.getResult());
    }

}
