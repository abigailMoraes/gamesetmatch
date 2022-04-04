package com.zoomers.GameSetMatch.scheduler.scorers;

import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;

public class RandomMatchByScorer extends Scorer {

    @Override
    public int calculateMatchByScore(Registrant r1, Registrant r2, Timeslot t, int matchDuration) {

        int availabilityMultiplier = 2;
        int skillMultiplier = 1;

        int availabilityScore = 0;
        int skillScore = 0;

        int matchIndex = (int) Math.ceil(matchDuration / 30.0);
        for (int i = t.getID(); i < t.getID() + matchIndex; i++) {
            if (r1.checkAvailability(i, matchDuration)) {
                availabilityScore++;
            }

            if (r2.checkAvailability(i, matchDuration)) {
                availabilityScore++;
            }
        }

        skillScore = Math.abs(r1.getSkill() - r2.getSkill());

        return availabilityMultiplier * availabilityScore + skillMultiplier * skillScore;
    }
}
