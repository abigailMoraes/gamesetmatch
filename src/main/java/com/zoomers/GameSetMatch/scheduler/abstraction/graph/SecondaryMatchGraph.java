/**
 * Undirected (Weighted) Graph for Secondary Scheduling
 *
 * @since 2022-03-21
 */

package com.zoomers.GameSetMatch.scheduler.abstraction.graph;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;

import java.util.LinkedHashSet;
import java.util.List;

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
