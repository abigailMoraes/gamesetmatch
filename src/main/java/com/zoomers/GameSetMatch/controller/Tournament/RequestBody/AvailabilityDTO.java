package com.zoomers.GameSetMatch.controller.Tournament.RequestBody;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvailabilityDTO {
    private int dayOfWeek;
    private String availabilityString; // 24 character string to represent 30 min intervals from 9am -9pm

    public AvailabilityDTO(int dayOfWeek, String availabilityString) {
        this.dayOfWeek = dayOfWeek;
        this.availabilityString = availabilityString;
    }
}
