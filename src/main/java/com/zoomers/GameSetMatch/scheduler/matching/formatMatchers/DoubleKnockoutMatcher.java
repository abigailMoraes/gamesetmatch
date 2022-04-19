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

        int priorityMultiplier = 3;
        int priorityScore = r1.getPlayersToPlay().size() != r2.getPlayersToPlay().size() ? 1 : 0;

        int hasNotPlayedScore = r1.hasNotPlayed(r2) ? 1 : 0;

        return priorityMultiplier * priorityScore + hasNotPlayedScore;
    }
}
