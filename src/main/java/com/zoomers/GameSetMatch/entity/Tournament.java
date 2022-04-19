package com.zoomers.GameSetMatch.entity;

import com.zoomers.GameSetMatch.scheduler.domain.MockTournament;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Tournament")
@Getter
@Setter
@SqlResultSetMapping(name="MockTournamentMapping",
        classes = @ConstructorResult(
                targetClass = MockTournament.class,
                columns = {
                        @ColumnResult(name="tournamentID", type=Integer.class),
                        @ColumnResult(name="format", type=Integer.class),
                        @ColumnResult(name="series", type=Integer.class),
                        @ColumnResult(name="match_by", type=Integer.class),
                        @ColumnResult(name="match_duration", type=Integer.class),
                        @ColumnResult(name="round_start_date", type=Date.class),
                        @ColumnResult(name="current_round", type=Integer.class),
                        @ColumnResult(name="status", type=Integer.class),
                        @ColumnResult(name="min_participants", type=Integer.class)
                }
        )
)
@NamedNativeQuery(
        name="Tournament.getMockTournamentByID",
        query="SELECT tournamentID, format, series, match_by, match_duration, " +
                "round_start_date, current_round, status, min_participants " +
                "FROM Tournament WHERE tournamentID = :tournamentID",
        resultSetMapping = "MockTournamentMapping"
)
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tournamentID;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "close_registration_date")
    private Date closeRegistrationDate;

    @Column(name = "location")
    private String location;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "prize")
    private String prize;

    @Column(name = "format")
    private Integer format;

    @Column(name = "series")
    private Integer series;

    @Column(name = "match_by")
    private Integer matchBy;

    @Column(name = "match_duration")
    private Integer matchDuration;

    // The latest admin ID who creates/modifies the tournament
    @Column(name = "admin_hosts_tournament")
    private int adminHostsTournament;

    @Column(name = "status")
    private int status;

    @Column(name="current_round")
    private int currentRound;

    @Column(name="min_participants")
    private Integer minParticipants;

    @Column(name = "round_start_date")
    private Date roundStartDate;

    public Tournament() {
        this.status = -1;
    }

}
