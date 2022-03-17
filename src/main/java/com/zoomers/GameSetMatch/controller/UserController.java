package com.zoomers.GameSetMatch.controller;

import com.zoomers.GameSetMatch.entity.User;
import com.zoomers.GameSetMatch.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/hello")
    public String home() {
        return "Hello World!";
    }

    @PostMapping("/employee")
    User newEmployee(@RequestBody User newEmployee) {
        return repository.save(newEmployee);
    }

    @GetMapping("/all")
    public List<User> getAll () {
            return repository.findAll();
        }

    @GetMapping("/participants/match/{id}")
    public List<User> getMatchParticipants ( @PathVariable int id){
        return repository.findMatchParticipantInfo(id);
    }

        if (e.getIsAdmin() == 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(email + " is already an admin!");
        } else if (e.getIsAdmin() == 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You don't have permission to touch this user!");
        }
        e.setIsAdmin(1);
        return ResponseEntity.status(HttpStatus.OK).body(repository.save(e));
    }
}
