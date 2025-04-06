package com.chilitos.optimizador;

import javafx.stage.Stage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


import javafx.scene.layout.BorderPane;
import netscape.javascript.JSObject;

public class Interfaz extends BorderPane {
    private WebEngine engine;

    public Interfaz(Stage mainStage) {
        WebView webView = new WebView();
        engine = webView.getEngine();

        engine.setOnAlert(event -> {
            System.out.println("JS alert: " + event.getData());
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
}

