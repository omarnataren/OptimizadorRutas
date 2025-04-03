package com.chilitos.optimizador.firebase;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import java.util.List;

public class FirebaseService {
    public static List<Paquete> obtenerPaquetes() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection("paquetes").get();
        List<QueryDocumentSnapshot> documentos = future.get().getDocuments();

        List<Paquete> lista = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documentos) {
            Paquete p = doc.toObject(Paquete.class);
            lista.add(p);
        }
        return lista;
    }
}
