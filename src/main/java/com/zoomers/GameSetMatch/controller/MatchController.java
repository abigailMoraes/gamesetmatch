package com.zoomers.GameSetMatch.controller;

import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingAttendance;
import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingMatch;
import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingResults;
import com.zoomers.GameSetMatch.entity.Match;
import com.zoomers.GameSetMatch.entity.Round;
import com.zoomers.GameSetMatch.entity.Tournament;
import com.zoomers.GameSetMatch.entity.UserMatchTournamentInfo;
import com.zoomers.GameSetMatch.repository.MatchRepository;
import com.zoomers.GameSetMatch.repository.RoundRepository;
import com.zoomers.GameSetMatch.repository.TournamentRepository;
import com.zoomers.GameSetMatch.repository.UserMatchTournamentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "*", maxAge = 3700)
@RequestMapping("/api")
public class MatchController {
    private final UserMatchTournamentRepository userMatchTournamentRepository;
    private final MatchRepository matchRepository;
    private final RoundRepository roundRepository;

    public MatchController(UserMatchTournamentRepository userMatchTournamentRepository, MatchRepository matchRepository, RoundRepository roundRepository, TournamentRepository tournamentRepository) {
        this.userMatchTournamentRepository = userMatchTournamentRepository;
        this.matchRepository = matchRepository;
        this.roundRepository = roundRepository;
        this.tournamentRepository = tournamentRepository;
    }


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
        return userMatchTournamentRepository.findMatchInfoByMatchID(id, uid);
    }


    @GetMapping( "/rounds/{roundID}/matches")
    List<Match> getMatchesByRoundID(@PathVariable int roundID){
        return matchRepository.getMatchesByRound(roundID);
    }

    @PutMapping("/match/userAttendance")
    public void updateAttendance(@RequestBody IncomingAttendance attendance) {
        userMatchTournamentRepository.dropOutForUser(attendance.getMatchID(), attendance.getUserID(), attendance.getAttendance());
    }

    @PutMapping("/match/userResults")
    public void updateMatchResults(@RequestBody IncomingResults results) {
        userMatchTournamentRepository.updateMatchResults(results.getMatchID(), results.getUserID(), results.getResults());
    }

    @PutMapping("/tournament/{tournamentID}/round/{roundID}")
    public void updateRoundSchedule(@PathVariable int tournamentID, @PathVariable int roundID,
                                    @RequestBody List<IncomingMatch> matches) {
        Date latestMatchDate = Date.valueOf(matches.get(0).getEndTime().substring(0,9));
        for (IncomingMatch match : matches) {
            Optional<Match> existingMatch = Optional.of(matchRepository.getById(match.getID()));
            if (existingMatch.isPresent()) {
                matchRepository.updateMatchInfo(match.getID(),
                        match.getStartTime(), match.getEndTime(), match.getDuration(), roundID);
            } else {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Match ID");
            }
            Optional<Round> existingRound = Optional.of(roundRepository.getById(roundID));
            /* Update end date for existing round if date for match end time is later than the corresponding
            round end date */
            if(latestMatchDate.compareTo(Date.valueOf(match.getEndTime().substring(0,10))) < 0){
                latestMatchDate = Date.valueOf(match.getEndTime().substring(0,10));
            }
            if(existingRound.isPresent()){
               if (!(existingRound.get().getEndDate().compareTo(latestMatchDate) == 0)){
                   existingRound.get().setEndDate(latestMatchDate);
                   }else {
                       ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Tournament ID");
                   }
               }
            }else {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Round ID");
            }
        }
    }
}
