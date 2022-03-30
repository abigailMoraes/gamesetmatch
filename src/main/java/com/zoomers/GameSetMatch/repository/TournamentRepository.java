package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
    @Query(value = "SELECT * FROM Tournament WHERE status = ?1 AND admin_hosts_tournament = ?2", nativeQuery = true)
    List<Tournament> findTournaments(int status, int id);

    void deleteTournamentByTournamentID(Integer id);

    @Query(value = "SELECT * FROM Tournament WHERE status = 5 AND + \n"
            +"EXISTS (SELECT * FROM user_registers_tournament WHERE user_registers_tournament.userID = :userID \n" +
            " AND Tournament.tournamentID = user_registers_tournament.tournamentID)",
    nativeQuery = true)
    List<Tournament> findCompletedTournamentsForUser(int userID);
}
