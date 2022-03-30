package com.zoomers.GameSetMatch.controller.Login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private int id;
    private String email;
    private String name;
    private int isAdmin;
    private String picture;

    public UserResponse(int id, String email, String name, int isAdmin, String picture) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.isAdmin = isAdmin;
        this.picture = picture;
    }

    public UserResponse() { }
}
