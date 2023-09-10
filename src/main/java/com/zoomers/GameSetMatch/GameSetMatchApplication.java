package com.zoomers.GameSetMatch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.nio.charset.StandardCharsets;

import java.io.*;

@SpringBootApplication
@ComponentScan(basePackages= { "com.zoomers" })
public class GameSetMatchApplication {
	@Value("${FIREBASE_KEY}")
    private String serviceAccountKeyFile;

	public static void main(String[] args) throws IOException {
        //Resource resource = new ClassPathResource("Firebase_ServiceAccountKey.json");
		String firebaseKey = System.getenv("FIREBASE_KEY");
    	System.out.println("FIREBASE_KEY: " + firebaseKey);
		GameSetMatchApplication application = new GameSetMatchApplication();
        application.initializeFirebaseAndRun(firebaseKey, args);

		
	}
    public void initializeFirebaseAndRun(String serviceAccountKeyFile, String[] args) throws IOException {
		InputStream serviceAccount = new ByteArrayInputStream(serviceAccountKeyFile.getBytes());
			System.out.println("FIREBASE_KEY: " + serviceAccount);
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.build();

			FirebaseApp.initializeApp(options);

			SpringApplication.run(GameSetMatchApplication.class, args);
	}

}
