package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;

import java.util.LinkedHashSet;
import java.util.PriorityQueue;
import java.util.Set;

public abstract class MatchingAlgorithm {

    protected Set<Match> matches;
    protected PriorityQueue<Match> priorityQueue;

    public MatchingAlgorithm(Set<Match> matches) {
        this.matches = matches;
    }

    public Set<Match> findMatches() {

        Set<Match> s = new LinkedHashSet<>();

        while (!this.matches.isEmpty()) {

            Match match = this.priorityQueue.poll();
            s.add(match);
            visitMatches(match);
            buildPriorityQueue();
        }

        return s;
    }

    protected abstract void buildPriorityQueue();

    protected abstract void visitMatches(Match match);
}
