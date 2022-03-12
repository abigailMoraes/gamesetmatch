package com.zoomers.GameSetMatch.controller;

import com.zoomers.GameSetMatch.entity.Match;
import com.zoomers.GameSetMatch.repository.MatchRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/match")
public class MatchController {
    private final MatchRepository matchRepository;

    public MatchController(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @GetMapping("/all")
    List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @GetMapping("/involves/user/{id}")
    List<Match> getMatchesForUser(@PathVariable int id){
        return matchRepository.findMatchesByUserID(id);
    }

    @GetMapping("/history/involves/user/{id}")
    List<Match> getPastMatchesForUser(@PathVariable int id){
        return matchRepository.findPastMatchesByUserID(id);}

    @GetMapping("/match/{id}")
    Match getMatchInfoById(@PathVariable int id){
        return matchRepository.findMatchInfoByMatchID(id);
            }

}