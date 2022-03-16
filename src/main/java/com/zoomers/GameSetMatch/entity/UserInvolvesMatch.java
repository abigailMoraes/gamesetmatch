package com.zoomers.GameSetMatch.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "user_involves_match")


@IdClass(UserMatchPairingId.class)
public class UserInvolvesMatch {
    @Id
    @Column(name="userID")
    private Long userID;

    @Id
    @Column(name="matchID")
    private Long matchID;

    String results;
    String attendance;
}
