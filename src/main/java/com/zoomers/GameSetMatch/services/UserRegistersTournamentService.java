package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.entity.UserRegistersTournament;
import com.zoomers.GameSetMatch.repository.UserRegistersTournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRegistersTournamentService {

    @Autowired
    private UserRegistersTournamentRepository userRegistersTournament;

    public List<Integer> getUserRegisteredInTournamentIDs(Integer userID) {
        List<Integer> registeredTournaments = new ArrayList<>();

        List<UserRegistersTournament> registrations = userRegistersTournament.findByUserID(userID);

        for (UserRegistersTournament registration : registrations) {
            registeredTournaments.add(registration.getTournamentID());
        }

        return registeredTournaments;
    }

    public List<UserRegistersTournamentRepository.IRegistrant> getRegistrants(Integer tournamentID) {
        return userRegistersTournament.findRegistrantsByTournamentID(tournamentID);
    }

    public void saveRegistration(Integer tournamentID, Integer userID, Integer skillLevel) {
        UserRegistersTournament registration = new UserRegistersTournament(tournamentID, userID, skillLevel);
        userRegistersTournament.save(registration);
    }

    public void deleteByTournamentID(Integer tournamentID) {
        userRegistersTournament.deleteUserRegistersTournamentsByTournamentID(tournamentID);
    }
}
