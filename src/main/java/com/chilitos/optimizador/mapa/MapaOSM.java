package com.chilitos.optimizador.mapa;

import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import com.chilitos.optimizador.JavaBridge;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

        VBox barraLateral = new VBox(12);
        barraLateral.setPadding(new Insets(20));
        barraLateral.setStyle("-fx-background-color:rgb(28, 105, 229);");
        
        Label opciones = new Label("Opciones: ");
        opciones.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        opciones.setTextFill(Color.WHITE);
        Label origen = new Label("Origen ");
        TextField origenField = new TextField();
        Button origenButton = new Button("+ Añadir");
        Button origenMButton = new Button("Añadir Manualmente");
        Button limpiarOButton = new Button("Limpiar origen");
        Label destino = new Label("Destino ");
        TextField destinoField = new TextField();
        Button destinoButton = new Button("+ Añadir");
        Button destinoMButton = new Button("Añadir Manualmente");
        Button limpiarButton = new Button("Limpiar destinos");
        Button calcular = new Button("Calcular ruta");
        calcular.setPrefWidth(220);
        calcular.setPrefHeight(40);
        calcular.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        calcular.setTextFill(Color.WHITE);
        
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
        
        origenMButton.setOnAction(e -> {
            if (javaBridge != null) {
                javaBridge.setModoSeleccion("origen");
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Modo actualizado");
                alerta.setHeaderText(null);
                alerta.setContentText("Estas en modo de seleccion de origen");
                alerta.showAndWait();
            }
        });
        
        destinoMButton.setOnAction(e -> {
            if (javaBridge != null) {
                javaBridge.setModoSeleccion("destino");
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Modo actualizado");
                alerta.setHeaderText(null);
                alerta.setContentText("Estas en modo de seleccion de destino");
                alerta.showAndWait();
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
        
        HBox origenGrupo = new HBox(10);
        origenGrupo.getChildren().addAll(origenField, origenButton);

        HBox destinoGrupo = new HBox(10);
        destinoGrupo.getChildren().addAll(destinoField, destinoButton);

        barraLateral.getChildren().addAll(
            opciones,
            origen, origenGrupo, origenMButton,
            limpiarOButton,
            destino, destinoGrupo, destinoMButton,
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


