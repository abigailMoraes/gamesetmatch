package com.zoomers.GameSetMatch.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
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
}
