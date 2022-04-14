package com.zoomers.GameSetMatch.scheduler.enumerations;

public enum TournamentStatus {
    DEFAULT(0),
    OPEN_FOR_REGISTRATION(1),
    REGISTRATION_CLOSED(2),
    READY_TO_SCHEDULE(3),
    READY_TO_PUBLISH_SCHEDULE(4),
    ONGOING(5),
    READY_TO_PUBLISH_NEXT_ROUND(6),
    FINAL_ROUND(7),
    TOURNAMENT_OVER(8);

    private final int status;
    TournamentStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
