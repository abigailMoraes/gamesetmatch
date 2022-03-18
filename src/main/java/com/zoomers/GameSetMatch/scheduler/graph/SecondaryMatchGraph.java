package com.zoomers.GameSetMatch.scheduler.graph;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;
import com.zoomers.GameSetMatch.scheduler.matching.util.Tuple;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SecondaryMatchGraph extends MatchGraph {

    private final int matchDuration;

    public SecondaryMatchGraph(List<Registrant> registrants, List<Timeslot> timeslots, int matchDuration) {

        super(new LinkedHashSet<>(registrants),
                new LinkedHashSet<>(timeslots),
                new LinkedHashSet<>()
        );
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
}
