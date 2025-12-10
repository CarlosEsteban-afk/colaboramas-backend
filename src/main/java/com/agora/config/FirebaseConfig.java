package com.agora.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        // Try several strategies to locate the Firebase service account JSON:
        // 1) Path from env var GOOGLE_APPLICATION_CREDENTIALS
        // 2) Path from env var FIREBASE_SERVICE_ACCOUNT
        // 3) File named `serviceAccountKey.json` in working dir
        // 4) If `serviceAccountKey.json` is a directory, pick the first *.json inside
        // 5) Classpath resource `serviceAccountKey.json`

        String pathEnv = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        if (pathEnv == null || pathEnv.isBlank()) {
            pathEnv = System.getenv("FIREBASE_SERVICE_ACCOUNT");
        }

        InputStream in = null;

        if (pathEnv != null && !pathEnv.isBlank()) {
            Path p = Paths.get(pathEnv);
            if (Files.exists(p)) {
                if (Files.isRegularFile(p)) {
                    in = Files.newInputStream(p);
                } else if (Files.isDirectory(p)) {
                    Optional<Path> firstJson = Files.list(p)
                        .filter(f -> f.toString().toLowerCase().endsWith(".json"))
                        .findFirst();
                    if (firstJson.isPresent()) {
                        in = Files.newInputStream(firstJson.get());
                    }
                }
            }
        }

        if (in == null) {
            Path local = Paths.get("serviceAccountKey.json");
            if (Files.exists(local)) {
                if (Files.isRegularFile(local)) {
                    in = Files.newInputStream(local);
                } else if (Files.isDirectory(local)) {
                    Optional<Path> firstJson = Files.list(local)
                        .filter(f -> f.toString().toLowerCase().endsWith(".json"))
                        .findFirst();
                    if (firstJson.isPresent()) {
                        in = Files.newInputStream(firstJson.get());
                    }
                }
            }
        }

        if (in == null) {
            // try classpath
            in = FirebaseConfig.class.getClassLoader().getResourceAsStream("serviceAccountKey.json");
        }

        if (in == null) {
            throw new java.io.FileNotFoundException("serviceAccountKey.json not found. Provide path via GOOGLE_APPLICATION_CREDENTIALS or FIREBASE_SERVICE_ACCOUNT, or place the file in the working dir or classpath.");
        }

        try (InputStream serviceAccount = in) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            return FirebaseMessaging.getInstance();
        }
    }
}