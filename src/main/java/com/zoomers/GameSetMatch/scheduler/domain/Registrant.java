package com.zoomers.GameSetMatch.scheduler.domain;

public class Registrant {

    private int id;
    private String availability; // 24 character string

    public Registrant(int id, String availability) {
        this.id = id;
        this.availability = availability;

        assert(this.availability.length() == 24);
    }

    public int getID() {
        return this.id;
    }

    public String getAvailability() {
        return availability;
    }
}
