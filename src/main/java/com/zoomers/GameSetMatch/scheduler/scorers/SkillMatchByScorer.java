package com.zoomers.GameSetMatch.scheduler.scorers;

import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;

public class SkillMatchByScorer extends Scorer {
    @Override
    public int calculateMatchByScore(Registrant r1, Registrant r2, Timeslot t, int matchDuration) {

        int skillMultiplier = 2;
        int availabilityMultiplier = 1;

        int availabilityScore = 0;

        if (r1.checkAvailability(t.getID(), matchDuration)) {
            availabilityScore++;
        }

        if (r2.checkAvailability(t.getID(), matchDuration)) {
            availabilityScore++;
        }

        int skillScore = r1.getSkill() + r2.getSkill();

        return availabilityMultiplier * availabilityScore + skillMultiplier * skillScore;
    }
}
