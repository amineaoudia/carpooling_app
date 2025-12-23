package com.covoiturage.business.models;

import java.sql.Timestamp;

public class Avis {
    private int idAvis;
    private int idReservation;
    private int idDonneur;   // Passager qui donne l'avis
    private int idReceveur;  // Conducteur qui reçoit l'avis
    private int note;        // 1 à 5
    private String commentaire;
    private Timestamp dateAvis;
    
    // Constructeurs
    public Avis() {}
    
    public Avis(int idReservation, int idDonneur, int idReceveur, int note, String commentaire) {
        this.idReservation = idReservation;
        this.idDonneur = idDonneur;
        this.idReceveur = idReceveur;
        this.note = note;
        this.commentaire = commentaire;
        this.dateAvis = new Timestamp(System.currentTimeMillis());
    }
    
    // Getters et Setters
    public int getIdAvis() { return idAvis; }
    public void setIdAvis(int idAvis) { this.idAvis = idAvis; }
    
    public int getIdReservation() { return idReservation; }
    public void setIdReservation(int idReservation) { this.idReservation = idReservation; }
    
    public int getIdDonneur() { return idDonneur; }
    public void setIdDonneur(int idDonneur) { this.idDonneur = idDonneur; }
    
    public int getIdReceveur() { return idReceveur; }
    public void setIdReceveur(int idReceveur) { this.idReceveur = idReceveur; }
    
    public int getNote() { return note; }
    public void setNote(int note) { 
        if (note < 1) note = 1;
        if (note > 5) note = 5;
        this.note = note; 
    }
    
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    
    public Timestamp getDateAvis() { return dateAvis; }
    public void setDateAvis(Timestamp dateAvis) { this.dateAvis = dateAvis; }
}