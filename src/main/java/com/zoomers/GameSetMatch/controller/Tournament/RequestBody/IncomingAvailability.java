package com.zoomers.GameSetMatch.controller.Tournament.RequestBody;


public class IncomingAvailability {
    private int dayOfWeek;
    private String availabilityString; // 24 character string to represent 30 min intervals from 9am -9pm

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public String getAvailabilityString() {
        return availabilityString;
    }
}
