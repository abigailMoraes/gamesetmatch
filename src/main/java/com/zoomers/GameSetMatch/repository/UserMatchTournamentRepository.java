package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.UserMatchTournamentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserMatchTournamentRepository extends JpaRepository<UserMatchTournamentInfo, Long> {
    @Query(
           value ="SELECT u.results, u.attendance, match_has.matchID, match_has.start_time, match_has.end_time,\n" +
                   "match_has.duration,round_has.type,tournament.name,tournament.location,tournament.description \n" +
                   "FROM (SELECT * FROM user_involves_match WHERE userID = :id) \n " +
                   "u LEFT JOIN match_has ON match_has.matchID = u.matchID LEFT JOIN \n" +
                   " round_has ON match_has.roundID = round_has.roundID LEFT JOIN tournament \n" +
                   " ON round_has.tournamentID = tournament.tournamentID;",
            nativeQuery = true)
    List<UserMatchTournamentInfo> findMatchesByUserID(int id);

    @Query(  value ="SELECT u.results, u.attendance, m.matchID, m.start_time, m.end_time,\n" +
            "m.duration, tournament.name, tournament.location, tournament.description \n" +
            "FROM (SELECT * FROM user_involves_match WHERE userID = :id) u \n" +
            "JOIN (SELECT * FROM match_has WHERE end_time <= NOW()) m ON m.matchID = u.matchID LEFT JOIN \n" +
            " round_has ON m.roundID = round_has.roundID LEFT JOIN tournament \n" +
            " ON round_has.tournamentID = tournament.tournamentID;",
            nativeQuery = true)
    List<UserMatchTournamentInfo> findPastMatchesByUserID(int id);

    @Query(  value ="SELECT m.matchID, m.start_time, m.end_time, m.duration, round_has.type, \n" +
            " tournament.name, tournament.location, tournament.description \n" +
            "FROM (SELECT * FROM match_has WHERE matchID = :id) m JOIN round_has ON m.roundID = round_has.roundID \n" +
            "LEFT JOIN tournament ON round_has.tournamentID = tournament.tournamentID;",
            nativeQuery = true)
    UserMatchTournamentInfo findMatchInfoByMatchID(int id);

    @Transactional
    @Modifying
    @Query( value = "UPDATE user_involves_match SET user_involves_match.attendance = :attendance \n"+
            "WHERE user_involves_match.matchID = :mid AND user_involves_match.userID = :uid", nativeQuery = true)
    void confirmMatchAttendanceForUser(int mid, int uid, String attendance);


    @Transactional
    @Modifying
    @Query( value = "UPDATE user_involves_match SET user_involves_match.attendance = :attendance \n"+
            "WHERE user_involves_match.matchID = :mid AND user_involves_match.userID = :uid", nativeQuery = true)
    void dropOutForUser(int mid, int uid, String attendance);

    @Transactional
    @Modifying
    @Query( value = "UPDATE user_involves_match SET user_involves_match.results = :results \n"+
            "WHERE user_involves_match.matchID = :mid AND user_involves_match.userID = :uid", nativeQuery = true)
    void updateMatchResults(int mid, int uid, String results);
}
