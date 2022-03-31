package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.UserRegistersTournament;
import com.zoomers.GameSetMatch.entity.UserRegistersTournamentID;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRegistersTournamentRepository extends JpaRepository<UserRegistersTournament, UserRegistersTournamentID> {
    List<UserRegistersTournament> findByUserID(Integer userID);

    @Query(value = "SELECT u.userID, u.name, u.email, r.skill_level FROM " +
            "User_registers_tournament r " +
            "INNER JOIN User u on r.userID = u.userID " +
            "WHERE (r.tournamentID = :tournamentID)" +
            "ORDER BY u.name;",
            nativeQuery = true)
    List<IRegistrant> findRegistrantsByTournamentID(Integer tournamentID);

    @Query(nativeQuery = true)
    List<Registrant> getSchedulerRegistrantsByTournamentID(@Param("tournamentID") Integer tournamentID);

    @Query(value = "SELECT player_status FROM User_registers_tournament " +
            "WHERE userID = :id AND tournamentID = :t_id", nativeQuery = true)
    List<Integer> getPlayerStatusByTournamentID(int id, int t_id);

    //TODO: add skill level table that maps value to meaning e.g. skill 1 = beginner, 2 = intermediate...
    interface IRegistrant {
        Integer getUserID();
        String getName();
        String getEmail();
        String getSkillLevel();
    }

    void deleteUserRegistersTournamentsByTournamentID(Integer tournamentID);
}
