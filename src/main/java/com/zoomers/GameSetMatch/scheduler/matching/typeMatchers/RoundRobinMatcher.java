package com.zoomers.GameSetMatch.scheduler.matching.typeMatchers;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;

public class RoundRobinMatcher extends TypeMatcher {

    @Override
    protected boolean areMatchConditionsSatisfied(Registrant r1, Registrant r2, Timeslot t) {
        return r1.hasNotPlayed(r2);
    }

    /*@Override
    protected int calculateMatchScore(Registrant r1, Registrant r2, Timeslot t, int matchDuration) {

        int availabilityMultiplier = 2;
        int skillMultiplier = 1;

        int availabilityScore = 0;
        int skillScore = 0;

        if (r1.checkAvailability(t.getID()) &&
                r2.checkAvailability(t.getID())) {
            availabilityScore += 2;
        }
        else if (r1.checkAvailability(t.getID()) ||
                r2.checkAvailability(t.getID())) {
            availabilityScore++;
        }

        skillScore -= Math.abs(r1.getSkill() - r2.getSkill());

        return availabilityMultiplier * availabilityScore + skillMultiplier * skillScore;
    }*/
}
