package com.zoomers.GameSetMatch.scheduler.domain;

import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentSeries;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentFormat;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import java.util.Date;

public class MockTournament {

    private final int tournamentID;
    private final TournamentFormat tournamentFormat;
    private final TournamentSeries tournamentSeries;
    private final int matchBySkill;
    private final int matchDuration;
    private final Date startDate;
    private Date roundEndDate;
    private int currentRound;
    private boolean isFinalRound;

    public MockTournament(
            int tournamentID,
            int tournamentFormat,
            int tournamentSeries,
            int matchBySkill,
            int matchDuration,
            Date startDate,
            int currentRound
    ) {
        this.tournamentID = tournamentID;
        this.tournamentFormat = TournamentFormat.values()[tournamentFormat - 1];
        this.tournamentSeries = TournamentSeries.values()[tournamentSeries - 1];
        this.matchBySkill = matchBySkill;
        this.matchDuration = matchDuration;
        this.startDate = startDate;
        this.currentRound = currentRound;
    }

    public void setRoundEndDate(Date date) {

        this.roundEndDate = date;
    }

    public void setFinalRound(boolean isFinalRound) {
        this.isFinalRound = isFinalRound;
    }

    public int getTournamentID() {
        return tournamentID;
    }

    public TournamentFormat getTournamentFormat() {
        return tournamentFormat;
    }

    public TournamentSeries getTournamentSeries() {
        return tournamentSeries;
    }

    public int getMatchBy() {
        return matchBySkill;
    }

    public Date getStartDate() {

        return startDate;
    }

    public int getMatchDuration() {
        return matchDuration;
    }

    public int getCurrentRound() {
        return currentRound;
    }
}
