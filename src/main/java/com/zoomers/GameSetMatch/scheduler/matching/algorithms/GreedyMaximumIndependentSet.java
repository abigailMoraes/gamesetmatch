package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.graphs.MatchGraph;
import com.zoomers.GameSetMatch.scheduler.domain.Match;

import java.util.*;

public class GreedyMaximumIndependentSet extends GreedyMatchingAlgorithm {


    public GreedyMaximumIndependentSet(MatchGraph matchGraph) {

        super(matchGraph);
    }

    @Override
    protected void buildPriorityQueue() {
        this.priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Match::getDegrees));

        this.matchGraph.setMatchDegrees();

        priorityQueue.addAll(this.matchGraph.getMatches());
    }
}
