package com.chilitos.optimizador.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseConfig {
    public static void initFirebase() {
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/clave-firebase.json");

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://<optimizador-rutas-4334f>.firebaseio.com")
                .build();

            FirebaseApp.initializeApp(options);
            System.out.println("✅ Firebase inicializado correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

