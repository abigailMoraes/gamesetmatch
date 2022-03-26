package com.zoomers.GameSetMatch.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "User_involves_match")

@IdClass(UserMatchPairingId.class)
public class UserInvolvesMatch {
    @Id
    @Column(name="userID")
    private Integer userID;

    @Id
    @Column(name="matchID")
    private Integer matchID;

    String results;
    String attendance;
}
