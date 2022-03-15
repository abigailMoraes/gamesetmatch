package com.zoomers.GameSetMatch.controller;

import com.zoomers.GameSetMatch.entity.InvitationCode;
import com.zoomers.GameSetMatch.repository.InvitationCodeRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")
public class InvitationCodeController {
    private final InvitationCodeRepository repository;

    public InvitationCodeController(InvitationCodeRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/inviteCode")
    InvitationCode newInvitationCode() {
        InvitationCode inviteCode = new InvitationCode();
        return repository.save(inviteCode);
    }
}