package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.controller.Match.ResponseBody.MatchDetailsForCalendar;
import com.zoomers.GameSetMatch.controller.Match.ResponseBody.UsersMatchInfo;
import com.zoomers.GameSetMatch.entity.Match;
import com.zoomers.GameSetMatch.entity.UserInvolvesMatch;
import com.zoomers.GameSetMatch.repository.MatchRepository;
import com.zoomers.GameSetMatch.repository.UserInvolvesMatchRepository;
import com.zoomers.GameSetMatch.entity.EnumsForColumns.MatchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserInvolvesMatchService {
    @Autowired
    UserInvolvesMatchRepository userInvolvesMatchRepository;
    @Autowired
    MatchRepository matchRepository;

    @Transactional
    public void updateMatchResults(int matchID, int userID, int result) throws EntityNotFoundException {
        // there is a match for each user, set the result to be the same in both i.e tie, player1 or player2 was the winner
        List<UserInvolvesMatch> matches = userInvolvesMatchRepository.getUserInvolvesMatchByMatchID(matchID);
        if(matches.size() == 0) {
            throw new EntityNotFoundException("Match not found in our records. Unable to update.");
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
