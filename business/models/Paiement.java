package com.covoiturage.business.models;

import java.sql.Timestamp;

public class Paiement {
    private int idPaiement;
    private int idReservation;
    private double montant;
    private Timestamp datePaiement;
    private String methodePaiement; // "SOLDE", "CARTE"
    private String statut; // "PAYE", "EN_ATTENTE", "REFUSE"
    
    // Constructeurs
    public Paiement() {}
    
    public Paiement(int idReservation, double montant, String methodePaiement) {
        this.idReservation = idReservation;
        this.montant = montant;
        this.methodePaiement = methodePaiement;
        this.statut = "EN_ATTENTE";
        this.datePaiement = new Timestamp(System.currentTimeMillis());
    }
    
    // Getters et Setters
    public int getIdPaiement() { return idPaiement; }
    public void setIdPaiement(int idPaiement) { this.idPaiement = idPaiement; }
    
    public int getIdReservation() { return idReservation; }
    public void setIdReservation(int idReservation) { this.idReservation = idReservation; }
    
    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }
    
    public Timestamp getDatePaiement() { return datePaiement; }
    public void setDatePaiement(Timestamp datePaiement) { this.datePaiement = datePaiement; }
    
    public String getMethodePaiement() { return methodePaiement; }
    public void setMethodePaiement(String methodePaiement) { this.methodePaiement = methodePaiement; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}