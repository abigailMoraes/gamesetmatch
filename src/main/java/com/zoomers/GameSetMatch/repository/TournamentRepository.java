package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.Tournament;
import com.zoomers.GameSetMatch.scheduler.domain.MockTournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Integer> {

    @Query(value = "SELECT * FROM Tournament WHERE status = ?1 AND admin_hosts_tournament = ?2", nativeQuery = true)
    List<Tournament> findTournaments(int status, int id);

    @Query(value = "SELECT * FROM Tournament WHERE status = :status", nativeQuery = true)
    List<Tournament> findByStatus(int status);

    @Query(nativeQuery = true)
    MockTournament getMockTournamentByID(@Param("tournamentID") Integer tournamentID);

    void deleteTournamentByTournamentID(Integer id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Tournament SET status = :status, current_round = :roundNumber " +
            "WHERE tournamentID = :tournamentID", nativeQuery = true)
    void updateTournament(int tournamentID, int status, int roundNumber);
}
