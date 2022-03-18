package com.zoomers.GameSetMatch.controller.Match.RequestBody;

public class IncomingAttendance {
    private int matchID;
    private int userID;
    private String attendance;

    public String getAttendance(){
        return this.attendance;
    }
    
    public int getMatchID(){
        return this.matchID;
        
    }
    
    public int getUserID(){
        return this.userID;   
    }

}
