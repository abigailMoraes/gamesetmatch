package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.Tournament;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository {
    void saveTournament(Tournament tournament);

}
