package com.zoomers.GameSetMatch.controller.Match.RequestBody;

public class IncomingResults {
    private int userID;
    private int matchID;
    private String results;

    public String getResults(){
        return this.results;
    }
    
    public int getMatchID(){
        return this.matchID;
    }
    
    public int getUserID(){
        return this.userID;
    }
}
