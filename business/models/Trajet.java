package com.covoiturage.business.models;

import java.sql.Date;
import java.sql.Time;

public class Trajet {
    private int idTrajet;
    private int idConducteur;
    private String villeDepart;
    private String villeArrivee;
    private Date dateDepart;
    private Time heureDepart;
    private double prix;
    private int placesDisponibles;
    private String statut; // "PLANIFIE", "EN_COURS", "TERMINE", "ANNULE"
    
    // Constructeurs
    public Trajet() {}
    
    public Trajet(int idConducteur, String villeDepart, String villeArrivee, 
                 Date dateDepart, Time heureDepart, double prix, int placesDisponibles) {
        this.idConducteur = idConducteur;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.dateDepart = dateDepart;
        this.heureDepart = heureDepart;
        this.prix = prix;
        this.placesDisponibles = placesDisponibles;
        this.statut = "PLANIFIE";
    }
    
    // Getters et Setters
    public int getIdTrajet() { return idTrajet; }
    public void setIdTrajet(int idTrajet) { this.idTrajet = idTrajet; }
    
    public int getIdConducteur() { return idConducteur; }
    public void setIdConducteur(int idConducteur) { this.idConducteur = idConducteur; }
    
    public String getVilleDepart() { return villeDepart; }
    public void setVilleDepart(String villeDepart) { this.villeDepart = villeDepart; }
    
    public String getVilleArrivee() { return villeArrivee; }
    public void setVilleArrivee(String villeArrivee) { this.villeArrivee = villeArrivee; }
    
    public Date getDateDepart() { return dateDepart; }
    public void setDateDepart(Date dateDepart) { this.dateDepart = dateDepart; }
    
    public Time getHeureDepart() { return heureDepart; }
    public void setHeureDepart(Time heureDepart) { this.heureDepart = heureDepart; }
    
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    
    public int getPlacesDisponibles() { return placesDisponibles; }
    public void setPlacesDisponibles(int placesDisponibles) { this.placesDisponibles = placesDisponibles; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    // Méthode métier
    public boolean estDisponible() {
        return "PLANIFIE".equals(statut) && placesDisponibles > 0;
    }
    
    @Override
    public String toString() {
        return villeDepart + " → " + villeArrivee + " (" + dateDepart + " " + heureDepart + ") - " + prix + "€";
    }
}