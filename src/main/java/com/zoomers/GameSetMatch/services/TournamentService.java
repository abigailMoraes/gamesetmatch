package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.entity.Tournament;
import com.zoomers.GameSetMatch.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TournamentService {
    @Autowired
    private TournamentRepository tournament;

    public List<Tournament> getAllTournaments() {
        return tournament.findAll();
    }

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

    public void deleteTournamentByID(Integer id) {
        tournament.deleteTournamentByTournamentID(id);
    }
}


