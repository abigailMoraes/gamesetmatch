package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.graphs.MatchGraph;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class InitialKnockoutMatchingAlgorithm extends MatchingAlgorithm{

    private int expectedMatches;

    public InitialKnockoutMatchingAlgorithm(MatchGraph matchGraph, int expectedMatches) {

        super(matchGraph);
        this.expectedMatches = expectedMatches;
    }

    @Override
    public Set<Match> findMatches() {

        Set<Match> initialKnockoutMatches = new HashSet<>();

        buildPriorityQueue();

        while (initialKnockoutMatches.size() < this.expectedMatches && !matchGraph.getMatches().isEmpty()) {

            Match match = this.priorityQueue.poll();
            initialKnockoutMatches.add(match);
            setLastMatchDate(match);
            visitMatches(match);
            buildPriorityQueue();
        }

        return initialKnockoutMatches;
    }

    @Override
    protected void buildPriorityQueue() {

        this.priorityQueue = new PriorityQueue<>((m1, m2) -> {

            if (m1.getMatchScore() != m2.getMatchScore()) {
                return m2.getMatchScore() - m1.getMatchScore();
            }

            return m1.getTimeslot().getDate().before(m2.getTimeslot().getDate()) ? -1 : 1;
        });

        priorityQueue.addAll(this.matchGraph.getMatches());
    }

    @Override
    protected void visitMatches(Match match) {

        Set<Match> matchesToRemove = new LinkedHashSet<>();

        markMatch(match);

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
