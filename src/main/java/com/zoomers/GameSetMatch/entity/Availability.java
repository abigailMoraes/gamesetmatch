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
    @Column(name = "date")
    private Date date;

    @Column(name = "availability_binary")
    private int availability;

    public Availability() {

    }

    public Availability(Integer tournamentID, Integer userID, Date date, int availability) {
        this.tournamentID = tournamentID;
        this.userID = userID;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }
}
