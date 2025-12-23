package com.covoiturage.business.models;

import java.sql.Timestamp;

public class Reservation {
    private int idReservation;
    private int idTrajet;
    private int idPassager;
    private Timestamp dateReservation;
    private int nombrePlaces;
    private String statut; // "CONFIRMEE", "EN_ATTENTE", "ANNULEE"
    
    // Constructeurs
    public Reservation() {}
    
    public Reservation(int idTrajet, int idPassager, int nombrePlaces) {
        this.idTrajet = idTrajet;
        this.idPassager = idPassager;
        this.nombrePlaces = nombrePlaces;
        this.statut = "EN_ATTENTE";
        this.dateReservation = new Timestamp(System.currentTimeMillis());
    }
    
    // Getters et Setters
    public int getIdReservation() { return idReservation; }
    public void setIdReservation(int idReservation) { this.idReservation = idReservation; }
    
    public int getIdTrajet() { return idTrajet; }
    public void setIdTrajet(int idTrajet) { this.idTrajet = idTrajet; }
    
    public int getIdPassager() { return idPassager; }
    public void setIdPassager(int idPassager) { this.idPassager = idPassager; }
    
    public Timestamp getDateReservation() { return dateReservation; }
    public void setDateReservation(Timestamp dateReservation) { this.dateReservation = dateReservation; }
    
    public int getNombrePlaces() { return nombrePlaces; }
    public void setNombrePlaces(int nombrePlaces) { this.nombrePlaces = nombrePlaces; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    // Méthode métier
    public double calculerPrixTotal(double prixUnitaire) {
        return prixUnitaire * nombrePlaces;
    }
}