package com.zoomers.GameSetMatch.entity;

import java.io.Serializable;

public class UserMatchPairingId implements Serializable {
    private Integer userID;
    private Integer matchID;

    public UserMatchPairingId() {}

    public UserMatchPairingId(Integer userID, Integer matchID){
        this.userID = userID;
        this.matchID = matchID;
    }
}
