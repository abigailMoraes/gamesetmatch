package com.zoomers.GameSetMatch.controller.Match.ResponseBody;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static com.zoomers.GameSetMatch.services.DateAndLocalDateService.timezoneOfData;

public class UserMatchTournamentInfoResp {
    private Integer results;
    private String attendance;
    private Integer matchID;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    private String name;
    private String location;
    private String description;

    public UserMatchTournamentInfoResp(Integer results, String attendance, Integer matchID,
                                       LocalDateTime startTime, LocalDateTime endTime, String name,
                                       String location, String description) {
        this.results = results;
        this.attendance = attendance;
        this.matchID = matchID;
        this.startTime = startTime.atZone(timezoneOfData);
        this.endTime = startTime.atZone(timezoneOfData);
        this.name = name;
        this.location = location;
        this.description = description;
    }
}
