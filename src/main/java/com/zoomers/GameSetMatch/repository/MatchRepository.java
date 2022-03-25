package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match,Integer> {

    @Query(value="UPDATE match_has SET match_has.start_time = :startTime, match_has.end_time = :endTime,\n" +
            "match_has.duration = :duration, match_has.roundID = :roundID WHERE match_has.matchID = :matchID",
         nativeQuery = true)
    void updateMatchInfo( int matchID, String startTime, String endTime, long duration, int roundID);

    @Query(value = "SELECT * FROM match_has WHERE roundID = :roundID", nativeQuery = true)
    List<Match> getMatchesByRound(int roundID);

    @Query(value = "SELECT * FROM match_has WHERE matchID = :matchID", nativeQuery = true)
    List<Match> getMatchesByID(int matchID);

    @Query(value = "SELECT * FROM match_has " +
            "WHERE (userID_1 = :userID OR userID_2 = :userID) AND start_time >= NOW()",
            nativeQuery = true)
    List<Match> getUpcomingMatchesByUserID(int userID);
}
