package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoundHasRepository extends JpaRepository<Round, Integer> {

    @Query (
            value = "SELECT DISTINCT r.tournamentID FROM Round_has r WHERE end_date = STR_TO_DATE(:date, '%Y-%m-%d %T')",
            nativeQuery = true
    )
    List<Integer> findNextRoundTournamentId(String date);

}