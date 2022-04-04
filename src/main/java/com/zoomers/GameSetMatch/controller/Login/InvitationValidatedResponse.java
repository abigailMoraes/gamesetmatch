package com.zoomers.GameSetMatch.controller.Login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvitationValidatedResponse {
    private int id;
    private String message;

    public InvitationValidatedResponse(int id, String message){
        this.id = id;
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("{\"id\":%d,\"message\":\"%s\"}",
                this.id, this.message);
    }

}
