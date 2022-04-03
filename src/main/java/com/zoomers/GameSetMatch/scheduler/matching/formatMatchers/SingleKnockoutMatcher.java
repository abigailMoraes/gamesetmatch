package com.zoomers.GameSetMatch.scheduler.matching.formatMatchers;

import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;

public class SingleKnockoutMatcher extends FormatMatcher {

    @Override
    protected boolean areMatchConditionsSatisfied(Registrant r1, Registrant r2, Timeslot t) {
        return true;
    }

    @Override
    protected int calculateMatchFormatScore(Registrant r1, Registrant r2) {
        return 0;
    }
}
