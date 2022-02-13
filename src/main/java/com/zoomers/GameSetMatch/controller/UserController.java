package com.zoomers.GameSetMatch.controller;

import com.zoomers.GameSetMatch.entity.Employee;
import com.zoomers.GameSetMatch.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
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

    @PostMapping("/employee")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }
}
