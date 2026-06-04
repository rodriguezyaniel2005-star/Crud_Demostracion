package org.example.data;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static final String DB_NAME = "tienda";
    
    public static MongoDatabase getInstance() {
        if (database == null) {
            try {
                String connectionString = "mongodb://localhost:27017";
                mongoClient = MongoClients.create(connectionString);
                database = mongoClient.getDatabase(DB_NAME);
                
                System.out.println("MongoDB connection established successfully");
                System.out.println("Database: " + DB_NAME);
                
                // Initialize database with collections and seed data if needed
                DataSeeder.seedDatabaseIfEmpty(database);
                
            } catch (Exception e) {
                System.err.println("Failed to connect to MongoDB: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return database;
    }
    
    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed");
        }
    }
}
