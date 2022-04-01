package com.zoomers.GameSetMatch.controller;

import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingAttendance;
import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingMatch;
import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingResults;
import com.zoomers.GameSetMatch.controller.Match.ResponseBody.MatchDetailsForCalendar;
import com.zoomers.GameSetMatch.entity.Match;
import com.zoomers.GameSetMatch.entity.Round;
import com.zoomers.GameSetMatch.entity.UserMatchTournamentInfo;
import com.zoomers.GameSetMatch.repository.MatchRepository;
import com.zoomers.GameSetMatch.repository.RoundRepository;
import com.zoomers.GameSetMatch.repository.UserMatchTournamentRepository;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentStatus;
import com.zoomers.GameSetMatch.services.TournamentService;
import com.zoomers.GameSetMatch.services.UserInvolvesMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @GetMapping("/match/involves/user/{id}")
    List<UserMatchTournamentInfo> getMatchesForUser(@PathVariable int id) {
        return userMatchTournamentRepository.findMatchesByUserID(id);
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
        UserMatchTournamentRepository.WinnerID winnerID = userMatchTournamentRepository.getWinnerUserID(oldMatchID);
        UserMatchTournamentRepository.NumQuery winnerMatchID =
                userMatchTournamentRepository.getNextWinnerMatchID(oldRoundID, winnerID.getWinner(), oldMatchID);
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
        UserMatchTournamentRepository.LoserID loserID = userMatchTournamentRepository.getLoserUserID(oldMatchID);
        UserMatchTournamentRepository.NumQuery loserMatchID =
                userMatchTournamentRepository.getNextLoserMatchID(oldRoundID, loserID.getLoser(), oldMatchID);
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
    public void updateMatchResults(@RequestBody IncomingResults results) {
        userInvolvesMatchService.updateMatchResults(results.getMatchID(), results.getUserID(), results.getResults());
    }

    @PutMapping("/tournaments/{tournamentID}/round/{roundID}")
    public void updateRoundSchedule(@PathVariable int tournamentID, @PathVariable int roundID,
                                    @RequestBody List<IncomingMatch> matches) {
        LocalDateTime latestMatchDate =matches.get(0).getEndTime();
        for (IncomingMatch match : matches) {
            Optional<Match> existingMatch = Optional.of(matchRepository.getById(match.getID()));

            if (existingMatch.isPresent()) {
                matchRepository.updateMatchInfo(match.getID(),
                        match.getStartTime(), match.getEndTime());
            } else {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Match ID");
            }
            Optional<Round> existingRound = Optional.of(roundRepository.getById(roundID));
            /* Update end date for existing round if date for match end time is later than the corresponding
            round end date */
            if(latestMatchDate.isBefore(match.getEndTime())){
                latestMatchDate = match.getEndTime();
            }
            else {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Round ID");
            }
        }
        tournamentService.changeTournamentStatus(tournamentID, TournamentStatus.ONGOING);
    }
}
