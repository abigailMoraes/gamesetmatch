package com.zoomers.GameSetMatch.controller.Tournament;

import com.zoomers.GameSetMatch.controller.Tournament.RequestBody.IncomingRegistration;
import com.zoomers.GameSetMatch.controller.Tournament.ResponseBody.OutgoingTournament;
import com.zoomers.GameSetMatch.entity.Tournament;

import com.zoomers.GameSetMatch.repository.AdminRepository;
import com.zoomers.GameSetMatch.services.AdminService;
import com.zoomers.GameSetMatch.services.AvailabilityService;
import com.zoomers.GameSetMatch.services.TournamentService;
import com.zoomers.GameSetMatch.services.UserRegistersTournamentService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
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

    @Autowired
    private AdminService AdminService;

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

    @PostMapping(value = "/save")
    @ResponseBody
    public void createTournament(@RequestBody String detail) throws ParseException, java.text.ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(detail);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        formatter.parse((String) json.get("start_date"));

        String name = (String) json.get("name");
        String description = (String) json.get("description");
        Date startDate = formatter.parse((String) json.get("start_date"));
        Date closeRegistrationDate = formatter.parse((String) json.get("close_registration_date"));
        String location = (String) json.get("location");
        Integer maxParticipants = (Integer) json.get("max_Participants");
        Integer minParticipants = (Integer) json.get("min_Participants");
        String prize = (String) json.get("prize");
        String format = (String) json.get("format");
        String type = (String) json.get("type");
        Long matchDuration = (Long) json.get("match_duration");
        Integer numberOfMatches = (Integer) json.get("number_of_matches");
        Integer roundDuration = (Integer) json.get("round_duration");

        Tournament tournament = new Tournament();
        tournament.setName(name);
        tournament.setDescription(description);
        tournament.setStartDate(startDate);
        tournament.setCloseRegistrationDate(closeRegistrationDate);
        tournament.setLocation(location);
        tournament.setMaxParticipants(maxParticipants);
        tournament.setMatchDuration(matchDuration);
        tournament.setMinParticipants(minParticipants);
        tournament.setPrize(prize);
        tournament.setFormat(format);
        tournament.setType(type);
        tournament.setNumberOfMatches(numberOfMatches);
        tournament.setRoundDuration(roundDuration);

        AdminService.saveTournament(tournament);
    }

    @PostMapping(value = "/{tournamentID}/register")
    public void registerForTournament(@RequestBody IncomingRegistration newRegistrtation, @PathVariable Long tournamentID) {
        Long userID = newRegistrtation.getUserID();

        userRegistersTournament.saveRegistration(tournamentID, userID);
        availability.saveAvailabilities(tournamentID, userID, newRegistrtation.getAvailabilities());

    }


}
