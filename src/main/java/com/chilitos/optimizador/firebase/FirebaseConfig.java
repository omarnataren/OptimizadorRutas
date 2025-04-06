package com.chilitos.optimizador.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.Gson;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FirebaseConfig {
    public static void initFirebase() {
        try {
            Dotenv dotenv = Dotenv.load();

            InputStream templateStream = FirebaseConfig.class.getClassLoader().getResourceAsStream("firebase-service-template.json");
            if (templateStream == null) {
                throw new FileNotFoundException("No se encontró el archivo de plantilla.");
            }

            String templateJson = new String(templateStream.readAllBytes(), StandardCharsets.UTF_8);

            // Reemplaza las variables del template por sus valores del .env
            String jsonWithSecrets = templateJson
                    .replace("${private_key_id}", dotenv.get("private_key_id"))
                    .replace("${private_key}", dotenv.get("private_key").replace("\\n", "\n"))
                    .replace("${client_email}", dotenv.get("client_email"))
                    .replace("${client_id}", dotenv.get("client_id"))
                    .replace("${client_x509_cert_url}", dotenv.get("client_x509_cert_url"));

            InputStream stream = new ByteArrayInputStream(jsonWithSecrets.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials creds = GoogleCredentials.fromStream(stream);
                        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(creds)
                .build();

            FirebaseApp.initializeApp(options);
            System.out.println("Firebase inicializado correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

