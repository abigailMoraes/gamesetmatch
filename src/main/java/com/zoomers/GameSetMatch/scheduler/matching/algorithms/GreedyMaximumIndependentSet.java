package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.graph.MatchGraph;

import java.util.*;

public class GreedyMaximumIndependentSet {

    private final Set<Match> matches;
    private PriorityQueue<Match> priorityQueue;

    public GreedyMaximumIndependentSet(Set<Match> matches) {

        this.matches = matches;
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

        priorityQueue.addAll(matches);
    }

    private void visitMatches(Match match) {

        this.matches.remove(match);

        Set<Match> matchesToRemove = new LinkedHashSet<>();

        for (Match m2 : this.matches) {

            if (match.sharePlayers(m2)) {// || match.shareTimeslot(m2)) {

                /*for (Match m3 : this.matches) {

                    if (m2.getMatch_id() == m3.getMatch_id()) {
                        continue;
                    }

                    if (m2.sharePlayers(m3)) {// || m2.shareTimeslot(m3)) {

                        m3.decreaseDegrees();
                    }
                }*/

                matchesToRemove.add(m2);
            }
        }

        this.matches.removeAll(matchesToRemove);
    }
}
