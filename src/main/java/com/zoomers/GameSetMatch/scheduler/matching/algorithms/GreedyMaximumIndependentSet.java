package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.Scheduler;
import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.enumerations.MatchStatus;
import com.zoomers.GameSetMatch.scheduler.matching.util.Tuple;

import java.util.*;

public class GreedyMaximumIndependentSet extends MatchingAlgorithm {

    private final Set<Match> matches;
    private PriorityQueue<Match> priorityQueue;
    private final Integer[] playerDegrees;
    private final Integer[] timeDegrees;
    private final HashMap<Tuple, Integer> playerRepeats;
    private final HashMap<Integer, Integer[]> timeRepeats;

    public GreedyMaximumIndependentSet(
            Set<Match> matches,
            Integer[] playerDegrees,
            Integer[] timeDegrees,
            HashMap<Tuple, Integer> playerRepeats,
            HashMap<Integer, Integer[]> timeRepeats
    ) {

        this.matches = matches;
        this.playerDegrees = playerDegrees;
        this.timeDegrees = timeDegrees;
        this.playerRepeats = playerRepeats;
        this.timeRepeats = timeRepeats;
        buildPriorityQueue();
    }

    public Set<Match> findGreedyMaximumIndependentSet() {

        Set<Match> s = new LinkedHashSet<>();

        while (!this.matches.isEmpty()) {

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

            m.setDegrees(Scheduler.calculateDegrees(m,
                    this.playerDegrees,
                    this.timeDegrees,
                    this.timeRepeats,
                    this.playerRepeats)
            );
        }

        priorityQueue.addAll(matches);
    }

    private void visitMatches(Match match) {

        this.matches.remove(match);
        match.setMatchStatus(MatchStatus.VALID);

        System.out.println("Adding " + match + " to Independent Set with degree " + match.getDegrees());

        Set<Match> matchesToRemove = new LinkedHashSet<>();

        for (Match m2 : this.matches) {

            if (match.sharePlayers(m2) || match.shareTimeslot(m2)) {

                decrementDegree(m2);
                matchesToRemove.add(m2);
                System.out.println("  Removing " + m2 + " from matches to check");
            }
        }
        this.matches.removeAll(matchesToRemove);

        System.out.println("    Matches left to check " + this.matches);
    }

    private void decrementDegree(Match match) {

        int m1First = match.getPlayers().getFirst();
        int m1Second = match.getPlayers().getSecond();
        int m1Time = match.getTimeslot().getID();
        this.playerDegrees[m1First]--;
        this.playerDegrees[m1Second]--;
        this.timeDegrees[m1Time]--;
        this.timeRepeats.get(m1First)[m1Time]--;
        this.timeRepeats.get(m1Second)[m1Time]--;
        this.playerRepeats.put(Tuple.of(m1First, m1Second), this.playerRepeats.get(Tuple.of(m1First, m1Second)) - 1);
    }
}
