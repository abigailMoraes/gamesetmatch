package com.zoomers.GameSetMatch.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class AvailabilityID implements Serializable {
    private Integer userID;
    private Integer tournamentID;
    private Date date;

    public AvailabilityID() {
    }

    public AvailabilityID(Integer userID, Integer tournamentID, Date date) {
        this.userID = userID;
        this.tournamentID = tournamentID;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvailabilityID)) return false;
        AvailabilityID that = (AvailabilityID) o;
        return userID.equals(that.userID) &&
                tournamentID.equals(that.tournamentID) &&
                date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, tournamentID, date);
    }
}
