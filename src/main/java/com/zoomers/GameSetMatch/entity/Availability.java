package com.zoomers.GameSetMatch.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@IdClass(AvailabilityID.class)
@Table(name = "Availability")
public class Availability {
    @Id
    @Column(name = "tournamentID")
    private Long tournamentID;

    @Id
    @Column(name = "userID")
    private Long userID;

    @Id
    @Column(name = "date")
    private Date date;

    @Column(name = "availability_binary")
    private int availability;

    public Availability() {

    }

    public Availability(Long tournamentID, Long userID, Date date, int availability) {
        this.tournamentID = tournamentID;
        this.userID = userID;
        this.date = date;
        this.availability = availability;
    }

    public Long getTournamentID() {
        return tournamentID;
    }

    public void setTournamentID(Long tournamentID) {
        this.tournamentID = tournamentID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
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
