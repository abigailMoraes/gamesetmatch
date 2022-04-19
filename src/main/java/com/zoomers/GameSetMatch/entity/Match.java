package com.zoomers.GameSetMatch.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Match_Has")
@Getter
@Setter
@ToString

public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer matchID;

    @Column(name="start_time")
    private LocalDateTime startTime;

    @Column(name="end_time")
    private LocalDateTime endTime;

    @Column(name="roundID")

    private int roundID;

    @Column(name="match_status")
    private int matchStatus;

    @Column(name="userID_1")
    private int userID_1;

    @Column(name="userID_2")
    private int userID_2;

    @Column(name="isPublished")
    private int isPublished;

}
