package com.zoomers.GameSetMatch.repository;


import com.zoomers.GameSetMatch.entity.UserInvolvesMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInvolvesMatchRepository extends JpaRepository<UserInvolvesMatch,Integer> {

    List<UserInvolvesMatch> getUserInvolvesMatchByMatchID(int mID);


}
