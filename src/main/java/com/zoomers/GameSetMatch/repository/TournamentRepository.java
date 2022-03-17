package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

}
