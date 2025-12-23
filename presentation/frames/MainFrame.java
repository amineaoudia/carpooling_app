package com.covoiturage.presentation.frames;

import com.covoiturage.business.models.Utilisateur;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private Utilisateur utilisateur;
    
    public MainFrame(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        initComponents();
        setupFrame();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblWelcome = new JLabel(
            "<html><center>" +
            "<h1 style='color: #0066cc;'>🚗 Application Covoiturage</h1>" +
            "<h2>Bienvenue " + utilisateur.getPrenom() + " " + utilisateur.getNom() + " !</h2>" +
            "<p>Vous êtes connecté en tant que <b>" + utilisateur.getTypeUtilisateur() + "</b></p>" +
            "<hr>" +
            "<p><b>📧 Email:</b> " + utilisateur.getEmail() + "</p>" +
            "<p><b>📞 Téléphone:</b> " + utilisateur.getTelephone() + "</p>" +
            "<p><b>💰 Solde:</b> <span style='color: #2E7D32; font-size: 18px;'>" + 
            utilisateur.getSolde() + " €</span></p>" +
            "</center></html>",
            SwingConstants.CENTER
        );
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton btnRechercher = new JButton("🔍 Rechercher un trajet");
        JButton btnProfil = new JButton("👤 Mon profil");
        JButton btnDeconnexion = new JButton("🚪 Se déconnecter");
        
        btnRechercher.setPreferredSize(new Dimension(200, 40));
        btnProfil.setPreferredSize(new Dimension(200, 40));
        btnDeconnexion.setPreferredSize(new Dimension(200, 40));
        
        btnDeconnexion.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        
        buttonPanel.add(btnRechercher);
        buttonPanel.add(btnProfil);
        buttonPanel.add(btnDeconnexion);
        
        mainPanel.add(lblWelcome, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void setupFrame() {
        setTitle("CoCar - " + utilisateur.getPrenom() + " " + utilisateur.getNom());
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}