package com.covoiturage.business.services;

import com.covoiturage.business.dao.UtilisateurDAO;
import com.covoiturage.business.dao.ConducteurDAO; // AJOUTÉ
import com.covoiturage.business.models.Utilisateur;
import com.covoiturage.business.models.Conducteur;

public class AuthService {
    private UtilisateurDAO utilisateurDAO;
    private ConducteurDAO conducteurDAO; // AJOUTÉ
    
    public AuthService() {
        this.utilisateurDAO = new UtilisateurDAO();
        this.conducteurDAO = new ConducteurDAO(); // INITIALISÉ
    }
    
    public boolean registerSimple(Utilisateur utilisateur) {
    try {
        System.out.println("\n📋 TENTATIVE D'INSCRIPTION SIMPLE");
        System.out.println("Email: " + utilisateur.getEmail());
        System.out.println("Nom: " + utilisateur.getNom() + " " + utilisateur.getPrenom());
        System.out.println("Type: " + utilisateur.getTypeUtilisateur());
        
        // Vérifier connexion BDD
        System.out.println("🔗 Test connexion BDD...");
        try {
            // Test rapide de connexion
            java.sql.Connection testConn = com.covoiturage.business.dao.DatabaseConnection.getConnection();
            System.out.println("✅ Connexion BDD OK");
            testConn.close();
        } catch (Exception e) {
            System.err.println("❌ Erreur connexion BDD: " + e.getMessage());
            return false;
        }
        
        // Vérifier si email existe
        System.out.println("🔍 Vérification email...");
        if (emailExists(utilisateur.getEmail())) {
            System.out.println("❌ Email déjà utilisé");
            return false;
        }
        
        // Créer l'utilisateur
        System.out.println("💾 Création dans UtilisateurDAO...");
        boolean success = utilisateurDAO.create(utilisateur);
        
        if (success) {
            System.out.println("✅ Utilisateur créé avec ID: " + utilisateur.getIdUtilisateur());
            System.out.println("✅ Solde initial: " + utilisateur.getSolde() + " DA");
            return true;
        } else {
            System.out.println("❌ Échec création dans DAO");
            return false;
        }
        
    } catch (Exception e) {
        System.err.println("💥 ERREUR INSCRIPTION: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
    // Connexion
    public Utilisateur login(String email, String password) {
        try {
            return utilisateurDAO.authenticate(email, password);
        } catch (Exception e) {
            System.err.println("Erreur lors de la connexion: " + e.getMessage());
            return null;
        }
    }
    
    // Inscription Complète (Sauvegarde réelle en BDD)
    public boolean register(Utilisateur utilisateur, Object details) {
        try {
            System.out.println("📝 Tentative d'inscription pour: " + utilisateur.getEmail());
            
            // 1. Vérifier si l'email existe déjà
            if (emailExists(utilisateur.getEmail())) {
                System.out.println("❌ Email déjà utilisé: " + utilisateur.getEmail());
                return false;
            }
            
            // 2. Créer l'utilisateur de base (Table Utilisateur)
            boolean userSuccess = utilisateurDAO.create(utilisateur);
            
            if (userSuccess) {
                System.out.println("✅ Utilisateur créé (ID: " + utilisateur.getIdUtilisateur() + ")");
                
                // 3. Si c'est un conducteur, enregistrer les détails du véhicule (Table Conducteur)
                if (details instanceof Conducteur) {
                    Conducteur conducteur = (Conducteur) details;
                    
                    // IMPORTANT : Lier l'ID utilisateur généré au conducteur
                    conducteur.setIdUtilisateur(utilisateur.getIdUtilisateur());
                    
                    // Sauvegarde réelle dans la table Conducteur via le DAO
                    boolean condSuccess = conducteurDAO.create(conducteur);
                    
                    if (condSuccess) {
                        System.out.println("✅ Infos conducteur enregistrées (Véhicule: " + conducteur.getModeleVoiture() + ")");
                        return true;
                    } else {
                        System.out.println("❌ Échec de l'enregistrement des infos conducteur");
                        return false;
                    }
                }
                
                // Si c'est un passager simple (pas de details spécifiques à sauver)
                return true;
                
            } else {
                System.out.println("❌ Échec de l'inscription dans la table Utilisateur");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'inscription: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Vérifier si email existe
    public boolean emailExists(String email) {
        try {
            Utilisateur utilisateur = utilisateurDAO.readByEmail(email);
            return utilisateur != null;
        } catch (Exception e) {
            System.err.println("Erreur vérification email: " + e.getMessage());
            return false;
        }
    }
    
    // Vérifier le solde
    public double getSolde(int idUtilisateur) {
        try {
            Utilisateur utilisateur = utilisateurDAO.readById(idUtilisateur);
            return utilisateur != null ? utilisateur.getSolde() : 0;
        } catch (Exception e) {
            System.err.println("Erreur récupération solde: " + e.getMessage());
            return 0;
        }
    }
}