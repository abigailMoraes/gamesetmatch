package com.zoomers.GameSetMatch.controller;


import com.zoomers.GameSetMatch.entity.Round;
import com.zoomers.GameSetMatch.repository.RoundRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3700)
@RequestMapping("/api")
public class RoundController {
    private final RoundRepository roundRepository;

    public RoundController(RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    @GetMapping("/tournaments/{tournamentID}/rounds")
    List<Round> getTournamentRounds(@PathVariable int tournamentID) {
        return this.roundRepository.getRoundsByID(tournamentID);
    }

}
