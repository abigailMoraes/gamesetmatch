package com.zoomers.GameSetMatch.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
@Getter
@Setter
@ToString

public class Administrator extends User{
    private @GeneratedValue int adminId;

    public int getAdminID() {
        return this.adminId;
    }
}
