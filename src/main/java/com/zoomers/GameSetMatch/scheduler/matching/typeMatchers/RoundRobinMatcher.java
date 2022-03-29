package com.zoomers.GameSetMatch.scheduler.matching.typeMatchers;

import com.zoomers.GameSetMatch.scheduler.abstraction.TypeMatcher;
import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;

public class RoundRobinMatcher extends TypeMatcher {

    @Override
    protected boolean areMatchConditionsSatisfied(Registrant r1, Registrant r2, Timeslot t) {
        return r1.hasNotPlayed(r2);
    }

    @Override
    protected int calculateMatchScore(Match match) {
        return 0;
    }
}
