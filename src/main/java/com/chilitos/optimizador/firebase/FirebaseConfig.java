package com.chilitos.optimizador.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.Gson;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FirebaseConfig {
    public static void initFirebase() {
        try {
            Dotenv dotenv = Dotenv.load();

            Map<String, Object> credentials = new HashMap<>();
            credentials.put("type", dotenv.get("type"));
            credentials.put("project_id", dotenv.get("project_id"));
            credentials.put("private_key_id", dotenv.get("private_key_id"));
            credentials.put("private_key", dotenv.get("private_key").replace("\\n", "\n"));
            credentials.put("client_email", dotenv.get("client_email"));
            credentials.put("client_id", dotenv.get("client_id"));
            credentials.put("auth_uri", dotenv.get("auth_uri"));
            credentials.put("token_uri", dotenv.get("token_uri"));
            credentials.put("auth_provider_x509_cert_url", dotenv.get("auth_provider_x509_cert_url"));
            credentials.put("client_x509_cert_url", dotenv.get("client_x509_cert_url"));

            Gson gson = new Gson();
            String json = gson.toJson(credentials);

            InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials creds = GoogleCredentials.fromStream(stream);
                        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(creds)
                .build();

            FirebaseApp.initializeApp(options);
            System.out.println("✅ Firebase inicializado correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

