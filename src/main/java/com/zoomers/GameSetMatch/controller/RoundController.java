package com.zoomers.GameSetMatch.controller;


import com.zoomers.GameSetMatch.controller.Round.RoundResponse;
import com.zoomers.GameSetMatch.entity.Round;
import com.zoomers.GameSetMatch.repository.RoundRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    List<RoundResponse> getTournamentRounds(@PathVariable int tournamentID) {
        return mapRoundListToResponse(this.roundRepository.getRoundsByID(tournamentID));
    }

    private RoundResponse mapRoundToResponse(Round r) {
        return new RoundResponse(r.getRoundID(),
                r.getRoundNumber(),
                r.getTournamentID(),
                r.getStartDate(),
                r.getEndDate());
    }

    private List<RoundResponse> mapRoundListToResponse(List<Round> rounds) {
        List<RoundResponse> responseResponse = new ArrayList<>();
        for(Round r : rounds) {
            responseResponse.add(mapRoundToResponse(r));
        }

        return responseResponse;
    }
}
