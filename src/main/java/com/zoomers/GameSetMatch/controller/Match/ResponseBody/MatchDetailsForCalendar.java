package com.zoomers.GameSetMatch.controller.Match.ResponseBody;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static com.zoomers.GameSetMatch.services.DateAndLocalDateService.timezoneOfData;

@Getter
@Setter
public class MatchDetailsForCalendar {
    private Integer matchID;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private int roundID;
    private int matchStatus;
    private int playerOneID;
    private int playerTwoID;
    private List<UsersMatchInfo> participants;

    public MatchDetailsForCalendar(Integer matchID, LocalDateTime startTime, LocalDateTime endTime, int roundID,
                                   int matchStatus, int playerOneID, int playerTwoID) {
        this.matchID = matchID;
        this.startTime = startTime.atZone(timezoneOfData);
        this.endTime = endTime.atZone(timezoneOfData);
        this.roundID = roundID;
        this.matchStatus = matchStatus;
        this.playerOneID = playerOneID;
        this.playerTwoID = playerTwoID;
    }
}
