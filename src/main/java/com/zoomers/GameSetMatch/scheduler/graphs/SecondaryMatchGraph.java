/**
 * Undirected (Weighted) Graph for Secondary Scheduling
 *
 * @since 2022-03-21
 */

package com.zoomers.GameSetMatch.scheduler.graphs;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class SecondaryMatchGraph extends MatchGraph {

    public SecondaryMatchGraph(List<Registrant> registrants, List<Timeslot> timeslots) {

        super(new HashSet<>(registrants),
                new LinkedHashSet<>(timeslots),
                new HashSet<>()
        );
    }

    @Override
    public void decrementDegree(Match m) {

    }

    @Override
    public void setMatchDegrees() {

    }
}
