package com.zoomers.GameSetMatch.controller.Round;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Date;

import static com.zoomers.GameSetMatch.services.DateAndLocalDateService.timezoneOfData;

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
        this.startDate = ZonedDateTime.ofInstant(startDate.toInstant(), timezoneOfData);
        this.endDate = ZonedDateTime.ofInstant(endDate.toInstant(), timezoneOfData);
    }
}
