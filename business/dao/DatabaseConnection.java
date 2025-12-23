package com.covoiturage.business.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // CORRECTION : Utilisez rootroot comme mot de passe
    private static final String URL = "jdbc:mysql://localhost:3306/covoiturage_db";
    private static final String USER = "root";          // Utilisateur MySQL
    private static final String PASSWORD = "rootroot";  // Mot de passe MySQL
    
    static {
        try {
            // Charger le driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("");
        } catch (ClassNotFoundException e) {
            System.err.println("");
            System.err.println("");
            System.err.println("");
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        System.out.println("");
        System.out.println("   URL: " + URL);
        System.out.println("   User: " + USER);
        System.out.println("   Password: " + PASSWORD.replaceAll(".", "*"));
        
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Connexion MySQL établie !");
        return conn;
    }
    
    public static boolean testConnection() {
        System.out.println("\nTEST DE CONNEXION MYSQL ?");
        
        try {
            Connection conn = getConnection();
            
            // Tester si la base existe
            String dbName = conn.getCatalog();
            System.out.println("Connexion réussie à la base: " + dbName);
            
            // Tester la version
            String version = conn.getMetaData().getDatabaseProductVersion();
            System.out.println("Version MySQL: " + version);
            
            // Tester les tables
            boolean hasUserTable = tableExists("Utilisateur");
            System.out.println("Table Utilisateur existe: " + hasUserTable);
            
            conn.close();
            return true;
            
        } catch (SQLException e) {
            System.err.println("\n❌ ERREUR DE CONNEXION MYSQL");
            System.err.println("Message: " + e.getMessage());
            System.err.println("\n🔧 SOLUTIONS :");
            System.err.println("1. Vérifiez que MySQL est démarré :");
            System.err.println("   Windows: services.msc → MySQL");
            System.err.println("   Linux/Mac: sudo service mysql start");
            System.err.println("2. Créez la base si elle n'existe pas :");
            System.err.println("   CREATE DATABASE covoiturage_db;");
            System.err.println("3. Vérifiez les identifiants :");
            System.err.println("   User: " + USER);
            System.err.println("   Password: " + PASSWORD);
            return false;
        }
    }
    
    private static boolean tableExists(String tableName) {
        String sql = "SHOW TABLES LIKE ?";
        
        try (Connection conn = getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tableName);
            java.sql.ResultSet rs = pstmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            System.err.println("Erreur vérification table: " + e.getMessage());
            return false;
        }
    }
    
    // Méthode pour créer la base de données si elle n'existe pas
    public static void createDatabaseIfNotExists() {
        try {
            // Se connecter sans base spécifique
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/", USER, PASSWORD);
            
            java.sql.Statement stmt = conn.createStatement();
            
            // Créer la base si elle n'existe pas
            String createDbSQL = "CREATE DATABASE IF NOT EXISTS covoiturage_db " +
                               "CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
            stmt.executeUpdate(createDbSQL);
            System.out.println("✅ Base 'covoiturage_db' vérifiée/créée");
            
            // Utiliser la base
            stmt.executeUpdate("USE covoiturage_db");
            
            // Créer la table Utilisateur si elle n'existe pas
            String createTableSQL = 
                "CREATE TABLE IF NOT EXISTS Utilisateur (" +
                "  idUtilisateur INT PRIMARY KEY AUTO_INCREMENT," +
                "  nom VARCHAR(50) NOT NULL," +
                "  prenom VARCHAR(50) NOT NULL," +
                "  email VARCHAR(100) UNIQUE NOT NULL," +
                "  motDePasse VARCHAR(255) NOT NULL," +
                "  telephone VARCHAR(20)," +
                "  dateInscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "  solde DECIMAL(10,2) DEFAULT 0.00," +
                "  typeUtilisateur ENUM('CONDUCTEUR', 'PASSAGER') DEFAULT 'PASSAGER'" +
                ")";
            stmt.executeUpdate(createTableSQL);
            System.out.println("✅ Table 'Utilisateur' vérifiée/créée");
            
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Erreur création base/tables: " + e.getMessage());
        }
    }
}