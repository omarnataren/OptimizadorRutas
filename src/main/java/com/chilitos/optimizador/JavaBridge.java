package com.chilitos.optimizador;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.logging.Log;

import com.chilitos.optimizador.firebase.FirebaseService;
import com.chilitos.optimizador.firebase.Paquete;
import com.chilitos.optimizador.firebase.Transportista;
import com.chilitos.optimizador.mapa.GrafoBuilder;
import com.chilitos.optimizador.mapa.MapaOSM;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.Gson;

public class JavaBridge {
    private WebEngine engine;
    private String modoSeleccion = "ninguno";
    private GrafoBuilder grafoBuilder = new GrafoBuilder(); 
    private Stage parentStage;

    private double origenLat;
    private double origenLng;
    private boolean origenSet = false;

    private List<double[]> destinos = new ArrayList<>();
    private List<String> direccionesSeleccionadas = new ArrayList<>();

    public JavaBridge(Stage parentStage) {
        this.parentStage = parentStage;
    }

    public JavaBridge(WebEngine engine) {
        this.engine = engine;
    }

    public String obtenerPaquetesComoJson() throws Exception {
        List<Map<String, Object>> paquetes = FirebaseService.obtenerPaquetes();
        return new Gson().toJson(paquetes);
    }

    public String obtenerTransportistasComoJson() throws Exception {
        List<Map<String, Object>> transportistas = FirebaseService.obtenerTransportistas();
        return new Gson().toJson(transportistas);
    }

    public void añadirPaquetes(String nombre, String descripcion, double peso, String direccion, String estatus) throws Exception{
        try{
            String idPersonalizado = FirebaseService.generarIdPersonalizado("paquetes");
            Paquete paquete = new Paquete(nombre, descripcion, peso, direccion, estatus);
            FirestoreClient.getFirestore().collection("paquetes").document(idPersonalizado).set(paquete);
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("Error al añadir paquete: " + e.getMessage());
        }
    }

    public void añadirTransportistas(String nombre, String apellidoP, String apellidoM) throws Exception{
        try{    
            String idPersonalizado = FirebaseService.generarIdPersonalizado("transportista");
            Transportista transportista = new Transportista( nombre, apellidoP, apellidoM);
            FirestoreClient.getFirestore().collection("transportista").document(idPersonalizado).set(transportista);
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("Error al añadir transportista: " + e.getMessage());
        }
    }
    
    public void eliminarPaquetes(String tipo, String id){
        System.out.println("🧪 ID recibido: " + id);
        Alert alerta = new Alert(AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar acción");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Estás seguro de que deseas eliminar el elemento con ID " + id + "?");
        
        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK){
            Firestore db = FirestoreClient.getFirestore();
            DocumentReference docRef = db.collection(tipo).document(id);
            try {
                docRef.delete().get();
                System.out.println("Se eliminó el documento: " + id);
                DocumentReference contadorRef = db.collection("contadores").document("global");

                db.runTransaction(transaction -> {
                Long valorActual = transaction.get(contadorRef).get().getLong(tipo);
                long nuevoValor = (valorActual != null && valorActual > 0) ? valorActual - 1 : 0;
                transaction.update(contadorRef, tipo, nuevoValor);
                System.out.println("Contador actualizado. Nuevo valor de " + tipo + ": " + nuevoValor);
                return null;
            }).get();
            
            } catch (Exception e) {
                System.err.println("Error al eliminar el documento:");
                e.printStackTrace();
            }
        } else {
            System.out.println("Acción cancelada");
        }
    }

    public void abrirMapa() {
        MapaOSM mapa = new MapaOSM();
        Scene scene = new Scene(mapa, 900, 600);
        Stage mapaStage = new Stage();
        mapaStage.setScene(scene);
        mapaStage.setTitle("Mapa de Rutas");
        mapaStage.show();

        mapa.onDocumentReady(() -> {
            for (String direccion : direccionesSeleccionadas) {
                mapa.getJavaBridge().buscarYSeleccionar(direccion, "destino");
            }
        });
    }

    public void setDirecciones(Object lista) {
    
        if (lista instanceof JSObject) {
            JSObject jsList = (JSObject) lista;
            int length = (int) jsList.getMember("length");
    
            List<String> direcciones = new ArrayList<>();
    
            for (int i = 0; i < length; i++) {
                Object value = jsList.getSlot(i);
                direcciones.add(String.valueOf(value));
            }
    
            this.direccionesSeleccionadas = direcciones;
    
            System.out.println("Lista de direcciones:");
            direcciones.forEach(System.out::println);
        } else {
            System.out.println("No es una lista válida");
        }
    }

    public String getModoSeleccion() {
        return modoSeleccion;
    }

    public void setModoSeleccion(String modo) {
        this.modoSeleccion = modo;
    }

    public void setPoint(double lat, double lng) {
        if ("origen".equals(modoSeleccion)) {
            origenLat = lat;
            origenLng = lng;
            origenSet = true;
            System.out.println("Origen seleccionado: " + lat + ", " + lng);
            
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Origen añadido");
            alerta.setHeaderText(null);
            alerta.setContentText("Origen establecido correctamente.");
            alerta.showAndWait();
        } else if ("destino".equals(modoSeleccion)) {
            destinos.add(new double[]{lat, lng});
            System.out.println("Destino seleccionado: " + lat + ", " + lng);

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Destino añadido");
            alerta.setHeaderText(null);
            alerta.setContentText("Destino establecido correctamente.");
            alerta.showAndWait();
        } else {
            System.out.println("Selecciona un modo antes de hacer clic.");
        }
        this.modoSeleccion = "ninguno";
    }

    public void buscarYSeleccionar(String texto, String tipo) {
        JSObject window = (JSObject) engine.executeScript("window");
        window.call("buscarLugar", texto, tipo);
    }

    public void limpiarOrigen() {
        destinos.clear();
        JSObject window = (JSObject) engine.executeScript("window");
        window.call("limpiarMarcadores", "origen");

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Limpieza de origen");
        alerta.setHeaderText(null);
        alerta.setContentText("Se ha eliminado el origen del mapa");
        alerta.showAndWait();
    }

    public void limpiarDestinos() {
        destinos.clear();
        JSObject window = (JSObject) engine.executeScript("window");
        window.call("limpiarMarcadores", "destino");

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Limpieza de destinos");
        alerta.setHeaderText(null);
        alerta.setContentText("Se han eliminado los destinos del mapa");
        alerta.showAndWait();
    }

    public void calcularRuta() {
    if (!origenSet) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Ruta no válida");
        alerta.setHeaderText(null);
        alerta.setContentText("Primero selecciona un origen.");
        alerta.showAndWait();
        return;
    }

    try {
        grafoBuilder.construirDesdeOverpass(origenLat, origenLng, 100);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
