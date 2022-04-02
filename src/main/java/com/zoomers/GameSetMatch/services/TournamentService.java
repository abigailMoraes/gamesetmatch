package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.entity.Tournament;
import com.zoomers.GameSetMatch.repository.TournamentRepository;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TournamentService {
    @Autowired
    private TournamentRepository tournament;

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


    public List<Tournament> getCompletedTournamentsForUser(int userID) {
       return tournament.findCompletedTournamentsForUser(userID);
    }

    @Transactional
    public void deleteTournamentByID(Integer id) {
        tournament.deleteTournamentByTournamentID(id);
    }

    @Transactional
    public boolean changeTournamentStatus(Integer id, TournamentStatus status) {
        Tournament tournament = this.findTournamentByID(id).orElse(null);
        if (tournament != null) {
            tournament.setStatus(status.ordinal());
            if (status == TournamentStatus.REGISTRATION_CLOSED) {
                tournament.setCloseRegistrationDate(new Date());
            }
            this.saveTournament(tournament);
            return true;
        }
        return false;
    }

    public List<Tournament> getRegisteredTournaments(int userID) {
        return tournament.findRegisteredTournamentsForUser(userID, TournamentStatus.READY_TO_PUBLISH_SCHEDULE.getStatus());
    }
}


