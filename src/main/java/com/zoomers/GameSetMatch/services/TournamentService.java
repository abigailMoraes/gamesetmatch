package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.entity.Round;
import com.zoomers.GameSetMatch.entity.Tournament;
import com.zoomers.GameSetMatch.entity.UserInvolvesMatch;
import com.zoomers.GameSetMatch.entity.UserRegistersTournament;
import com.zoomers.GameSetMatch.repository.*;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.enumerations.MatchBy;
import com.zoomers.GameSetMatch.scheduler.enumerations.PlayerStatus;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentFormat;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentStatus;
import com.zoomers.GameSetMatch.scheduler.exceptions.ScheduleException;
import com.zoomers.GameSetMatch.services.Errors.InvalidActionForTournamentStatusException;
import com.zoomers.GameSetMatch.services.Errors.MinRegistrantsNotMetException;
import com.zoomers.GameSetMatch.services.Errors.MissingMatchResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    private UserRegistersTournamentRepository userRegistersTournamentRepository;

    @Autowired
    private UserInvolvesMatchService userInvolvesMatchService;

    @Autowired
    private UserInvolvesMatchRepository userInvolvesMatchRepository;

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

    @Transactional
    public void saveTournament(Tournament tour) {
        tournament.save(tour);
    }

    public Optional<Tournament> findTournamentByID(Integer id) {
        return tournament.findById(Math.toIntExact(id));
    }

    public List<Tournament> getTournaments(int status, int id) {
        return tournament.findTournaments(status, id);
    }

    @Transactional
    public void deleteTournamentByID(Integer id) throws MessagingException, InvalidActionForTournamentStatusException {
        // remove references in other tables
        Tournament t = this.findTournamentByID(id).orElse(null);

        if (isNull(t)) {
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
        return tournament.findCompletedTournamentsForUser(userID, TournamentStatus.TOURNAMENT_OVER.getStatus());
    }


    @Transactional
    public void changeTournamentStatus(Integer id, TournamentStatus status) throws EntityNotFoundException {
        Tournament tournament = this.findTournamentByID(id).orElse(null);

        if (isNull(tournament)) {
            throw new EntityNotFoundException(String.format("Unable to find the tournament with id %d", tournament.getTournamentID()));
        }
        tournament.setStatus(status.getStatus());

        this.saveTournament(tournament);
    }

    public UserMatchTournamentRepository.NumQuery getNumberOfTournamentsPlayed(Integer userID) {
        return tournament.getNumberOfCompletedTournamentsForUser(userID, TournamentStatus.TOURNAMENT_OVER.getStatus());
    }

    public UserMatchTournamentRepository.NumQuery getNumberOfTournamentsWon(Integer userID) {
        return tournament.getNumberOfTournamentsWonByUser(userID, TournamentStatus.TOURNAMENT_OVER.getStatus(),
                PlayerStatus.SAFE.getStatus());
    }

    public List<Tournament> getRegisteredTournaments(int userID) {
        return tournament.findRegisteredTournamentsForUser(userID, TournamentStatus.READY_TO_PUBLISH_SCHEDULE.getStatus());
    }

    @Transactional
    public void closeRegistration(Integer id) throws MinRegistrantsNotMetException, EntityNotFoundException, InvalidActionForTournamentStatusException {
        Tournament tournament = this.findTournamentByID(id).orElse(null);
        Integer numRegistrants = userRegistersTournament.getNumberOfRegistrantsForATournament(id);
        if (isNull(tournament)) {
            throw new EntityNotFoundException(String.format("Unable to find the tournament with id %d", tournament.getTournamentID()));

        } else if (numRegistrants < tournament.getMinParticipants()) {
            throw new MinRegistrantsNotMetException(
                    String.format("Minimum of %d players not reached. %d %s currently registered.",
                            tournament.getMinParticipants(),
                            numRegistrants,
                            numRegistrants == 1 ? "player is" : "players are"
                    ));

        }

        if (tournament.getStatus() != TournamentStatus.OPEN_FOR_REGISTRATION.getStatus()) {
            throw new InvalidActionForTournamentStatusException("Tournament registration is already closed");
        }

        // update close registration date
        tournament.setCloseRegistrationDate(new Date());
        // close registration
        tournament.setStatus(TournamentStatus.READY_TO_SCHEDULE.getStatus());
        this.saveTournament(tournament);

    }

    @Transactional
    public int endCurrentRound(Integer tournamentID) throws MissingMatchResultsException, EntityNotFoundException, ScheduleException {
        Tournament currentTournament = this.findTournamentByID(tournamentID).orElse(null);
        if (isNull(currentTournament)) {
            throw new EntityNotFoundException(String.format("Unable to find the tournament with id %d", currentTournament.getTournamentID()));

        }

        List<Integer> roundID = roundRepository.findIDByTournamentCurrentRound(currentTournament.getTournamentID(), currentTournament.getCurrentRound());

        if (roundID.size() != 1) {
            throw new EntityNotFoundException(String.format("Unable to locate the round resource.", currentTournament.getTournamentID()));
        }

        List<Integer> pendingMatches = userInvolvesMatchService.findMatchesForRoundWithPendingResults(roundID.get(0));

        if (pendingMatches.size() > 0) {
            throw new MissingMatchResultsException("Match results need to be updated before ending the round.");

        }

        // update player statuses
        List<UserRegistersTournament> remainingRegistrants = userRegistersTournamentRepository.getUserRegistersTournamentsByTournamentID(currentTournament.getTournamentID())
                .stream().filter(r -> r.getPlayerStatus() != PlayerStatus.ELIMINATED).collect(Collectors.toList());

        for (UserRegistersTournament r : remainingRegistrants) {
            List<UserInvolvesMatch> playedInRound = userInvolvesMatchRepository.getUsersMatchesForRound(roundID.get(0), r.getUserID());
            if (playedInRound.size() > 0) {
                userInvolvesMatchService.updateToNewPlayerStatus(currentTournament, r.getUserID(), roundID.get(0), playedInRound);
            }
        }

        if (canEndTournament(currentTournament)) {
            currentTournament.setStatus(TournamentStatus.TOURNAMENT_OVER.getStatus());

        } else {
            Round round = roundRepository.findById(roundID.get(0)).orElse(null);

            if (isNull(round)) {
                throw new EntityNotFoundException(String.format("Unable to locate the round resource.", currentTournament.getTournamentID()));
            }

            currentTournament.setStatus(TournamentStatus.READY_TO_SCHEDULE.getStatus());

            Date today = DateAndLocalDateService.localDateToDate(LocalDate.now());
            Date nextRoundStartDate = DateAndLocalDateService.localDateToDate(LocalDate.now().plusDays(DateAndLocalDateService.DaysBetweenRounds));

            currentTournament.setRoundStartDate(nextRoundStartDate);
            round.setEndDate(today);

            if (round.getStartDate().after(round.getEndDate())) {
                round.setStartDate(today);
            }

            roundRepository.save(round);
        }

        tournament.save(currentTournament);
        return currentTournament.getStatus();
    }

    private boolean canEndTournament(Tournament t) {

        if (t.getStatus() != TournamentStatus.FINAL_ROUND.getStatus()) {
            return false;
        }

        TournamentFormat format = TournamentFormat.values()[t.getFormat()];

        switch (format) {
            case DOUBLE_KNOCKOUT:
                // make sure that there was a winner for double knockout before ending the tournament
                return numberOfRemainingPlayers(t.getTournamentID()) <= 1;
            case ROUND_ROBIN:
            case SINGLE_BRACKET:
            case SINGLE_KNOCKOUT:
            default:
                return true;
        }
    }

    public Tournament getTournament(int tournamentID) {
        Tournament currentTournament = this.findTournamentByID(tournamentID).orElse(null);
        if (isNull(currentTournament)) {
            throw new EntityNotFoundException(String.format("Unable to find the tournament with id %d", currentTournament.getTournamentID()));

        }
        return currentTournament;
    }

    public boolean isEnteringFinalRound(int tournamentID) throws ScheduleException {
        List<Registrant> registrants = userRegistersTournamentRepository.getSchedulerRegistrantsByTournamentID(tournamentID);
        Set<Integer> registrantIDs = registrants.stream().map(Registrant::getID).collect(Collectors.toSet());
        Tournament t = getTournament(tournamentID);
        TournamentFormat format = TournamentFormat.values()[t.getFormat()];

        for (Registrant r : registrants) {

            r.setPlayersToPlay(new LinkedHashSet<>(registrantIDs));
            r.initCurrentStatus(
                    format,
                    MatchBy.values()[t.getMatchBy()],
                    t.getTournamentID()
            );
        }

        switch (format) {
            case ROUND_ROBIN:
                return checkIfAllPlayersHavePlayed(registrants);
            case DOUBLE_KNOCKOUT:
            case SINGLE_BRACKET:
            case SINGLE_KNOCKOUT:
                registrants.removeIf(registrant -> registrant.getStatus() == PlayerStatus.ELIMINATED);
                return registrants.size() == 2;
        }

        return false;
    }

    public boolean checkIfAllPlayersHavePlayed(List<Registrant> registrants) {

        for (Registrant r : registrants) {
            if (r.getPlayersToPlay().size() > 1) {
                return false;
            }
        }
        return true;
    }

    private Integer numberOfRemainingPlayers(int tournamentID) {
        // get players from tournament and see if they are all eliminated but one
        List<UserRegistersTournament> registrants = userRegistersTournament.getRegistrantsByTournamentID(tournamentID);

        List<UserRegistersTournament> notEliminated = registrants.stream()
                .filter(r -> r.getPlayerStatus() != PlayerStatus.ELIMINATED).collect(Collectors.toList());

        return notEliminated.size();
    }
}
