package com.zoomers.GameSetMatch.controller.Match.RequestBody;

public class IncomingResults {
    private int userID;
    private int matchID;
    private Integer results;

    public Integer getResults(){
        return this.results;
    }
    
    public int getMatchID(){
        return this.matchID;
    }
    
    public int getUserID(){
        return this.userID;
    }
}
