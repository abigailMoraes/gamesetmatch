package com.zoomers.GameSetMatch.controller;

import com.zoomers.GameSetMatch.controller.Error.ApiException;
import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingAttendance;
import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingCheckNewMatchTime;
import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingMatch;
import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingResults;
import com.zoomers.GameSetMatch.controller.Match.ResponseBody.MatchDetailsForCalendar;
import com.zoomers.GameSetMatch.controller.Match.ResponseBody.UserMatchTournamentInfoResp;
import com.zoomers.GameSetMatch.entity.Match;
import com.zoomers.GameSetMatch.entity.UserMatchTournamentInfo;
import com.zoomers.GameSetMatch.repository.MatchRepository;
import com.zoomers.GameSetMatch.repository.RoundRepository;
import com.zoomers.GameSetMatch.repository.UserMatchTournamentRepository;
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
import java.util.ArrayList;
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

    @GetMapping("/match/involves/user/{id}")
    List<UserMatchTournamentInfoResp> getPublishedMatchesForUser(@PathVariable int id) {
        List<UserMatchTournamentInfoResp> info = mapUserMatchTournamentInfoToResponse(userMatchTournamentRepository.findPublishedMatchesByUserID(id));
        return info;
    }

    private UserMatchTournamentInfoResp mapUserMatchTournamentInfoToResponse(UserMatchTournamentInfo match) {
        return new UserMatchTournamentInfoResp(match.getResults(),
                match.getAttendance(),
                match.getMatchID(),
                match.getStartTime(),
                match.getEndTime(),
                match.getName(),
                match.getLocation(),
                match.getDescription());
    }

    private List<UserMatchTournamentInfoResp> mapUserMatchTournamentInfoToResponse(List<UserMatchTournamentInfo> matches) {
        List<UserMatchTournamentInfoResp> responseMatches = new ArrayList<>();
        for(UserMatchTournamentInfo m : matches){
            responseMatches.add(mapUserMatchTournamentInfoToResponse(m));
        }

        return responseMatches;
    }

    @GetMapping("/match/history/involves/user/{id}")
    List<UserMatchTournamentInfoResp> getPastMatchesForUser(@PathVariable int id) {
        List<UserMatchTournamentInfoResp> info =  mapUserMatchTournamentInfoToResponse(userMatchTournamentRepository.findPastMatchesByUserID(id));
        return info;
    }

    @GetMapping("/match/{id}/{uid}")
    UserMatchTournamentInfoResp getMatchInfoById(@PathVariable int id, @PathVariable int uid) {
        UserMatchTournamentInfoResp response = mapUserMatchTournamentInfoToResponse(userMatchTournamentRepository.findMatchInfoByMatchID(id));
        return response;
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
    List<UserMatchTournamentRepository.IParticipantInfo> getMatchUserInfoByMatchID(@PathVariable int matchID){
        return userMatchTournamentRepository.getUserMatchInfoByMatchID(matchID);
    }

    @GetMapping("/tournament/round/{roundID}/match/{matchID}")
    Optional<UserMatchTournamentRepository.NumQuery> getNextMatchInBracketSingleElimination(@PathVariable int roundID, @PathVariable int matchID) {
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
    List<UserMatchTournamentRepository.IBracketMatchInfo> getBracketMatchInfoByTournamentID(@PathVariable int tournamentID){
       return  userMatchTournamentRepository.getBracketMatchInfoByTournamentID(tournamentID);
    }

    @GetMapping("/round/{oldRoundID}/match/{oldMatchID}/next/winner")
    Optional<UserMatchTournamentRepository.NumQuery> getNextWinnerMatchID(@PathVariable int oldRoundID, @PathVariable int oldMatchID){
        UserMatchTournamentRepository.WinnerID winnerID = userMatchTournamentRepository.getWinnerUserID(oldMatchID, oldRoundID);
        UserMatchTournamentRepository.LoserID loserID = userMatchTournamentRepository.getLoserUserID(oldMatchID, oldRoundID);
        UserMatchTournamentRepository.NumQuery winnerMatchID = userMatchTournamentRepository.getNextWinnerMatchID(oldRoundID, winnerID.getWinner(), loserID.getLoser(), oldMatchID);

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
    Optional<UserMatchTournamentRepository.NumQuery> getNextWinnerMatchIDForMultipleMatches(@PathVariable int oldRoundID, @PathVariable int oldMatchID){
        UserMatchTournamentRepository.WinnerID winnerID = userMatchTournamentRepository.getWinnerUserID(oldMatchID, oldRoundID);
        UserMatchTournamentRepository.LoserID loserID = userMatchTournamentRepository.getLoserUserID(oldMatchID, oldRoundID);
        UserMatchTournamentRepository.NumQuery winnerMatchID = userMatchTournamentRepository.getNextWinnerMatchIDMultipleMatchesPerRound(oldRoundID,
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
    Optional<UserMatchTournamentRepository.NumQuery> getNextLoserMatchID(@PathVariable int oldRoundID, @PathVariable int oldMatchID){
        UserMatchTournamentRepository.LoserID loserID = userMatchTournamentRepository.getLoserUserID(oldMatchID, oldRoundID);
        UserMatchTournamentRepository.WinnerID winnerID = userMatchTournamentRepository.getWinnerUserID(oldMatchID, oldRoundID);
        UserMatchTournamentRepository.NumQuery loserMatchID = userMatchTournamentRepository.getNextLoserMatchID(oldRoundID, winnerID.getWinner(),
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
            userInvolvesMatchService.updateMatchResults(results.getMatchID(), results.getUserID(), results.getResults());

        } catch (EntityNotFoundException e){
            ApiException error = new ApiException(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<Object>(error, error.getHttpStatus());
        } catch (ScheduleException e) {
            ApiException error = new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<Object>(error, error.getHttpStatus());
        }
        return ResponseEntity.ok("Update successful.");
    }

    @PutMapping("/tournaments/{tournamentID}/round/{roundID}")
    public ResponseEntity publishRoundSchedule(@PathVariable int tournamentID, @PathVariable int roundID,
                                               @RequestBody List<IncomingMatch> matches) {
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

            matchService.checkNewMatchTime(tournamentID, matchID, newMatchTime.getNewMatchAsAvailabilityString(), newMatchTime.getDayOfWeek());

        } catch (ProposedMatchChangeConflictException e) {
            ApiException error = new ApiException(HttpStatus.BAD_REQUEST,
                    e.getMessage());
            return new ResponseEntity<Object>(error, error.getHttpStatus());
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
