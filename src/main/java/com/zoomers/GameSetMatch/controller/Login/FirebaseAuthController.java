package com.zoomers.GameSetMatch.controller.Login;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.zoomers.GameSetMatch.entity.InvitationCode;
import com.zoomers.GameSetMatch.entity.User;
import com.zoomers.GameSetMatch.repository.InvitationCodeRepository;
import com.zoomers.GameSetMatch.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api")
public class FirebaseAuthController {

    private final InvitationCodeRepository codeRepository;
    private final UserRepository repository;

    public FirebaseAuthController(UserRepository repository, InvitationCodeRepository codeRepository) {
        this.repository = repository;
        this.codeRepository = codeRepository;
    }

    @PostMapping(value = {"/verifyIdToken","/verifyIdToken/{invitationcode}"})
    public ResponseEntity<String> verifyIdToken(@RequestBody String firebaseIdToken,
                                                @PathVariable(required = false) String invitationcode ) {
        UserResponse returnUser = new UserResponse();
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseIdToken);
            String uid = decodedToken.getUid();
            String name = decodedToken.getName();
            String email = decodedToken.getEmail();
            String picture = decodedToken.getPicture();

            // check DB if user exist
            User database_user = repository.findByFirebaseId(uid);
            if (database_user != null) {
                returnUser.setId(database_user.getId());
                returnUser.setFirebaseId(database_user.getFirebaseId());
                returnUser.setEmail(database_user.getEmail());
                returnUser.setName(database_user.getName());
                returnUser.setIsAdmin(database_user.getIsAdmin());
                returnUser.setPicture(picture);
                return ResponseEntity.status(HttpStatus.OK).body(returnUser.toString());
            } else if (invitationcode != null) {
                InvitationCode code = codeRepository.getById(invitationcode);
                boolean isValid = code.isValid() && !code.isExpired();
                if (isValid && database_user == null) {
                    User newRegistrant = new User();
                    newRegistrant.setName(name);
                    newRegistrant.setEmail(email);
                    newRegistrant.setFirebaseId(uid);
                    newRegistrant.setIsAdmin(0);
                    repository.save(newRegistrant);
                    return ResponseEntity.status(HttpStatus.OK).body(newRegistrant.toString());
                }
            }
            UserResponse unregisteredUser = new UserResponse();
            unregisteredUser.setName(name);
            unregisteredUser.setEmail(email);
            unregisteredUser.setFirebaseId(uid);
            unregisteredUser.setIsAdmin(0);
            unregisteredUser.setPicture(picture);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(unregisteredUser.toString());
        } catch (FirebaseAuthException | ParseException ex) {
            ex.printStackTrace();
    }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Should not reach here");
    }
}
