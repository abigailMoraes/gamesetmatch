package com.zoomers.GameSetMatch.controller;

import com.zoomers.GameSetMatch.entity.InvitationCode;
import com.zoomers.GameSetMatch.entity.User;
import com.zoomers.GameSetMatch.repository.InvitationCodeRepository;
import com.zoomers.GameSetMatch.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InvitationCodeController {
    private final InvitationCodeRepository codeRepository;
    private final UserRepository userRepository;

    public InvitationCodeController(InvitationCodeRepository repository, UserRepository userRepository) {
        this.codeRepository = repository;
        this.userRepository = userRepository;
    }

    @PostMapping("/inviteCode")
    InvitationCode newInvitationCode() {
        InvitationCode inviteCode = new InvitationCode();
        return codeRepository.save(inviteCode);
    }

    @GetMapping("/validateInviteCode/{code}/{email}")
    Boolean validateInviteCode(@PathVariable String code, @PathVariable String email) throws ParseException {
        Optional<InvitationCode> c = codeRepository.findById(code);
        if (c.isPresent() && !c.get().isExpired()) {
            User user = new User();
            user.setEmail(email);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
