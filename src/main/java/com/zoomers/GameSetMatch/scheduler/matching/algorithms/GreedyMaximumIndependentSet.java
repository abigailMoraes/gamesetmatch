package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.graph.MatchGraph;

import java.util.*;

public class GreedyMaximumIndependentSet {

    private final Set<Match> matches;
    private PriorityQueue<Match> priorityQueue;
    private final Integer[] playerDegrees;

    public GreedyMaximumIndependentSet(Set<Match> matches, Integer[] playerDegrees) {

        this.matches = matches;
        this.playerDegrees = playerDegrees;
        buildPriorityQueue();
    }

    public HashSet<Match> findGreedyMaximumIndependentSet() {

        HashSet<Match> s = new LinkedHashSet<>();

        while (this.matches.size() != 0) {

            Match match = this.priorityQueue.poll();
            s.add(match);
            visitMatches(match);
            buildPriorityQueue();
        }

        return s;
    }

    private void buildPriorityQueue() {
        this.priorityQueue = new PriorityQueue<Match>(new Comparator<Match>() {
            @Override
            public int compare(Match m1, Match m2) {

                if (m1.getDegrees() != m2.getDegrees()) {
                    return m1.getDegrees() - m2.getDegrees();
                }
                else {
                    return m1.getMatch_id() - m2.getMatch_id();
                }
            }
        });

        for (Match m : this.matches) {

            int p1Edges = this.playerDegrees[m.getPlayers().getFirst()];
            int p2Edges = this.playerDegrees[m.getPlayers().getSecond()];
            m.setDegrees(p1Edges + p2Edges);
        }

        priorityQueue.addAll(matches);
    }

    private void visitMatches(Match match) {

        this.matches.remove(match);
        int m1p1Edges = match.getPlayers().getFirst();
        int m1p2Edges = match.getPlayers().getSecond();
        this.playerDegrees[m1p1Edges]--;
        this.playerDegrees[m1p2Edges]--;

        Set<Match> matchesToRemove = new LinkedHashSet<>();

        for (Match m2 : this.matches) {

            if (match.sharePlayers(m2) || match.shareTimeslot(m2)) {

                int m2p1Edges = m2.getPlayers().getFirst();
                int m2p2Edges = m2.getPlayers().getSecond();
                this.playerDegrees[m2p1Edges]--;
                this.playerDegrees[m2p2Edges]--;

                matchesToRemove.add(m2);
            }
        }

        this.matches.removeAll(matchesToRemove);
    }
}
