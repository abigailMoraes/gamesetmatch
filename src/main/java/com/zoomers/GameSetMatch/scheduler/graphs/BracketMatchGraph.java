package com.zoomers.GameSetMatch.scheduler.graphs;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BracketMatchGraph extends MatchGraph {

    public BracketMatchGraph(List<Registrant> registrants, List<Timeslot> timeslots) {
        super(
                new HashSet<>(registrants),
                new LinkedHashSet<>(timeslots),
                new LinkedHashSet<>());
    }

    @Override
    public void decrementDegree(Match m) {

    }

    @Override
    public void setMatchDegrees() {

    }
}
