package com.zoomers.GameSetMatch.controller.Tournament;

import com.zoomers.GameSetMatch.controller.Tournament.ResponseBody.OutgoingTournament;
import com.zoomers.GameSetMatch.controller.Tournament.RequestBody.IncomingRegistration;
import com.zoomers.GameSetMatch.entity.Tournament;

import com.zoomers.GameSetMatch.repository.UserRegistersTournamentRepository;
import com.zoomers.GameSetMatch.scheduler.Scheduler;
import com.zoomers.GameSetMatch.services.AvailabilityService;
import com.zoomers.GameSetMatch.services.TournamentService;
import com.zoomers.GameSetMatch.services.UserRegistersTournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

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

    @GetMapping()
    public List<OutgoingTournament> getAllTournaments(@RequestParam int registeredUser) {
        List<Integer> registeredTournaments = userRegistersTournament.getUserRegisteredInTournamentIDs(registeredUser);
        List<Tournament> tournaments = tournament.getAllTournaments();

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
            outgoingTournament.setNumberOfMatches(tournament.getNumberOfMatches());

            if (tournament.getRoundDuration() != null) {
                outgoingTournament.setRoundDuration(tournament.getRoundDuration());
            }

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
        if (tournament.getStatus() == -1) {
            tournament.setStatus(0);
        }
        tournamentService.saveTournament(tournament);
        return tournament;
    }

    @PostMapping(value = "/{tournamentID}/register")
    public void registerForTournament(@RequestBody IncomingRegistration newRegistrtation, @PathVariable Integer tournamentID) {
        Integer userID = newRegistrtation.getUserID();

        userRegistersTournament.saveRegistration(tournamentID, userID);
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
            if (incoming.getMaxParticipants() != null) {
                tour.setMaxParticipants(incoming.getMaxParticipants());
            }
            if (incoming.getMinParticipants() != null) {
                tour.setMinParticipants(incoming.getMinParticipants());
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
            if (incoming.getNumberOfMatches() != null) {
                tour.setNumberOfMatches(incoming.getNumberOfMatches());
            }
            if (incoming.getRoundDuration() != null) {
                tour.setRoundDuration(incoming.getRoundDuration());
            }
            if (incoming.getAdminHostsTournament() != 0) {
                tour.setAdminHostsTournament(incoming.getAdminHostsTournament());
            }
            if (incoming.getStatus() != -1) {
                tour.setStatus(incoming.getStatus());
            }

            tournamentService.saveTournament(tour);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Tournament ID");
        }
        return ResponseEntity.status(HttpStatus.OK).body("ID: " + tournamentID + " Tournament is updated");
    }

    @GetMapping(value = "", params = {"status", "createdBy"})
    public List<Tournament> getTournament(@RequestParam(name = "status") int status,
                                                 @RequestParam(name = "createdBy") int user) {
        return tournamentService.getTournaments(status, user);
    }

    @DeleteMapping(value = "/{tournamentID}")
    public ResponseEntity<String> deleteInactiveTournament(@PathVariable Integer tournamentID) {
        Optional<Tournament> tournament = tournamentService.findTournamentByID(tournamentID);

        if (tournament.isPresent()) {
            Tournament tour = tournament.get();

            if (tour.getStatus() == 4) {
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

    @PostMapping(value = "/{tournamentID}/schedule")
    public void createSchedule(@PathVariable(name = "tournamentID") int tournamentID) {
        scheduler.createSchedule(tournamentID);
    }
}
