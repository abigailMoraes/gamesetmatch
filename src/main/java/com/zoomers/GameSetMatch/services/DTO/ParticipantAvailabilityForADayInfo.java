package com.zoomers.GameSetMatch.services.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantAvailabilityForADayInfo {
    int userID;
    String name;
    String availabilityString;

    public ParticipantAvailabilityForADayInfo(int userID, String name, String availabilityString) {
        this.userID = userID;
        this.name = name;
        this.availabilityString = availabilityString;
    }
}
