package com.zoomers.GameSetMatch.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Getter
@Setter
@ToString
@Table(name = "match_has")
public class Match{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchID;
    private int result;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="start_time")
    private String startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="end_time")
    private String endTime;
    private int duration;
    private String type;
    private String name;
    private String location;
    private String description;

}
