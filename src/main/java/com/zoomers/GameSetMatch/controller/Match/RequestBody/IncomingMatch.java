package com.zoomers.GameSetMatch.controller.Match.RequestBody;

import java.time.LocalDateTime;

public class IncomingMatch {
    private Integer matchID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
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

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public LocalDateTime getEndTime(){
        return this.endTime;
    }
}
