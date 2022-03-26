package com.zoomers.GameSetMatch.controller.Tournament.RequestBody;


public class IncomingAvailability {
    private int dayOfWeek;
    private String slots;

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public String getSlots() {
        return slots;
    }
}
