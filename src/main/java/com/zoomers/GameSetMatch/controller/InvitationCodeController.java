package com.zoomers.GameSetMatch.controller;

import com.zoomers.GameSetMatch.controller.Login.InvitationValidatedResponse;
import com.zoomers.GameSetMatch.entity.InvitationCode;
import com.zoomers.GameSetMatch.entity.User;
import com.zoomers.GameSetMatch.repository.InvitationCodeRepository;
import com.zoomers.GameSetMatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InvitationCodeController {
    @Autowired
    private InvitationCodeRepository codeRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/inviteCode")
    InvitationCode newInvitationCode() {
        InvitationCode inviteCode = new InvitationCode();
        return codeRepository.save(inviteCode);
    }

    @PostMapping(value = "/validateInviteCode/{code}")
    ResponseEntity<String> validateInviteCode(@PathVariable String code, @RequestBody User user) throws ParseException {
        Optional<InvitationCode> c = codeRepository.findById(code);

        if (c.isPresent() && !c.get().isExpired()) {
            User registeredUser = userRepository.findByEmail(user.getEmail());

            // user already in database
            if (registeredUser != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new InvitationValidatedResponse(registeredUser.getId(), "You have been registered.").toString());
            }

            User unregisteredUser = userRepository.save(user);

            return ResponseEntity.status(HttpStatus.OK).body(new InvitationValidatedResponse(unregisteredUser.getId(), "You have now been registered.").toString());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid code or code has expired.");
    }
}
