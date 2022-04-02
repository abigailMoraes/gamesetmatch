package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.UserMatchTournamentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserMatchTournamentRepository extends JpaRepository<UserMatchTournamentInfo, Long> {


    @Query(value = "SELECT * FROM Tournament WHERE status = 5 AND + \n"
            +"EXISTS (SELECT * FROM user_registers_tournament WHERE user_registers_tournament.userID = :userID \n" +
            " AND Tournament.tournamentID = user_registers_tournament.tournamentID)",
            nativeQuery = true)
    List<UserMatchTournamentInfo> getCompletedTournamentsForUser(int userID);

    @Query(
           value ="SELECT u.results, u.attendance, Match_Has.matchID, Match_Has.start_time, Match_Has.end_time,\n" +
                   "Round_Has.roundNumber,t.name,t.location,t.description \n" +
                   "FROM (SELECT * FROM User_involves_match WHERE userID = :id) \n " +
                   "u LEFT JOIN Match_Has ON Match_Has.matchID = u.matchID LEFT JOIN \n" +
                   " Round_Has ON Match_Has.roundID = Round_Has.roundID LEFT JOIN (SELECT * FROM Tournament WHERE \n"+
                   " status >= :status) t \n" +
                   " ON Round_Has.tournamentID = t.tournamentID;",
            nativeQuery = true)
    List<UserMatchTournamentInfo> findMatchesByUserID(int id, int status);

    @Query(  value ="SELECT u.results, u.attendance, m.matchID, m.start_time, m.end_time,\n" +
            "t.name, t.location, t.description \n" +
            "FROM (SELECT * FROM User_involves_match WHERE userID = :id) u \n" +
            "JOIN (SELECT * FROM Match_Has WHERE end_time <= NOW()) m ON m.matchID = u.matchID LEFT JOIN \n" +
            " Round_Has ON m.roundID = Round_Has.roundID LEFT JOIN (SELECT * FROM Tournament WHERE \n" +
            "status >= :status) t\n" +
            " ON Round_Has.tournamentID = t.tournamentID;",
            nativeQuery = true)
    List<UserMatchTournamentInfo> findPastMatchesByUserID(int id, int status);

    @Query(  value ="SELECT m.matchID \n" +
            "FROM (SELECT * FROM User_involves_match WHERE userID = :id) u \n" +
            "JOIN (SELECT * FROM Match_Has WHERE end_time <= NOW()) m ON m.matchID = u.matchID LEFT JOIN \n" +
            " Round_Has ON m.roundID = Round_Has.roundID WHERE Round_Has.tournamentID = :t_id",
            nativeQuery = true)
    List<Integer> findPastTournamentMatchIDsByUserID(int id, int t_id);

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

    @Transactional
    @Modifying
    @Query( value = "UPDATE user_involves_match SET user_involves_match.results = :results \n" +
       "User_involves_match.attendance = :attendance WHERE User_involves_match.matchID = :mid \n " +
            "AND User_involves_match.userID = :uid", nativeQuery = true)
    void updateMatchUserInformation(int mid, int uid, String results, String attendance);

    @Query(value ="SELECT u.userID as id, u.results as resultText, u.attendance as status, p.name\n " +
            "FROM user_involves_match u \n" +
            "JOIN (SELECT * FROM match_has WHERE match_has.matchID = :matchID) m ON m.matchID = u.matchID JOIN \n" +
            " round_has r ON m.roundID = r.roundID JOIN tournament t\n" +
            " ON r.tournamentID = t.tournamentID JOIN User p ON u.userID = p.userID", nativeQuery = true)
    List<IParticipantInfo> getUserMatchInfoByMatchID(int matchID);


    @Query(value ="SELECT m.matchID as id, t.name as name, m.roundID as tournamentRoundText, m.start_time as \n" +
            "startTime " +
            "FROM match_has m JOIN \n"
            + "round_has r ON m.roundID = r.roundID JOIN (SELECT * FROM tournament WHERE \n" +
            "tournament.tournamentID = :tournamentID) t on t.tournamentID = r.tournamentID group by m.userID_1,\n" +
            "m.userID_2, m.roundID;", nativeQuery = true)
    List<UserMatchTournamentRepository.IBracketMatchInfo> getBracketMatchInfoByTournamentID(int tournamentID);

    @Query(value ="SELECT matchID as next from match_has where roundID = (SELECT roundID FROM round_has WHERE " +
            "roundNumber = \n"
            + "(SELECT roundNumber from round_has where roundID = :oldRoundID) + 1\n" +
            " AND tournamentID = (SELECT tournamentID from round_has where roundID = :oldRoundID)) \n" +
            " AND ((userID_1 in ((select userID_1 from match_has where matchID = :oldMatchID),\n " +
            "(select userID_2 from match_has where\n" +
            " matchID = :oldMatchID))) OR (userID_2 in ((select userID_1 from match_has where matchID = :oldMatchID),\n"
            + "(select userID_2 from match_has where\n" +
            " matchID = :oldMatchID))));", nativeQuery = true)
    NumQuery getNextMatchID(int oldMatchID, int oldRoundID);


    @Query(value = "SELECT userID As winner from (SELECT userID, count(*) as count FROM \n" +
            " match_has m JOIN user_involves_match u ON m.matchID = u.matchID \n" +
            " WHERE roundID = :oldRoundID and userID in ((select userID_1 from match_has where \n" +
            "matchID = :oldMatchID),(select userID_2 from match_has where matchID = :oldMatchID)) \n" +
            "and results = 'Win' group by userID) \n " +
            "w order by count desc LIMIT 1;", nativeQuery = true)
    WinnerID getWinnerUserID(int oldMatchID, int oldRoundID);


    @Query(value = "SELECT u.name AS winner FROM User u JOIN ( SELECT userID From user_involves_match \n " +
            "WHERE matchID = :oldMatchID \n" +
            " AND results = 'Win') m ON u.userID = m.userID", nativeQuery = true)
    WinnerName getWinnerName(int oldMatchID);

    @Query(value = "SELECT roundNumber AS roundNumber FROM round_has where roundID = :roundID", nativeQuery = true)
    RoundNumber getRoundNumber(int roundID);

    @Query(value = "SELECT userID As loser from (SELECT userID, count(*) as count FROM \n" +
            " match_has m JOIN user_involves_match u ON m.matchID = u.matchID \n" +
            " WHERE roundID = :oldRoundID and userID in ((select userID_1 from match_has where \n" +
            "matchID = :oldMatchID),(select userID_2 from match_has where matchID = :oldMatchID)) \n" +
            "and results = 'Loss' group by userID) \n " +
            "w order by count desc LIMIT 1;", nativeQuery = true)
    LoserID getLoserUserID(int oldMatchID, int oldRoundID);

    @Query(value = "SELECT MIN(matchID) as next from (SELECT roundID FROM round_has \n"+
            "WHERE tournamentID = (SELECT tournamentID from round_has \n"+
            "where roundID = :oldRoundID)) r JOIN (select * from match_has \n" +
            "WHERE ((:winnerID in (userID_1, userID_2)) and (:loserID not in(userID_1,userID_2))) \n"+
            "and matchID > :oldMatchID) m ON r.roundID = m.roundID;", nativeQuery = true)
    NumQuery getNextWinnerMatchID( int oldRoundID, int winnerID, int loserID, int oldMatchID);

    @Query(value = "SELECT MIN(matchID) as next from (SELECT roundID FROM round_has \n"+
            "WHERE tournamentID = (SELECT tournamentID from round_has \n"+
            "where roundID = :oldRoundID)) r JOIN (select * from match_has \n" +
            "WHERE ((:winnerID in (userID_1, userID_2)) and (:loserID not in(userID_1,userID_2))) \n"+
            "and matchID > :oldMatchID)  m ON r.roundID = m.roundID;", nativeQuery = true)
    NumQuery getNextWinnerMatchIDMultipleMatchesPerRound( int oldRoundID, int winnerID, int loserID, int oldMatchID);


    @Query(value = "SELECT MIN(matchID) as next from (SELECT roundID FROM round_has \n"+
            "WHERE tournamentID = (SELECT tournamentID from round_has \n"+
            "where roundID = :oldRoundID)) r JOIN (select * from match_has \n" +
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
