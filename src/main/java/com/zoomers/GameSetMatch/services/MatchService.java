package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingMatch;
import com.zoomers.GameSetMatch.entity.Match;
import com.zoomers.GameSetMatch.entity.Round;
import com.zoomers.GameSetMatch.entity.Tournament;
import com.zoomers.GameSetMatch.repository.AvailabilityRepository;
import com.zoomers.GameSetMatch.repository.MatchRepository;
import com.zoomers.GameSetMatch.repository.RoundRepository;
import com.zoomers.GameSetMatch.repository.TournamentRepository;
import com.zoomers.GameSetMatch.services.DTO.ParticipantAvailabilityForADayInfo;
import com.zoomers.GameSetMatch.services.Errors.ProposedMatchChangeConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class MatchService {

    @Autowired
    AvailabilityRepository availabilityRepository;

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    RoundRepository roundRepository;

    @Autowired
    TournamentRepository tournamentRepository;

    public void checkNewMatchTime(int tournamentID, int matchID, String newMatchAsAvailabilityString, int dayOfWeek) throws ProposedMatchChangeConflictException {
        List<ParticipantAvailabilityForADayInfo> participants = availabilityRepository.getParticipantsAvailabilityForADay(tournamentID, matchID, dayOfWeek);
        List<String> conflictsWith = new ArrayList<>();
        int newMatchSlot = Integer.parseInt(newMatchAsAvailabilityString, 2);

        for (ParticipantAvailabilityForADayInfo participant : participants) {
            int participantsAvail = Integer.parseInt(participant.getAvailabilityString(), 2);

            if ((participantsAvail & newMatchSlot) == 0) {
                conflictsWith.add(participant.getName());
            }
        }

        if (conflictsWith.size() > 0) {
            throw new ProposedMatchChangeConflictException(String.format("Conflicts with the availability for: %s", String.join(", ", conflictsWith)));
        }
    }

    @Transactional
    public void updateMatchesInARound(int tournamentID, int roundID, List<IncomingMatch> matches) throws EntityNotFoundException {
        LocalDateTime latestMatchDate = matches.get(0).getEndTime();
        LocalDateTime firstMatchDate = matches.get(0).getStartTime();
        List<Match> updatedMatches = new ArrayList<>();
        for (IncomingMatch match : matches) {
            System.out.println(match.getID());
            Match existingMatch = matchRepository.getById(match.getID());

            if (!isNull(existingMatch)) {
                existingMatch.setStartTime(match.getStartTime());
                existingMatch.setEndTime(match.getEndTime());
                existingMatch.setIsPublished(1);
                updatedMatches.add(existingMatch);
            } else {
                throw new EntityNotFoundException(String.format("Invalid Match ID: %d,", match.getID()));
            }

            if (latestMatchDate.isBefore(match.getEndTime())) {
                latestMatchDate = match.getEndTime();
            }

            if(firstMatchDate.isAfter(match.getStartTime())){
                firstMatchDate = match.getStartTime();
            }
        }

        Round round = roundRepository.getById(roundID);
        if (isNull(round)) {
            throw new EntityNotFoundException(String.format("Invalid Round ID: %d", roundID));
        }

        Tournament tournament = tournamentRepository.getById(tournamentID);

        if (isNull(tournament)) {
            throw new EntityNotFoundException(String.format("Invalid Tournament ID: %d", tournamentID));
        }

        Date roundStartDate = DateAndLocalDateService.localDateToDate(firstMatchDate.toLocalDate());
        Date roundEndDate = DateAndLocalDateService.localDateToDate(latestMatchDate.toLocalDate());
        Date nextRoundStartDate = DateAndLocalDateService
                .localDateToDate(latestMatchDate.toLocalDate().plusDays(DateAndLocalDateService.DaysBetweenRounds));

        round.setStartDate(roundStartDate);
        round.setEndDate(roundEndDate);
        tournament.setRoundStartDate(nextRoundStartDate);

        matchRepository.saveAll(updatedMatches);
        tournamentRepository.save(tournament);
        roundRepository.save(round);
    }

}
