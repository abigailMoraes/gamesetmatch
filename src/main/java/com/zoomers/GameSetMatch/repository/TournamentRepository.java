package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.SchedulerTournament;
import com.zoomers.GameSetMatch.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Integer> {

    @Query(value = "SELECT * FROM Tournament WHERE status = ?1 AND admin_hosts_tournament = ?2", nativeQuery = true)
    List<Tournament> findTournaments(int status, int id);

    @Query(value = "SELECT t.tournamentID, t.type, t.format, t.match_by_skill, t.match_duration, t.start_date " +
            "FROM Tournament t WHERE t.tournamentID = :id", nativeQuery = true)
    SchedulerTournament getSchedulerTournamentByID(int id);
}
