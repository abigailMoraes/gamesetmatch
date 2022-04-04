package com.zoomers.GameSetMatch.controller.Tournament;

import com.zoomers.GameSetMatch.controller.Error.ApiException;
import com.zoomers.GameSetMatch.controller.Tournament.RequestBody.AvailabilityDTO;
import com.zoomers.GameSetMatch.controller.Tournament.RequestBody.IncomingRegistration;
import com.zoomers.GameSetMatch.controller.Tournament.RequestBody.TournamentByStatuses;
import com.zoomers.GameSetMatch.controller.Tournament.ResponseBody.CurrentTournamentStatus;
import com.zoomers.GameSetMatch.controller.Tournament.ResponseBody.OutgoingTournament;
import com.zoomers.GameSetMatch.entity.Tournament;
import com.zoomers.GameSetMatch.repository.UserMatchTournamentRepository;
import com.zoomers.GameSetMatch.repository.UserRegistersTournamentRepository;
import com.zoomers.GameSetMatch.scheduler.Scheduler;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentStatus;
import com.zoomers.GameSetMatch.scheduler.exceptions.ScheduleException;
import com.zoomers.GameSetMatch.services.AvailabilityService;
import com.zoomers.GameSetMatch.services.Errors.InvalidActionForTournamentStatusException;
import com.zoomers.GameSetMatch.services.Errors.MinRegistrantsNotMetException;
import com.zoomers.GameSetMatch.services.Errors.MissingMatchResultsException;
import com.zoomers.GameSetMatch.services.TournamentService;
import com.zoomers.GameSetMatch.services.UserRegistersTournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Transactional
@RequestMapping("/api/tournaments")
public class TournamentController {

    @Autowired
    AvailabilityService availability;

    @Autowired
    UserRegistersTournamentService userRegistersTournament;

    @Autowired
    TournamentService tournamentService;

    @Autowired
    Scheduler scheduler;

    @GetMapping()
    public List<OutgoingTournament> getAllTournaments(@RequestParam int registeredUser, @RequestParam int status) {
        List<Integer> registeredTournaments = userRegistersTournament.getUserRegisteredInTournamentIDs(registeredUser);
        List<Tournament> tournaments;

        tournaments = tournamentService.findAllByStatus(status);

        List<OutgoingTournament> responseTournaments = new ArrayList<>();

        for (Tournament tournament : tournaments) {
            OutgoingTournament outgoingTournament = new OutgoingTournament();

            outgoingTournament.setTournamentID(tournament.getTournamentID());
            outgoingTournament.setName(tournament.getName());
            outgoingTournament.setDescription(tournament.getDescription());
            outgoingTournament.setStartDate(tournament.getStartDate());
            outgoingTournament.setLocation(tournament.getLocation());
            outgoingTournament.setFormat(tournament.getFormat());
            outgoingTournament.setSeries(tournament.getSeries());
            outgoingTournament.setCloseRegistrationDate(tournament.getCloseRegistrationDate());
            outgoingTournament.setMatchDuration(tournament.getMatchDuration());
            outgoingTournament.setMinParticipants(tournament.getMinParticipants());
            boolean registeredInTournament = registeredTournaments.contains(tournament.getTournamentID());
            outgoingTournament.setRegistered(registeredInTournament);

            responseTournaments.add(outgoingTournament);
        }
        return responseTournaments;
    }

    @GetMapping(value = "/{tournamentID}/registrants")
    public List<UserRegistersTournamentRepository.IRegistrant> getRegistrants(@PathVariable int tournamentID) {
        return userRegistersTournament.getRegistrants(tournamentID);
    }

    @PostMapping()
    public Tournament createTournament(@RequestBody Tournament tournament) {
        if (tournament.getStatus() == TournamentStatus.DEFAULT.getStatus()) {
            tournament.setStatus(TournamentStatus.OPEN_FOR_REGISTRATION.getStatus());
            tournament.setRoundStartDate(tournament.getStartDate());
            tournament.setCurrentRound(0);
        }
        tournamentService.saveTournament(tournament);
        return tournament;
    }

    @PostMapping(value = "/{tournamentID}/register")
    public ResponseEntity registerForTournament(@RequestBody IncomingRegistration newRegistrtation,
                                                @PathVariable Integer tournamentID) {
        Tournament tournament = tournamentService.findTournamentByID(tournamentID).get();

        if (tournament.getStatus() == TournamentStatus.OPEN_FOR_REGISTRATION.getStatus()) {
            Integer userID = newRegistrtation.getUserID();
            userRegistersTournament.saveRegistration(tournamentID, userID, newRegistrtation.getSkillLevel());
            availability.saveAvailabilities(tournamentID, userID, newRegistrtation.getAvailabilities());
        } else {
            String errorMsg = "The tournament registration is closed. Cannot register.";
            ApiException error = new ApiException(HttpStatus.BAD_REQUEST, errorMsg);
            return new ResponseEntity<Object>(error, error.getHttpStatus());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Successfully registered!");
    }

    @GetMapping(value="/{tournamentID}/availabilities/{userID}")
    public List<AvailabilityDTO> getPlayerAvailabilities(@PathVariable Integer tournamentID, @PathVariable Integer userID) {
        return availability.getUsersAvailabilityForTournament(userID, tournamentID);
    }

    @PutMapping(value="/{tournamentID}/availabilities/{userID}")
    public void  updateUsersAvailabilityForTournament(@RequestBody List<AvailabilityDTO> updatedAvailability, @PathVariable Integer tournamentID, @PathVariable Integer userID) {
        availability.updateUsersAvailabilityForTournament(tournamentID, userID, updatedAvailability);
    }

    @PostMapping(value = "/{tournamentID}/deregister/{userID}")
    public void undoRegistration(@PathVariable Integer tournamentID, @PathVariable Integer userID) {
        userRegistersTournament.undoRegistration(tournamentID, userID);
    }


    @PutMapping(value = "/{tournamentID}")
    public ResponseEntity<String> changeTournamentInfo(@PathVariable Integer tournamentID, @RequestBody Tournament incoming) {
        Optional<Tournament> tournament = tournamentService.findTournamentByID(tournamentID);
        if (tournament.isPresent()) {
            Tournament tour = tournament.get();

            if (incoming.getName() != null) {
                tour.setName(incoming.getName());
            }
            if (incoming.getDescription() != null) {
                tour.setDescription(incoming.getDescription());
            }
            if (incoming.getStartDate() != null) {
                tour.setStartDate(incoming.getStartDate());
                tour.setRoundStartDate(incoming.getStartDate());
            }
            if (incoming.getCloseRegistrationDate() != null) {
                tour.setCloseRegistrationDate(incoming.getCloseRegistrationDate());
            }
            if (incoming.getLocation() != null) {
                tour.setLocation(incoming.getLocation());
            }
            if (incoming.getEndDate() != null) {
                tour.setEndDate(incoming.getEndDate());
            }
            if (incoming.getPrize() != null) {
                tour.setPrize(incoming.getPrize());
            }
            if (incoming.getFormat() != null) {
                tour.setFormat(incoming.getFormat());
            }
            if (incoming.getSeries() != null) {
                tour.setSeries(incoming.getSeries());
            }
            if (incoming.getMatchDuration() != null) {
                tour.setMatchDuration(incoming.getMatchDuration());
            }
            if (incoming.getAdminHostsTournament() != 0) {
                tour.setAdminHostsTournament(incoming.getAdminHostsTournament());
            }
            if (incoming.getStatus() != TournamentStatus.DEFAULT.getStatus()) {
                tour.setStatus(incoming.getStatus());
            }
            if (incoming.getMinParticipants() != null) {
                tour.setMinParticipants(incoming.getMinParticipants());
            }

            tournamentService.saveTournament(tour);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Tournament ID");
        }
        return ResponseEntity.status(HttpStatus.OK).body("ID: " + tournamentID + " Tournament is updated");
    }

    @PutMapping(value = "/{tournamentID}/closeRegistration")
    public ResponseEntity<Object> closeRegistration(@PathVariable Integer tournamentID) {
        try {
            tournamentService.closeRegistration(tournamentID);
        } catch (MinRegistrantsNotMetException | InvalidActionForTournamentStatusException e) {
            ApiException error = new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<Object>(error, error.getHttpStatus());
        } catch (EntityNotFoundException e) {
            ApiException error = new ApiException(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<Object>(error, error.getHttpStatus());
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping(value = "/{tournamentID}/endCurrentRound")
    public ResponseEntity<Object> endCurrentRound(@PathVariable Integer tournamentID) {
        try {
            int currentStatus = tournamentService.endCurrentRound(tournamentID);
            return ResponseEntity.status(HttpStatus.OK).body(new CurrentTournamentStatus(currentStatus));
        }catch (EntityNotFoundException e) {
            ApiException error = new ApiException(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<Object>(error, error.getHttpStatus());
        } catch (MissingMatchResultsException | ScheduleException e) {
            ApiException error = new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<Object>(error, error.getHttpStatus());
        }
    }

    @PostMapping(value = "", params = {"createdBy"})
    public List<Tournament> getTournament(@RequestBody TournamentByStatuses statuses,
                                          @RequestParam(name = "createdBy") int user) {

        List<Tournament> fullList = new ArrayList<>();
        for (Integer status : statuses.getStatuses() ) {
            List<Tournament> t = tournamentService.getTournaments(status, user);
            fullList = Stream.concat(fullList.stream(), t.stream())
                    .collect(Collectors.toList());
        }
        return fullList;
    }

    @DeleteMapping(value = "/{tournamentID}")
    public ResponseEntity<Object> deleteInactiveTournament(@PathVariable Integer tournamentID){
        try {
            tournamentService.deleteTournamentByID(tournamentID);
        } catch (InvalidActionForTournamentStatusException e) {
            ApiException error = new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<Object>(error, error.getHttpStatus());
        } catch (EntityNotFoundException e) {
            ApiException error = new ApiException(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<Object>(error, error.getHttpStatus());
        } catch (MailException e) {
            ApiException error = new ApiException(HttpStatus.BAD_REQUEST,
                    "SEND_EMAIL_ERROR_MAIL",
                    e.getMessage());
            return new ResponseEntity<Object>(error, error.getHttpStatus());
        } catch (MessagingException e) {
            ApiException error = new ApiException(HttpStatus.BAD_REQUEST,
                    "SEND_EMAIL_ERROR_MESSAGING",
                    e.getMessage());
            return new ResponseEntity<Object>(error, error.getHttpStatus());
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(value = "/{tournamentID}/runCreateSchedule")
    public ResponseEntity createSchedule(@PathVariable(name = "tournamentID") int tournamentID) {
        try {

            scheduler.createSchedule(tournamentID);
        } catch (ScheduleException e) {
            ApiException error = new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<Object>(error, error.getHttpStatus());
        } catch (EntityNotFoundException e) {
            ApiException error = new ApiException(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<Object>(error, error.getHttpStatus());
        }

        return ResponseEntity.status(HttpStatus.OK).body("Schedule created for Tournament ID: " + tournamentID);
    }

    @GetMapping(value = "/user/{userID}/completed")
    public List<Tournament> getCompletedTournamentsByUser(@PathVariable int userID){
        return tournamentService.getCompletedTournamentsForUser(userID);
    }

    @GetMapping(value = "/user/{userID}/number/completed")
    public Optional<UserMatchTournamentRepository.NumQuery>
    getNumberOfCompletedTournamentsByUser(@PathVariable int userID) {
        UserMatchTournamentRepository.NumQuery completed =
                tournamentService.getNumberOfTournamentsPlayed(userID);
        UserMatchTournamentRepository.NumQuery empty = new UserMatchTournamentRepository.NumQuery() {
            @Override
            public Integer getNext() {
                return null;
            }
        };
        if (Optional.ofNullable(completed).isPresent()) {
            return Optional.of(completed);
        } else {
            return Optional.of(empty);
        }
    }

    @GetMapping(value = "/user/{userID}/number/won")
    public Optional<UserMatchTournamentRepository.NumQuery>
    getNumberOfTournamentsWonByUser(@PathVariable int userID) {
        UserMatchTournamentRepository.NumQuery won =
                tournamentService.getNumberOfTournamentsWon(userID);
        UserMatchTournamentRepository.NumQuery empty = new UserMatchTournamentRepository.NumQuery() {
            @Override
            public Integer getNext() {
                return null;
            }
        };
        if (Optional.ofNullable(won).isPresent()) {
            return Optional.of(won);
        } else {
            return Optional.of(empty);
        }
    }
}