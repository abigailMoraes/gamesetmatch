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
        return userMatchTournamentRepository.findPastMatchesByUserID(id);}

    @GetMapping("/match/{id}")
    UserMatchTournamentInfo getMatchInfoById(@PathVariable int id){
        return userMatchTournamentRepository.findMatchInfoByMatchID(id);
            }

    @PutMapping("/match/confirm/{uid}/{mid}")
    public void confirmAttendance(@RequestBody IncomingAttendance attendance, @PathVariable int uid, @PathVariable int mid){
        userMatchTournamentRepository.confirmMatchAttendanceForUser(mid,uid, attendance.getAttendance());
    }

    @PutMapping("/match/dropOut/{uid}/{mid}")
    public void dropOut(@RequestBody IncomingAttendance attendance, @PathVariable int uid, @PathVariable int mid){
        userMatchTournamentRepository.dropOutForUser(mid,uid, attendance.getAttendance());
    }
    @PutMapping("match/update/results/{uid}/{mid}")
    public void updateMatchResults(@RequestBody IncomingResults results, @PathVariable int uid, @PathVariable int mid){
        userMatchTournamentRepository.updateMatchResults(mid,uid, results.getResults());
    }
}
