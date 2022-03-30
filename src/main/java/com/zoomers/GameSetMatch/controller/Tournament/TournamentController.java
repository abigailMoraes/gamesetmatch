package com.zoomers.GameSetMatch.controller.Tournament;

import com.zoomers.GameSetMatch.controller.MailController;
import com.zoomers.GameSetMatch.controller.Tournament.RequestBody.IncomingRegistration;
import com.zoomers.GameSetMatch.controller.Tournament.RequestBody.TournamentByStatuses;
import com.zoomers.GameSetMatch.controller.Tournament.ResponseBody.OutgoingTournament;
import com.zoomers.GameSetMatch.entity.Tournament;
import com.zoomers.GameSetMatch.repository.UserRegistersTournamentRepository;
import com.zoomers.GameSetMatch.scheduler.Scheduler;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentStatus;
import com.zoomers.GameSetMatch.services.AvailabilityService;
import com.zoomers.GameSetMatch.services.TournamentService;
import com.zoomers.GameSetMatch.services.UserRegistersTournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
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
    TournamentService tournament;

    @Autowired
    UserRegistersTournamentService userRegistersTournament;

    @Autowired
    TournamentService tournamentService;

    @Autowired
    Scheduler scheduler;

    @Autowired
    MailController mailController;

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
    public Tournament createTournament(@RequestBody Tournament tournament)  {
        if (tournament.getStatus() == TournamentStatus.DEFAULT.getStatus()) {
            tournament.setStatus(TournamentStatus.OPEN_FOR_REGISTRATION.getStatus());
            tournament.setCurrentRound(0);
        }
        tournamentService.saveTournament(tournament);
        return tournament;
    }

    @PostMapping(value = "/{tournamentID}/register")
    public void registerForTournament(@RequestBody IncomingRegistration newRegistrtation, @PathVariable Integer tournamentID) {
        Integer userID = newRegistrtation.getUserID();
        userRegistersTournament.saveRegistration(tournamentID, userID, newRegistrtation.getSkillLevel());
        availability.saveAvailabilities(tournamentID, userID, newRegistrtation.getAvailabilities());

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

            tournamentService.saveTournament(tour);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Tournament ID");
        }
        return ResponseEntity.status(HttpStatus.OK).body("ID: " + tournamentID + " Tournament is updated");
    }

    @PutMapping(value = "/{tournamentID}/closeRegistration")
    public ResponseEntity<String> closeRegistration(@PathVariable Integer tournamentID) {
            boolean res = tournamentService.changeTournamentStatus(tournamentID, TournamentStatus.REGISTRATION_CLOSED);
       return  res ? ResponseEntity.status(HttpStatus.OK).body("ID: " + tournamentID + " Tournament registration is closed") :
               ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to close registration");
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
    public ResponseEntity<String> deleteInactiveTournament(@PathVariable Integer tournamentID) throws MessagingException {
        Optional<Tournament> tournament = tournamentService.findTournamentByID(tournamentID);

        if (tournament.isPresent()) {
            Tournament tour = tournament.get();

            if (tour.getStatus() == TournamentStatus.OPEN_FOR_REGISTRATION.getStatus()) {
                mailController.sendCancelMail(tournamentID);

                availability.deleteByTournamentID(tournamentID);
                userRegistersTournament.deleteByTournamentID(tournamentID);
                tournamentService.deleteTournamentByID(tournamentID);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("ID: " + tournamentID + " Tournament is currently active. Cannot delete.");
            }
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Tournament ID");
        }
        return ResponseEntity.status(HttpStatus.OK).body("ID: " + tournamentID + " Tournament is deleted.");
    }

    @PostMapping(value = "/{tournamentID}/runCreateSchedule")
    public ResponseEntity createSchedule(@PathVariable(name = "tournamentID") int tournamentID) {
        scheduler.createSchedule(tournamentID);
        boolean res = tournamentService.changeTournamentStatus(tournamentID, TournamentStatus.READY_TO_PUBLISH_SCHEDULE);
        return  res ? ResponseEntity.status(HttpStatus.OK).body("ID: " + tournamentID + " Schedule created.") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There was an error creating the schedule.");
    }
}
