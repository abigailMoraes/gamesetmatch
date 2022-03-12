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
            int availabilityString = convertToInt(availability.getSlots());
            Date date = availability.getDate();
            Availability newAvailability = new Availability(tournamentID, userID, date, availabilityString);
            availabilities.add(newAvailability);
        }

        availability.saveAll(availabilities);
    }

    private int convertToInt(List<Boolean> booleanList) {
        String stringAvailabilty = "";
        for (Boolean b : booleanList) {
            stringAvailabilty += b ? 1 : 0;
        }
        return Integer.parseInt(stringAvailabilty, 2);
    }
}
