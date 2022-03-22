package com.zoomers.GameSetMatch.controller.Match.RequestBody;

public class IncomingMatch {
    private Integer matchID;
    private String startTime;
    private String endTime;
    private String location;
    private int duration;
    private int roundID;

    public Integer getMatchID() {
        return matchID;
    }

    public int getRoundID() {
        return roundID;
    }

    public String getLocation() {
        return location;
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
    public int getDuration(){
        return this.duration;
    }



}
