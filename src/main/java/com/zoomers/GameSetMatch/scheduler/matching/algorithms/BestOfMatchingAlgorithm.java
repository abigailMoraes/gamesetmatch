package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.enumerations.MatchStatus;
import com.zoomers.GameSetMatch.scheduler.graph.BestOfMatchGraph;
import com.zoomers.GameSetMatch.scheduler.graph.MatchGraph;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class BestOfMatchingAlgorithm extends MatchingAlgorithm {

    private final BestOfMatchGraph bestOfMatchGraph;
    private Set<Match> matchesToSearch;

    public BestOfMatchingAlgorithm(MatchGraph matchGraph) {
        super(matchGraph);

        this.bestOfMatchGraph = (BestOfMatchGraph) matchGraph;
    }

    @Override
    public Set<Match> findMatches() {

        Set<Match> seriesMatches = new LinkedHashSet<>();

        for (int i = 0; i < bestOfMatchGraph.getNumberOfGames() - 1; i++) {

            matchesToSearch = pruneMatches(new LinkedHashSet<>(bestOfMatchGraph.getMatches()), seriesMatches);
            buildPriorityQueue();

            while (!matchesToSearch.isEmpty()) {

                Match match = this.priorityQueue.poll();
                seriesMatches.add(match);
                visitMatches(match);
                buildPriorityQueue();
            }
        }

        return seriesMatches;
    }

    private Set<Match> pruneMatches(Set<Match> availableMatches, Set<Match> scheduledSeriesMatches) {

        availableMatches.removeAll(scheduledSeriesMatches);

        for (Match match : scheduledSeriesMatches) {

            availableMatches.removeIf(match::shareDate);
        }

        return availableMatches;
    }

    @Override
    protected void buildPriorityQueue() {

        this.priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Match::getMatchScore).reversed());

        priorityQueue.addAll(this.matchesToSearch);
    }

    @Override
    protected void visitMatches(Match match) {

        // System.out.println("  Adding " + match + " with score " + match.getMatchScore());

        this.matchesToSearch.remove(match);
        match.setMatchStatus(MatchStatus.VALID);

        Set<Match> matchesToRemove = new LinkedHashSet<>();

        for (Match m2 : this.matchesToSearch) {

            if (match.sharePlayers(m2) || match.shareTimeslot(m2)) { //match.shareDate(m2)) {

                matchesToRemove.add(m2);
                // System.out.println("  Removing " + m2 + " from matches to check");
            }
        }

        this.matchesToSearch.removeAll(matchesToRemove);

        // System.out.println("    Matches left to check " + bestOfMatchGraph.getMatches().size());
    }
}
