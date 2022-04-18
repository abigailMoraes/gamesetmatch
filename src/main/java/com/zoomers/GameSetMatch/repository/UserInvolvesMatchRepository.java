package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.controller.Match.ResponseBody.UsersMatchInfo;
import com.zoomers.GameSetMatch.entity.UserInvolvesMatch;
import com.zoomers.GameSetMatch.scheduler.enumerations.PlayerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserInvolvesMatchRepository extends JpaRepository<UserInvolvesMatch, Integer> {

    @Query(nativeQuery = true)
    List<UsersMatchInfo> getUsersMatchInfoForCalendar(@Param("matchID") Integer matchID);

    List<UserInvolvesMatch> getUserInvolvesMatchByMatchID(int mID);

    @Query(value = "SELECT uim.matchID FROM " +
            "User_involves_match uim " +
            "INNER JOIN Match_Has m ON m.matchID = uim.matchID " +
            "WHERE m.roundID = :roundID " +
            "AND uim.results = :pendingStatus",
            nativeQuery = true)
    List<Integer> getPendingMatches(int roundID, int pendingStatus);


    @Query(value = "SELECT User_involves_match.results FROM User_involves_match WHERE User_involves_match.matchID " +
            "= :mID \n" +
            "AND User_involves_match.userID = :uID",
            nativeQuery = true)
    Integer findMatchResultByMatchIDAndUserID(int mID, int uID);
}