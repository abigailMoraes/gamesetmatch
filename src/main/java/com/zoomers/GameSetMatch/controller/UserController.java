package com.zoomers.GameSetMatch.controller;

import com.zoomers.GameSetMatch.entity.User;
import com.zoomers.GameSetMatch.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")
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

    @GetMapping("/employee/{email}")
    public User getEmployeesByEmail(@PathVariable String email) {
        User e = repository.findByEmail(email);
        return e;
    }

    @PostMapping("/employeeToAdmin/{email}")
    @ResponseBody
    ResponseEntity<Object> toAdmin(@PathVariable String email) {
        User e = repository.findByEmail(email);
        if (e == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot find user with this email!");
        }
        if (e.getIs_admin() == 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(email + " is already an admin!");
        } else if (e.getIs_admin() == 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You don't have permission to touch this user!");
        }
        e.setIs_admin(1);
        return ResponseEntity.status(HttpStatus.OK).body(repository.save(e));
    }
}
