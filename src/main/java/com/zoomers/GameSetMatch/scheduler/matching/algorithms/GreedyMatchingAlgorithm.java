package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.graphs.MatchGraph;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class GreedyMatchingAlgorithm extends MatchingAlgorithm {

    public GreedyMatchingAlgorithm(MatchGraph matchGraph) {

        super(matchGraph);
        buildPriorityQueue();
    }

    @Override
    protected void visitMatches(Match match) {

        this.matchGraph.removeMatch(match);
        markMatch(match);

        // System.out.println("Adding " + match + " to Independent Set with degree " + match.getDegrees());

        Set<Match> matchesToRemove = new LinkedHashSet<>();

        for (Match m2 : this.matchGraph.getMatches()) {

            if (match.sharePlayers(m2) || (match.shareTimeslot(m2) && match.shareDate(m2))) {// && match.shareDate(m2))) {

                // System.out.println("  Removing " + m2 + " with " + match.getDegrees() + " degrees" + " from matches to check");
                matchGraph.decrementDegree(m2);
                matchesToRemove.add(m2);

            }
        }

        this.matchGraph.removeAll(matchesToRemove);

        // System.out.println("    Matches left to check " + this.matchGraph.getMatches());
    }

    protected abstract void buildPriorityQueue();
}
