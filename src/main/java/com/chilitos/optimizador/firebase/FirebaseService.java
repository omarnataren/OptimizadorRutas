package com.chilitos.optimizador.firebase;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import java.util.List;
import java.util.Map;

public class FirebaseService {
    public static List<Map<String, Object>> obtenerPaquetes() throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection("paquetes").get();
        List<QueryDocumentSnapshot> documentos = future.get().getDocuments();

        List<Map<String, Object>> lista = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documentos) {
            Map<String, Object> data = doc.getData();
            data.put("id", doc.getId());
            lista.add(data);
        }
        return lista;
    }

    public static List<Map<String, Object>> obtenerTransportistas() throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection("transportista").get();
        List<QueryDocumentSnapshot> documentos = future.get().getDocuments();

        List<Map<String, Object>> lista = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documentos) {
            Map<String, Object> data = doc.getData();
            data.put("id", doc.getId());
            lista.add(data);
        }
        return lista;
    }

    public static String generarIdPersonalizado(String tipo) throws Exception{
         Firestore db = FirestoreClient.getFirestore();
        DocumentReference ref = db.collection("contadores").document("global");
        return db.runTransaction(transaction -> {
        DocumentSnapshot snapshot = transaction.get(ref).get();

        long nuevo = snapshot.getLong(tipo) != null ? snapshot.getLong(tipo) + 1 : 1;

        transaction.update(ref, tipo, nuevo);

        String prefijo = tipo.equals("paquete") ? "PAQ" : "TRANS";
        return String.format("%s%04d", prefijo, nuevo);
    }).get();
    }


}
