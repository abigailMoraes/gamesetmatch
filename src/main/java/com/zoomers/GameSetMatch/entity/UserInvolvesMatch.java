package com.zoomers.GameSetMatch.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "User_involves_match")


@IdClass(UserMatchPairingId.class)
public class UserInvolvesMatch {
    @Id
    private Integer userID;

    @Id
    private Integer matchID;

    String results;

    String attendance;

    public Integer getUserID() {
        return this.userID;
    }

    public void setUserID(Integer id) {
        this.userID = id;
    }

    public Integer getMatchID() {
        return this.matchID;
    }

    public void setMatchID(Integer id) {
        this.matchID = id;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }
}
