package com.zoomers.GameSetMatch.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@Table(name = "user_involves_match")


@IdClass(UserMatchPairingId.class)
public class UserMatchPairing {
    @Id
    @Column(name="userID")
    private int userID;

    @Id
    @Column(name="matchID")
    private int matchID;
}
