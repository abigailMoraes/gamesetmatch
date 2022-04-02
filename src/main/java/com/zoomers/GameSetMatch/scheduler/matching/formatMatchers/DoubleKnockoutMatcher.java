package com.zoomers.GameSetMatch.scheduler.matching.formatMatchers;

import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;

public class DoubleKnockoutMatcher extends FormatMatcher {

    @Override
    protected boolean areMatchConditionsSatisfied(Registrant r1, Registrant r2, Timeslot t) {
        return r1.getStatus() == r2.getStatus();
    }

    @Override
    protected int calculateMatchFormatScore(Registrant r1, Registrant r2) {
        return r1.hasNotPlayed(r2) ? 1 : 0;
    }
}
