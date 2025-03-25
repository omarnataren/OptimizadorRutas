package com.chilitos.optimizador;

import javafx.scene.control.Alert;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaBridge {
    private WebEngine engine;
    private String modoSeleccion = "ninguno";

    private double origenLat;
    private double origenLng;
    private boolean origenSet = false;

    private List<double[]> destinos = new ArrayList<>();

    public JavaBridge(WebEngine engine) {
        this.engine = engine;
    }

    public String getModoSeleccion() {
        return modoSeleccion;
    }

    public void setModoSeleccion(String modo) {
        this.modoSeleccion = modo;
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Aviso de selección");
        alerta.setHeaderText(null);
        alerta.setContentText("Se ha seleccionado un nuevo " + modo);
        alerta.showAndWait();
    }

    public void setPoint(double lat, double lng) {
        if ("origen".equals(modoSeleccion)) {
            origenLat = lat;
            origenLng = lng;
            origenSet = true;
            System.out.println("Origen seleccionado: " + lat + ", " + lng);
        } else if ("destino".equals(modoSeleccion)) {
            destinos.add(new double[]{lat, lng});
            System.out.println("Destino seleccionado: " + lat + ", " + lng);
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


