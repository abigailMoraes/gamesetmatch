package com.zoomers.GameSetMatch.controller.Match.ResponseBody;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MatchDetailsForCalendar {
    private Integer matchID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int roundID;
    private int isConflict;
    private int playerOneID;
    private int playerTwoID;
    private List<UsersMatchInfo> participants;

    public MatchDetailsForCalendar(Integer matchID, LocalDateTime startTime, LocalDateTime endTime, int roundID,
                                   int isConflict, int playerOneID, int playerTwoID) {
        this.matchID = matchID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roundID = roundID;
        this.isConflict = isConflict;
        this.playerOneID = playerOneID;
        this.playerTwoID = playerTwoID;
    }
}
