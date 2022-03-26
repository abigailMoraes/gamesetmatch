package com.zoomers.GameSetMatch.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
@Entity
@Table(name = "Match_Has")
@Getter
@Setter
@ToString
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer matchID;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="start_time")
    private String startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="end_time")
    private String endTime;

    private int duration;
    private int roundID;

    @Column(name="is_conflict")
    private int isConflict;
    /*indicates whether there is a conflict in both player's attendance*/
    @Column(name = "userID_1")
    private int ID1;

    @Column(name = "userID_2")
    private int ID2;

    public Integer getMatchID() {
        return matchID;
    }

    public void setMatchID(Integer matchID) {
        this.matchID = matchID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getRoundID() {
        return roundID;
    }

    public void setRoundID(int roundID) {
        this.roundID = roundID;
    }

    public int getIsConflict() {
        return isConflict;
    }

    public void setIsConflict(int isConflict) {
        this.isConflict = isConflict;
    }

    public int getID1() {
        return ID1;
    }

    public void setID1(int ID1) {
        this.ID1 = ID1;
    }

    public int getID2() {
        return ID2;
    }

    public void setID2(int ID2) {
        this.ID2 = ID2;
    }
}
