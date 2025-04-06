package com.chilitos.optimizador;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.chilitos.optimizador.firebase.FirebaseService;
import com.chilitos.optimizador.firebase.Paquete;
import com.chilitos.optimizador.firebase.Transportista;
import com.chilitos.optimizador.mapa.GrafoBuilder;
import com.chilitos.optimizador.mapa.MapaOSM;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.Gson;

public class JavaBridge {
    private WebEngine engine;
    private String modoSeleccion = "ninguno";

    private Stage parentStage;

    private double origenLat;
    private double origenLng;
    private boolean origenSet = false;

    private List<double[]> destinos = new ArrayList<>();

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
            String idPersonalizado = FirebaseService.generarIdPersonalizado("paquete");
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

    public void abrirMapa() {
        MapaOSM mapa = new MapaOSM();
        Scene scene = new Scene(mapa, 900, 600);
        Stage mapaStage = new Stage();
        mapaStage.setScene(scene);
        mapaStage.setTitle("Mapa de Rutas");
        mapaStage.show();
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

    private GrafoBuilder grafoBuilder = new GrafoBuilder(); 

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
        grafoBuilder.construirDesdeOverpass(origenLat, origenLng, 300);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
