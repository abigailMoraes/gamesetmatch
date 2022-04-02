package com.zoomers.GameSetMatch.controller.Login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private int id;
    private String firebaseId;
    private String email;
    private String name;
    private int isAdmin;
    private String picture;

    public UserResponse(int id, String firebaseId, String email, String name, int isAdmin, String picture) {
        this.id = id;
        this.firebaseId = firebaseId;
        this.email = email;
        this.name = name;
        this.isAdmin = isAdmin;
        this.picture = picture;
    }

    public UserResponse() { }

    @Override
    public String toString() {
        return String.format("{\"id\":%d,\"firebaseId\":\"%s\",\"email\":\"%s\",\"name\":\"%s\",\"isAdmin\":%d,\"picture\":\"%s\"}",
                this.id, this.firebaseId, this.email, this.name, this.isAdmin, this.picture);
    }
}
