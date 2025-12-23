package com.covoiturage.business.dao;

import com.covoiturage.business.models.Paiement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaiementDAO {
    
    public boolean create(Paiement paiement) {
        String sql = "INSERT INTO Paiement (idReservation, montant, methodePaiement) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, paiement.getIdReservation());
            pstmt.setDouble(2, paiement.getMontant());
            pstmt.setString(3, paiement.getMethodePaiement());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    paiement.setIdPaiement(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur création paiement: " + e.getMessage());
        }
        return false;
    }
    
    public List<Paiement> getByUtilisateur(int idUtilisateur) {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT p.* FROM Paiement p " +
                    "JOIN Reservation r ON p.idReservation = r.idReservation " +
                    "WHERE r.idPassager = ? " +
                    "ORDER BY p.datePaiement DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUtilisateur);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Paiement paiement = new Paiement();
                paiement.setIdPaiement(rs.getInt("idPaiement"));
                paiement.setIdReservation(rs.getInt("idReservation"));
                paiement.setMontant(rs.getDouble("montant"));
                paiement.setDatePaiement(rs.getTimestamp("datePaiement"));
                paiement.setMethodePaiement(rs.getString("methodePaiement"));
                paiement.setStatut(rs.getString("statut"));
                
                paiements.add(paiement);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération paiements: " + e.getMessage());
        }
        return paiements;
    }
}