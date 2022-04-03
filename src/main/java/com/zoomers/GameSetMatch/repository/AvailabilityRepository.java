package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.controller.Tournament.RequestBody.AvailabilityDTO;
import com.zoomers.GameSetMatch.entity.Availability;
import com.zoomers.GameSetMatch.entity.AvailabilityID;
import com.zoomers.GameSetMatch.services.DTO.ParticipantAvailabilityForADayInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, AvailabilityID> {

    @Query(value = "SELECT availability_string FROM Availability WHERE userID = :id AND tournamentID = :tid ORDER BY day_of_week", nativeQuery = true)
    List<String> findRegistrantAvailability(int id, int tid);

    @Query(nativeQuery = true)
    List<ParticipantAvailabilityForADayInfo> getParticipantsAvailabilityForADay(int tournamentID, int matchID, int dayOfWeek);

    void deleteAvailabilitiesByTournamentID(Integer tournamentID);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Availability WHERE userID = :userID AND tournamentID = :tournamentID", nativeQuery = true)
    void removeAvailabilityForUser(Integer tournamentID, Integer userID);

    @Query(nativeQuery = true)
    List<AvailabilityDTO> getUsersAvailabilityForTournament(int userID, int tournamentID);
}
