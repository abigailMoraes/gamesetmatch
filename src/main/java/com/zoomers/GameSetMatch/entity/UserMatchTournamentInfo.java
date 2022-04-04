package com.zoomers.GameSetMatch.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Entity

public class UserMatchTournamentInfo {
    private Integer results;
    private String attendance;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer matchID;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @Column(name="start_time")
//    private String startTime;
    @Column(name="start_time")
    private LocalDateTime startTime;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @Column(name="end_time")
//    private String endTime;

    @Column(name="end_time")
    private LocalDateTime endTime;

    private String name;
    private String location;
    private String description;
}
