package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.repository.AvailabilityRepository;
import com.zoomers.GameSetMatch.services.DTO.ParticipantAvailabilityForADayInfo;
import com.zoomers.GameSetMatch.services.Errors.ProposedMatchChangeConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    @Autowired
    AvailabilityRepository availabilityRepository;

    public void checkNewMatchTime(int tournamentID, int matchID, String newMatchAsAvailabilityString, int dayOfWeek) throws ProposedMatchChangeConflictException {
        List<ParticipantAvailabilityForADayInfo> participants = availabilityRepository.getParticipantsAvailabilityForADay(tournamentID, matchID, dayOfWeek);

        int newMatchSlot = Integer.parseInt(newMatchAsAvailabilityString, 2);

        for (ParticipantAvailabilityForADayInfo participant : participants) {
            int participantsAvail = Integer.parseInt(participant.getAvailabilityString(), 2);

            if ((participantsAvail & newMatchSlot) == 0) {
                throw new ProposedMatchChangeConflictException(String.format("Conflicts with %s's availability", participant.getName()));
            }
        }
    }

}
