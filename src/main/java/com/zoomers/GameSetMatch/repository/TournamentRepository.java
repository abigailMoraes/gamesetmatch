package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.Tournament;
import com.zoomers.GameSetMatch.scheduler.domain.MockTournament;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentFormat;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import java.util.Date;
import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Integer> {

    @Query(value = "SELECT * FROM Tournament WHERE status = ?1 AND admin_hosts_tournament = ?2", nativeQuery = true)
    List<Tournament> findTournaments(int status, int id);

    /*@Query(value = "SELECT t.tournamentID, t.type, t.format, t.match_by_skill, t.match_duration, t.start_date " +
            "FROM Tournament t WHERE t.tournamentID = :id", nativeQuery = true)*/
    @Query(nativeQuery = true, value = "SELECT" +
    "t.tournamentID AS tournamentID, t.format")
    MockTournament getSchedulerTournamentByID(int id);

    /*@SqlResultSetMapping(
            name="TournamentResult",
            classes={
                    @ConstructorResult(
                            targetClass=com.zoomers.GameSetMatch.scheduler.domain.MockTournament.class,
                            columns={
                                    @ColumnResult(name="tournamentID", type=Integer.class),
                                    @ColumnResult(name="tournamentFormat", type=TournamentFormat.class),
                                    @ColumnResult(name="tournamentSeries", type=TournamentSeries.class),
                                    @ColumnResult(name="matchBySkill", type=Integer.class),
                                    @ColumnResult(name="matchDuration", type=Integer.class),
                                    @ColumnResult(name="startDate", type=Date.class)
                            }
                    )
            }
    )*/
}
