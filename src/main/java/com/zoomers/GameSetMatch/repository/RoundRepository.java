package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoundRepository extends JpaRepository<Round, Integer>{

    @Query(value="SELECT * FROM round_has WHERE round_has.tournamentID = :tournamentID",nativeQuery = true)
    List<Round> getRoundsByID(int tournamentID);

    @Query(value ="SELECT Round_Has.roundNumber FROM Round_Has WHERE Round_Has.roundID = ?1",nativeQuery = true)
    Integer getRoundNumberByRoundID(int roundID);

    @Query(value ="SELECT Round_Has.tournamentID FROM Round_Has WHERE Round_Has.roundID = ?1",nativeQuery = true)
    Integer getTournamentIDByRoundID(int roundID);
}
