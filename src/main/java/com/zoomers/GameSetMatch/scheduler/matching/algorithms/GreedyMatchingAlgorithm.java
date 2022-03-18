package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.enumerations.MatchStatus;
import com.zoomers.GameSetMatch.scheduler.graph.PrimaryMatchGraph;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class GreedyMatchingAlgorithm extends MatchingAlgorithm {

    public GreedyMatchingAlgorithm(PrimaryMatchGraph matchGraph) {

        super(matchGraph);
        buildPriorityQueue();
    }

    @Override
    protected void visitMatches(Match match) {

        this.matchGraph.removeMatch(match);
        match.setMatchStatus(MatchStatus.VALID);

        // System.out.println("Adding " + match + " to Independent Set with degree " + match.getDegrees());

        Set<Match> matchesToRemove = new LinkedHashSet<>();

        for (Match m2 : this.matchGraph.getMatches()) {

            if (match.sharePlayers(m2) || match.shareTimeslot(m2)) {

                matchGraph.decrementDegree(m2);
                matchesToRemove.add(m2);
                // System.out.println("  Removing " + m2 + " from matches to check");
            }
        }

        this.matchGraph.removeAll(matchesToRemove);

        // System.out.println("    Matches left to check " + this.matches);
    }

    protected abstract void buildPriorityQueue();
}
