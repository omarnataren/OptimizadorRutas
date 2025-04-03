package com.chilitos.optimizador;

import com.chilitos.optimizador.firebase.FirebaseConfig;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        Interfaz vista = new Interfaz(primaryStage);
        Scene scene = new Scene(vista, 900, 600);
        
        primaryStage.setTitle("Gestión de Rutas y Paquetes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        FirebaseConfig.initFirebase();
        launch(args);
    }
}


