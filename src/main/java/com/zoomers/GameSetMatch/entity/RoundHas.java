package com.zoomers.GameSetMatch.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "round_has")

public class RoundHas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "roundID")
    private Integer roundID;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column (name = "end_date")
    private Date end_date;

    @Column (name = "duration")
    private int duration;

    @Column (name = "type")
    private String type;

    @Column (name = "tournamentID")
    private String tournamentID;
}