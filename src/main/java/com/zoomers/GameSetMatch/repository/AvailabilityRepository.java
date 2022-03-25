package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.Availability;
import com.zoomers.GameSetMatch.entity.AvailabilityID;
import com.zoomers.GameSetMatch.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, AvailabilityID> {

    @Query(value = "SELECT availability_binary FROM Availability WHERE userID = :id", nativeQuery = true)
    List<Availability> findRegistrantAvailability(int id);
}
