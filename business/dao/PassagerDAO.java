package com.covoiturage.business.dao;

import com.covoiturage.business.models.Passager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PassagerDAO {
    
    public boolean create(Passager passager) {
        String sql = "INSERT INTO Passager (idUtilisateur, preferences) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, passager.getIdUtilisateur());
            pstmt.setString(2, passager.getPreferences());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    passager.setIdPassager(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur création passager: " + e.getMessage());
        }
        return false;
    }
    
    public Passager readByUserId(int idUtilisateur) {
        String sql = "SELECT * FROM Passager WHERE idUtilisateur = ?";
        Passager passager = null;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUtilisateur);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                passager = new Passager();
                passager.setIdPassager(rs.getInt("idPassager"));
                passager.setIdUtilisateur(rs.getInt("idUtilisateur"));
                passager.setPreferences(rs.getString("preferences"));
                passager.setNoteMoyenne(rs.getDouble("noteMoyenne"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lecture passager: " + e.getMessage());
        }
        return passager;
    }
}