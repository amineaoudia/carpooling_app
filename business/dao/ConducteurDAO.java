package com.covoiturage.business.dao;

import com.covoiturage.business.models.Conducteur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConducteurDAO {
    
    // Créer un conducteur
    public boolean create(Conducteur conducteur) {
        String sql = "INSERT INTO Conducteur (idUtilisateur, numeroPermis, modeleVoiture, couleurVoiture, plaqueImmatriculation) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, conducteur.getIdUtilisateur());
            pstmt.setString(2, conducteur.getNumeroPermis());
            pstmt.setString(3, conducteur.getModeleVoiture());
            pstmt.setString(4, conducteur.getCouleurVoiture());
            pstmt.setString(5, conducteur.getPlaqueImmatriculation());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    conducteur.setIdConducteur(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur création conducteur: " + e.getMessage());
        }
        return false;
    }
    
    // Lire un conducteur par ID utilisateur
    public Conducteur readByUserId(int idUtilisateur) {
        String sql = "SELECT * FROM Conducteur WHERE idUtilisateur = ?";
        Conducteur conducteur = null;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUtilisateur);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                conducteur = new Conducteur();
                conducteur.setIdConducteur(rs.getInt("idConducteur"));
                conducteur.setIdUtilisateur(rs.getInt("idUtilisateur"));
                conducteur.setNumeroPermis(rs.getString("numeroPermis"));
                conducteur.setModeleVoiture(rs.getString("modeleVoiture"));
                conducteur.setCouleurVoiture(rs.getString("couleurVoiture"));
                conducteur.setPlaqueImmatriculation(rs.getString("plaqueImmatriculation"));
                conducteur.setNoteMoyenne(rs.getDouble("noteMoyenne"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lecture conducteur: " + e.getMessage());
        }
        return conducteur;
    }
    
    // Mettre à jour la note moyenne
    public boolean updateNoteMoyenne(int idConducteur, double nouvelleNote) {
        String sql = "UPDATE Conducteur SET noteMoyenne = ? WHERE idConducteur = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, nouvelleNote);
            pstmt.setInt(2, idConducteur);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour note: " + e.getMessage());
        }
        return false;
    }
    
    // Obtenir tous les conducteurs
    public List<Conducteur> getAll() {
        List<Conducteur> conducteurs = new ArrayList<>();
        String sql = "SELECT * FROM Conducteur";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Conducteur conducteur = new Conducteur();
                conducteur.setIdConducteur(rs.getInt("idConducteur"));
                conducteur.setIdUtilisateur(rs.getInt("idUtilisateur"));
                conducteur.setNumeroPermis(rs.getString("numeroPermis"));
                conducteur.setModeleVoiture(rs.getString("modeleVoiture"));
                conducteur.setCouleurVoiture(rs.getString("couleurVoiture"));
                conducteur.setPlaqueImmatriculation(rs.getString("plaqueImmatriculation"));
                conducteur.setNoteMoyenne(rs.getDouble("noteMoyenne"));
                
                conducteurs.add(conducteur);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération conducteurs: " + e.getMessage());
        }
        return conducteurs;
    }
}