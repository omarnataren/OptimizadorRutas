package com.chilitos.optimizador;

import com.chilitos.optimizador.firebase.Paquete;
import com.chilitos.optimizador.firebase.Transportista;
import com.google.gson.Gson;
import com.chilitos.optimizador.firebase.FirebaseService;
import javafx.stage.Stage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.BorderPane;
import netscape.javascript.JSObject;

public class Interfaz extends BorderPane {
    private WebEngine engine;

    public Interfaz(Stage mainStage) {
        WebView webView = new WebView();
        engine = webView.getEngine();

        engine.setOnAlert(event -> {
            System.out.println("📢 JS alert: " + event.getData());
        });        

        engine.load(getClass().getResource("/index.html").toExternalForm());
        this.setCenter(webView);

        engine.documentProperty().addListener((obs, oldDoc, newDoc) -> {
            if (newDoc != null) {
                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("javaConnector", new JavaBridge(mainStage));
            }
        });

    }

    public void cargarYMostrarPaquetes(){
        List<Paquete> paquetes = new ArrayList<>();
        try {
            paquetes = FirebaseService.obtenerPaquetes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String jsonPaquetes = gson.toJson(paquetes);
        engine.executeScript("mostrarPaquetes(" + jsonPaquetes + ")");
    }

    public void cargarYMostrarTransportistas(){
        List<Transportista> transportistas = new ArrayList<>();
        try {
            transportistas = FirebaseService.obtenerTransportistas();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String jsonTransportista = gson.toJson(transportistas);
        engine.executeScript("mostrarPaquetes(" + jsonTransportista + ")");
    }
}

