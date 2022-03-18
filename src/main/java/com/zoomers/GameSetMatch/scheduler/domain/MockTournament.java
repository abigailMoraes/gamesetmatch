package com.zoomers.GameSetMatch.scheduler.domain;

import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentFormat;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentType;

public class MockTournament {

    private final int tournamentID;
    private final TournamentType tournamentType;
    private final TournamentFormat tournamentFormat;
    private final boolean matchBySkill;
    private final int matchDuration;

    public MockTournament(
            int tournamentID,
            TournamentType tournamentType,
            TournamentFormat tournamentFormat,
            boolean matchBySkill,
            int matchDuration
    ) {
        this.tournamentID = tournamentID;
        this.tournamentType = tournamentType;
        this.tournamentFormat = tournamentFormat;
        this.matchBySkill = matchBySkill;
        this.matchDuration = matchDuration;
    }

    public int getTournamentID() {
        return tournamentID;
    }

    public TournamentType getTournamentType() {
        return tournamentType;
    }

    public TournamentFormat getTournamentFormat() {
        return tournamentFormat;
    }

    public boolean isMatchBySkill() {
        return matchBySkill;
    }

    public int getMatchDuration() {
        return matchDuration;
    }
}
