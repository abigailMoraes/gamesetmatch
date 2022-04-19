package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.UserRegistersTournament;
import com.zoomers.GameSetMatch.entity.UserRegistersTournamentID;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRegistersTournamentRepository extends JpaRepository<UserRegistersTournament, UserRegistersTournamentID> {
    List<UserRegistersTournament> findByUserID(Integer userID);

    @Query(value = "SELECT u.userID, u.name, u.email, r.skill_level FROM " +
            "User_registers_tournament r " +
            "INNER JOIN User u ON r.userID = u.userID " +
            "WHERE (r.tournamentID = :tournamentID)" +
            "ORDER BY u.name;",
            nativeQuery = true)
    List<IRegistrant> findRegistrantsByTournamentID(Integer tournamentID);

    @Query(nativeQuery = true)
    List<Registrant> getSchedulerRegistrantsByTournamentID(@Param("tournamentID") Integer tournamentID);

    @Query(value = "SELECT player_status FROM User_registers_tournament " +
            "WHERE userID = :id AND tournamentID = :t_id", nativeQuery = true)
    List<Integer> getPlayerStatusByTournamentID(int id, int t_id);

    @Query(value = "SELECT count(*) AS next FROM User WHERE userID IN (SELECT userID FROM User_registers_tournament \n"
            + "WHERE tournamentID = :tournamentID)",nativeQuery = true)
    UserMatchTournamentRepository.NumQuery getPlayersInTournament(Integer tournamentID);

    @Query(value = "SELECT * FROM User_registers_tournament " +
            "WHERE userID = :id AND tournamentID = :t_id", nativeQuery = true)
    List<UserRegistersTournament> getTournamentRegistrationForUser(int id, int t_id);


    //TODO: add skill level table that maps value to meaning e.g. skill 1 = beginner, 2 = intermediate...
    interface IRegistrant {
        Integer getUserID();
        String getName();
        String getEmail();
        Integer getSkillLevel();
    }

    void deleteUserRegistersTournamentsByTournamentID(Integer tournamentID);

    List<UserRegistersTournament> getUserRegistersTournamentsByTournamentID(Integer tournamentID);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM User_registers_tournament WHERE userID = :userID AND tournamentID = :tournamentID", nativeQuery = true)
    void removeRegistrationForUser(Integer tournamentID, Integer userID);

    @Query(value = "SELECT COUNT(*) FROM " +
            "User_registers_tournament r " +
            "WHERE (r.tournamentID = :tournamentID)",
            nativeQuery = true)
    Integer getNumberOfRegistrantsForATournament(Integer tournamentID);

    @Transactional
    @Modifying
    @Query(value="UPDATE User_registers_tournament u SET u.player_status = :newStatus " +
            "WHERE (u.userID = :currUserID AND u.tournamentID = :currTournamentID)",
    nativeQuery = true)
    void updatePlayerStatusForATournament(int currUserID, int currTournamentID, int newStatus);
}
