package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.UserMatchTournamentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserMatchTournamentRepository extends JpaRepository<UserMatchTournamentInfo, Long> {
    @Query(
           value ="SELECT u.results, u.attendance, Match_Has.matchID, Match_Has.start_time, Match_Has.end_time,\n" +
                   "Round_Has.roundNumber,Tournament.name,Tournament.location,Tournament.description \n" +
                   "FROM (SELECT * FROM User_involves_match WHERE userID = :id) \n " +
                   "u LEFT JOIN Match_Has ON Match_Has.matchID = u.matchID AND Match_Has.publishedOrNot = 1 LEFT JOIN \n" +
                   " Round_Has ON Match_Has.roundID = Round_Has.roundID LEFT JOIN Tournament \n" +
                   " ON Round_Has.tournamentID = Tournament.tournamentID;",
            nativeQuery = true)
    List<UserMatchTournamentInfo> findPublishedMatchesByUserID(int id);

    @Query(  value ="SELECT u.results, u.attendance, m.matchID, m.start_time, m.end_time,\n" +
            "Tournament.name, Tournament.location, Tournament.description \n" +
            "FROM (SELECT * FROM User_involves_match WHERE userID = :id) u \n" +
            "JOIN (SELECT * FROM Match_Has WHERE end_time <= NOW()) m ON m.matchID = u.matchID LEFT JOIN \n" +
            " Round_Has ON m.roundID = Round_Has.roundID LEFT JOIN Tournament \n" +
            " ON Round_Has.tournamentID = Tournament.tournamentID;",
            nativeQuery = true)
    List<UserMatchTournamentInfo> findPastMatchesByUserID(int id);

    @Query(  value ="SELECT m.matchID, m.start_time, m.end_time, Round_Has.roundNumber, \n" +
            " Tournament.name, Tournament.location, Tournament.description \n" +
            "FROM (SELECT * FROM Match_Has WHERE matchID = :id) m JOIN Round_Has ON m.roundID = Round_Has.roundID \n" +
            "LEFT JOIN Tournament ON Round_Has.tournamentID = Tournament.tournamentID;",
            nativeQuery = true)
    UserMatchTournamentInfo findMatchInfoByMatchID(int id);

    @Transactional
    @Modifying
    @Query( value = "UPDATE User_involves_match SET User_involves_match.attendance = :attendance \n"+
            "WHERE User_involves_match.matchID = :mid AND User_involves_match.userID = :uid", nativeQuery = true)
    void confirmMatchAttendanceForUser(int mid, int uid, String attendance);


    @Transactional
    @Modifying
    @Query( value = "UPDATE User_involves_match SET User_involves_match.attendance = :attendance \n"+
            "WHERE User_involves_match.matchID = :mid AND User_involves_match.userID = :uid", nativeQuery = true)
    void dropOutForUser(int mid, int uid, String attendance);

    @Transactional
    @Modifying
    @Query( value = "UPDATE User_involves_match SET User_involves_match.results = :results \n"+
            "WHERE User_involves_match.matchID = :mid AND User_involves_match.userID = :uid", nativeQuery = true)
    void updateMatchResults(int mid, int uid, String results);
}
