package com.zoomers.GameSetMatch.scheduler.graphs;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;

import java.util.LinkedHashSet;
import java.util.List;

public class RoundRobinGraph extends MatchGraph {

    public RoundRobinGraph(List<Registrant> registrants, List<Timeslot> timeslots) {
        super(new LinkedHashSet<>(registrants),
                new LinkedHashSet<>(timeslots),
                new LinkedHashSet<>()
        );
    }

    @Override
    public void decrementDegree(Match m) {

    }

    @Override
    public void setMatchDegrees() {

    }
}
