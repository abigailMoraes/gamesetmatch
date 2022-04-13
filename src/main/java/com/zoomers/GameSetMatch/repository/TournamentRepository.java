package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.Tournament;
import com.zoomers.GameSetMatch.scheduler.domain.MockTournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.LinkedHashSet;
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


    @Query(value ="SELECT Tournament.name FROM Tournament WHERE Tournament.tournamentID = ?1",nativeQuery = true)
    String getNameByTournamentID(Integer id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Tournament SET status = :status, current_round = :roundNumber " +
            "WHERE tournamentID = :tournamentID", nativeQuery = true)
    void updateTournament(int tournamentID, int status, int roundNumber);

    @Query( value = "SELECT t.tournamentID FROM Tournament t WHERE status = :closedStatus OR " +
            "(close_registration_date = current_date AND status = :openStatus)",
            nativeQuery = true
    )
    LinkedHashSet<Integer> getTournamentsPastCloseRegistrationDate(int closedStatus, int openStatus);

    @Transactional
    @Modifying
    @Query ( value = "UPDATE Tournament t SET t.status = :status WHERE t.tournamentID = :tournamentID",
            nativeQuery = true

    )
    void setTournamentStatus (int status, int tournamentID);


    @Query(value = "SELECT * FROM Tournament t " +
            "INNER JOIN User_registers_tournament u ON u.tournamentID = t.tournamentID " +
            "WHERE status < :status " +
            "AND u.userID = :userID ",
            nativeQuery = true)
    List<Tournament> findRegisteredTournamentsForUser(int userID, int status);

    @Query(value = "SELECT * FROM Tournament WHERE status = :status AND + \n"
            + "EXISTS (SELECT * FROM User_registers_tournament WHERE User_registers_tournament.userID = :userID \n" +
            " AND Tournament.tournamentID = User_registers_tournament.tournamentID)",
            nativeQuery = true)
    List<Tournament> findCompletedTournamentsForUser(int userID, int status);

    @Query(value = "SELECT count(*) as next FROM Tournament WHERE status = :status AND + \n"
            + "EXISTS (SELECT * FROM User_registers_tournament WHERE User_registers_tournament.userID = :userID \n" +
            " AND Tournament.tournamentID = User_registers_tournament.tournamentID)",
            nativeQuery = true)
    UserMatchTournamentRepository.NumQuery getNumberOfCompletedTournamentsForUser(int userID, int status);

    @Query(value = "SELECT count(*) as next FROM Tournament WHERE status = :status AND + \n"
            + "EXISTS (SELECT * FROM User_registers_tournament WHERE User_registers_tournament.userID = :userID \n" +
            " AND Tournament.tournamentID = User_registers_tournament.tournamentID AND player_status = :safeStatus)",
            nativeQuery = true)
    UserMatchTournamentRepository.NumQuery getNumberOfTournamentsWonByUser(int userID, int status, int safeStatus);
}
