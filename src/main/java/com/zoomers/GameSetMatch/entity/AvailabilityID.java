package com.zoomers.GameSetMatch.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class AvailabilityID implements Serializable {
    private Integer userID;
    private Integer tournamentID;
    private int dayOfWeek;

    public AvailabilityID() {
    }

    public AvailabilityID(Integer userID, Integer tournamentID, int dayOfWeek) {
        this.userID = userID;
        this.tournamentID = tournamentID;
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvailabilityID)) return false;
        AvailabilityID that = (AvailabilityID) o;
        return userID.equals(that.userID) &&
                tournamentID.equals(that.tournamentID) &&
                dayOfWeek == that.dayOfWeek;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, tournamentID, dayOfWeek);
    }
}
