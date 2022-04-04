package com.zoomers.GameSetMatch;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


import java.io.*;

@SpringBootApplication
@ComponentScan(basePackages= { "com.zoomers" })
public class GameSetMatchApplication {
	public static void main(String[] args) throws IOException {
        Resource resource = new ClassPathResource("Firebase_ServiceAccountKey.json");

		InputStream serviceAccount = resource.getInputStream();

		FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();

		FirebaseApp.initializeApp(options);


		SpringApplication.run(GameSetMatchApplication.class, args);
	}

}
