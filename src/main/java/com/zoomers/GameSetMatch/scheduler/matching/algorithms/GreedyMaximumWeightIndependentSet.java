package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.Scheduler;
import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.enumerations.MatchStatus;
import com.zoomers.GameSetMatch.scheduler.matching.util.Tuple;

import java.util.*;

public class GreedyMaximumWeightIndependentSet extends GreedyMatchingAlgorithm {

    public GreedyMaximumWeightIndependentSet(
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
        this.priorityQueue = new PriorityQueue<>(new Comparator<Match>() {
            @Override
            public int compare(Match m1, Match m2) {

                return m2.getSkillWeight() - m1.getSkillWeight();
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
