package com.chilitos.optimizador;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        MapaOSM panelMapa = new MapaOSM();
        Scene scene = new Scene(panelMapa, 900, 700);

        stage.setTitle("Optimizador de Rutas");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}


