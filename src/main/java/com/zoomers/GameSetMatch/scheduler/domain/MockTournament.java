package com.zoomers.GameSetMatch.scheduler.domain;

import com.zoomers.GameSetMatch.scheduler.enumerations.MatchBy;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentFormat;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentSeries;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentStatus;

import java.util.Date;

public class MockTournament {

    private final int tournamentID;
    private final TournamentFormat tournamentFormat;
    private final TournamentSeries tournamentSeries;
    private final MatchBy matchBy;
    private final int matchDuration;
    private final Date startDate;
    private Date roundEndDate;
    private int currentRound;
    private TournamentStatus tournamentStatus;

    public MockTournament(
            int tournamentID,
            int tournamentFormat,
            int tournamentSeries,
            int matchBy,
            int matchDuration,
            Date startDate,
            int previousRound
    ) {
        this.tournamentID = tournamentID;
        this.tournamentFormat = TournamentFormat.values()[tournamentFormat - 1];
        this.tournamentSeries = TournamentSeries.values()[tournamentSeries - 1];
        this.matchBy = MatchBy.values()[matchBy - 1];
        this.matchDuration = matchDuration;
        this.startDate = startDate;
        this.currentRound = previousRound + 1;
    }

    public void setRoundEndDate(Date date) {

        this.roundEndDate = date;
    }

    public void setTournamentStatus(TournamentStatus tournamentStatus) {
        this.tournamentStatus = tournamentStatus;
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

    public MatchBy getMatchBy() {
        return matchBy;
    }

    public Date getStartDate() {

        return this.startDate;
    }

    public Date getRoundEndDate() {
        return this.roundEndDate;
    }

    public int getMatchDuration() {
        return matchDuration;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public TournamentStatus getTournamentStatus() {
        return tournamentStatus;
    }
}
