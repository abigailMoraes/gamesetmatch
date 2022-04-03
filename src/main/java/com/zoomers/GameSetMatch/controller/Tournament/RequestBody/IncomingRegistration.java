package com.zoomers.GameSetMatch.controller.Tournament.RequestBody;

import java.util.List;

public class IncomingRegistration {
    private Integer userID;
    private List<AvailabilityDTO> availabilities;
    private Integer skillLevel;

    public Integer getUserID() {
        return userID;
    }

    public List<AvailabilityDTO> getAvailabilities() {
        return availabilities;
    }

    public Integer getSkillLevel() {
        return skillLevel;
    }
}
