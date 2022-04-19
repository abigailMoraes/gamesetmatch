package com.zoomers.GameSetMatch.entity;

import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.enumerations.PlayerStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@IdClass(UserRegistersTournamentID.class)
@Table(name = "User_registers_tournament")

@SqlResultSetMapping(name="RegistrantMapping",
        classes = @ConstructorResult(
                targetClass = Registrant.class,
                columns = {
                        @ColumnResult(name="userID", type=Integer.class),
                        @ColumnResult(name="skill_level", type=Integer.class),
                        @ColumnResult(name="tournamentID", type=Integer.class)
                }
        )
)
@NamedNativeQuery(
        name="UserRegistersTournament.getSchedulerRegistrantsByTournamentID",
        query="SELECT userID, skill_level, tournamentID " +
                "FROM User_registers_tournament WHERE tournamentID = :tournamentID",
        resultSetMapping = "RegistrantMapping"
)

@Getter
@Setter
public class UserRegistersTournament {
    public UserRegistersTournament(Integer tournamentID, Integer userID, Integer skillLevel, PlayerStatus playerStatus) {
        this.tournamentID = tournamentID;
        this.userID = userID;
        this.skillLevel = skillLevel;
        this.playerStatus = playerStatus;
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

    @Enumerated(EnumType.ORDINAL)
    @Column(name="player_status")
    private PlayerStatus playerStatus;

}
