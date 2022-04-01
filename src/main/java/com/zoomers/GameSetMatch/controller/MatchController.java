package com.zoomers.GameSetMatch.controller;

import com.zoomers.GameSetMatch.controller.Error.ApiError;
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
import com.zoomers.GameSetMatch.services.Errors.EntityNotFoundError;
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

    @PutMapping("/match/userAttendance")
    public void updateAttendance(@RequestBody IncomingAttendance attendance) {
        userMatchTournamentRepository.dropOutForUser(attendance.getMatchID(), attendance.getUserID(), attendance.getAttendance());
    }

    @PutMapping("/match/userResults")
    public ResponseEntity updateMatchResults(@RequestBody IncomingResults results) {
        try {
            userInvolvesMatchService.updateMatchResults(results.getMatchID(), results.getUserID(), results.getResults());

        } catch (EntityNotFoundError e){
            ApiError error = new ApiError(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<Object>(error, error.getHttpStatus());
        }
        return ResponseEntity.ok("Update successful.");
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
        }
        tournamentService.changeTournamentStatus(tournamentID, TournamentStatus.ONGOING);
    }
}
