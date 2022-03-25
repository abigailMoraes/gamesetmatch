package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.controller.Tournament.RequestBody.IncomingAvailability;
import com.zoomers.GameSetMatch.entity.Availability;
import com.zoomers.GameSetMatch.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AvailabilityService {
    @Autowired
    AvailabilityRepository availability;

    public void saveAvailabilities(Integer tournamentID, Integer userID, List<IncomingAvailability> incomingAvailabilities) {
        List<Availability> availabilities = new ArrayList<>();

        for (IncomingAvailability availability : incomingAvailabilities) {
            String availabilityString = convertToString(availability.getSlots());
            int dayOfWeek = availability.getDayOfWeek();
            Availability newAvailability = new Availability(tournamentID, userID, dayOfWeek, availabilityString);
            availabilities.add(newAvailability);
        }

        availability.saveAll(availabilities);
    }

    private String convertToString(List<Boolean> booleanList) {
        String stringAvailability = "";
        for (Boolean b : booleanList) {
            stringAvailability += b ? 1 : 0;
        }
        return stringAvailability;
    }

    public List<String> getPlayerAvailabilities(int r_id) {
        return availability.findRegistrantAvailability(r_id);
    }
}
