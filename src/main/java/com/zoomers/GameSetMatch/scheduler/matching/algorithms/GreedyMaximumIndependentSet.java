package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.graph.PrimaryMatchGraph;

import java.util.*;

public class GreedyMaximumIndependentSet extends GreedyMatchingAlgorithm {


    public GreedyMaximumIndependentSet(PrimaryMatchGraph matchGraph) {

        super(matchGraph);
    }

    @Override
    protected void buildPriorityQueue() {
        this.priorityQueue = new PriorityQueue<>((m1, m2) -> {

            if (m1.getDegrees() != m2.getDegrees()) {
                return m1.getDegrees() - m2.getDegrees();
            } else {
                return m1.getMatch_id() - m2.getMatch_id();
            }
        });

        this.matchGraph.setMatchDegrees();

        priorityQueue.addAll(this.matchGraph.getMatches());
    }
}
