package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.graphs.MatchGraph;

import java.util.Comparator;
import java.util.PriorityQueue;

public class GreedySeededIndependentSet extends GreedyMatchingAlgorithm {

    public GreedySeededIndependentSet(MatchGraph matchGraph) {
        super(matchGraph);
    }

    @Override
    protected void buildPriorityQueue() {
        this.priorityQueue = new PriorityQueue<>((m1, m2) -> {

            if (m1.getSkillWeight() != m2.getSkillWeight()) {
                return m2.getSkillWeight() - m1.getSkillWeight();
            }

            return (int) ((m2.getTimeslot().getTime() - m1.getTimeslot().getTime()) * 2);
        });

        this.matchGraph.setMatchDegrees();

        priorityQueue.addAll(this.matchGraph.getMatches());
    }
}
