package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.enumerations.MatchStatus;
import com.zoomers.GameSetMatch.scheduler.graph.MatchGraph;

import java.util.LinkedHashSet;
import java.util.PriorityQueue;
import java.util.Set;

public abstract class MatchingAlgorithm {

    protected MatchGraph matchGraph;
    protected PriorityQueue<Match> priorityQueue;
    protected Set<Registrant> registrants;

    public MatchingAlgorithm(MatchGraph matchGraph) {
        this.matchGraph = matchGraph;
        this.registrants = this.matchGraph.getRegistrants();
    }

    public Set<Match> findMatches() {

        Set<Match> s = new LinkedHashSet<>();

        while (!this.matchGraph.getMatches().isEmpty()) {

            Match match = this.priorityQueue.poll();
            s.add(match);
            visitMatches(match);
            buildPriorityQueue();
        }

        return s;
    }

    protected void markMatch(Match match) {

        match.setMatchStatus(MatchStatus.VALID);

        Registrant r1 = registrants.stream().filter(r -> r.getID() == match.getPlayers().getFirst()).findFirst().get();
        Registrant r2 = registrants.stream().filter(r -> r.getID() == match.getPlayers().getSecond()).findFirst().get();
        r1.decreaseGamesToSchedule();
        r2.decreaseGamesToSchedule();
    }

    protected abstract void buildPriorityQueue();

    protected abstract void visitMatches(Match match);
}
