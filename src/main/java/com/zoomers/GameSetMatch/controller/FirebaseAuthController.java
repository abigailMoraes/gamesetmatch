package com.zoomers.GameSetMatch.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.zoomers.GameSetMatch.entity.User;
import com.zoomers.GameSetMatch.repository.UserRepository;
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
    public User verifyIdToken(@RequestBody String firebaseIdToken) throws FirebaseAuthException {
        User user = new User();
        try {

            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseIdToken);
            String uid = decodedToken.getUid();
            String name = decodedToken.getName();
            String email = decodedToken.getEmail();

            // check DB if user exist
            User database_user = repository.findByFirebaseId(uid);

            if(database_user == null)  {
                user.setName(name);
                user.setEmail(email);
                user.setFirebaseId(uid);
                user.setIsAdmin(0);
                repository.save(user);
            } else {
                user = database_user;
            }

        } catch (FirebaseAuthException ex) {
            ex.printStackTrace();
    }
        return user;
    }
}