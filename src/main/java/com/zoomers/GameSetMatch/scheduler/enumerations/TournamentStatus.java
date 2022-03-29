package com.zoomers.GameSetMatch.scheduler.enumerations;

public enum TournamentStatus {
    DEFAULT(-1),
    OPEN_FOR_REGISTRATION(0),
    REGISTRATION_CLOSED(1),
    READY_TO_PUBLISH_SCHEDULE(2),
    ONGOING(3),
    READY_TO_PUBLISH_NEXT_ROUND(4),
    FINAL_ROUND(5),
    TOURNAMENT_OVER(6);

    private final int status;
    private TournamentStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
