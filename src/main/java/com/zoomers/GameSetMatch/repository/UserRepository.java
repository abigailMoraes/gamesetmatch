package com.zoomers.GameSetMatch.repository;
import com.zoomers.GameSetMatch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}