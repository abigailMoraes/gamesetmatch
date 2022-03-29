package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.RoundHas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoundHasRepository extends JpaRepository<RoundHas, Integer> {

    @Query (
            value = "SELECT DISTINCT r.tournamentID FROM round_has r WHERE end_date = STR_TO_DATE(:date, '%Y-%m-%d %T')",
            nativeQuery = true
    )
    List<Integer> findRoundsPastEndDate(String date);

    @Query (
            value = "SELECT * FROM round_has r WHERE end_date = current_date",
            nativeQuery = true
    )
    List<RoundHas> findRoundsPastEndDate1();
}