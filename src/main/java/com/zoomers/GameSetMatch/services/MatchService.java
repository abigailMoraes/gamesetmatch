package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.entity.Match;
import com.zoomers.GameSetMatch.repository.MatchRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class MatchService {

    private MatchRepository match;

    public Optional<Match> getMatchByID(int id){
        return match.findById(id);
    }

    public void updateMatchInformation(int matchID, String startTime, String endTime){
       match.updateMatchInfo(matchID, LocalDateTime.parse(startTime), LocalDateTime.parse(endTime));
    }

}
