package com.zoomers.GameSetMatch.scheduler.scorers;

import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;

public abstract class Scorer {

    public abstract int calculateMatchByScore(Registrant r1, Registrant r2, Timeslot t, int matchDuration);
}
