package com.zoomers.GameSetMatch.controller.Match.RequestBody;

public class IncomingMatch {
    private Integer matchID;
    private String startTime;
    private String endTime;
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

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime(){
        return this.endTime;
    }
}
