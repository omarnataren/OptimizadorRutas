package com.chilitos.optimizador;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Optimi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel mapLabel = new JLabel("Aqui va a aparecer el mapa", SwingConstants.CENTER);
        mapLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(mapLabel, BorderLayout.CENTER);
        
        JPanel controlPanel = new JPanel();
        JButton loadMapButton = new JButton("Cargar Mapa");
        JButton findRouteButton = new JButton("Encontrar ruta");
        JTextField startField = new JTextField(10);
        JTextField endField = new JTextField(10);
        
        controlPanel.add(new JLabel("Inicio"));
        controlPanel.add(startField);
        controlPanel.add(new JLabel("Final:"));
        controlPanel.add(endField);
        controlPanel.add(loadMapButton);
        controlPanel.add(findRouteButton);
        
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        frame.add(panel);
        frame.setVisible(true);
    }
}
