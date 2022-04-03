package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.controller.Tournament.RequestBody.AvailabilityDTO;
import com.zoomers.GameSetMatch.entity.Availability;
import com.zoomers.GameSetMatch.entity.AvailabilityID;
import com.zoomers.GameSetMatch.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class AvailabilityService {
    @Autowired
    AvailabilityRepository availability;

    @Transactional
    public void saveAvailabilities(Integer tournamentID, Integer userID, List<AvailabilityDTO> incomingAvailabilities) {
        List<Availability> availabilities = new ArrayList<>();

        for (AvailabilityDTO availability : incomingAvailabilities) {
            String availabilityString = availability.getAvailabilityString();
            int dayOfWeek = availability.getDayOfWeek();
            Availability newAvailability = new Availability(tournamentID, userID, dayOfWeek, availabilityString);
            availabilities.add(newAvailability);
        }

        availability.saveAll(availabilities);
    }

    @Transactional
    public void updateUsersAvailabilityForTournament(Integer tournamentID, Integer userID, List<AvailabilityDTO> incomingAvailabilities){
        for (AvailabilityDTO updatedAvailability : incomingAvailabilities) {
            AvailabilityID aID = new AvailabilityID(userID, tournamentID, updatedAvailability.getDayOfWeek());
            Availability a = availability.getById(aID);
            if(!isNull(a)){
                a.setAvailability(updatedAvailability.getAvailabilityString());
                availability.save(a);
            }
        }
    }


    public List<String> getPlayerAvailabilities(int r_id, int t_id) {
        return availability.findRegistrantAvailability(r_id, t_id);
    }

    public List<AvailabilityDTO> getUsersAvailabilityForTournament(int userID, int t_id) {
        return availability.getUsersAvailabilityForTournament(userID, t_id);
    }

    @Transactional
    public void deleteByTournamentID(Integer tournamentID) {
        availability.deleteAvailabilitiesByTournamentID(tournamentID);
    }
}
