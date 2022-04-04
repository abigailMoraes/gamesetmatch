package com.zoomers.GameSetMatch.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "Round_Has")
@Getter
@Setter

public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roundID;

    @Column(name="roundNumber")
    private Integer roundNumber;

    @Column(name="tournamentID")
    private Integer tournamentID;

    @Column(name="start_date")
    private Date startDate;

    @Column(name="end_date")
    private Date endDate;

}
