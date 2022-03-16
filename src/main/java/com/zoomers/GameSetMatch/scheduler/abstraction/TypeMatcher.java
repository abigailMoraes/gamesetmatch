package com.zoomers.GameSetMatch.scheduler.abstraction;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;
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

                Registrant r_i = registrants.get(i);
                primaryMatchGraph.initializeTimeRepeat(r_i.getID());

                for (int j = i+1; j < registrants.size(); j++) {

                    Registrant r_j = registrants.get(j);

                    if (!areMatchConditionsSatisfied(r_i, r_j, t)) {
                        continue;
                    }

                    Match m = new Match(
                            r_i.getID(),
                            r_j.getID(),
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

                Registrant r_i = registrantsToBeMatched.get(i);

                for (int j = i+1; j < registrantsToBeMatched.size(); j++) {

                    Registrant r_j = registrantsToBeMatched.get(j);

                    if (!areMatchConditionsSatisfied(r_i, r_j, t)) {
                        continue;
                    }

                    Match m = new Match(
                            r_i.getID(),
                            r_j.getID(),
                            t,
                            matchDuration,
                            Math.abs(r_i.getSkill() - r_j.getSkill())
                    );
                    m.setMatchScore(calculateMatchScore(r_i, r_j, t));

                    secondaryMatchGraph.addMatch(m);
                }
            }
        }

        return secondaryMatchGraph;
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

        /*if (r1.hasNotPlayed(r2)) {
            matchScore += 2;
        }*/

        matchScore -= Math.abs(r1.getSkill() - r2.getSkill());

        return matchScore;
    }

    protected abstract boolean areMatchConditionsSatisfied(Registrant r1, Registrant r2, Timeslot t);
}
