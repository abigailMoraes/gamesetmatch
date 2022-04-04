package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.graphs.MatchGraph;
import com.zoomers.GameSetMatch.scheduler.graphs.SecondaryMatchGraph;

import java.util.*;

public class MaximumMatchScoreMatcher extends MatchingAlgorithm {

    public MaximumMatchScoreMatcher(MatchGraph matchGraph) {

        super(matchGraph);
        buildPriorityQueue();
    }

    @Override
    protected void buildPriorityQueue() {
        this.priorityQueue = new PriorityQueue<Match>((m1, m2) -> {

            if (m1.getMatchScore() != m2.getMatchScore()) {
                return m2.getMatchScore() - m1.getMatchScore();
            }

            return m1.getTimeslot().getDate().before(m2.getTimeslot().getDate()) ? -1 : 1;
        }
        );

        priorityQueue.addAll(this.matchGraph.getMatches());
    }

    @Override
    protected void visitMatches(Match match) {

        Set<Match> matchesToRemove = new LinkedHashSet<>();

        markMatch(match);

        System.out.println("Adding " + match + " to matches with score " + match.getMatchScore());

        for (Match m2 : this.matchGraph.getMatches()) {

            if (match.sharePlayers(m2)) {

                matchesToRemove.add(m2);
            }
            else if (match.shareTimeslot(m2) && match.shareDate(m2))
            {
                m2.moveToNextWeek();
            }
        }

        this.matchGraph.removeAll(matchesToRemove);
    }
}
