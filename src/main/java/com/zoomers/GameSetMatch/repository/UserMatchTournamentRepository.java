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
                   "match_has.duration,tournament.name,tournament.location,tournament.description \n" +
                   "FROM (SELECT * FROM user_involves_match WHERE userID = :id) \n " +
                   "u LEFT JOIN match_has ON match_has.matchID = u.matchID LEFT JOIN \n" +
                   " round_has ON match_has.roundID = round_has.roundID LEFT JOIN tournament \n" +
                   " ON round_has.tournamentID = tournament.tournamentID;",
            nativeQuery = true)
    List<UserMatchTournamentInfo> findMatchesByUserID(int id);

    @Query(  value ="SELECT u.results, u.attendance, m.matchID, m.start_time, m.end_time,\n" +
            "m.duration,tournament.name, tournament.location, tournament.description \n" +
            "FROM (SELECT * FROM user_involves_match WHERE userID = :id) u \n" +
            "JOIN (SELECT * FROM match_has WHERE end_time <= NOW()) m ON m.matchID = u.matchID LEFT JOIN \n" +
            " round_has ON m.roundID = round_has.roundID LEFT JOIN tournament \n" +
            " ON round_has.tournamentID = tournament.tournamentID;",
            nativeQuery = true)
    List<UserMatchTournamentInfo> findPastMatchesByUserID(int id);

    @Query(  value ="SELECT u.results, u.attendance, m.matchID, m.start_time, m.end_time,\n" +
            "m.duration,tournament.name, tournament.location, tournament.description \n" +
            "FROM(SELECT * FROM user_involves_match WHERE userID = :uid) u \n" +
            "JOIN(SELECT * FROM match_has WHERE match_has.matchID = :id) m ON m.matchID = u.matchID LEFT JOIN \n" +
            " round_has ON m.roundID = round_has.roundID LEFT JOIN tournament \n" +
            " ON round_has.tournamentID = tournament.tournamentID;",
            nativeQuery = true)
    UserMatchTournamentInfo findMatchInfoByMatchID(int id, int uid);




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

    @Transactional
    @Modifying
    @Query( value = "UPDATE user_involves_match SET user_involves_match.results = :results \n" +
       "user_involves_match.attendance = :attendance WHERE user_involves_match.matchID = :mid \n " +
            "AND user_involves_match.userID = :uid", nativeQuery = true)
    void updateMatchUserInformation(int mid, int uid, String results, String attendance);

    @Query(value ="SELECT u.userID as id, u.results as resultText, u.attendance as status, p.name\n " +
            "FROM user_involves_match u \n" +
            "JOIN (SELECT * FROM match_has WHERE match_has.matchID = :matchID) m ON m.matchID = u.matchID JOIN \n" +
            " round_has r ON m.roundID = r.roundID JOIN tournament t\n" +
            " ON r.tournamentID = t.tournamentID JOIN User p ON u.userID = p.userID", nativeQuery = true)
    List<IParticipantInfo> getUserMatchInfoByMatchID(int matchID);


    @Query(value ="SELECT m.matchID as id, t.name as name, m.roundID as tournamentRoundText, m.start_time as startTime FROM match_has m JOIN \n"
            + "round_has r ON m.roundID = r.roundID JOIN (SELECT * FROM tournament WHERE \n" +
            "tournament.tournamentID = :tournamentID) t on t.tournamentID = r.tournamentID", nativeQuery = true)
    List<UserMatchTournamentRepository.IBracketMatchInfo> getBracketMatchInfoByTournamentID(int tournamentID);

    @Query(value ="SELECT matchID as next from match_has where roundID = (SELECT roundID FROM round_has WHERE roundNumber = \n"
            + "(SELECT roundNumber from round_has where roundID = :oldRoundID) + 1\n" +
            " AND tournamentID = (SELECT tournamentID from round_has where roundID = :oldRoundID)) \n" +
            " AND ((userID_1 in ((select userID_1 from match_has where matchID = :oldMatchID),\n " +
            "(select userID_2 from match_has where\n" +
            " matchID = :oldMatchID))) OR (userID_2 in ((select userID_1 from match_has where matchID = :oldMatchID),\n"
            + "(select userID_2 from match_has where\n" +
            " matchID = :oldMatchID))));", nativeQuery = true)
    NumQuery getNextMatchID(int oldMatchID, int oldRoundID);

    interface NumQuery{
        Integer getNext();
    }

    interface IParticipantInfo{
        String getID();
        String getResultText();
        String getStatus();
        String getName();
    }



    interface IBracketMatchInfo{
        Integer getID();
        String  getName();
        String  getTournamentRoundText();
        String  getStartTime();
    }





}
