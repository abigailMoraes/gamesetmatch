package com.zoomers.GameSetMatch.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoomers.GameSetMatch.scheduler.domain.MockTournament;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "match_has")
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
    @Column(name="userID_1")
    private int userID_1;
    @Column(name="userID_2")
    private int userID_2;
    /*indicates whether there is a conflict in both player's attendance*/
}
