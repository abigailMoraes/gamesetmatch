package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface RoundRepository extends JpaRepository<Round, Integer>{

    @Query(value="SELECT * FROM round_has WHERE round_has.tournamentID = :tournamentID",nativeQuery = true)
    List<Round> getRoundsByID(int tournamentID);

    @Query(value = "INSERT INTO round_has VALUES :endDate, :roundNumber, :startDate, :tournamentID", nativeQuery = true)
    void createRound(int tournamentID, Date endDate, int roundNumber, Date startDate);

    @Query(value = "SELECT MAX(roundID) FROM round_has WHERE tournamentID = :tournamentID", nativeQuery = true)
    int getLastTournamentRound(int tournamentID);
}
