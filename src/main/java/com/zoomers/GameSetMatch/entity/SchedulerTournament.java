package com.zoomers.GameSetMatch.entity;

import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentSeries;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentType;

import javax.persistence.*;
import java.util.Date;

@Entity
public class SchedulerTournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tournamentID;

    @Column(name = "type")
    private TournamentType tournamentType;

    @Column(name = "format")
    private TournamentSeries tournamentSeries;

    // TODO ADD MATCH BY SKILL TO DB
    private boolean matchBySkill;

    @Column(name = "match_duration")
    private int matchDuration;

    @Column(name = "start_date")
    private Date startDate;

    private Date roundEndDate;

    public SchedulerTournament() { }

    public void setId(Integer id) {
        this.tournamentID = id;
    }

    @Id
    public Integer getId() { return this.tournamentID ; }

    public void setRoundEndDate(Date date) { this.roundEndDate = date; }

    public TournamentType getTournamentType() {
        return tournamentType;
    }

    public TournamentSeries getTournamentSeries() {
        return tournamentSeries;
    }

    public boolean isMatchBySkill() {
        return matchBySkill;
    }

    public Date getStartDate() { return startDate; }

    public int getMatchDuration() {
        return matchDuration;
    }
}
