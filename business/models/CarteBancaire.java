package com.covoiturage.business.models;

import java.sql.Date;

public class CarteBancaire {
    private int idCarte;
    private int idPassager;
    private String numeroCarte;
    private String titulaire;
    private Date dateExpiration;
    private String cryptogramme;
    
    // Constructeurs
    public CarteBancaire() {}
    
    public CarteBancaire(int idPassager, String numeroCarte, String titulaire, 
                        Date dateExpiration, String cryptogramme) {
        this.idPassager = idPassager;
        this.numeroCarte = numeroCarte;
        this.titulaire = titulaire;
        this.dateExpiration = dateExpiration;
        this.cryptogramme = cryptogramme;
    }
    
    // Getters et Setters
    public int getIdCarte() { return idCarte; }
    public void setIdCarte(int idCarte) { this.idCarte = idCarte; }
    
    public int getIdPassager() { return idPassager; }
    public void setIdPassager(int idPassager) { this.idPassager = idPassager; }
    
    public String getNumeroCarte() { return numeroCarte; }
    public void setNumeroCarte(String numeroCarte) { this.numeroCarte = numeroCarte; }
    
    public String getTitulaire() { return titulaire; }
    public void setTitulaire(String titulaire) { this.titulaire = titulaire; }
    
    public Date getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(Date dateExpiration) { this.dateExpiration = dateExpiration; }
    
    public String getCryptogramme() { return cryptogramme; }
    public void setCryptogramme(String cryptogramme) { this.cryptogramme = cryptogramme; }
    
    // Méthode métier
    public String getNumeroMasque() {
        if (numeroCarte == null || numeroCarte.length() < 4) {
            return "****";
        }
        return "**** **** **** " + numeroCarte.substring(numeroCarte.length() - 4);
    }
}