package com.zoomers.GameSetMatch.controller.Tournament.ResponseBody;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentTournamentStatus {
    private int currentTournamentStatus;

    public CurrentTournamentStatus(int currentTournamentStatus) {
        this.currentTournamentStatus = currentTournamentStatus;
    }
}