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

            if(tournament.getRoundDuration() !=null){
                outgoingTournament.setRoundDuration(tournament.getRoundDuration());
            }

            boolean registeredInTournament = registeredTournaments.contains(tournament.getTournamentID());
            outgoingTournament.setRegistered(registeredInTournament);

            responseTournaments.add(outgoingTournament);
        }
        return responseTournaments;
    }

    @PostMapping(value = "/{tournamentID}/register")
    public void registerForTournament(@RequestBody IncomingRegistration newRegistrtation, @PathVariable Long tournamentID) {
        Long userID = newRegistrtation.getUserID();

        userRegistersTournament.saveRegistration(tournamentID, userID);
        availability.saveAvailabilities(tournamentID, userID, newRegistrtation.getAvailabilities());

    }

}
