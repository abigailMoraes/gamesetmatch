package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.Scheduler;
import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.enumerations.MatchStatus;
import com.zoomers.GameSetMatch.scheduler.matching.util.Tuple;

import java.util.*;

public class GreedyMaximumIndependentSet extends GreedyMatchingAlgorithm {

    public GreedyMaximumIndependentSet(
            Set<Match> matches,
            Integer[] playerDegrees,
            Integer[] timeDegrees,
            HashMap<Tuple, Integer> playerRepeats,
            HashMap<Integer, Integer[]> timeRepeats
    ) {

        super(matches, playerDegrees, timeDegrees, playerRepeats, timeRepeats);
    }

    @Override
    protected void buildPriorityQueue() {
        this.priorityQueue = new PriorityQueue<Match>(new Comparator<Match>() {
            @Override
            public int compare(Match m1, Match m2) {

                if (m1.getDegrees() != m2.getDegrees()) {
                    return m1.getDegrees() - m2.getDegrees();
                }
                else {
                    return m1.getMatch_id() - m2.getMatch_id();
                }
            }
        });

        for (Match m : this.matches) {

            m.setDegrees(Scheduler.calculateDegrees(m,
                    this.playerDegrees,
                    this.timeDegrees,
                    this.timeRepeats,
                    this.playerRepeats)
            );
        }

        priorityQueue.addAll(matches);
    }
}
