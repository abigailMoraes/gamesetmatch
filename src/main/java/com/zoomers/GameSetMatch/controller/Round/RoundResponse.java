package com.zoomers.GameSetMatch.controller.Round;

import lombok.Getter;
import lombok.Setter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Getter
@Setter
public class RoundResponse {
    private Integer roundID;
    private Integer roundNumber;
    private Integer tournamentID;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;

    public RoundResponse(Integer roundID, Integer roundNumber, Integer tournamentID, Date startDate, Date endDate) {
        this.roundID = roundID;
        this.roundNumber = roundNumber;
        this.tournamentID = tournamentID;
        this.startDate =  ZonedDateTime.ofInstant(startDate.toInstant(), ZoneId.of("America/Los_Angeles")) ;
        this.endDate =  ZonedDateTime.ofInstant(endDate.toInstant(), ZoneId.of("America/Los_Angeles")) ;
    }
}
