package com.zoomers.GameSetMatch.controller;

import com.zoomers.GameSetMatch.entity.Tournament;
import com.zoomers.GameSetMatch.entity.User;
import com.zoomers.GameSetMatch.repository.UserRepository;
import com.zoomers.GameSetMatch.services.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    @Autowired
    private UserRepository repository;

    @Autowired
    private TournamentService tournamentService;



    @PostMapping("/employee")
    User newEmployee(@RequestBody User newEmployee) {
        return repository.save(newEmployee);
    }

    @GetMapping("/user/{email}")
    public User getEmployeeByEmail(@PathVariable String email) {
        return repository.findByEmail(email);
    }

    @GetMapping("/match/{matchID}/participants")
    public List<User> getMatchParticipants ( @PathVariable int matchID){
        return repository.findMatchParticipantInfo(matchID);
    }

    @PutMapping("/user/{email}")
    @ResponseBody
    ResponseEntity<Object> toAdmin(@PathVariable String email) {
        User e = repository.findByEmail(email);
        if (e == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot find user with this email!");
        }
        if (e.getIsAdmin() == 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(email + " is already an admin!");
        } else if (e.getIsAdmin() == 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You don't have permission to touch this user!");
        }
        e.setIsAdmin(1);
        repository.save(e);
        return ResponseEntity.status(HttpStatus.OK).body(e.getEmail() + " is now an admin!");
    }

    @GetMapping(value = "/user/{userID}/registeredTournaments")
    public List<Tournament> getRegisteredTournaments(@PathVariable int userID) {
        return tournamentService.getRegisteredTournaments(userID);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable int id) {
        repository.delete(repository.findById(id));
    }
}

