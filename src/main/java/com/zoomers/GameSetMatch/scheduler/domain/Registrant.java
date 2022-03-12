package com.zoomers.GameSetMatch.scheduler.domain;

import com.zoomers.GameSetMatch.scheduler.enumerations.Skill;

import java.util.List;

public class Registrant {

    private int id;
    private String availability; // 24 character string
    private Skill skillLevel = Skill.BEGINNER;
    private List<Registrant> playersToPlay;

    public Registrant(int id, String availability, Skill skillLevel) {
        this.id = id;
        this.availability = availability;
        this.skillLevel = skillLevel;

        assert(this.availability.length() == 24);
    }

    public Registrant(int id, String availability) {
        this.id = id;
        this.availability = availability;

        assert(this.availability.length() == 24);
    }

    public boolean checkAvailability(int timeID) {
        return this.availability.charAt(timeID) == '1';
    }

    public boolean hasNotPlayed(Registrant r2) {
        return this.playersToPlay.contains(r2);
    }

    public int getSkill() {
        return this.skillLevel.ordinal();
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
