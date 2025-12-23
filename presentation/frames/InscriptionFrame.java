package com.covoiturage.presentation.frames;

import com.covoiturage.business.services.AuthService;
import com.covoiturage.business.models.Utilisateur;
import com.covoiturage.business.models.Conducteur;
import com.covoiturage.business.models.Passager;
import javax.swing.*;
import java.awt.*;

public class InscriptionFrame extends JFrame {
    private JTextField txtNom, txtPrenom, txtEmail, txtTelephone;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JComboBox<String> comboType;
    private JButton btnRegister, btnBack;
    private JPanel panelConducteur;
    private JTextField txtNumeroPermis, txtModeleVoiture, txtPlaque;
    private AuthService authService;
    private final Color PRIMARY_BLUE = new Color(0, 102, 204);

    public InscriptionFrame() {
        authService = new AuthService();
        initComponents();
        setupFrame();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));

        // Logo
        JLabel lblLogo = new JLabel("CoCar");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 38));
        lblLogo.setForeground(PRIMARY_BLUE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblLogo);
        mainPanel.add(Box.createVerticalStrut(20));

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        txtNom = new JTextField();
        txtPrenom = new JTextField();
        txtEmail = new JTextField();
        txtTelephone = new JTextField();
        txtPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();
        comboType = new JComboBox<>(new String[]{"PASSAGER", "CONDUCTEUR"});
        
        // Taille uniforme des champs
        Dimension fieldSize = new Dimension(250, 35);
        txtNom.setPreferredSize(fieldSize);
        txtPrenom.setPreferredSize(fieldSize);
        txtEmail.setPreferredSize(fieldSize);
        txtTelephone.setPreferredSize(fieldSize);
        txtPassword.setPreferredSize(fieldSize);
        txtConfirmPassword.setPreferredSize(fieldSize);
        comboType.setPreferredSize(fieldSize);

        int r = 0;
        addField(formPanel, "Nom", txtNom, gbc, r++);
        addField(formPanel, "Prénom", txtPrenom, gbc, r++);
        addField(formPanel, "Email", txtEmail, gbc, r++);
        addField(formPanel, "Téléphone", txtTelephone, gbc, r++);
        addField(formPanel, "Mot de passe", txtPassword, gbc, r++);
        addField(formPanel, "Confirmation", txtConfirmPassword, gbc, r++);
        addField(formPanel, "Type d'utilisateur", comboType, gbc, r++);
        mainPanel.add(formPanel);

        // Panel Conducteur - RESTAURÉ
        panelConducteur = new JPanel(new GridBagLayout());
        panelConducteur.setBackground(new Color(248, 249, 250));
        panelConducteur.setVisible(false);
        txtNumeroPermis = new JTextField();
        txtModeleVoiture = new JTextField();
        txtPlaque = new JTextField();
        txtNumeroPermis.setPreferredSize(fieldSize);
        txtModeleVoiture.setPreferredSize(fieldSize);
        txtPlaque.setPreferredSize(fieldSize);

        int cr = 0;
        addField(panelConducteur, "N° Permis", txtNumeroPermis, gbc, cr++);
        addField(panelConducteur, "Véhicule", txtModeleVoiture, gbc, cr++);
        addField(panelConducteur, "Plaque", txtPlaque, gbc, cr++);
        mainPanel.add(panelConducteur);
        
        // Gestion affichage/masquage panel conducteur
        comboType.addActionListener(e -> {
            panelConducteur.setVisible("CONDUCTEUR".equals(comboType.getSelectedItem()));
            revalidate();
            repaint();
        });

        mainPanel.add(Box.createVerticalStrut(30));

        // Bouton Inscription
        btnRegister = new JButton("S'INSCRIRE");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRegister.setBackground(PRIMARY_BLUE);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setOpaque(true);
        btnRegister.setBorderPainted(false);
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegister.setMaximumSize(new Dimension(300, 45));
        mainPanel.add(btnRegister);

        // Bouton Retour
        btnBack = new JButton("Retour à la connexion");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnBack.setForeground(Color.GRAY);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnBack);

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        this.setContentPane(scroll);

        btnRegister.addActionListener(e -> register());
        btnBack.addActionListener(e -> goBack());
    }

    private void addField(JPanel p, String l, JComponent c, GridBagConstraints g, int row) {
        g.gridy = row * 2; p.add(new JLabel(l), g);
        g.gridy = (row * 2) + 1; p.add(c, g);
    }

    private void register() {
        // 1. Récupération des valeurs
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtTelephone.getText().trim();
        String pass = new String(txtPassword.getPassword());
        String confirmPass = new String(txtConfirmPassword.getPassword());
        String type = (String) comboType.getSelectedItem();

        // 2. Vérification des champs vides BASIQUES
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || 
            phone.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
            return;
        }

        // 3. Vérification champs conducteur si nécessaire
        if ("CONDUCTEUR".equals(type)) {
            if (txtNumeroPermis.getText().trim().isEmpty() || 
                txtModeleVoiture.getText().trim().isEmpty() || 
                txtPlaque.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Veuillez remplir tous les champs conducteur.",
                    "Informations manquantes",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // 4. Vérification correspondance mots de passe
        if (!pass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, 
                "Les mots de passe ne sont pas identiques.", 
                "Erreur de confirmation", 
                JOptionPane.ERROR_MESSAGE);
            txtConfirmPassword.requestFocus();
            return;
        }

        // 5. Validation email SIMPLIFIÉE
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Email incorrect (ex: nom@gmail.com)");
            return;
        }

        // 6. Validation téléphone SIMPLIFIÉE
        if (!phone.matches("^0[567][0-9]{8}$")) {
            JOptionPane.showMessageDialog(this, "Le téléphone doit commencer par 05, 06 ou 07 et faire 10 chiffres.");
            return;
        }

        // 7. Validation mot de passe SIMPLIFIÉE
        if (pass.length() < 4) {
            JOptionPane.showMessageDialog(this, "Le mot de passe doit contenir au moins 4 caractères.");
            return;
        }

        // 8. Vérifier si email existe déjà
        if (authService.emailExists(email)) {
            JOptionPane.showMessageDialog(this,
                "Cet email est déjà utilisé. Veuillez en choisir un autre.",
                "Email déjà existant",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 9. CRÉATION DE L'UTILISATEUR DANS LA BDD
        try {
            // Créer l'objet Utilisateur
            Utilisateur utilisateur = new Utilisateur(nom, prenom, email, pass, phone, type);
            
            // Préparer les détails spécifiques
            Object details = null;
            if ("CONDUCTEUR".equals(type)) {
                Conducteur conducteur = new Conducteur();
                conducteur.setNumeroPermis(txtNumeroPermis.getText().trim());
                conducteur.setModeleVoiture(txtModeleVoiture.getText().trim());
                conducteur.setPlaqueImmatriculation(txtPlaque.getText().trim());
                details = conducteur;
            } else {
                Passager passager = new Passager();
                details = passager;
            }
            
            // Appeler le service d'inscription
            boolean success = authService.register(utilisateur, details);
            
            if (success) {
                // SUCCÈS - Afficher message et retourner au login
                JOptionPane.showMessageDialog(this,
                    "✅ Inscription réussie sur CoCar !\n\n" +
                    "Bienvenue " + prenom + " " + nom + " !\n" +
                    "Type: " + type + "\n\n" +
                    "Vous pouvez maintenant vous connecter.",
                    "Inscription réussie",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Retour au login
                goBack();
            } else {
                JOptionPane.showMessageDialog(this,
                    "❌ Erreur lors de l'inscription.\nVeuillez réessayer.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "❌ Erreur technique : " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void goBack() {
        // Ouvrir LoginFrame
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
        // Fermer cette fenêtre
        this.dispose();
    }

    private void setupFrame() {
        setTitle("CoCar - Inscription");
        setSize(500, 700); // Un peu plus grand pour le panel conducteur
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}