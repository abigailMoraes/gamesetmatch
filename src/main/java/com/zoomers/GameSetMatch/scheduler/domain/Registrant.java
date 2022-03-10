package com.zoomers.GameSetMatch.scheduler.domain;

import com.zoomers.GameSetMatch.scheduler.enumerations.Skill;

import java.util.List;

public class Registrant {

    private int id;
    private String availability; // 24 character string
    private Skill skillLevel;
    private List<Registrant> playersToPlay;

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

    @Override
    public String toString() {
        return "Registrant{" +
                "id=" + id +
                '}';
    }
}
