package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.Availability;
import com.zoomers.GameSetMatch.entity.AvailabilityID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, AvailabilityID> {

    @Query(value = "SELECT availability_string FROM Availability WHERE userID = :id AND tournamentID = :tid ORDER BY day_of_week", nativeQuery = true)
    List<String> findRegistrantAvailability(int id, int tid);

    void deleteAvailabilitiesByTournamentID(Integer tournamentID);
}
