package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.enumerations.MatchStatus;
import com.zoomers.GameSetMatch.scheduler.graph.SecondaryMatchGraph;

import java.util.*;

public class MaximumMatchScoreMatcher extends MatchingAlgorithm {

    public MaximumMatchScoreMatcher(SecondaryMatchGraph matchGraph) {

        super(matchGraph);
        buildPriorityQueue();
    }

    @Override
    protected void buildPriorityQueue() {
        this.priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Match::getMatchScore));

        priorityQueue.addAll(this.matchGraph.getMatches());
    }

    @Override
    protected void visitMatches(Match match) {

        this.matchGraph.removeMatch(match);

        match.setMatchStatus(MatchStatus.VALID);

        Set<Match> matchesToRemove = new LinkedHashSet<>();

        for (Match m2 : this.matchGraph.getMatches()) {

            if (match.sharePlayers(m2) || match.shareTimeslot(m2)) {

                matchesToRemove.add(m2);
            }
        }

        this.matchGraph.removeAll(matchesToRemove);
    }
}
