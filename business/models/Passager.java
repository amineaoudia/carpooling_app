package com.covoiturage.business.models;

public class Passager {
    private int idPassager;
    private int idUtilisateur;
    private String preferences;
    private double noteMoyenne;
    
    // Constructeurs
    public Passager() {}
    
    public Passager(int idUtilisateur, String preferences) {
        this.idUtilisateur = idUtilisateur;
        this.preferences = preferences;
        this.noteMoyenne = 0.0;
    }
    
    // Getters et Setters
    public int getIdPassager() { return idPassager; }
    public void setIdPassager(int idPassager) { this.idPassager = idPassager; }
    
    public int getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }
    
    public String getPreferences() { return preferences; }
    public void setPreferences(String preferences) { this.preferences = preferences; }
    
    public double getNoteMoyenne() { return noteMoyenne; }
    public void setNoteMoyenne(double noteMoyenne) { this.noteMoyenne = noteMoyenne; }
}