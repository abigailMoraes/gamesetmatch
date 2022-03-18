package com.zoomers.GameSetMatch.controller;

import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingAttendance;
import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingResults;
import com.zoomers.GameSetMatch.entity.Match;
import com.zoomers.GameSetMatch.entity.UserMatchTournamentInfo;
import com.zoomers.GameSetMatch.repository.MatchRepository;
import com.zoomers.GameSetMatch.repository.UserMatchTournamentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "*", maxAge = 3700)
@RequestMapping("/api")
public class MatchController {
    private final UserMatchTournamentRepository userMatchTournamentRepository;
    private final MatchRepository matchRepository;

    public MatchController(UserMatchTournamentRepository userMatchTournamentRepository, MatchRepository matchRepository) {
        this.userMatchTournamentRepository = userMatchTournamentRepository;
        this.matchRepository = matchRepository;
    }


    @GetMapping("/match/involves/user/{id}")
    List<UserMatchTournamentInfo> getMatchesForUser(@PathVariable int id){
        return userMatchTournamentRepository.findMatchesByUserID(id);
    }

    @GetMapping("/match/history/involves/user/{id}")
    List<UserMatchTournamentInfo> getPastMatchesForUser(@PathVariable int id){
        return userMatchTournamentRepository.findPastMatchesByUserID(id);
    }

    @GetMapping("/match/{id}")
    UserMatchTournamentInfo getMatchInfoById(@PathVariable int id){
        return userMatchTournamentRepository.findMatchInfoByMatchID(id);
    }

    @PutMapping("/match/userAttendance")
    public void updateAttendance(@RequestBody IncomingAttendance attendance){
        userMatchTournamentRepository.dropOutForUser(attendance.getMatchID(), attendance.getUserID(), attendance.getAttendance());
    }
    @PutMapping("/match/userResults")
    public void updateMatchResults(@RequestBody IncomingResults results){
        userMatchTournamentRepository.updateMatchResults(results.getMatchID(),results.getUserID(), results.getResults());
    }
}
