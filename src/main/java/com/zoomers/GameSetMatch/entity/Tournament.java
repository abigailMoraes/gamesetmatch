package com.zoomers.GameSetMatch.entity;

import com.zoomers.GameSetMatch.scheduler.domain.MockTournament;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentFormat;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentSeries;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Tournament")
@SqlResultSetMapping(name="MockTournamentMapping",
        classes = @ConstructorResult(
                targetClass = MockTournament.class,
                columns = {
                        @ColumnResult(name="tournamentID", type=Integer.class),
                        @ColumnResult(name="format", type=Integer.class),
                        @ColumnResult(name="series", type=Integer.class),
                        @ColumnResult(name="match_by", type=Integer.class),
                        @ColumnResult(name="match_duration", type=Integer.class),
                        @ColumnResult(name="start_date", type=Date.class),
                        @ColumnResult(name="current_round", type=Integer.class)
                }
        )
)
@NamedNativeQuery(
        name="Tournament.getMockTournamentByID",
        query="SELECT tournamentID, format, series, match_by, match_duration, start_date, current_round " +
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

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "min_participants")
    private Integer minParticipants;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "prize")
    private String prize;

    @Column(name = "format")
    private Integer format;

    @Column(name = "series")
    private Integer series;

    @Column(name = "match_by")
    private Integer matchBySkill;

    @Column(name = "match_duration")
    private Integer matchDuration;

    @Column(name = "number_of_matches")
    private Integer numberOfMatches;

    @Column(name = "round_duration")
    private Integer roundDuration;

    // The latest admin ID who creates/modifies the tournament
    @Column(name = "admin_hosts_tournament")
    private int adminHostsTournament;

    //     -1 = default value in constructor
    //     0 = open for registration,
    //     1 = ready to schedule,
    //     2 = ongoing tournament,
    //     3 = ready to schedule next round,
    //     4 = tournament over
    @Column(name = "status")
    private int status;


    public Tournament() {
        this.status = -1;
    }

    public Integer getTournamentID() {
        return tournamentID;
    }

    public void setTournamentID(Integer tournamentID) {
        this.tournamentID = tournamentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getCloseRegistrationDate() {
        return closeRegistrationDate;
    }

    public void setCloseRegistrationDate(Date closeRegistrationDate) {
        this.closeRegistrationDate = closeRegistrationDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Integer getMinParticipants() {
        return minParticipants;
    }

    public void setMinParticipants(Integer minParticipants) {
        this.minParticipants = minParticipants;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public Integer getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public Integer getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public Integer getMatchDuration() {
        return matchDuration;
    }

    public void setMatchDuration(Integer matchDuration) {
        this.matchDuration = matchDuration;
    }

    public Integer getNumberOfMatches() {
        return numberOfMatches;
    }

    public void setNumberOfMatches(Integer numberOfMatches) {
        this.numberOfMatches = numberOfMatches;
    }

    public Integer getRoundDuration() {
        return roundDuration;
    }

    public void setRoundDuration(Integer roundDuration) {
        this.roundDuration = roundDuration;
    }

    public void setAdminHostsTournament(int id) {this.adminHostsTournament= id;}

    public int getAdminHostsTournament() {return this.adminHostsTournament;}

    public void setStatus(int status) {this.status = status;}

    public int getStatus() {return this.status;}
}
