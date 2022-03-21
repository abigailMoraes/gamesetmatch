package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface MatchRepository extends JpaRepository<Match,Integer> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO match_has " +
            "VALUES match_has.start_time = :startTime, match_has.end_time = :endTime, match_has.duration = duration",
            nativeQuery = true)
    void addMatch(String startTime, String endTime, int duration);
}
