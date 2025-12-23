package com.covoiturage.business.dao;

import com.covoiturage.business.models.Avis;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisDAO {
    
    public boolean create(Avis avis) {
        String sql = "INSERT INTO Avis (idReservation, idDonneur, idReceveur, note, commentaire) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, avis.getIdReservation());
            pstmt.setInt(2, avis.getIdDonneur());
            pstmt.setInt(3, avis.getIdReceveur());
            pstmt.setInt(4, avis.getNote());
            pstmt.setString(5, avis.getCommentaire());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    avis.setIdAvis(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur création avis: " + e.getMessage());
        }
        return false;
    }
    
    public List<Avis> getByConducteur(int idConducteur) {
        List<Avis> avisList = new ArrayList<>();
        String sql = "SELECT * FROM Avis WHERE idReceveur = ? ORDER BY dateAvis DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idConducteur);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Avis avis = new Avis();
                avis.setIdAvis(rs.getInt("idAvis"));
                avis.setIdReservation(rs.getInt("idReservation"));
                avis.setIdDonneur(rs.getInt("idDonneur"));
                avis.setIdReceveur(rs.getInt("idReceveur"));
                avis.setNote(rs.getInt("note"));
                avis.setCommentaire(rs.getString("commentaire"));
                avis.setDateAvis(rs.getTimestamp("dateAvis"));
                
                avisList.add(avis);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération avis: " + e.getMessage());
        }
        return avisList;
    }
    
    public double getNoteMoyenne(int idConducteur) {
        String sql = "SELECT AVG(note) as moyenne FROM Avis WHERE idReceveur = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idConducteur);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("moyenne");
            }
        } catch (SQLException e) {
            System.err.println("Erreur calcul note moyenne: " + e.getMessage());
        }
        return 0.0;
    }
}