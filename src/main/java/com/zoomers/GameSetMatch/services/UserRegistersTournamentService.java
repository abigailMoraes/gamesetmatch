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

    public void saveRegistration(Long tournamentID, Long userID) {
        UserRegistersTournament registration = new UserRegistersTournament(tournamentID, userID);
        userRegistersTournament.save(registration);
    }
}
