package com.zoomers.GameSetMatch.entity;

import com.zoomers.GameSetMatch.controller.Tournament.RequestBody.AvailabilityDTO;
import com.zoomers.GameSetMatch.services.DTO.ParticipantAvailabilityForADayInfo;

import javax.persistence.*;

@SqlResultSetMapping(name = "AvailabilityDTOMapping",
        classes = @ConstructorResult(
                targetClass = AvailabilityDTO.class,
                columns = {
                        @ColumnResult(name = "day_of_week", type = Integer.class),
                        @ColumnResult(name = "availability_string", type = String.class),
                }
        )
)
@NamedNativeQuery(
        name = "Availability.getUsersAvailabilityForTournament",
        query = "SELECT day_of_week, availability_string " +
                "FROM Availability " +
                "WHERE userID = :userID AND tournamentID = :tournamentID " +
                "ORDER BY day_of_week",
        resultSetMapping = "AvailabilityDTOMapping"
)

@SqlResultSetMapping(name = "ParticipantAvailabilityForDayMapping",
        classes = @ConstructorResult(
                targetClass = ParticipantAvailabilityForADayInfo.class,
                columns = {
                        @ColumnResult(name = "userID", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "availability_string", type = String.class)
                }
        )
)
@NamedNativeQuery(
        name = "Availability.getParticipantsAvailabilityForADay",
        query = "SELECT User.userID, User.name, a.availability_string FROM Availability a " +
                "INNER JOIN User_involves_match u ON  a.userID = u.userID " +
                "INNER JOIN User on User.userID = u.userID " +
                "WHERE a.day_of_week = :dayOfWeek AND " +
                "u.matchID = :matchID AND " +
                "a.tournamentID = :tournamentID",
        resultSetMapping = "ParticipantAvailabilityForDayMapping"
)

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
    @Column(name = "day_of_week")
    private int dayOfWeek;

    @Column(name = "availability_string")
    private String availability;

    public Availability() {

    }

    public Availability(Integer tournamentID, Integer userID, int day_of_week, String availability) {
        this.tournamentID = tournamentID;
        this.userID = userID;
        this.dayOfWeek = day_of_week;
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

    public int getDate() {
        return dayOfWeek;
    }

    public void setDate(int day_of_week) {
        this.dayOfWeek = day_of_week;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
