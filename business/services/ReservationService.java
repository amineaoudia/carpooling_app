package com.covoiturage.business.services;

import com.covoiturage.business.dao.ReservationDAO;
import com.covoiturage.business.models.Reservation;
import java.util.List;

public class ReservationService {
    private ReservationDAO reservationDAO;
    
    public ReservationService() {
        this.reservationDAO = new ReservationDAO();
    }
    
    // Créer une nouvelle réservation
    public boolean creerReservation(int idTrajet, int idPassager, int nombrePlaces) {
        try {
            Reservation reservation = new Reservation(idTrajet, idPassager, nombrePlaces);
            boolean result = reservationDAO.create(reservation);
            
            if (result) {
                System.out.println("Réservation créée avec succès. ID: " + reservation.getIdReservation());
            } else {
                System.out.println("Échec de la création de la réservation");
            }
            
            return result;
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de la réservation: " + e.getMessage());
            return false;
        }
    }
    
    // Récupérer les réservations d'un passager
    public List<Reservation> getReservationsByPassager(int idPassager) {
        try {
            List<Reservation> reservations = reservationDAO.getByPassager(idPassager);
            System.out.println("Réservations trouvées pour le passager #" + idPassager + ": " + reservations.size());
            return reservations;
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des réservations passager: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }
    
    // Récupérer les réservations d'un conducteur (pour ses trajets)
    public List<Reservation> getReservationsByConducteur(int idConducteur) {
        try {
            List<Reservation> reservations = reservationDAO.getByConducteur(idConducteur);
            System.out.println("Réservations trouvées pour le conducteur #" + idConducteur + ": " + reservations.size());
            return reservations;
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des réservations conducteur: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }
    
    // Annuler une réservation
    public boolean annulerReservation(int idReservation) {
        try {
            boolean result = reservationDAO.annuler(idReservation);
            
            if (result) {
                System.out.println("Réservation #" + idReservation + " annulée avec succès");
            } else {
                System.out.println("Échec de l'annulation de la réservation #" + idReservation);
            }
            
            return result;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'annulation de la réservation: " + e.getMessage());
            return false;
        }
    }
    
    // Confirmer une réservation
    public boolean confirmerReservation(int idReservation) {
        try {
            boolean result = reservationDAO.confirmer(idReservation);
            
            if (result) {
                System.out.println("Réservation #" + idReservation + " confirmée avec succès");
            } else {
                System.out.println("Échec de la confirmation de la réservation #" + idReservation);
            }
            
            return result;
        } catch (Exception e) {
            System.err.println("Erreur lors de la confirmation de la réservation: " + e.getMessage());
            return false;
        }
    }
    
    // Récupérer une réservation par son ID
    public Reservation getReservationById(int idReservation) {
        try {
            Reservation reservation = reservationDAO.getById(idReservation);
            if (reservation == null) {
                System.out.println("Réservation #" + idReservation + " non trouvée");
            }
            return reservation;
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération de la réservation: " + e.getMessage());
            return null;
        }
    }
    
    // Vérifier si une place est disponible sur un trajet
    public boolean verifierDisponibilite(int idTrajet, int placesDemandees) {
        // Note: Cette méthode nécessite d'accéder au TrajetDAO
        // Pour l'instant, on retourne true (implémentez la logique réelle)
        System.out.println("Vérification disponibilité pour trajet #" + idTrajet + ", places: " + placesDemandees);
        return true;
    }
}