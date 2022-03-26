package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.abstraction.graph.BestOfMatchGraph;
import com.zoomers.GameSetMatch.scheduler.abstraction.graph.MatchGraph;

import java.util.*;

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

        for (Registrant r : registrants) {

            availableMatches.removeIf(match ->
                   (r.getID() == match.getPlayers().getFirst() ||
                    r.getID() == match.getPlayers().getSecond()) &&
                    r.getGamesToSchedule() == 0);
        }

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
        // MATCHES ARE BEING DUPLICATED
        this.matchesToSearch.remove(match);

        markMatch(match);

        Set<Match> matchesToRemove = new LinkedHashSet<>();

        for (Match m2 : this.matchesToSearch) {

            if (m2.getPlayers().getFirst() == 19) {
                boolean a = (match.shareTimeslot(m2) && match.shareDate(m2));
            }

            if (match.sharePlayers(m2) || (match.shareTimeslot(m2) && match.shareDate(m2))) {

                matchesToRemove.add(m2);
                // System.out.println("    Removing " + m2 + " from matches to check");
            }
        }

        this.matchesToSearch.removeAll(matchesToRemove);

        // System.out.println("    Matches left to check " + bestOfMatchGraph.getMatches().size());
    }
}
