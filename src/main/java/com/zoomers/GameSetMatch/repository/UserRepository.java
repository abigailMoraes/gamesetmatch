package com.zoomers.GameSetMatch.repository;
import com.zoomers.GameSetMatch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT User.userID, User.firebase_id, User.name, User.email, User.is_admin \n"+
            "FROM (SELECT * FROM User_involves_match WHERE matchID = :id) \n" +
            "u JOIN User ON u.userID = User.userID; ",
            nativeQuery = true)
    List<User> findMatchParticipantInfo(int id);

    User findByEmail(String email);

    User findByFirebaseId(String uid);

}
