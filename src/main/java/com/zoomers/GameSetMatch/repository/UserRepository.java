package com.zoomers.GameSetMatch.repository;
import com.zoomers.GameSetMatch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT user.userID, user.firebase_id, user.name, user.email, user.is_admin \n"+
            "FROM (SELECT * FROM user_involves_match WHERE matchID = :id) \n" +
            "u JOIN user ON u.userID = user.userID; ",
            nativeQuery = true)
    List<User> findMatchParticipantInfo(int id);

    User findByEmail(String email);

    User findByFirebaseId(String uid);

    User getUserById(int id);

}
