package com.zoomers.GameSetMatch.controller.Match.ResponseBody;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersMatchInfo {
    private String name;
    private String email;
    private int results;
    private String attendance;
    private int userID;

    public UsersMatchInfo(String name, String email, int results, String attendance, int userID) {
        this.name = name;
        this.email = email;
        this.results = results;
        this.attendance = attendance;
        this.userID = userID;
    }
}
