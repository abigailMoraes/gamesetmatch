package com.zoomers.GameSetMatch.controller;

import com.zoomers.GameSetMatch.entity.User;
import com.zoomers.GameSetMatch.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api")
public class UserController {
    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/hello")
    public String home() {
        return "Hello World!";
    }

    @PostMapping("/user")
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }
}
