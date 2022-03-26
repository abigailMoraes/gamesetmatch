package com.zoomers.GameSetMatch.scheduler.enumerations;

public enum TournamentStatus {

    ONGOING(3),
    FINAL_ROUND(4);

    private final int status;
    private TournamentStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
