package com.zoomers.GameSetMatch.controller.Tournament;

import com.zoomers.GameSetMatch.controller.Tournament.RequestBody.IncomingRegistration;
import com.zoomers.GameSetMatch.controller.Tournament.ResponseBody.OutgoingTournament;
import com.zoomers.GameSetMatch.entity.Tournament;

import com.zoomers.GameSetMatch.services.AvailabilityService;
import com.zoomers.GameSetMatch.services.TournamentService;
import com.zoomers.GameSetMatch.services.UserRegistersTournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/tournament")
public class TournamentController {

    @Autowired
    AvailabilityService availability;

    @Autowired
    TournamentService tournament;

    @Autowired
    UserRegistersTournamentService userRegistersTournament;

    @Autowired
    private TournamentService tournamentService;

    @GetMapping()
    public List<OutgoingTournament> getAllTournaments() {
        List<Long> registeredTournaments = userRegistersTournament.getUserRegisteredInTournamentIDs((long) 1);
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
            outgoingTournament.setType(tournament.getType());
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

    @PostMapping()
    public Tournament createTournament(@RequestBody Tournament tournament)  {
        tournament.setStatus(0);
        tournamentService.saveTournament(tournament);
        return tournament;
    }

    @PostMapping(value = "/{tournamentID}/register")
    public void registerForTournament(@RequestBody IncomingRegistration newRegistrtation, @PathVariable Long tournamentID) {
        Long userID = newRegistrtation.getUserID();

        userRegistersTournament.saveRegistration(tournamentID, userID);
        availability.saveAvailabilities(tournamentID, userID, newRegistrtation.getAvailabilities());

    }

    @PutMapping(value = "/{tournamentID}")
    public Tournament changeTournamentInfo(@PathVariable Long tournamentID, @RequestBody Tournament incoming) {
        Optional<Tournament> tournament = tournamentService.findTournamentByID(tournamentID);
        Tournament tour = new Tournament();
        if (tournament.isPresent()) {
            tour = tournament.get();

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
            if (incoming.getType() != null) {
                tour.setType(incoming.getType());
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
            tour.setName("WARNING: This Tournament ID is not valid");
        }
        return tour;
    }

    @GetMapping(value = "/status")
    public List<Tournament> getCertainTournament(@RequestParam int status) {
        return tournamentService.getTournamentByStatus(status);
    }
}
