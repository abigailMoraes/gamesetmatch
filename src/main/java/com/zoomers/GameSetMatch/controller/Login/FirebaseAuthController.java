package com.zoomers.GameSetMatch.controller.Login;

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
    public UserResponse verifyIdToken(@RequestBody String firebaseIdToken) throws FirebaseAuthException {
        UserResponse returnUser = new UserResponse();
        User user = new User();
        try {

            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseIdToken);
            String uid = decodedToken.getUid();
            String name = decodedToken.getName();
            String email = decodedToken.getEmail();
            String picture = decodedToken.getPicture();

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

            returnUser.setId(user.getId());
            returnUser.setEmail(user.getEmail());
            returnUser.setName(user.getName());
            returnUser.setIsAdmin(user.getIsAdmin());
            returnUser.setPicture(picture);
        } catch (FirebaseAuthException ex) {
            ex.printStackTrace();
    }
        return returnUser;
    }
}