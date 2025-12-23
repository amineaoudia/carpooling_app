package com.covoiturage.business.models;

public class Conducteur {
    private int idConducteur;
    private int idUtilisateur;
    private String numeroPermis;
    private String modeleVoiture;
    private String couleurVoiture;
    private String plaqueImmatriculation;
    private double noteMoyenne;
    
    // Constructeurs
    public Conducteur() {}
    
    public Conducteur(int idUtilisateur, String numeroPermis, String modeleVoiture, 
                     String couleurVoiture, String plaqueImmatriculation) {
        this.idUtilisateur = idUtilisateur;
        this.numeroPermis = numeroPermis;
        this.modeleVoiture = modeleVoiture;
        this.couleurVoiture = couleurVoiture;
        this.plaqueImmatriculation = plaqueImmatriculation;
        this.noteMoyenne = 0.0;
    }
    
    // Getters et Setters
    public int getIdConducteur() { return idConducteur; }
    public void setIdConducteur(int idConducteur) { this.idConducteur = idConducteur; }
    
    public int getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }
    
    public String getNumeroPermis() { return numeroPermis; }
    public void setNumeroPermis(String numeroPermis) { this.numeroPermis = numeroPermis; }
    
    public String getModeleVoiture() { return modeleVoiture; }
    public void setModeleVoiture(String modeleVoiture) { this.modeleVoiture = modeleVoiture; }
    
    public String getCouleurVoiture() { return couleurVoiture; }
    public void setCouleurVoiture(String couleurVoiture) { this.couleurVoiture = couleurVoiture; }
    
    public String getPlaqueImmatriculation() { return plaqueImmatriculation; }
    public void setPlaqueImmatriculation(String plaqueImmatriculation) { this.plaqueImmatriculation = plaqueImmatriculation; }
    
    public double getNoteMoyenne() { return noteMoyenne; }
    public void setNoteMoyenne(double noteMoyenne) { this.noteMoyenne = noteMoyenne; }
}