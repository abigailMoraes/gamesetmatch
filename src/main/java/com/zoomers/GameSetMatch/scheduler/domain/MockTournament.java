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
    private final Date roundStartDate;
    private Date roundEndDate;
    private final int currentRound;
    private TournamentStatus tournamentStatus;
    private final int minPlayers;

    public MockTournament(
            int tournamentID,
            int tournamentFormat,
            int tournamentSeries,
            int matchBy,
            int matchDuration,
            Date roundStartDate,
            int previousRound,
            int tournamentStatus,
            int minPlayers
    ) {
        this.tournamentID = tournamentID;
        this.tournamentFormat = TournamentFormat.values()[tournamentFormat];
        this.tournamentSeries = TournamentSeries.values()[tournamentSeries];
        this.matchBy = MatchBy.values()[matchBy];
        this.matchDuration = matchDuration;
        this.roundStartDate = roundStartDate;
        this.currentRound = previousRound + 1;
        this.tournamentStatus = TournamentStatus.values()[tournamentStatus];
        this.minPlayers = minPlayers;
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

    public Date getRoundStartDate() {

        return this.roundStartDate;
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

    public int getMinPlayers() { return minPlayers; }
}
