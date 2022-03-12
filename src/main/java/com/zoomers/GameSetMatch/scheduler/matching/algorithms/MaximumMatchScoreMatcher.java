package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.enumerations.MatchStatus;

import java.util.*;

public class MaximumMatchScoreMatcher extends MatchingAlgorithm {

    private final Set<Match> matches;
    private PriorityQueue<Match> priorityQueue;

    public MaximumMatchScoreMatcher(
            Set<Match> matches
    ) {

        this.matches = matches;
        buildPriorityQueue();
    }

    private void buildPriorityQueue() {
        this.priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Match::getMatchScore));

        priorityQueue.addAll(matches);
    }

    public Set<Match> findRemainingMatches() {

        Set<Match> s = new LinkedHashSet<>();

        while (!this.matches.isEmpty()) {

            Match match = this.priorityQueue.poll();
            s.add(match);
            visitMatches(match);
            buildPriorityQueue();
        }

        return s;
    }

    private void visitMatches(Match match) {

        this.matches.remove(match);
        match.setMatchStatus(MatchStatus.VALID);

        System.out.println("Adding " + match + " to Scheduled Matches");

        Set<Match> matchesToRemove = new LinkedHashSet<>();

        for (Match m2 : this.matches) {

            if (match.sharePlayers(m2) || match.shareTimeslot(m2)) {

                matchesToRemove.add(m2);
                System.out.println("  Removing " + m2 + " from matches to check");
            }
        }
        this.matches.removeAll(matchesToRemove);

        System.out.println("    Matches left to check " + this.matches);
    }
}
