package com.zoomers.GameSetMatch.controller;

import com.zoomers.GameSetMatch.entity.User;
import com.zoomers.GameSetMatch.repository.UserRepository;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
public class UserController {
    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/hello")
    public String home() {
        return "Hello World!";
    }

    @GetMapping("/all")
    public List<User> getAll(){
        return repository.findAll();
    }

    @GetMapping("/match/{id}")
    public List<User> getMatchParticipants(@PathVariable int id){
        return repository.findMatchParticipantInfo(id);
    }

    @PostMapping("/new")
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }
}
