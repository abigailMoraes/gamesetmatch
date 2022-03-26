package com.zoomers.GameSetMatch.controller.Tournament.RequestBody;

import java.util.List;

public class IncomingRegistration {
    private Integer userID;
    private List<IncomingAvailability> availabilities;
    private Integer skillLevel;

    public Integer getUserID() {
        return userID;
    }

    public List<IncomingAvailability> getAvailabilities() {
        return availabilities;
    }

    public Integer getSkillLevel() {
        return skillLevel;
    }
}
