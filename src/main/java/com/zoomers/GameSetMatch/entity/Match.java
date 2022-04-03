package com.zoomers.GameSetMatch.entity;


import com.zoomers.GameSetMatch.controller.Match.ResponseBody.MatchDetailsForCalendar;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Match_Has")
@Getter
@Setter
@ToString
@SqlResultSetMapping(name="MatchForCalendarMapping",
        classes = @ConstructorResult(
                targetClass = MatchDetailsForCalendar.class,
                columns = {
                        @ColumnResult(name="matchID", type=Integer.class),
                        @ColumnResult(name="start_time", type=LocalDateTime.class),
                        @ColumnResult(name="end_time", type=LocalDateTime.class),
                        @ColumnResult(name="roundID", type=Integer.class),
                        @ColumnResult(name="is_conflict", type=Integer.class),
                        @ColumnResult(name="userID_1", type= Integer.class),
                        @ColumnResult(name="userID_2", type=Integer.class),
                        @ColumnResult(name="name1", type=String.class),
                        @ColumnResult(name="email1", type=String.class),
                        @ColumnResult(name="name2", type=String.class),
                        @ColumnResult(name="email2", type=String.class),
                        @ColumnResult(name="results1", type=Integer.class),
                        @ColumnResult(name="attendance1", type= String.class),
                        @ColumnResult(name="results2", type=Integer.class),
                        @ColumnResult(name="attendance2", type=String.class)
                }
        )
)
@NamedNativeQuery(
        name="Match.getMatchDetailsForCalendarByRoundID",
        query="SELECT DISTINCT(m.matchID), m.start_time, m.end_time, m.roundID, m.is_conflict, \n" +
                "userID_1, userID_2, u1.name AS name1, u1.email as email1, u2.name as name2, u2.email as email2,\n" +
                "uim1.results as results1, uim1.attendance as attendance1, uim2.results as results2, uim2.attendance as attendance2\n" +
                "FROM Match_Has m\n" +
                "INNER JOIN User u1 ON u1.userID = m.userID_1\n" +
                "INNER JOIN User u2 ON u2.userID = m.userID_2\n" +
                "INNER JOIN User_involves_match uim1 ON uim1.userID =  m.userID_1\n" +
                "INNER JOIN User_involves_match uim2 ON uim2.userID =  m.userID_2\n" +
                "WHERE m.roundID = :roundID",
        resultSetMapping = "MatchForCalendarMapping"
)
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer matchID;

    @Column(name="start_time")
    private LocalDateTime startTime;

    @Column(name="end_time")
    private LocalDateTime endTime;

    @Column(name="roundID")

    private int roundID;

    @Column(name="is_conflict")
    private int isConflict;

    @Column(name="userID_1")
    private int userID_1;

    @Column(name="userID_2")
    private int userID_2;

    private Boolean isPublished;

}
