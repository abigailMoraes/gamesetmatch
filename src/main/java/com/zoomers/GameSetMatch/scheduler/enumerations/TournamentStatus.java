package com.zoomers.GameSetMatch.scheduler.enumerations;

public enum TournamentStatus {
    DEFAULT(0),
    OPEN_FOR_REGISTRATION(1),
    READY_TO_SCHEDULE(2),
    READY_TO_PUBLISH_SCHEDULE(3),
    ONGOING(4),
    READY_TO_PUBLISH_NEXT_ROUND(5),
    FINAL_ROUND(6),
    TOURNAMENT_OVER(7);

    private final int status;
    TournamentStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
