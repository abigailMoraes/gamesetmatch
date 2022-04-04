package com.zoomers.GameSetMatch.entity;

import java.io.Serializable;
import java.util.Objects;


public class UserRegistersTournamentID implements Serializable {
    private Integer userID;
    private Integer tournamentID;

    public UserRegistersTournamentID() {

    }

    public UserRegistersTournamentID(Integer userID, Integer tournamentID) {
        this.userID = userID;
        this.tournamentID = tournamentID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRegistersTournamentID)) return false;
        UserRegistersTournamentID that = (UserRegistersTournamentID) o;
        return userID.equals(that.userID) &&
                tournamentID.equals(that.tournamentID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, tournamentID);
    }
}
