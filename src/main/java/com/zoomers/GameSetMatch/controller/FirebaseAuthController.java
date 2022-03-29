package com.zoomers.GameSetMatch.controller;

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
        User user = new User();
        try {

            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseIdToken);
            String uid = decodedToken.getUid();
            String name = decodedToken.getName();
            String email = decodedToken.getEmail();

            // check DB if user exist
            User database_user = repository.findByFirebaseId(uid);

            if(database_user == null)  {
                if (repository.findByEmail(email) != null) {
                    User unregisteredUser = repository.findByEmail(email);
                    unregisteredUser.setName(name);
                    unregisteredUser.setEmail(email);
                    unregisteredUser.setFirebaseId(uid);
                    unregisteredUser.setIsAdmin(0);
                    repository.save(unregisteredUser);
                    return ResponseEntity.status(HttpStatus.OK).body(unregisteredUser.toString());
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("{\"email\":\"%s\"}", email));
            }
            user = database_user;

        } catch (FirebaseAuthException ex) {
            ex.printStackTrace();
    }
        System.out.println("should be valid user");
        return ResponseEntity.status(HttpStatus.OK).body(user.toString());
    }
}
