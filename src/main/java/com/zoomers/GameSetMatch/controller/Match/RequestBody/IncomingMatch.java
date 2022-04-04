package com.zoomers.GameSetMatch.controller.Match.RequestBody;

import java.time.ZonedDateTime;

public class IncomingMatch {
    private Integer matchID;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private int roundID;

    public Integer getMatchID() {
        return matchID;
    }

    public int getRoundID() {
        return roundID;
    }

    public Integer getID() {
        return this.matchID;
    }

    public ZonedDateTime getStartTime() {
        return this.startTime;
    }

    public ZonedDateTime getEndTime(){
        return this.endTime;
    }
}
