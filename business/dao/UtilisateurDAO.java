package com.covoiturage.business.dao;

import com.covoiturage.business.models.Utilisateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {
    
    // Créer un utilisateur
    public boolean create(Utilisateur utilisateur) {
        String sql = "INSERT INTO Utilisateur (nom, prenom, email, motDePasse, telephone, typeUtilisateur) VALUES (?, ?, ?, SHA2(?, 256), ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, utilisateur.getNom());
            pstmt.setString(2, utilisateur.getPrenom());
            pstmt.setString(3, utilisateur.getEmail());
            pstmt.setString(4, utilisateur.getMotDePasse());
            pstmt.setString(5, utilisateur.getTelephone());
            pstmt.setString(6, utilisateur.getTypeUtilisateur());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    utilisateur.setIdUtilisateur(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur création utilisateur: " + e.getMessage());
        }
        return false;
    }
    
    // Lire un utilisateur par email
    public Utilisateur readByEmail(String email) {
        String sql = "SELECT * FROM Utilisateur WHERE email = ?";
        Utilisateur utilisateur = null;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                utilisateur = new Utilisateur();
                utilisateur.setIdUtilisateur(rs.getInt("idUtilisateur"));
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setPrenom(rs.getString("prenom"));
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setMotDePasse(rs.getString("motDePasse"));
                utilisateur.setTelephone(rs.getString("telephone"));
                utilisateur.setDateInscription(rs.getDate("dateInscription"));
                utilisateur.setSolde(rs.getDouble("solde"));
                utilisateur.setTypeUtilisateur(rs.getString("typeUtilisateur"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lecture utilisateur: " + e.getMessage());
        }
        return utilisateur;
    }
    
    // Authentifier un utilisateur
    public Utilisateur authenticate(String email, String password) {
        String sql = "SELECT * FROM Utilisateur WHERE email = ? AND motDePasse = SHA2(?, 256)";
        Utilisateur utilisateur = null;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                utilisateur = new Utilisateur();
                utilisateur.setIdUtilisateur(rs.getInt("idUtilisateur"));
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setPrenom(rs.getString("prenom"));
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setTelephone(rs.getString("telephone"));
                utilisateur.setDateInscription(rs.getDate("dateInscription"));
                utilisateur.setSolde(rs.getDouble("solde"));
                utilisateur.setTypeUtilisateur(rs.getString("typeUtilisateur"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur authentification: " + e.getMessage());
        }
        return utilisateur;
    }
    
    // Mettre à jour le solde
    public boolean updateSolde(int idUtilisateur, double nouveauSolde) {
        String sql = "UPDATE Utilisateur SET solde = ? WHERE idUtilisateur = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, nouveauSolde);
            pstmt.setInt(2, idUtilisateur);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour solde: " + e.getMessage());
        }
        return false;
    }
    
    public Utilisateur readById(int idUtilisateur) {
    String sql = "SELECT * FROM Utilisateur WHERE idUtilisateur = ?";
    Utilisateur utilisateur = null;
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, idUtilisateur);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            utilisateur = new Utilisateur();
            utilisateur.setIdUtilisateur(rs.getInt("idUtilisateur"));
            utilisateur.setNom(rs.getString("nom"));
            utilisateur.setPrenom(rs.getString("prenom"));
            utilisateur.setEmail(rs.getString("email"));
            utilisateur.setMotDePasse(rs.getString("motDePasse"));
            utilisateur.setTelephone(rs.getString("telephone"));
            utilisateur.setDateInscription(rs.getDate("dateInscription"));
            utilisateur.setSolde(rs.getDouble("solde"));
            utilisateur.setTypeUtilisateur(rs.getString("typeUtilisateur"));
        }
    } catch (SQLException e) {
        System.err.println("Erreur lecture utilisateur par ID: " + e.getMessage());
    }
    return utilisateur;
}
    
    // Obtenir tous les utilisateurs
    public List<Utilisateur> getAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM Utilisateur";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setIdUtilisateur(rs.getInt("idUtilisateur"));
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setPrenom(rs.getString("prenom"));
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setTelephone(rs.getString("telephone"));
                utilisateur.setDateInscription(rs.getDate("dateInscription"));
                utilisateur.setSolde(rs.getDouble("solde"));
                utilisateur.setTypeUtilisateur(rs.getString("typeUtilisateur"));
                
                utilisateurs.add(utilisateur);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération utilisateurs: " + e.getMessage());
        }
        return utilisateurs;
    }
    
    
}