package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.entity.Tournament;
import com.zoomers.GameSetMatch.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class TournamentService {
    @Autowired
    private TournamentRepository tournament;

    public List<Tournament> getAllTournaments() {
        return tournament.findAll();
    }
}
