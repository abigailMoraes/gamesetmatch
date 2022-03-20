package com.zoomers.GameSetMatch.scheduler.abstraction;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;
import com.zoomers.GameSetMatch.scheduler.graph.BestOfMatchGraph;
import com.zoomers.GameSetMatch.scheduler.graph.BipartiteGraph;
import com.zoomers.GameSetMatch.scheduler.graph.PrimaryMatchGraph;
import com.zoomers.GameSetMatch.scheduler.graph.SecondaryMatchGraph;

import java.util.*;

public abstract class TypeMatcher {

    public PrimaryMatchGraph createPossiblePrimaryMatches(BipartiteGraph bipartiteGraph) {

        PrimaryMatchGraph primaryMatchGraph = new PrimaryMatchGraph(bipartiteGraph);

        Map<Timeslot, List<Registrant>> bgList = bipartiteGraph.getAdjacencyList();

        for (Timeslot t : bgList.keySet()) {
            List<Registrant> registrants = bgList.get(t);
            for (int i = 0; i < registrants.size(); i++) {

                Registrant r1 = registrants.get(i);
                primaryMatchGraph.initializeTimeRepeat(r1.getID());

                for (int j = i+1; j < registrants.size(); j++) {

                    Registrant r2 = registrants.get(j);

                    if (!areMatchConditionsSatisfied(r1, r2, t)) {
                        continue;
                    }

                    Match m = new Match(
                            r1.getID(),
                            r2.getID(),
                            t,
                            bipartiteGraph.getMatchDuration(),
                            Math.abs(registrants.get(i).getSkill() - registrants.get(j).getSkill())
                    );

                    primaryMatchGraph.addMatch(m);
                }
            }
        }

        return primaryMatchGraph;
    }

    public SecondaryMatchGraph createPossibleSecondaryMatches(
            List<Registrant> registrantsToBeMatched,
            List<Timeslot> availableTimeslots,
            int matchDuration
    ){

        SecondaryMatchGraph secondaryMatchGraph = new SecondaryMatchGraph(
                registrantsToBeMatched,
                availableTimeslots,
                matchDuration
        );

        for (Timeslot t : availableTimeslots) {

            for (int i = 0; i < registrantsToBeMatched.size(); i++) {

                Registrant r1 = registrantsToBeMatched.get(i);

                for (int j = i+1; j < registrantsToBeMatched.size(); j++) {

                    Registrant r2 = registrantsToBeMatched.get(j);

                    if (!areMatchConditionsSatisfied(r1, r2, t)) {
                        continue;
                    }

                    Match m = new Match(
                            r1.getID(),
                            r2.getID(),
                            t,
                            matchDuration,
                            Math.abs(r1.getSkill() - r1.getSkill())
                    );
                    m.setMatchScore(calculateMatchScore(r1, r2, t));

                    secondaryMatchGraph.addMatch(m);
                }
            }
        }

        return secondaryMatchGraph;
    }

    public BestOfMatchGraph createPossibleBestOfMatches(
            Set<Registrant> registrants,
            Set<Timeslot> timeslots,
            Set<Match> existingMatches,
            int bestOfSeries,
            int matchDuration
    ) {

        // System.out.println("Timeslots: " + timeslots.size());
        // System.out.println("Matches: " + existingMatches.size());

        BestOfMatchGraph bestOfMatchGraph = new BestOfMatchGraph(
                registrants,
                timeslots,
                bestOfSeries,
                matchDuration
        );

        List<Registrant> registrantsList = new ArrayList<>(registrants);

        for (Match m : existingMatches) {

            for (Timeslot t : timeslots) {

                if (Objects.equals(t.getDate(), m.getTimeslot().getDate())) {
                    continue;
                }

                Registrant r1 = registrantsList.stream().filter(r -> r.getID() == m.getPlayers().getFirst()).findFirst().get();
                Registrant r2 = registrantsList.stream().filter(r -> r.getID() == m.getPlayers().getSecond()).findFirst().get();

                if (r1.getGamesToSchedule() == 0 || r2.getGamesToSchedule() == 0) {
                    continue;
                }

                Match seriesMatch = new Match(
                        m.getPlayers().getFirst(),
                        m.getPlayers().getSecond(),
                        t,
                        matchDuration,
                        1
                );

                seriesMatch.setMatchScore(calculateMatchScore(r1, r2, t));
                bestOfMatchGraph.addMatch(seriesMatch);

                /*if (calculateMatchScore(r1, r2, t) > 0) {
                }*/
            }
        }

        return bestOfMatchGraph;
    }

    private int calculateMatchScore(Registrant r1, Registrant r2, Timeslot t) {

        int matchScore = 0;
        if (r1.checkAvailability(t.getID()) &&
                r2.checkAvailability(t.getID())) {
            matchScore += 2;
        }
        else if (r1.checkAvailability(t.getID()) ||
                r2.checkAvailability(t.getID())) {
            matchScore++;
        }

        if (r1.hasNotPlayed(r2)) {
            matchScore += 2;
        }

        matchScore -= Math.abs(r1.getSkill() - r2.getSkill());

        return matchScore;
    }

    protected abstract boolean areMatchConditionsSatisfied(Registrant r1, Registrant r2, Timeslot t);
}
