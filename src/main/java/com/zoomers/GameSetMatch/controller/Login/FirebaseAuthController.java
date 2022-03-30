package com.zoomers.GameSetMatch.controller.Login;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.zoomers.GameSetMatch.entity.User;
import com.zoomers.GameSetMatch.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api")
public class FirebaseAuthController {

    private final UserRepository repository;

    public FirebaseAuthController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/verifyIdToken")
    public ResponseEntity<String> verifyIdToken(@RequestBody String firebaseIdToken) throws FirebaseAuthException {
        UserResponse returnUser = new UserResponse();
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseIdToken);
            String uid = decodedToken.getUid();
            String name = decodedToken.getName();
            String email = decodedToken.getEmail();
            String picture = decodedToken.getPicture();

            // check DB if user exist
            User database_user = repository.findByFirebaseId(uid);

            if(database_user == null)  {
                User unregisteredUser = new User();
                unregisteredUser.setName(name);
                unregisteredUser.setEmail(email);
                unregisteredUser.setFirebaseId(uid);
                unregisteredUser.setIsAdmin(0);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(unregisteredUser.toString());
            }

            returnUser.setId(database_user.getId());
            returnUser.setEmail(database_user.getEmail());
            returnUser.setName(database_user.getName());
            returnUser.setIsAdmin(database_user.getIsAdmin());
            returnUser.setPicture(picture);
        } catch (FirebaseAuthException ex) {
            ex.printStackTrace();
    }
        return ResponseEntity.status(HttpStatus.OK).body(returnUser.toString());
    }
}
