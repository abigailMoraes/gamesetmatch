package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.abstraction.graph.PrimaryMatchGraph;

import java.util.*;

public class GreedyMinimumWeightIndependentSet extends GreedyMatchingAlgorithm {

    public GreedyMinimumWeightIndependentSet(PrimaryMatchGraph matchGraph) {
        super(matchGraph);
    }

    @Override
    protected void buildPriorityQueue() {
        this.priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Match::getSkillWeight));

        this.matchGraph.setMatchDegrees();

        priorityQueue.addAll(this.matchGraph.getMatches());
    }
}
