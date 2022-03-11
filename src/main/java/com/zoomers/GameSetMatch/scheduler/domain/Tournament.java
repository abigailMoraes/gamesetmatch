package com.zoomers.GameSetMatch.scheduler.domain;

import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentType;

public class Tournament {

    private TournamentType tournamentType;
    private boolean matchBySkill;

    public Tournament(TournamentType tournamentType) {
        this.tournamentType = tournamentType;
        this.matchBySkill = false;
    }
}
