package com.zoomers.GameSetMatch.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Amazon ELB sends health check requests -> GET /
// our system returning 400 and ELB thinks its an error
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class HealthCheckController {
    @GetMapping("/")
    String healthCheck() {
        return "hi";
    }
}
