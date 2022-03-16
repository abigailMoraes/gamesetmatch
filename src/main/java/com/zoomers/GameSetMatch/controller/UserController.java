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

        @GetMapping("/match/{id}")
        public List<User> getMatchParticipants ( @PathVariable int id){
            return repository.findMatchParticipantInfo(id);
        }

        @PostMapping("/new")
        User newUser (@RequestBody User newUser){
            return repository.save(newUser);
        }

}
