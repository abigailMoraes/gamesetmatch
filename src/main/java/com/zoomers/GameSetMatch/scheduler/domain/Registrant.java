package com.zoomers.GameSetMatch.scheduler.domain;

import com.zoomers.GameSetMatch.scheduler.enumerations.Skill;

import java.util.Objects;
import java.util.Set;

public class Registrant {

    private final int id;
    private String availability; // 24 * 7 character string
    private Skill skillLevel = Skill.BEGINNER;
    private Set<Registrant> playersToPlay;
    private int losses = 0;
    private int gamesToSchedule;

    public Registrant(int id, String availability, Skill skillLevel, int gamesToSchedule) {
        this.id = id;
        this.availability = availability;
        this.skillLevel = skillLevel;
        this.gamesToSchedule = gamesToSchedule;

        // assert(this.availability.length() == 24);// * 7);
    }

    public Registrant(int id, String availability, int gamesToSchedule) {
        this.id = id;
        this.availability = availability;
        this.skillLevel = Skill.BEGINNER;
        this.gamesToSchedule = gamesToSchedule;

        // assert(this.availability.length() == 24);// * 7);
    }

    public boolean checkAvailability(int timeID) {
        return this.availability.charAt(timeID) == '1';
    }

    public void setAvailabilityString(int day) {

        if (day == 1) {
            return;
        }

        int availabilityIndex = 24 * (day - 1);

        availability = availability.substring(availabilityIndex) + availability.substring(0, availabilityIndex);
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setPlayersToPlay(Set<Registrant> playersToPlay) {
        this.playersToPlay = playersToPlay;
    }

    public void decreaseGamesToSchedule() { this.gamesToSchedule--; }

    public boolean hasNotPlayed(Registrant r2) {
        return false;// this.playersToPlay.contains(r2);
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

    public int getLosses() {
        return losses;
    }

    public int getGamesToSchedule() { return gamesToSchedule; }

    @Override
    public String toString() {
        return "Registrant{" +
                "id=" + id +
                '}';
    }
}
