package com.zoomers.GameSetMatch.scheduler.domain;

import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentSeries;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentType;

import java.util.Date;

public class MockTournament {

    private final int tournamentID;
    private final TournamentType tournamentType;
    private final TournamentSeries tournamentSeries;
    private final boolean matchBySkill;
    private final int matchDuration;
    private final Date startDate;
    private Date roundEndDate;

    public MockTournament(
            int tournamentID,
            TournamentType tournamentType,
            TournamentSeries tournamentFormat,
            boolean matchBySkill,
            int matchDuration,
            Date startDate
    ) {
        this.tournamentID = tournamentID;
        this.tournamentType = tournamentType;
        this.tournamentSeries = tournamentFormat;
        this.matchBySkill = matchBySkill;
        this.matchDuration = matchDuration;
        this.startDate = startDate;
    }

    public void setRoundEndDate(Date date) {

        this.roundEndDate = date;
    }

    public int getTournamentID() {
        return tournamentID;
    }

    public TournamentType getTournamentType() {
        return tournamentType;
    }

    public TournamentSeries getTournamentSeries() {
        return tournamentSeries;
    }

    public boolean isMatchBySkill() {
        return matchBySkill;
    }

    public Date getStartDate() {

        return startDate;
    }

    public int getMatchDuration() {
        return matchDuration;
    }
}
