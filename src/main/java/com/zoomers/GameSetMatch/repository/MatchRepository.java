package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.Match;
import com.zoomers.GameSetMatch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Integer> {
    @Query(
           value ="SELECT match_has.matchID,match_has.result, match_has.start_time, match_has.end_time,\n" +
                   "match_has.duration,round_has.type,tournament.name,tournament.location,tournament.description \n" +
                   "FROM (SELECT * FROM user_involves_match WHERE userID = :id) \n " +
                   "u LEFT JOIN match_has ON match_has.matchID = u.matchID LEFT JOIN \n" +
                   " round_has ON match_has.roundID = round_has.roundID LEFT JOIN tournament \n" +
                   " ON round_has.tournamentID = tournament.tournamentID;",
            nativeQuery = true)
    List<Match> findMatchesByUserID(int id);

    @Query(  value ="SELECT m.matchID,m.result, m.start_time, m.end_time,\n" +
            "m.duration,round_has.type,tournament.name,tournament.location,tournament.description \n" +
            "FROM (SELECT * FROM user_involves_match WHERE userID = :id) u \n " +
            "JOIN (SELECT * FROM match_has WHERE end_time <= NOW()) m ON m.matchID = u.matchID LEFT JOIN \n" +
            " round_has ON m.roundID = round_has.roundID LEFT JOIN tournament \n" +
            " ON round_has.tournamentID = tournament.tournamentID;",
            nativeQuery = true)
    List<Match> findPastMatchesByUserID(int id);

    @Query(  value ="SELECT m.matchID, m.result, m.start_time, m.end_time, m.duration, round_has.type, \n" +
            " tournament.name, tournament.location, tournament.description \n" +
            "FROM (SELECT * FROM match_has WHERE matchID = :id) m JOIN round_has ON m.roundID = round_has.roundID \n" +
            "LEFT JOIN tournament ON round_has.tournamentID = tournament.tournamentID;",
            nativeQuery = true)
    Match findMatchInfoByMatchID(int id);


}
