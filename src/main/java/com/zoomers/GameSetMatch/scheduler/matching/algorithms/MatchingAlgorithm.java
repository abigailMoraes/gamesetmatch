package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.graph.MatchGraph;
import com.zoomers.GameSetMatch.scheduler.graph.PrimaryMatchGraph;

import java.util.LinkedHashSet;
import java.util.PriorityQueue;
import java.util.Set;

public abstract class MatchingAlgorithm {

    protected MatchGraph matchGraph;
    protected PriorityQueue<Match> priorityQueue;

    public MatchingAlgorithm(MatchGraph matchGraph) { this.matchGraph = matchGraph; }

    public Set<Match> findMatches() {

        Set<Match> s = new LinkedHashSet<>();

        while (!this.matchGraph.getMatches().isEmpty()) {

            Match match = this.priorityQueue.poll();
            s.add(match);
            visitMatches(match);
            buildPriorityQueue();
        }

        return s;
    }

    protected abstract void buildPriorityQueue();

    protected abstract void visitMatches(Match match);
}
