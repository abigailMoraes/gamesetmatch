package com.zoomers.GameSetMatch.controller.Tournament.ResponseBody;

import com.zoomers.GameSetMatch.entity.Tournament;

public class OutgoingTournament extends Tournament {
    private boolean isRegistered;

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }
}
