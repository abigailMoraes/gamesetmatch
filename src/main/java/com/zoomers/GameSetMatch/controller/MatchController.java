package com.zoomers.GameSetMatch.controller;

import com.zoomers.GameSetMatch.controller.Error.ApiException;
import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingAttendance;
import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingCheckNewMatchTime;
import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingMatch;
import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingResults;
import com.zoomers.GameSetMatch.controller.Match.ResponseBody.MatchDetailsForCalendar;
import com.zoomers.GameSetMatch.entity.Match;
import com.zoomers.GameSetMatch.entity.UserMatchTournamentInfo;
import com.zoomers.GameSetMatch.repository.MatchRepository;
import com.zoomers.GameSetMatch.repository.RoundRepository;
import com.zoomers.GameSetMatch.repository.UserMatchTournamentRepository;
import com.zoomers.GameSetMatch.entity.*;
import com.zoomers.GameSetMatch.entity.EnumsForColumns.MatchResult;
import com.zoomers.GameSetMatch.repository.*;
import com.zoomers.GameSetMatch.scheduler.enumerations.PlayerStatus;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentFormat;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentSeries;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentStatus;
import com.zoomers.GameSetMatch.scheduler.exceptions.ScheduleException;
import com.zoomers.GameSetMatch.services.Errors.ProposedMatchChangeConflictException;
import com.zoomers.GameSetMatch.services.MatchService;
import com.zoomers.GameSetMatch.services.TournamentService;
import com.zoomers.GameSetMatch.services.UserInvolvesMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "*", maxAge = 3700)
@RequestMapping("/api")
public class MatchController {
    @Autowired
    TournamentService tournamentService;
    @Autowired
    UserMatchTournamentRepository userMatchTournamentRepository;

    @Autowired
    UserInvolvesMatchService userInvolvesMatchService;

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    RoundRepository roundRepository;

    @Autowired
    MatchService matchService;

    @Autowired
    UserRegistersTournamentRepository userRegistersTournamentRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/match/involves/user/{id}")
    List<UserMatchTournamentInfo> getPublishedMatchesForUser(@PathVariable int id) {
        return userMatchTournamentRepository.findPublishedMatchesByUserID(id);
    }

    @GetMapping("/match/history/involves/user/{id}")
    List<UserMatchTournamentInfo> getPastMatchesForUser(@PathVariable int id) {
        return userMatchTournamentRepository.findPastMatchesByUserID(id);
    }

    @GetMapping("/match/{id}/{uid}")
    UserMatchTournamentInfo getMatchInfoById(@PathVariable int id, @PathVariable int uid) {
        return userMatchTournamentRepository.findMatchInfoByMatchID(id);
    }


    @GetMapping( "/rounds/{roundID}/matches")
    List<MatchDetailsForCalendar> getMatchesByRoundID(@PathVariable int roundID){
       return userInvolvesMatchService.getMatchesByRoundForCalendar(roundID);
    }

    @GetMapping("/tournament/{tournamentID}/matches")
    List<Match> getMatchesByTournamentID(@PathVariable int tournamentID){
        return matchRepository.getMatchesByTournamentID(tournamentID);
    }

    @GetMapping("/tournament/{matchID}/userMatchInfo")
    List<UserMatchTournamentRepository.IParticipantInfo>
    getMatchUserInfoByMatchID(@PathVariable int matchID){
        return userMatchTournamentRepository.getUserMatchInfoByMatchID(matchID);
    }

    @GetMapping("/tournament/round/{roundID}/match/{matchID}")
    Optional<UserMatchTournamentRepository.NumQuery>
    getNextMatchInBracketSingleElimination(@PathVariable int roundID, @PathVariable int matchID) {
        UserMatchTournamentRepository.NumQuery num = userMatchTournamentRepository.getNextMatchID(matchID, roundID);
        UserMatchTournamentRepository.NumQuery empty = new UserMatchTournamentRepository.NumQuery() {
            @Override
            public Integer getNext() {
                return null;
            }
        };
        if (Optional.ofNullable(num).isPresent()) {
            return Optional.ofNullable(userMatchTournamentRepository.getNextMatchID(matchID, roundID));
        } else {
            return Optional.of(empty);
        }
    }

    @GetMapping("/tournament/{tournamentID}/bracketMatchInfo")
    List<UserMatchTournamentRepository.IBracketMatchInfo>
    getBracketMatchInfoByTournamentID(@PathVariable int tournamentID){
       return  userMatchTournamentRepository.getBracketMatchInfoByTournamentID(tournamentID);
    }

    @GetMapping("/round/{oldRoundID}/match/{oldMatchID}/next/winner")
    Optional<UserMatchTournamentRepository.NumQuery>
    getNextWinnerMatchID(@PathVariable int oldRoundID, @PathVariable int oldMatchID){
        UserMatchTournamentRepository.WinnerID winnerID = userMatchTournamentRepository.getSeriesWinnerUserID(oldMatchID,
                oldRoundID);
        UserMatchTournamentRepository.LoserID loserID = userMatchTournamentRepository.getSeriesLoserUserID(oldMatchID,
                oldRoundID);
        UserMatchTournamentRepository.NumQuery winnerMatchID =
                userMatchTournamentRepository.getNextWinnerMatchID(oldRoundID, winnerID.getWinner(), loserID.getLoser(),
                        oldMatchID);
        UserMatchTournamentRepository.NumQuery empty = new UserMatchTournamentRepository.NumQuery() {
            @Override
            public Integer getNext() {
                return null;
            }
        };
        if (Optional.ofNullable(winnerMatchID).isPresent()) {
            return Optional.of(winnerMatchID);
        } else {
            return Optional.of(empty);
        }

    }

    @GetMapping("/round/{oldRoundID}/match/{oldMatchID}/next/winner/multiple")
    Optional<UserMatchTournamentRepository.NumQuery>
    getNextWinnerMatchIDForMultipleMatches(@PathVariable int oldRoundID, @PathVariable int oldMatchID){
        UserMatchTournamentRepository.WinnerID winnerID = userMatchTournamentRepository.getSeriesWinnerUserID(oldMatchID,
                oldRoundID);
        UserMatchTournamentRepository.LoserID loserID = userMatchTournamentRepository.getSeriesLoserUserID(oldMatchID,
                oldRoundID);
        UserMatchTournamentRepository.NumQuery winnerMatchID =
                userMatchTournamentRepository.getNextWinnerMatchIDMultipleMatchesPerRound(oldRoundID,
                        winnerID.getWinner(), loserID.getLoser(), oldMatchID);
        UserMatchTournamentRepository.NumQuery empty = new UserMatchTournamentRepository.NumQuery() {
            @Override
            public Integer getNext() {
                return null;
            }
        };
        if (Optional.ofNullable(winnerMatchID).isPresent()) {
            return Optional.of(winnerMatchID);
        } else {
            return Optional.of(empty);
        }

    }

    @GetMapping("/round/{oldRoundID}/roundNumber")
    Optional<UserMatchTournamentRepository.RoundNumber> getRoundNumberByRoundID(@PathVariable int oldRoundID){
        return Optional.of(userMatchTournamentRepository.getRoundNumber(oldRoundID));
    }

    @GetMapping("/match/{matchID}/winner")
    Optional<UserMatchTournamentRepository.WinnerName> getWinnerNameByMatchID(@PathVariable int matchID){
        return Optional.of(userMatchTournamentRepository.getWinnerName(matchID));
    }


    @GetMapping("/round/{oldRoundID}/match/{oldMatchID}/next/loser")
    Optional<UserMatchTournamentRepository.NumQuery>
    getNextLoserMatchID(@PathVariable int oldRoundID, @PathVariable int oldMatchID){
        UserMatchTournamentRepository.LoserID loserID = userMatchTournamentRepository.getSeriesLoserUserID(oldMatchID,
                oldRoundID);
        UserMatchTournamentRepository.WinnerID winnerID = userMatchTournamentRepository.getSeriesWinnerUserID(oldMatchID,
                oldRoundID);
        UserMatchTournamentRepository.NumQuery loserMatchID =
                userMatchTournamentRepository.getNextLoserMatchID(oldRoundID, winnerID.getWinner(),
                        loserID.getLoser(), oldMatchID);
        UserMatchTournamentRepository.NumQuery empty = new UserMatchTournamentRepository.NumQuery() {
            @Override
            public Integer getNext() {
                return null;
            }
        };
        if (Optional.ofNullable(loserMatchID).isPresent()) {
            return Optional.of(loserMatchID);
        } else {
            return Optional.of(empty);
        }
    }

    @PutMapping("/match/userAttendance")
    public void updateAttendance(@RequestBody IncomingAttendance attendance) {
        userMatchTournamentRepository.dropOutForUser(attendance.getMatchID(), attendance.getUserID(), attendance.getAttendance());
    }

    @PutMapping("/match/userResults")
    public ResponseEntity updateMatchResults(@RequestBody IncomingResults results) {
        try {
            userInvolvesMatchService.updateMatchResults(results.getMatchID(), results.getUserID(),
                    results.getResults());
            Optional<Tournament> tournamentQuery = null;
            Tournament tournament = null;
            tournamentQuery = tournamentService.getTournamentInfoByMatchID(results.getMatchID());
            if (tournamentQuery.isPresent()) {
                tournament = tournamentQuery.get();
            }
            int opponentID = matchRepository.findById(results.getMatchID()).get().getUserID_1() == results.getUserID()?
                    matchRepository.findById(results.getMatchID()).get().getUserID_2() :
                    matchRepository.findById(results.getMatchID()).get().getUserID_1();
            if (tournament != null) {
                if (matchIsTheLastMatchInTheSeries(results, tournament)) {
                    if (tournamentFormatIsSingleKnockoutOrSingleBracket(tournament)) {
                        updatePlayerStatusSingleKnockout(results, tournament,opponentID);
                    } else if (tournamentFormatIsDoubleKnockout(tournament)) {
                        updatePlayerStatusForDoubleKnockout(results, tournament,opponentID);
                    } else {
                        if (playerHasPlayedEveryOtherPlayerInRoundRobinTournament(tournament.getTournamentID(),
                                results.getUserID())) {
                            List<Integer> roundRobinWinners = userMatchTournamentRepository.getRoundRobinWinnerID(
                                    tournament.getTournamentID(), MatchResult.WIN.getResult(),
                                    MatchResult.TIE.getResult()
                            );
                            if (!roundRobinWinners.contains(results.getUserID())){
                                updatePlayerStatusInRoundRobin(results.getUserID(),
                                        tournament.getTournamentID(), PlayerStatus.ELIMINATED.getStatus());
                            }else{
                                userRegistersTournamentRepository.updatePlayerStatusForATournament(results.getUserID(),
                                tournament.getTournamentID(), PlayerStatus.SAFE.getStatus());
                            }
                        } else{
                            userRegistersTournamentRepository.updatePlayerStatusForATournament(results.getUserID(),
                                    tournament.getTournamentID(), PlayerStatus.SAFE.getStatus());
                        }
                    }
                }
            }
        }
        catch(EntityNotFoundException e){
                ApiException error = new ApiException(HttpStatus.NOT_FOUND, e.getMessage());
                return new ResponseEntity<Object>(error, error.getHttpStatus());
            }
            return ResponseEntity.ok("Update successful.");
    }


    private void updatePlayerStatusInRoundRobin(int userID, int tournamentID, int status) {
        userRegistersTournamentRepository.updatePlayerStatusForATournament(userID, tournamentID,status);
    }

    private boolean playerHasPlayedEveryOtherPlayerInRoundRobinTournament(Integer tournamentID, int userID) {
        UserMatchTournamentRepository.NumQuery allPlayersInTournament =
                userRegistersTournamentRepository.getPlayersInTournament(tournamentID);
        UserMatchTournamentRepository.NumQuery numberOfDistinctOpponentsFacedByPlayer =
                userMatchTournamentRepository.getNumberOfDistinctOpponentsFacedByPlayerInTournament(tournamentID,
                        userID);
       return allPlayersInTournament.getNext()-1 == numberOfDistinctOpponentsFacedByPlayer.getNext();
    }

    private boolean tournamentFormatIsDoubleKnockout(Tournament tournament) {
        return tournament.getFormat() == TournamentFormat.DOUBLE_KNOCKOUT.ordinal();
    }

    private boolean tournamentFormatIsSingleKnockoutOrSingleBracket(Tournament tournament) {
        return tournament.getFormat() == TournamentFormat.SINGLE_KNOCKOUT.ordinal()
                || tournament.getFormat() == TournamentFormat.SINGLE_BRACKET.ordinal();
    }

    private boolean matchIsTheLastMatchInTheSeries(IncomingResults results, Tournament tournament) {
        return tournament.getSeries().equals(matchRepository.getMatchNumber(results.getMatchID()).getNext());
    }

    private void updatePlayerStatusForDoubleKnockout(IncomingResults results, Tournament tournament, int opponentID) {
        List<User> remainingPlayersInTournament =
                userRepository.getRemainingPlayersWithSafeStatusInTournament(tournament.getTournamentID(),
                        PlayerStatus.SAFE.getStatus());
        if (tournament.getSeries() == TournamentSeries.BEST_OF_1.getNumberOfGames() && results.getResults() ==
                MatchResult.WIN.getResult()) {
            if (userRegistersTournamentRepository.getPlayerStatusByTournamentID(results.getUserID(),
                    tournament.getTournamentID()).get(0) != PlayerStatus.ONE_LOSS.getStatus()) {
                userRegistersTournamentRepository.updatePlayerStatusForATournament(results.getUserID(),
                        tournament.getTournamentID(), PlayerStatus.SAFE.getStatus());
            }
            if (userRegistersTournamentRepository.getPlayerStatusByTournamentID(opponentID,
                    tournament.getTournamentID()).get(0) == (PlayerStatus.SAFE.getStatus())) {
                userRegistersTournamentRepository.updatePlayerStatusForATournament(opponentID,
                        tournament.getTournamentID(), PlayerStatus.ONE_LOSS.getStatus());
            } else {
                userRegistersTournamentRepository.updatePlayerStatusForATournament(opponentID,
                        tournament.getTournamentID(), PlayerStatus.ELIMINATED.getStatus());
            }
        } else if (tournament.getSeries() == TournamentSeries.BEST_OF_1.getNumberOfGames() && results.getResults() ==
                MatchResult.TIE.getResult()) {
            if (userRegistersTournamentRepository.getPlayerStatusByTournamentID(results.getUserID(),
                    tournament.getTournamentID()).get(0) != PlayerStatus.ONE_LOSS.getStatus()) {
                userRegistersTournamentRepository.updatePlayerStatusForATournament(results.getUserID(),
                        tournament.getTournamentID(), PlayerStatus.SAFE.getStatus());
            }
            userRegistersTournamentRepository.updatePlayerStatusForATournament(opponentID,
                    tournament.getTournamentID(), PlayerStatus.SAFE.getStatus());
        } else if (tournament.getSeries() == TournamentSeries.BEST_OF_1.getNumberOfGames() && results.getResults() ==
                MatchResult.LOSS.getResult()) {
            if (userRegistersTournamentRepository.getPlayerStatusByTournamentID(results.getUserID(),
                    tournament.getTournamentID()).get(0) != PlayerStatus.ONE_LOSS.getStatus()) {
                userRegistersTournamentRepository.updatePlayerStatusForATournament(opponentID,
                        tournament.getTournamentID(), PlayerStatus.SAFE.getStatus());
            }
            if (userRegistersTournamentRepository.getPlayerStatusByTournamentID(results.getUserID(),
                    tournament.getTournamentID()).get(0) == PlayerStatus.SAFE.getStatus()) {
                userRegistersTournamentRepository.updatePlayerStatusForATournament(results.getUserID(),
                        tournament.getTournamentID(), PlayerStatus.ONE_LOSS.getStatus());
            } else {
                userRegistersTournamentRepository.updatePlayerStatusForATournament(results.getUserID(),
                        tournament.getTournamentID(), PlayerStatus.ELIMINATED.getStatus());
                userRegistersTournamentRepository.updatePlayerStatusForATournament(opponentID,
                        tournament.getTournamentID(), PlayerStatus.SAFE.getStatus());
            }
        } else {
            int seriesLoserID = userMatchTournamentRepository.getSeriesLoserUserID(results.getMatchID(),
                    matchRepository.findById(results.getMatchID()).get().getRoundID()).getLoser();
            if (userRegistersTournamentRepository.getPlayerStatusByTournamentID(seriesLoserID,
                    tournament.getTournamentID()).get(0) == (PlayerStatus.SAFE.getStatus())) {
                userRegistersTournamentRepository.updatePlayerStatusForATournament(seriesLoserID,
                        tournament.getTournamentID(), PlayerStatus.ONE_LOSS.getStatus());
            } else{
                userRegistersTournamentRepository.updatePlayerStatusForATournament(seriesLoserID,
                        tournament.getTournamentID(), PlayerStatus.ELIMINATED.getStatus());
            }
        }
    }

        private void updatePlayerStatusSingleKnockout (IncomingResults results, Tournament tournament,int opponentID){
            if (tournament.getSeries() == TournamentSeries.BEST_OF_1.getNumberOfGames() && results.getResults() ==
                    MatchResult.WIN.getResult()) {
                userRegistersTournamentRepository.updatePlayerStatusForATournament(results.getUserID(),
                        tournament.getTournamentID(), PlayerStatus.SAFE.getStatus());
                userRegistersTournamentRepository.updatePlayerStatusForATournament(opponentID,
                        tournament.getTournamentID(), PlayerStatus.ELIMINATED.getStatus());
            } else if (tournament.getSeries() == TournamentSeries.BEST_OF_1.getNumberOfGames() && results.getResults() ==
                    MatchResult.TIE.getResult()) {
                userRegistersTournamentRepository.updatePlayerStatusForATournament(results.getUserID(),
                        tournament.getTournamentID(), PlayerStatus.SAFE.getStatus());
                userRegistersTournamentRepository.updatePlayerStatusForATournament(opponentID,
                        tournament.getTournamentID(), PlayerStatus.SAFE.getStatus());
            } else if (tournament.getSeries() == TournamentSeries.BEST_OF_1.getNumberOfGames() && results.getResults() ==
                    MatchResult.LOSS.getResult()) {
                userRegistersTournamentRepository.updatePlayerStatusForATournament(results.getUserID(),
                        tournament.getTournamentID(), PlayerStatus.ELIMINATED.getStatus());
                userRegistersTournamentRepository.updatePlayerStatusForATournament(opponentID,
                        tournament.getTournamentID(), PlayerStatus.SAFE.getStatus());
            } else {
                int seriesWinnerID = userMatchTournamentRepository.getSeriesWinnerUserID(results.getMatchID(),
                        matchRepository.findById(results.getMatchID()).get().getRoundID()).getWinner();
                userRegistersTournamentRepository.updatePlayerStatusForATournament(seriesWinnerID, tournament.getTournamentID(),
                        PlayerStatus.SAFE.getStatus());
                int seriesLoserID = userMatchTournamentRepository.getSeriesLoserUserID(results.getMatchID(),
                        matchRepository.findById(results.getMatchID()).get().getRoundID()).getLoser();
                userRegistersTournamentRepository.updatePlayerStatusForATournament(seriesLoserID, tournament.getTournamentID(),
                        PlayerStatus.ELIMINATED.getStatus());
            }
        }

    @PutMapping("/tournaments/{tournamentID}/round/{roundID}")
    public ResponseEntity updateRoundSchedule(@PathVariable int tournamentID, @PathVariable int roundID,
                                    @RequestBody List<IncomingMatch> matches)  {
        try {
            matchService.updateMatchesInARound(tournamentID, roundID, matches);
            TournamentStatus newStatus = tournamentService.isEnteringFinalRound(tournamentID) ?
                    TournamentStatus.FINAL_ROUND : TournamentStatus.ONGOING;
            tournamentService.changeTournamentStatus(tournamentID, newStatus);
        } catch (EntityNotFoundException e) {
            ApiException error = new ApiException(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<Object>(error, error.getHttpStatus());
        } catch (ScheduleException e) {
            ApiException error = new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<Object>(error, error.getHttpStatus());
        }
        return ResponseEntity.ok("Update successful.");
    }

    @PostMapping("/tournaments/{tournamentID}/match/{matchID}/checkNewTime")
    public ResponseEntity<Object> checkNewMatchTime(@PathVariable int tournamentID, @PathVariable int matchID,
                                                    @RequestBody IncomingCheckNewMatchTime newMatchTime) {
        try {

            matchService.checkNewMatchTime(tournamentID, matchID, newMatchTime.getNewMatchAsAvailabilityString(),
                    newMatchTime.getDayOfWeek());

        } catch (ProposedMatchChangeConflictException e) {
            ApiException error = new ApiException(HttpStatus.BAD_REQUEST,
                    e.getMessage());
            return new ResponseEntity<Object>(error, error.getHttpStatus());
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
