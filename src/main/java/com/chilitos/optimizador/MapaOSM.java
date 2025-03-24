package com.chilitos.optimizador;

import javafx.scene.web.WebView;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;


public class MapaOSM extends BorderPane {

    public MapaOSM() {
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();

        engine.load(getClass().getResource("/mapa.html").toExternalForm());
        this.setCenter(webView);

        VBox barraLateral = new VBox(10);
        barraLateral.setPadding(new Insets(10));
        barraLateral.setStyle("-fx-background-color:rgb(216, 229, 143);");

        Label opciones = new Label("Opciones: ");
        Button button1 = new Button("Añadir nodo");
        Button button2 = new Button("Optimizar");

        barraLateral.getChildren().addAll(opciones, button1, button2);
        this.setLeft(barraLateral);
    }
}


