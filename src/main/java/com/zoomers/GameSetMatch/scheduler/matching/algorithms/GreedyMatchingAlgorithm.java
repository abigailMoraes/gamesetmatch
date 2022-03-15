package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.enumerations.MatchStatus;
import com.zoomers.GameSetMatch.scheduler.matching.util.Tuple;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;
import java.util.Set;

public abstract class GreedyMatchingAlgorithm extends MatchingAlgorithm {

    protected final Integer[] playerDegrees;
    protected final Integer[] timeDegrees;
    protected final HashMap<Tuple, Integer> playerRepeats;
    protected final HashMap<Integer, Integer[]> timeRepeats;

    public GreedyMatchingAlgorithm(
        Set<Match> matches,
        Integer[] playerDegrees,
        Integer[] timeDegrees,
        HashMap<Tuple, Integer> playerRepeats,
        HashMap<Integer, Integer[]> timeRepeats
    ) {

        super(matches);
        this.playerDegrees = playerDegrees;
        this.timeDegrees = timeDegrees;
        this.playerRepeats = playerRepeats;
        this.timeRepeats = timeRepeats;

        buildPriorityQueue();
    }

    @Override
    protected void visitMatches(Match match) {

        this.matches.remove(match);
        match.setMatchStatus(MatchStatus.VALID);

        // System.out.println("Adding " + match + " to Independent Set with degree " + match.getDegrees());

        Set<Match> matchesToRemove = new LinkedHashSet<>();

        for (Match m2 : this.matches) {

            if (match.sharePlayers(m2) || match.shareTimeslot(m2)) {

                decrementDegree(m2);
                matchesToRemove.add(m2);
                // System.out.println("  Removing " + m2 + " from matches to check");
            }
        }
        this.matches.removeAll(matchesToRemove);

        // System.out.println("    Matches left to check " + this.matches);
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

    protected abstract void buildPriorityQueue();
}
