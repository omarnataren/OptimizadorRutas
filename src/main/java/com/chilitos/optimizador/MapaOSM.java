package com.chilitos.optimizador;

import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebEngine;
import javafx.scene.paint.Color;


public class MapaOSM extends BorderPane {
    private WebView webView;
    private WebEngine engine;
    private JavaBridge javaBridge;

    public MapaOSM() {
        this.webView = new WebView();
        this.engine = webView.getEngine();

        engine.load(getClass().getResource("/mapa.html").toExternalForm());
        this.setCenter(webView);

        engine.documentProperty().addListener((obs, oldDoc, newDoc) -> {
            if (newDoc != null) {
                javaBridge = new JavaBridge(engine);
                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("javaConnector", javaBridge);
            }
        });

        VBox barraLateral = new VBox(15);
        barraLateral.setPadding(new Insets(15));
        barraLateral.setStyle("-fx-background-color:rgb(182, 197, 99);");
        
        Label opciones = new Label("Opciones: ");
        opciones.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        opciones.setTextFill(Color.WHITE);
        Label origen = new Label("Origen ");
        TextField origenField = new TextField();
        Button origenButton = new Button("+ Añadir");
        Button limpiarOButton = new Button("Limpiar origen");
        Label destino = new Label("Destino ");
        TextField destinoField = new TextField();
        Button destinoButton = new Button("+ Añadir");
        Button limpiarButton = new Button("Limpiar destinos");
        Button calcular = new Button("Calcular ruta");

        origenButton.setOnAction(e -> {
            String texto = origenField.getText();
            if (!texto.isEmpty() && javaBridge != null) {
                javaBridge.buscarYSeleccionar(texto, "origen");
            }
        });
        
        destinoButton.setOnAction(e -> {
            String texto = destinoField.getText();
            if (!texto.isEmpty() && javaBridge != null) {
                javaBridge.buscarYSeleccionar(texto, "destino");
            }
        });     
        
        limpiarOButton.setOnAction(e -> {
            origenField.setText("");
            if (javaBridge != null) {
                javaBridge.limpiarOrigen();
            }
        });

        limpiarButton.setOnAction(e -> {
            destinoField.setText("");
            if (javaBridge != null) {
                javaBridge.limpiarDestinos();
            }
        });

        calcular.setOnAction(e -> {
            if (javaBridge != null) {
                javaBridge.calcularRuta();
            }
        });
        

        barraLateral.getChildren().addAll(
            opciones,
            origen, origenField, origenButton,
            limpiarOButton,
            destino, destinoField, destinoButton,
            limpiarButton,
            calcular
        );

        this.setLeft(barraLateral);
    }

    public WebView getWebView() {
        return webView;
    }

    public JavaBridge getJavaBridge() {
        return javaBridge;
    }

    public WebEngine getWebEngine() {
        return engine;
    }
}


