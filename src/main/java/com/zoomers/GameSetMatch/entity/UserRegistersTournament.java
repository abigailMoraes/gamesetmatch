package com.zoomers.GameSetMatch.entity;

import javax.persistence.*;

@Entity
@IdClass(UserRegistersTournamentID.class)
@Table(name = "User_registers_tournament")
public class UserRegistersTournament {
    public UserRegistersTournament(Long tournamentID, Long userID) {
        this.tournamentID = tournamentID;
        this.userID = userID;
    }

    public UserRegistersTournament() {
    }

    @Id
    @Column(name = "tournamentID")
    private Long tournamentID;

    @Id
    @Column(name = "userID")
    private Long userID;

    @Column(name = "skill_level")
    private int skillLevel;

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

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }
}
