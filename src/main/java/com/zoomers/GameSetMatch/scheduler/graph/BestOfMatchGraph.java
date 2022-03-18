package com.zoomers.GameSetMatch.scheduler.graph;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BestOfMatchGraph extends MatchGraph {

    private final int numberOfGames;
    private final int matchDuration;

    public BestOfMatchGraph(List<Registrant> registrants, List<Timeslot> timeslots, int numberOfGames, int matchDuration) {
        super(new LinkedHashSet<>(registrants),
                new LinkedHashSet<>(timeslots),
                new LinkedHashSet<>()
        );

        this.numberOfGames = numberOfGames;
        this.matchDuration = matchDuration;
    }

    public void addMatch(Match m) {

        matches.add(m);
    }

    @Override
    public void decrementDegree(Match m) {

    }

    @Override
    public void setMatchDegrees() {

    }

    public int getMatchDuration() {
        return matchDuration;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }
}
