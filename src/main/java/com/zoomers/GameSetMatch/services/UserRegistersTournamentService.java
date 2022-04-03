package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.entity.UserRegistersTournament;
import com.zoomers.GameSetMatch.repository.AvailabilityRepository;
import com.zoomers.GameSetMatch.repository.UserRegistersTournamentRepository;
import com.zoomers.GameSetMatch.scheduler.enumerations.PlayerStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserRegistersTournamentService {

    @Autowired
    private UserRegistersTournamentRepository userRegistersTournament;

    @Autowired
    private AvailabilityRepository availabilityRepository;

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

    @Transactional
    public void saveRegistration(Integer tournamentID, Integer userID, Integer skillLevel) {
        UserRegistersTournament registration = new UserRegistersTournament(tournamentID, userID, skillLevel, PlayerStatus.SAFE);
        userRegistersTournament.save(registration);
    }

    @Transactional
    public void undoRegistration(Integer tournamentID, Integer userID) {
        availabilityRepository.removeAvailabilityForUser(tournamentID, userID);
        userRegistersTournament.removeRegistrationForUser(tournamentID, userID);
    }

    @Transactional
    public void deleteByTournamentID(Integer tournamentID) {
        userRegistersTournament.deleteUserRegistersTournamentsByTournamentID(tournamentID);
    }

    public List<UserRegistersTournament> getRegistrantsByTournamentID(Integer tournamentID) {
        return userRegistersTournament.getUserRegistersTournamentsByTournamentID(tournamentID);
    }

    public Integer getNumberOfRegistrantsForATournament(int tournamentID) {
        return userRegistersTournament.getNumberOfRegistrantsForATournament(tournamentID);
    }
}
