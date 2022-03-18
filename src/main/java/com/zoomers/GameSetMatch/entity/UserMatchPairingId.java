package com.zoomers.GameSetMatch.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;



public class UserMatchPairingId implements Serializable {
    private Integer userID;
    private Integer matchID;

    public UserMatchPairingId(Long userID, Long matchID){
        this.userID = userID;
        this.matchID = matchID;
    }
}
