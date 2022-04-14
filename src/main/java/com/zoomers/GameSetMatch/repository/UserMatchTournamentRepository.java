package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.UserMatchTournamentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface UserMatchTournamentRepository extends JpaRepository<UserMatchTournamentInfo, Long> {
    
    @Query(
           value ="SELECT u.results, u.attendance, Match_Has.matchID, Match_Has.start_time, Match_Has.end_time,\n" +
                   "Round_Has.roundNumber, Tournament.name, Tournament.location, Tournament.description \n" +
                   "FROM (SELECT * FROM User_involves_match WHERE userID = :id) \n " +
                   "u LEFT JOIN Match_Has ON Match_Has.matchID = u.matchID LEFT JOIN \n" +
                   " Round_Has ON Match_Has.roundID = Round_Has.roundID LEFT JOIN Tournament \n" +
                   " ON Round_Has.tournamentID = Tournament.tournamentID " +
                   "WHERE Match_Has.isPublished = 1;",
            nativeQuery = true)
    List<UserMatchTournamentInfo> findPublishedMatchesByUserID(int id);

    @Query(
            value = "select userID \n" +
                    "from user_involves_match u join (select * from match_has where roundID in \n" +
                    "(select roundID from match_has where matchID = :oldMatchID)\n" +
                    " and userID_1 in ((select userID_1 from match_has where matchID = :oldMatchID),\n" +
                    "(select userID_2 from match_has where matchID = :oldMatchID)) \n" +
                    "and userID_2 in ((select userID_1 from match_has where matchID = :oldMatchID),\n" +
                    "(select userID_2 from match_has where matchID = :oldMatchID))) s on s.matchID = u.matchID \n" +
                    "LEFT JOIN Round_Has ON s.roundID = Round_Has.roundID LEFT JOIN Tournament \n" +
                    "ON Round_Has.tournamentID = Tournament.tournamentID where results = 1;",
            nativeQuery = true
    )
    List<Integer> findUserWinsInSeries(int oldMatchID);

    @Query(  value ="SELECT u.results, u.attendance, m.matchID, m.start_time, m.end_time,\n" +
            "Tournament.name, Tournament.location, Tournament.description \n" +
            "FROM (SELECT * FROM User_involves_match WHERE userID = :id) u \n" +
            "JOIN (SELECT * FROM Match_Has WHERE end_time <= NOW()) m ON m.matchID = u.matchID LEFT JOIN \n" +
            " Round_Has ON m.roundID = Round_Has.roundID LEFT JOIN Tournament \n" +
            " ON Round_Has.tournamentID = Tournament.tournamentID;",
            nativeQuery = true)
    List<UserMatchTournamentInfo> findPastMatchesByUserID(int id);

    @Query(  value ="SELECT u.results, u.attendance, m.matchID, m.start_time, m.end_time,\n" +
            "Tournament.name, Tournament.location, Tournament.description \n" +
            "FROM (SELECT * FROM User_involves_match WHERE userID = :id) u \n" +
            "JOIN (SELECT * FROM Match_Has) m ON m.matchID = u.matchID LEFT JOIN \n" +
            " Round_Has ON m.roundID = Round_Has.roundID LEFT JOIN Tournament \n" +
            " ON Round_Has.tournamentID = Tournament.tournamentID WHERE Round_Has.tournamentID = :t_id",
            nativeQuery = true)
    List<UserMatchTournamentInfo> findPastMatchesInTournamentByUserID(int id, int t_id);

    @Query(value = "SELECT m.matchID \n" +
            "FROM (SELECT * FROM User_involves_match WHERE userID = :id) u \n" +
            "JOIN (SELECT * FROM Match_Has WHERE end_time <= NOW()) m ON m.matchID = u.matchID LEFT JOIN \n" +
            " Round_Has ON m.roundID = Round_Has.roundID WHERE Round_Has.tournamentID = :t_id",
            nativeQuery = true)
    List<Integer> findPastTournamentMatchIDsByUserID(int id, int t_id);

    @Query(value = "SELECT m.matchID \n" +
            "FROM (SELECT * FROM User_involves_match WHERE userID = :id AND results > :resultGreaterThan) u \n" +
            "JOIN (SELECT * FROM Match_Has) m ON m.matchID = u.matchID LEFT JOIN \n" +
            " Round_Has ON m.roundID = Round_Has.roundID WHERE Round_Has.tournamentID = :t_id",
            nativeQuery = true)
    List<Integer> findTournamentMatchIDsByUserIDWithResultsGreaterThan(int id, int t_id, int resultGreaterThan);

    @Query(value = "SELECT m.matchID, m.start_time, m.end_time, Round_Has.roundNumber, \n" +
            " Tournament.name, Tournament.location, Tournament.description \n" +
            "FROM (SELECT * FROM Match_Has WHERE matchID = :id) m JOIN Round_Has ON m.roundID = Round_Has.roundID \n" +
            "LEFT JOIN Tournament ON Round_Has.tournamentID = Tournament.tournamentID;",
            nativeQuery = true)
    UserMatchTournamentInfo findMatchInfoByMatchID(int id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE User_involves_match SET User_involves_match.attendance = :attendance \n" +
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

    @Transactional
    @Modifying
    @Query( value = "UPDATE User_involves_match SET User_involves_match.results = :results \n" +
       "User_involves_match.attendance = :attendance WHERE User_involves_match.matchID = :mid \n " +
            "AND User_involves_match.userID = :uid", nativeQuery = true)
    void updateMatchUserInformation(int mid, int uid, String results, String attendance);

    @Query(value ="SELECT u.userID as id, u.results as resultText, u.attendance as status, p.name\n " +
            "FROM User_involves_match u \n" +
            "JOIN (SELECT * FROM Match_Has WHERE Match_Has.matchID = :matchID) m ON m.matchID = u.matchID JOIN \n" +
            " Round_Has r ON m.roundID = r.roundID JOIN Tournament t\n" +
            " ON r.tournamentID = t.tournamentID JOIN User p ON u.userID = p.userID", nativeQuery = true)
    List<IParticipantInfo> getUserMatchInfoByMatchID(int matchID);





    @Query(value ="SELECT MIN(m.matchID) as id, t.name as name, m.roundID as tournamentRoundText " +
            "FROM Match_Has m JOIN \n"
            + "Round_Has r ON m.roundID = r.roundID JOIN (SELECT * FROM Tournament WHERE \n" +
            "Tournament.tournamentID = :tournamentID) t on t.tournamentID = r.tournamentID group by m.userID_1,\n" +
            "m.userID_2, m.roundID;", nativeQuery = true)
    List<UserMatchTournamentRepository.IBracketMatchInfo> getBracketMatchInfoByTournamentID(int tournamentID);

    @Query(value ="SELECT matchID as next from Match_Has where roundID = (SELECT roundID FROM Round_Has WHERE " +
            "roundNumber = \n"
            + "(SELECT roundNumber from Round_Has where roundID = :oldRoundID) + 1\n" +
            " AND tournamentID = (SELECT tournamentID from Round_Has where roundID = :oldRoundID)) \n" +
            " AND ((userID_1 in ((select userID_1 from Match_Has where matchID = :oldMatchID),\n " +
            "(select userID_2 from Match_Has where\n" +
            " matchID = :oldMatchID))) OR (userID_2 in ((select userID_1 from Match_Has where matchID = :oldMatchID),\n"
            + "(select userID_2 from Match_Has where\n" +
            " matchID = :oldMatchID))));", nativeQuery = true)
    NumQuery getNextMatchID(int oldMatchID, int oldRoundID);


    @Query(value = "SELECT userID As winner from (SELECT userID, count(*) as count FROM \n" +
            " Match_Has m JOIN User_involves_match u ON m.matchID = u.matchID \n" +
            " WHERE roundID = :oldRoundID and userID in ((select userID_1 from Match_Has where \n" +
            "matchID = :oldMatchID),(select userID_2 from Match_Has where matchID = :oldMatchID)) \n" +
            "and results = 1 \n" +
            " group by userID) \n " +
            "w order by count desc LIMIT 1;", nativeQuery = true)
    WinnerID getWinnerUserID(int oldMatchID, int oldRoundID);


    @Query(value = "SELECT u.name AS winner FROM User u JOIN ( SELECT userID From User_involves_match \n " +
            "WHERE matchID = :oldMatchID \n" +
            " AND results = 1) m ON u.userID = m.userID", nativeQuery = true)
    WinnerName getWinnerName(int oldMatchID);

    @Query(value = "SELECT roundNumber AS roundNumber FROM Round_Has where roundID = :roundID", nativeQuery = true)
    RoundNumber getRoundNumber(int roundID);

    @Query(value = "SELECT userID As loser from (SELECT userID, count(*) as count FROM \n" +
            " Match_Has m JOIN User_involves_match u ON m.matchID = u.matchID \n" +
            " WHERE roundID = :oldRoundID and userID in ((select userID_1 from Match_Has where \n" +
            "matchID = :oldMatchID),(select userID_2 from Match_Has where matchID = :oldMatchID)) \n" +
            "and results = 2 group by userID) \n " +
            "w order by count desc LIMIT 1;", nativeQuery = true)
    LoserID getLoserUserID(int oldMatchID, int oldRoundID);

    @Query(value = "SELECT MIN(j.min) as next from (SELECT Min(matchID) as min from (SELECT roundID FROM Round_Has \n" +
            "WHERE tournamentID = (SELECT tournamentID from Round_Has\n" +
            "where roundID = :oldRoundID)) r JOIN (select * from Match_Has\n" +
            "WHERE ((:winnerID in (userID_1, userID_2)) and \n" +
            "(:loserID not in(userID_1,userID_2)))\n" +
            "and matchID > :oldMatchID) m ON r.roundID = m.roundID\n" +
            "UNION\n" +
            "SELECT MIN(matchID) from (SELECT roundID FROM Round_Has \n" +
            "WHERE tournamentID = (SELECT tournamentID from Round_Has\n" +
            "where roundID = :oldRoundID)) r JOIN (select * from Match_Has\n" +
            "WHERE ((:winnerID in (userID_1, userID_2)))\n" +
            "and roundID > :oldRoundID) m ON r.roundID = m.roundID) j;\n", nativeQuery = true)
    NumQuery getNextWinnerMatchID( int oldRoundID, int winnerID, int loserID, int oldMatchID);

    @Query(value = "SELECT MIN(matchID) as next from (SELECT roundID FROM Round_Has \n"+
            "WHERE tournamentID = (SELECT tournamentID from Round_Has \n"+
            "where roundID = :oldRoundID)) r JOIN (select * from Match_Has \n" +
            "WHERE ((:winnerID in (userID_1, userID_2)) and (:loserID not in(userID_1,userID_2))) \n"+
            "and matchID > :oldMatchID)  m ON r.roundID = m.roundID;", nativeQuery = true)
    NumQuery getNextWinnerMatchIDMultipleMatchesPerRound( int oldRoundID, int winnerID, int loserID, int oldMatchID);


    @Query(value = "SELECT MIN(matchID) as next from (SELECT roundID FROM Round_Has \n"+
            "WHERE tournamentID = (SELECT tournamentID from Round_Has \n"+
            "where roundID = :oldRoundID)) r JOIN (select * from Match_Has \n" +
            "WHERE ((:LoserID in (userID_1, userID_2)) and (:winnerID not in(userID_1,userID_2))) \n"+
            "and matchID > :oldMatchID) m ON r.roundID = m.roundID;", nativeQuery = true)
    NumQuery getNextLoserMatchID( int oldRoundID, int winnerID,int LoserID, int oldMatchID);


    interface NumQuery{
        Integer getNext();
    }

    interface WinnerID{
        Integer getWinner();
    }

    interface WinnerName{
        String getWinner();
    }

    interface RoundNumber{
        Integer getRoundNumber();
    }

    interface LoserID{
        Integer getLoser();
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
