package com.chilitos.optimizador;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.chilitos.optimizador.mapa.GrafoBuilder;
import com.chilitos.optimizador.mapa.MapaOSM;

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
