package com.covoiturage.business.services;

import com.covoiturage.business.dao.PaiementDAO;
import com.covoiturage.business.dao.UtilisateurDAO;
import com.covoiturage.business.models.Paiement;
import com.covoiturage.business.models.Utilisateur;

public class PaiementService {
    private PaiementDAO paiementDAO;
    private UtilisateurDAO utilisateurDAO;
    
    public PaiementService() {
        this.paiementDAO = new PaiementDAO();
        this.utilisateurDAO = new UtilisateurDAO();
    }
    
    public boolean effectuerPaiement(int idReservation, double montant, String methode) {
        try {
            // Créer l'enregistrement de paiement
            Paiement paiement = new Paiement(idReservation, montant, methode);
            boolean success = paiementDAO.create(paiement);
            
            if (success && "SOLDE".equals(methode)) {
                System.out.println("Paiement par solde effectué pour la réservation #" + idReservation);
            }
            
            return success;
        } catch (Exception e) {
            System.err.println("Erreur lors du paiement: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean effectuerPaiementParSolde(int idUtilisateur, int idReservation, double montant) {
        try {
            // 1. Vérifier si l'utilisateur a assez de solde
            if (!verifierSoldeSuffisant(idUtilisateur, montant)) {
                System.out.println("Solde insuffisant pour l'utilisateur #" + idUtilisateur);
                return false;
            }
            
            // 2. Débiter le solde
            Utilisateur utilisateur = utilisateurDAO.readById(idUtilisateur);
            if (utilisateur == null) {
                System.out.println("Utilisateur non trouvé: #" + idUtilisateur);
                return false;
            }
            
            double ancienSolde = utilisateur.getSolde();
            double nouveauSolde = ancienSolde - montant;
            
            boolean miseAJour = utilisateurDAO.updateSolde(idUtilisateur, nouveauSolde);
            
            if (!miseAJour) {
                System.out.println("Échec de la mise à jour du solde");
                return false;
            }
            
            // 3. Créer l'enregistrement de paiement
            Paiement paiement = new Paiement(idReservation, montant, "SOLDE");
            boolean paiementCree = paiementDAO.create(paiement);
            
            if (paiementCree) {
                System.out.println("Paiement effectué avec succès. Ancien solde: " + ancienSolde + 
                                 " €, Nouveau solde: " + nouveauSolde + " €");
            }
            
            return paiementCree;
            
        } catch (Exception e) {
            System.err.println("Erreur lors du paiement par solde: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public void rechargerSolde(int idUtilisateur, double montant) {
        try {
            if (montant <= 0) {
                throw new IllegalArgumentException("Le montant doit être positif");
            }
            
            Utilisateur utilisateur = utilisateurDAO.readById(idUtilisateur);
            if (utilisateur == null) {
                throw new RuntimeException("Utilisateur non trouvé: #" + idUtilisateur);
            }
            
            double ancienSolde = utilisateur.getSolde();
            double nouveauSolde = ancienSolde + montant;
            
            boolean misAJour = utilisateurDAO.updateSolde(idUtilisateur, nouveauSolde);
            
            if (misAJour) {
                enregistrerRechargement(idUtilisateur, montant);
                System.out.println("Rechargement réussi. Ancien solde: " + ancienSolde + 
                                 " €, Nouveau solde: " + nouveauSolde + " €");
            } else {
                throw new RuntimeException("Échec de la mise à jour du solde en base de données");
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors du rechargement: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Échec du rechargement", e);
        }
    }
    
    public void enregistrerRechargement(int idUtilisateur, double montant) {
        System.out.println("Rechargement enregistré: Utilisateur #" + idUtilisateur + " +" + montant + " €");
    }
    
    public boolean verifierSoldeSuffisant(int idUtilisateur, double montant) {
        try {
            Utilisateur utilisateur = utilisateurDAO.readById(idUtilisateur);
            if (utilisateur == null) {
                System.out.println("Utilisateur non trouvé: #" + idUtilisateur);
                return false;
            }
            double soldeActuel = utilisateur.getSolde();
            System.out.println("Solde de l'utilisateur #" + idUtilisateur + ": " + soldeActuel + " €");
            return soldeActuel >= montant;
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification du solde: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public double getSoldeUtilisateur(int idUtilisateur) {
        try {
            Utilisateur utilisateur = utilisateurDAO.readById(idUtilisateur);
            if (utilisateur == null) {
                throw new RuntimeException("Utilisateur non trouvé: #" + idUtilisateur);
            }
            return utilisateur.getSolde();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération du solde: " + e.getMessage());
            e.printStackTrace();
            return 0.0;
        }
    }
}