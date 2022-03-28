package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.controller.Match.ResponseBody.UsersMatchInfo;
import com.zoomers.GameSetMatch.entity.UserInvolvesMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserInvolvesMatchRepository extends JpaRepository<UserInvolvesMatch, Integer> {

    @Query(nativeQuery = true)
    List<UsersMatchInfo> getUsersMatchInfoForCalendar(@Param("matchID") Integer matchID);

    @Query(value = "SELECT * FROM User_involves_match WHERE matchID = :matchID", nativeQuery = true)
    List<UserInvolvesMatch> findByMatchID(int matchID);
}
