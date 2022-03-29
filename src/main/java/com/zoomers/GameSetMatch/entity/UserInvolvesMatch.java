package com.zoomers.GameSetMatch.entity;

import com.zoomers.GameSetMatch.controller.Match.ResponseBody.UsersMatchInfo;
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

@SqlResultSetMapping(name = "UsersMatchInfoMapping",
        classes = @ConstructorResult(
                targetClass = UsersMatchInfo.class,
                columns = {
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "email", type = String.class),
                        @ColumnResult(name = "results", type = Integer.class),
                        @ColumnResult(name = "attendance", type = String.class),
                        @ColumnResult(name = "userID", type = Integer.class),
                }
        )
)
@NamedNativeQuery(
        name = "UserInvolvesMatch.getUsersMatchInfoForCalendar",
        query = "SELECT uim.userID, u.name AS name, u.email AS email, uim.results, uim.attendance " +
                "FROM User_involves_match uim \n" +
                "INNER JOIN User u ON u.userID = uim.userID \n" +
                "WHERE uim.matchID = :matchID",
        resultSetMapping = "UsersMatchInfoMapping"
)

public class UserInvolvesMatch {
    @Id
    private Integer userID;

    @Id
    private Integer matchID;

    Integer results;

    String attendance;

}
