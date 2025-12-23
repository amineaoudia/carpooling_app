package com.covoiturage.business.services;

import com.covoiturage.business.dao.TrajetDAO;
import com.covoiturage.business.dao.ReservationDAO;
import com.covoiturage.business.models.Trajet;
import com.covoiturage.business.models.Reservation;
import java.sql.Date;
import java.util.List;

public class TrajetService {
    private TrajetDAO trajetDAO;
    private ReservationDAO reservationDAO;
    
    public TrajetService() {
        this.trajetDAO = new TrajetDAO();
        this.reservationDAO = new ReservationDAO();
    }
    
    // Rechercher des trajets
    public List<Trajet> searchTrajets(String depart, String arrivee, Date date) {
        try {
            List<Trajet> trajets = trajetDAO.search(depart, arrivee, date);
            System.out.println(trajets.size() + " trajet(s) trouvé(s) pour " + depart + " → " + arrivee + " le " + date);
            return trajets;
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche de trajets: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }
    
    // Obtenir les trajets d'un conducteur
    public List<Trajet> getTrajetsByConducteur(int idConducteur) {
        try {
            List<Trajet> trajets = trajetDAO.getByConducteur(idConducteur);
            System.out.println(trajets.size() + " trajet(s) trouvé(s) pour le conducteur #" + idConducteur);
            return trajets;
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des trajets conducteur: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }
    
    // Obtenir les trajets réservés par un passager
    public List<Trajet> getTrajetsReserves(int idPassager) {
        try {
            List<Trajet> trajets = trajetDAO.getTrajetsReserves(idPassager);
            System.out.println(trajets.size() + " trajet(s) réservé(s) trouvé(s) pour le passager #" + idPassager);
            return trajets;
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des trajets réservés: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }
    
    // Publier un nouveau trajet
    public boolean publierTrajet(Trajet trajet) {
        try {
            boolean success = trajetDAO.create(trajet);
            if (success) {
                System.out.println("Trajet publié avec succès. ID: " + trajet.getIdTrajet());
            } else {
                System.out.println("Échec de la publication du trajet");
            }
            return success;
        } catch (Exception e) {
            System.err.println("Erreur lors de la publication du trajet: " + e.getMessage());
            return false;
        }
    }
    
    // Réserver un trajet
    public boolean reserverTrajet(int idTrajet, int idPassager, int places) {
        try {
            // Vérifier la disponibilité
            Trajet trajet = trajetDAO.getById(idTrajet);
            if (trajet == null) {
                System.out.println("Trajet #" + idTrajet + " non trouvé");
                return false;
            }
            
            if (trajet.getPlacesDisponibles() < places) {
                System.out.println("Places insuffisantes. Disponible: " + trajet.getPlacesDisponibles() + ", Demandé: " + places);
                return false;
            }
            
            if (!"PLANIFIE".equals(trajet.getStatut())) {
                System.out.println("Trajet non disponible (statut: " + trajet.getStatut() + ")");
                return false;
            }
            
            // Créer la réservation
            Reservation reservation = new Reservation(idTrajet, idPassager, places);
            boolean reservationCree = reservationDAO.create(reservation);
            
            if (!reservationCree) {
                System.out.println("Échec de la création de la réservation");
                return false;
            }
            
            // Mettre à jour les places disponibles
            int nouvellesPlaces = trajet.getPlacesDisponibles() - places;
            boolean placesMisesAJour = trajetDAO.updatePlaces(idTrajet, nouvellesPlaces);
            
            if (placesMisesAJour) {
                double prixTotal = trajet.getPrix() * places;
                System.out.println("Réservation créée avec succès. ID: " + reservation.getIdReservation());
                System.out.println("Prix total: " + prixTotal + " € (" + places + " place(s) à " + trajet.getPrix() + " €)");
                System.out.println("Places restantes: " + nouvellesPlaces);
                return true;
            } else {
                System.out.println("Échec de la mise à jour des places disponibles");
                // Annuler la réservation en cas d'échec
                reservationDAO.annuler(reservation.getIdReservation());
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la réservation du trajet: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Obtenir un trajet par son ID
    public Trajet getTrajetById(int idTrajet) {
        try {
            Trajet trajet = trajetDAO.getById(idTrajet);
            if (trajet == null) {
                System.out.println("Trajet #" + idTrajet + " non trouvé");
            }
            return trajet;
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération du trajet: " + e.getMessage());
            return null;
        }
    }
    
    // Annuler un trajet
    public boolean annulerTrajet(int idTrajet) {
        try {
            // Logique d'annulation à implémenter
            System.out.println("Annulation du trajet #" + idTrajet + " (à implémenter)");
            return false;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'annulation du trajet: " + e.getMessage());
            return false;
        }
    }
}