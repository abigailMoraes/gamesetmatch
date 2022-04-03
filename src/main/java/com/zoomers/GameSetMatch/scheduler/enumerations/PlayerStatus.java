package com.zoomers.GameSetMatch.scheduler.enumerations;

public enum PlayerStatus {
    SAFE(0),
    ELIMINATED(1),
    ONE_LOSS(2);
    private final int status;

    private PlayerStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
