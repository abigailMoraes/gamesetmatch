package com.zoomers.GameSetMatch.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


@Getter
@Setter
@ToString
@Entity

public class UserMatchTournamentInfo {
    private int results;
    private String attendance;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer matchID;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="start_time")
    private String startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="end_time")
    private String endTime;

    private String name;
    private String location;
    private String description;
}
