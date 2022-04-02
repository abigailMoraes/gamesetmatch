package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.enumerations.MatchStatus;
import com.zoomers.GameSetMatch.scheduler.graphs.MatchGraph;

import java.util.HashSet;
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

            // System.out.println("Matches left: " + this.matchGraph.getMatches().size());
            Match match = this.priorityQueue.poll();
            s.add(match);
            visitMatches(match);
            buildPriorityQueue();
        }

        return s;
    }

    protected void markMatch(Match match) {

        Registrant r1 = registrants.stream().filter(r -> r.getID() == match.getPlayers().getFirst()).findFirst().get();
        Registrant r2 = registrants.stream().filter(r -> r.getID() == match.getPlayers().getSecond()).findFirst().get();
        r1.decreaseGamesToSchedule();
        r2.decreaseGamesToSchedule();

        boolean r1Availability = r1.checkAvailability(match.getTimeslot().getID(), match.getMatchDuration());
        boolean r2Availability = r2.checkAvailability(match.getTimeslot().getID(), match.getMatchDuration());

        MatchStatus status;

        if (r1Availability && r2Availability) {
            status = MatchStatus.GREAT;
        }
        else if (r1Availability || r2Availability) {
            status = MatchStatus.OK;
        }
        else {
            status = MatchStatus.BAD;
        }

        match.setMatchStatus(status);
    }

    protected abstract void buildPriorityQueue();

    protected abstract void visitMatches(Match match);
}
