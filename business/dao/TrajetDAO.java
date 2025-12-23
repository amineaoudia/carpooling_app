package com.covoiturage.business.dao;

import com.covoiturage.business.models.Trajet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrajetDAO {
    
    // Créer un trajet
    public boolean create(Trajet trajet) {
        String sql = "INSERT INTO Trajet (idConducteur, villeDepart, villeArrivee, dateDepart, heureDepart, prix, placesDisponibles) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, trajet.getIdConducteur());
            pstmt.setString(2, trajet.getVilleDepart());
            pstmt.setString(3, trajet.getVilleArrivee());
            pstmt.setDate(4, trajet.getDateDepart());
            pstmt.setTime(5, trajet.getHeureDepart());
            pstmt.setDouble(6, trajet.getPrix());
            pstmt.setInt(7, trajet.getPlacesDisponibles());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    trajet.setIdTrajet(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur création trajet: " + e.getMessage());
        }
        return false;
    }
    
    // Rechercher des trajets
    public List<Trajet> search(String depart, String arrivee, Date date) {
        List<Trajet> trajets = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Trajet WHERE 1=1");
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            List<Object> params = new ArrayList<>();
            
            if (depart != null && !depart.isEmpty()) {
                sql.append(" AND villeDepart LIKE ?");
                params.add("%" + depart + "%");
            }
            
            if (arrivee != null && !arrivee.isEmpty()) {
                sql.append(" AND villeArrivee LIKE ?");
                params.add("%" + arrivee + "%");
            }
            
            if (date != null) {
                sql.append(" AND dateDepart = ?");
                params.add(date);
            }
            
            sql.append(" AND statut = 'PLANIFIE' AND placesDisponibles > 0 ORDER BY dateDepart, heureDepart");
            
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Trajet trajet = mapResultSetToTrajet(rs);
                trajets.add(trajet);
            }
        } catch (SQLException e) {
            System.err.println("Erreur recherche trajets: " + e.getMessage());
        }
        return trajets;
    }
    
    // Obtenir les trajets d'un conducteur
    public List<Trajet> getByConducteur(int idConducteur) {
        List<Trajet> trajets = new ArrayList<>();
        String sql = "SELECT * FROM Trajet WHERE idConducteur = ? ORDER BY dateDepart DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idConducteur);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Trajet trajet = mapResultSetToTrajet(rs);
                trajets.add(trajet);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération trajets conducteur: " + e.getMessage());
        }
        return trajets;
    }
    
    // MÉTHODE MANQUANTE - Obtenir un trajet par son ID
    public Trajet getById(int idTrajet) {
        String sql = "SELECT * FROM Trajet WHERE idTrajet = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idTrajet);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToTrajet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération trajet par ID: " + e.getMessage());
        }
        return null;
    }
    
    // MÉTHODE MANQUANTE - Mettre à jour les places disponibles
    public boolean updatePlaces(int idTrajet, int nouvellesPlaces) {
        String sql = "UPDATE Trajet SET placesDisponibles = ? WHERE idTrajet = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, nouvellesPlaces);
            pstmt.setInt(2, idTrajet);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour places: " + e.getMessage());
        }
        return false;
    }
    
    // Méthode utilitaire pour mapper un ResultSet à un Trajet
    private Trajet mapResultSetToTrajet(ResultSet rs) throws SQLException {
        Trajet trajet = new Trajet();
        trajet.setIdTrajet(rs.getInt("idTrajet"));
        trajet.setIdConducteur(rs.getInt("idConducteur"));
        trajet.setVilleDepart(rs.getString("villeDepart"));
        trajet.setVilleArrivee(rs.getString("villeArrivee"));
        trajet.setDateDepart(rs.getDate("dateDepart"));
        trajet.setHeureDepart(rs.getTime("heureDepart"));
        trajet.setPrix(rs.getDouble("prix"));
        trajet.setPlacesDisponibles(rs.getInt("placesDisponibles"));
        trajet.setStatut(rs.getString("statut"));
        return trajet;
    }
    
    // Méthode supplémentaire utile
    public List<Trajet> getTrajetsReserves(int idPassager) {
        List<Trajet> trajets = new ArrayList<>();
        String sql = "SELECT t.* FROM Trajet t " +
                    "JOIN Reservation r ON t.idTrajet = r.idTrajet " +
                    "WHERE r.idPassager = ? AND r.statut IN ('CONFIRMEE', 'EN_ATTENTE') " +
                    "ORDER BY t.dateDepart DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPassager);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Trajet trajet = mapResultSetToTrajet(rs);
                trajets.add(trajet);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération trajets réservés: " + e.getMessage());
        }
        
        
        return trajets;
    }
    
}