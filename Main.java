package com.covoiturage;

import com.covoiturage.presentation.frames.LoginFrame;
import com.covoiturage.business.dao.DatabaseConnection;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("🚗 DÉMARRAGE APPLICATION COVOITURAGE 🚗");
        System.out.println("========================================\n");
        
        // 1. Créer la base de données et les tables si nécessaire
        System.out.println("📊 INITIALISATION DE LA BASE DE DONNÉES...");
        DatabaseConnection.createDatabaseIfNotExists();
        
        // 2. Tester la connexion
        System.out.println("\n🔗 TEST DE CONNEXION...");
        boolean connected = DatabaseConnection.testConnection();
        
        if (!connected) {
            String errorMsg = 
                "ERREUR : Impossible de se connecter à MySQL\n\n" +
                "Vérifiez que :\n" +
                "1. MySQL est démarré\n" +
                "2. Les identifiants sont corrects\n" +
                "   User: root\n" +
                "   Password: rootroot\n" +
                "3. Le port 3306 est accessible";
            
            JOptionPane.showMessageDialog(null, errorMsg, 
                "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        // 4. Configurer l'interface
        System.out.println("\n🎨 CONFIGURATION DE L'INTERFACE...");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Erreur look and feel: " + e.getMessage());
        }
        
        // 5. Lancer l'application
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
            
            // Afficher les identifiants
            afficherAideConnexion();
        });
        
        
    }
    
    
    
    private static void afficherAideConnexion() {
        String aide = 
            "🎯 BIENVENUE DANS CoCar !\n\n" +
            "Pour vous connecter, utilisez :\n\n" +
            "‍💼 ADMIN (Conducteur)\n" +
            "   Email: admin@gmail.com\n" +
            "   Mot de passe: admin123\n\n" +
            "👤 UTILISATEUR (Passager)\n" +
            "   Email: passager@gmail.com\n" +
            "   Mot de passe: user123\n\n";
        
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, aide, 
                "Identifiants de démonstration", JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    
}