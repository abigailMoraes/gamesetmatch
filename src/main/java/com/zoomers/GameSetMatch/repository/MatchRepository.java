package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface MatchRepository extends JpaRepository<Match,Integer> {

    @Transactional
    @Modifying
    @Query(value="UPDATE Match_Has SET Match_Has.start_time = :startTime, Match_Has.end_time = :endTime\n" +
            "WHERE Match_Has.matchID = :matchID",
         nativeQuery = true)
    void updateMatchInfo( int matchID, LocalDateTime startTime, LocalDateTime endTime);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Match_Has SET Match_Has.isPublished = TRUE WHERE Match_Has.matchID = :matchID", nativeQuery = true)
    void updateMatchPublishStatus(int matchID);

    @Query(value = "SELECT * FROM Match_Has WHERE roundID = :roundID", nativeQuery = true)
    List<Match> getMatchesByRound(int roundID);

    @Query(value = "SELECT * FROM Match_Has WHERE matchID = :matchID", nativeQuery = true)
    List<Match> getMatchesByID(int matchID);

    @Query(value = "SELECT * FROM Match_Has " +
            "WHERE (userID_1 = :userID OR userID_2 = :userID) AND start_time >= NOW()",
            nativeQuery = true)
    List<Match> getTournamentMatchesByUserID(int userID);

    @Query(value = "SELECT * FROM Match_Has " +
            "WHERE (userID_1 = :userID OR userID_2 = :userID) AND start_time = :startTime",
            nativeQuery = true)
    List<Match> getMatchByUserIDAndTime(int userID, LocalDateTime startTime);

    @Query(nativeQuery = true, value = "INSERT INTO Match_Has VALUES :startTime, :endTime, :roundID, :matchStatus, :userOneID, :userTwoID")
    void addMatch(LocalDateTime startTime, LocalDateTime endTime, int roundID, int matchStatus, int userOneID, int userTwoID);


    @Query(value = "SELECT * FROM Match_Has JOIN (SELECT * FROM Round_Has WHERE tournamentID = :tournamentID) r ON \n"+
    "Match_Has.roundID = r.roundID", nativeQuery = true)
    List<Match> getMatchesByTournamentID(int tournamentID);

    @Query(value = "select n.matchNumber as next From (SELECT row_number() OVER ( order by matchID ) " +
            "as matchNumber, \n" +
            "matchID FROM (select * from match_has where roundID = \n " +
            "(Select roundID from match_has where matchID = :currMatchID) and userID_1 in \n"+
            "((Select userID_1 from match_has where matchID = :currMatchID), \n" +
            "(Select userID_2 from match_has where matchID = :currMatchID)) \n" +
            "and userID_2 in ((select userID_1 from match_has where matchID = :currMatchID), \n" +
            "(select userID_2 from match_has where matchID = :currMatchID))) q ) n where n.matchID = :currMatchID\n",
            nativeQuery = true)
    UserMatchTournamentRepository.NumQuery getMatchNumber(int currMatchID);

}
