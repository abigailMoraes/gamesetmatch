package com.zoomers.GameSetMatch.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@IdClass(AvailabilityID.class)
@Table(name = "Availability")
public class Availability {
    @Id
    @Column(name = "tournamentID")
    private Integer tournamentID;

    @Id
    @Column(name = "userID")
    private Integer userID;

    @Id
    @Column(name = "day_of_week")
    private int dayOfWeek;

    @Column(name = "availability_string")
    private String availability;

    public Availability() {

    }

    public Availability(Integer tournamentID, Integer userID, int day_of_week, String availability) {
        this.tournamentID = tournamentID;
        this.userID = userID;
        this.dayOfWeek = day_of_week;
        this.availability = availability;
    }

    public Integer getTournamentID() {
        return tournamentID;
    }

    public void setTournamentID(Integer tournamentID) {
        this.tournamentID = tournamentID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public int getDate() {
        return dayOfWeek;
    }

    public void setDate(int day_of_week) {
        this.dayOfWeek = day_of_week;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
