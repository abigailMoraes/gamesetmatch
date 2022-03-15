package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.UserRegistersTournament;
import com.zoomers.GameSetMatch.entity.UserRegistersTournamentID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRegistersTournamentRepository extends JpaRepository<UserRegistersTournament, UserRegistersTournamentID> {
    List<UserRegistersTournament> findByUserID(Long userID);
}
