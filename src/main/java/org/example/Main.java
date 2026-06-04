package org.example;

import org.example.view.MainFrame;
import org.example.data.MongoConnection;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Initialize MongoDB connection
        MongoConnection.getInstance();
        
        // Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
        });
        
        // Add shutdown hook to close database connection
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            MongoConnection.closeConnection();
            System.out.println("Application closed");
        }));
    }
}
