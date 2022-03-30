package com.zoomers.GameSetMatch.scheduler.matching.typeMatchers;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;

public class DoubleKnockoutMatcher extends TypeMatcher {

    @Override
    protected boolean areMatchConditionsSatisfied(Registrant r1, Registrant r2, Timeslot t) {
        return r1.getLosses() == r2.getLosses();
    }

    /*@Override
    protected int calculateMatchScore(Registrant r1, Registrant r2, Timeslot t, int matchDuration) {

        int skillMultiplier = 2;
        int availabilityMultiplier = 1;

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
