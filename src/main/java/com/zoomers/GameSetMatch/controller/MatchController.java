package com.zoomers.GameSetMatch.controller;

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

    @GetMapping("/match/all")
    List<Match> getAllMatches() {
        return matchRepository.findAll();
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
    public void confirmAttendance(@PathVariable int uid, @PathVariable int mid){
        userMatchTournamentRepository.confirmMatchAttendanceForUser(mid,uid);
    }

    @PutMapping("/match/dropOut/{uid}/{mid}")
    public void dropOut( @PathVariable int uid, @PathVariable int mid){
        userMatchTournamentRepository.dropOutForUser(mid,uid);
    }
}