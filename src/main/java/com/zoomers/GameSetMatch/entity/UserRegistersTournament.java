package com.zoomers.GameSetMatch.entity;

import com.zoomers.GameSetMatch.scheduler.domain.Registrant;

import javax.persistence.*;

@Entity
@IdClass(UserRegistersTournamentID.class)
@Table(name = "User_registers_tournament")

@SqlResultSetMapping(name="RegistrantMapping",
        classes = @ConstructorResult(
                targetClass = Registrant.class,
                columns = {
                        @ColumnResult(name="userID", type=Integer.class),
                        @ColumnResult(name="skill_level", type=Integer.class)
                }
        )
)
@NamedNativeQuery(
        name="UserRegistersTournament.getSchedulerRegistrantsByTournamentID",
        query="SELECT userID, skill_level " +
                "FROM User_registers_tournament WHERE tournamentID = :tournamentID",
        resultSetMapping = "RegistrantMapping"
)
public class UserRegistersTournament {
    public UserRegistersTournament(Integer tournamentID, Integer userID, Integer skillLevel) {
        this.tournamentID = tournamentID;
        this.userID = userID;
        this.skillLevel = skillLevel;
    }

    public UserRegistersTournament() {
    }

    @Id
    @Column(name = "tournamentID")
    private Integer tournamentID;

    @Id
    @Column(name = "userID")
    private Integer userID;

    @Column(name = "skill_level")
    private Integer skillLevel;

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

    public Integer getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(Integer skillLevel) {
        this.skillLevel = skillLevel;
    }
}
