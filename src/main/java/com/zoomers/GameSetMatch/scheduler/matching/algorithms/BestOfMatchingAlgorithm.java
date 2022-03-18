package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.graph.BestOfMatchGraph;
import com.zoomers.GameSetMatch.scheduler.graph.MatchGraph;

import java.util.LinkedHashSet;
import java.util.Set;

public class BestOfMatchingAlgorithm extends MatchingAlgorithm {

    private final BestOfMatchGraph bestOfMatchGraph;

    public BestOfMatchingAlgorithm(MatchGraph matchGraph) {
        super(matchGraph);

        this.bestOfMatchGraph = (BestOfMatchGraph) matchGraph;
    }

    @Override
    public Set<Match> findMatches() {

        Set<Match> seriesMatches = new LinkedHashSet<>();

        for (int i = 0; i < bestOfMatchGraph.getNumberOfGames(); i++) {
            Set<Match> matchesToSearch = pruneMatches(bestOfMatchGraph.getMatches(), seriesMatches);

        }

        return seriesMatches;
    }

    private Set<Match> pruneMatches(Set<Match> availableMatches, Set<Match> scheduledMatches) {

        return availableMatches;
    }

    @Override
    protected void buildPriorityQueue() {

    }

    @Override
    protected void visitMatches(Match match) {

    }
}
