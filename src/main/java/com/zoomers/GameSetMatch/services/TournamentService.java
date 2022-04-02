package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.entity.Tournament;
import com.zoomers.GameSetMatch.repository.RoundRepository;
import com.zoomers.GameSetMatch.repository.TournamentRepository;
import com.zoomers.GameSetMatch.repository.UserRegistersTournamentRepository;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentStatus;
import com.zoomers.GameSetMatch.services.Errors.InvalidActionForTournamentStatusException;
import com.zoomers.GameSetMatch.services.Errors.MinRegistrantsNotMetException;
import com.zoomers.GameSetMatch.services.Errors.MissingMatchResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class TournamentService {
    @Autowired
    private TournamentRepository tournament;

    @Autowired
    private AvailabilityService availability;

    @Autowired
    private UserRegistersTournamentService userRegistersTournament;

    @Autowired
    private UserInvolvesMatchService userInvolvesMatchService;

    @Autowired
    private SendEMailService sendEMailService;

    @Autowired
    private RoundRepository roundRepository;

    public List<Tournament> getAllTournaments() {
        return tournament.findAll();
    }

    public List<Tournament> findAllByStatus(int status) {
        return tournament.findByStatus(status);
    }

    public void saveTournament(Tournament tour) {
        tournament.save(tour);
    }

    public Optional<Tournament> findTournamentByID(Integer id) {
        return tournament.findById(Math.toIntExact(id));
    }

    public List<Tournament> getTournaments(int status, int id) {
        return tournament.findTournaments(status, id);
    }

    public void deleteTournamentByID(Integer id) throws MessagingException, InvalidActionForTournamentStatusException {
        // remove references in other tables
        Tournament t = this.findTournamentByID(id).orElse(null);

        if (isNull(t)){
            throw new EntityNotFoundException(String.format("Unable to find the tournament with id %d", id));
        }

        if (t.getStatus() >= TournamentStatus.ONGOING.getStatus()) {
            throw new InvalidActionForTournamentStatusException("Cannot delete a tournament that is in progress");
        }

        List<UserRegistersTournamentRepository.IRegistrant> participants = userRegistersTournament.getRegistrants(id);
        String tournamentName = t.getName();

        availability.deleteByTournamentID(id);
        userRegistersTournament.deleteByTournamentID(id);
        tournament.deleteTournamentByTournamentID(id);

        sendEMailService.sendCancelMail(participants, tournamentName);

    }

    public List<Tournament> getCompletedTournamentsForUser(int userID) {
        return tournament.findCompletedTournamentsForUser(userID);
    }

    public boolean changeTournamentStatus(Integer id, TournamentStatus status) {
        Tournament tournament = this.findTournamentByID(id).orElse(null);
        if (tournament != null) {
            tournament.setStatus(status.getStatus());
            if (status == TournamentStatus.REGISTRATION_CLOSED) {
                tournament.setCloseRegistrationDate(new Date());
            }
            this.saveTournament(tournament);
            return true;
        }
        return false;
    }

    public void closeRegistration(Integer id) throws MinRegistrantsNotMetException, EntityNotFoundException {
        Tournament tournament = this.findTournamentByID(id).orElse(null);
        Integer numRegistrants = userRegistersTournament.getNumberOfRegistrantsForATournament(id);
        if (tournament == null) {
            throw new EntityNotFoundException(String.format("Unable to find the tournament with id %d", tournament.getTournamentID()));

        } else if (numRegistrants < tournament.getMinParticipants()) {
            throw new MinRegistrantsNotMetException(
                    String.format("Minimum of %d players not reached. %d %s currently registered.",
                            tournament.getMinParticipants(),
                            numRegistrants,
                            numRegistrants == 1 ? "player is" : "players are"
                    ));

        }
        this.changeTournamentStatus(id, TournamentStatus.REGISTRATION_CLOSED);

    }

    public void endCurrentRound(Integer id) throws MissingMatchResultsException, EntityNotFoundException {
        Tournament tournament = this.findTournamentByID(id).orElse(null);
        if (tournament == null) {
            throw new EntityNotFoundException(String.format("Unable to find the tournament with id %d", tournament.getTournamentID()));

        }

        List<Integer> roundID = roundRepository.findIDByTournamentCurrentRound(tournament.getTournamentID(), tournament.getCurrentRound());

        if(roundID.size() != 1) {
          throw new EntityNotFoundException(String.format("Unable to locate the round resource to close", tournament.getTournamentID()));
        }

        List<Integer> pendingMatches = userInvolvesMatchService.findMatchesForRoundWithPendingResults(roundID.get(0));

        if (pendingMatches.size() > 0) {
            throw new MissingMatchResultsException("Match results need to be updated before ending the round");

        }
        this.changeTournamentStatus(id, TournamentStatus.REGISTRATION_CLOSED);

    }

}


