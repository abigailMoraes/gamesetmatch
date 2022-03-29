package com.zoomers.GameSetMatch.controller.Match.ResponseBody;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MatchDetailsForCalendar {
    private Integer matchID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int roundID;
    private int isConflict;
    private int userID_1;
    private int userID_2;
    private String userName1;
    private String userName2;
    private String userEmail1;
    private String userEmail2;
    private int userResults1;
    private String userAttendance1;
    private int userResults2;
    private String userAttendance2;

    public MatchDetailsForCalendar(Integer matchID, LocalDateTime startTime, LocalDateTime endTime, int roundID,
                                   int isConflict, int userID_1, int userID_2, String userName1, String userEmail1,
                                   String userName2, String userEmail2, int userResults1, String userAttendance1,
                                   int userResults2, String userAttendance2) {
        this.matchID = matchID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roundID = roundID;
        this.isConflict = isConflict;
        this.userID_1 = userID_1;
        this.userID_2 = userID_2;
        this.userName1 = userName1;
        this.userName2 = userName2;
        this.userEmail1 = userEmail1;
        this.userEmail2 = userEmail2;
        this.userResults1 = userResults1;
        this.userAttendance1 = userAttendance1;
        this.userResults2 = userResults2;
        this.userAttendance2 = userAttendance2;
    }
}
