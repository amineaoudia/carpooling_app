package com.covoiturage.business.dao;

import com.covoiturage.business.models.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    
    public boolean create(Reservation reservation) {
        String sql = "INSERT INTO Reservation (idTrajet, idPassager, nombrePlaces, statut) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, reservation.getIdTrajet());
            pstmt.setInt(2, reservation.getIdPassager());
            pstmt.setInt(3, reservation.getNombrePlaces());
            pstmt.setString(4, reservation.getStatut() != null ? reservation.getStatut() : "EN_ATTENTE");
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    reservation.setIdReservation(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur création réservation: " + e.getMessage());
        }
        return false;
    }
    
    public List<Reservation> getByPassager(int idPassager) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservation WHERE idPassager = ? ORDER BY dateReservation DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPassager);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Reservation reservation = mapResultSetToReservation(rs);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération réservations passager: " + e.getMessage());
        }
        return reservations;
    }
    
    // MÉTHODE MANQUANTE - Ajoutez celle-ci
    public List<Reservation> getByConducteur(int idConducteur) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.* FROM Reservation r " +
                    "JOIN Trajet t ON r.idTrajet = t.idTrajet " +
                    "WHERE t.idConducteur = ? " +
                    "ORDER BY r.dateReservation DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idConducteur);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Reservation reservation = mapResultSetToReservation(rs);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération réservations conducteur: " + e.getMessage());
        }
        return reservations;
    }
    
    public boolean annuler(int idReservation) {
        String sql = "UPDATE Reservation SET statut = 'ANNULEE' WHERE idReservation = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idReservation);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur annulation réservation: " + e.getMessage());
        }
        return false;
    }
    
    // MÉTHODE MANQUANTE - Ajoutez celle-ci
    public boolean confirmer(int idReservation) {
        String sql = "UPDATE Reservation SET statut = 'CONFIRMEE' WHERE idReservation = ? AND statut = 'EN_ATTENTE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idReservation);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur confirmation réservation: " + e.getMessage());
        }
        return false;
    }
    
    // Méthode utilitaire pour mapper un ResultSet à un objet Reservation
    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setIdReservation(rs.getInt("idReservation"));
        reservation.setIdTrajet(rs.getInt("idTrajet"));
        reservation.setIdPassager(rs.getInt("idPassager"));
        reservation.setDateReservation(rs.getTimestamp("dateReservation"));
        reservation.setNombrePlaces(rs.getInt("nombrePlaces"));
        reservation.setStatut(rs.getString("statut"));
        return reservation;
    }
    
    // Méthode supplémentaire utile
    public Reservation getById(int idReservation) {
        String sql = "SELECT * FROM Reservation WHERE idReservation = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idReservation);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération réservation par ID: " + e.getMessage());
        }
        return null;
    }
}