package com.covoiturage.presentation.frames;

import com.covoiturage.business.services.AuthService;
import com.covoiturage.business.models.Utilisateur;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;
    private AuthService authService;

    public LoginFrame() {
        // Initialisation du service (Assurez-vous que la classe AuthService est implémentée)
        try {
            authService = new AuthService();
        } catch (Exception e) {
            System.err.println("Erreur initialisation AuthService: " + e.getMessage());
        }
        
        initComponents();
        setupFrame();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        mainPanel.setBackground(Color.WHITE);

        // 1. TITRE
        JLabel lblTitle = new JLabel("CoCar");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(new Color(0, 102, 204));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Connectez-vous à votre compte");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(Color.GRAY);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 2. PANEL CHAMPS (GridBagLayout pour un alignement propre)
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtEmail = new JTextField(20);
        txtEmail.setPreferredSize(new Dimension(300, 35));

        JLabel lblPassword = new JLabel("Mot de passe");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtPassword = new JPasswordField(20);
        txtPassword.setPreferredSize(new Dimension(300, 35));

        // Ajout des composants au fieldsPanel
        gbc.gridy = 0; fieldsPanel.add(lblEmail, gbc);
        gbc.gridy = 1; fieldsPanel.add(txtEmail, gbc);
        gbc.gridy = 2; fieldsPanel.add(lblPassword, gbc);
        gbc.gridy = 3; fieldsPanel.add(txtPassword, gbc);

        // 3. BOUTONS
        btnLogin = new JButton("SE CONNECTER");
        stylePrimaryButton(btnLogin);

        btnRegister = new JButton("S'INSCRIRE");
        styleSecondaryButton(btnRegister);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(300, 100));
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);

        // 4. ASSEMBLAGE
        mainPanel.add(lblTitle);
        mainPanel.add(lblSubtitle);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(fieldsPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);

        // 5. ACTIONS
        btnLogin.addActionListener(e -> login());
        btnRegister.addActionListener(e -> openRegisterFrame());

        // Focus par défaut
        SwingUtilities.invokeLater(() -> txtEmail.requestFocusInWindow());
    }

    private void stylePrimaryButton(JButton btn) {
        btn.setBackground(new Color(0, 102, 204));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Important pour le rendu sur certains systèmes :
        btn.setOpaque(true);
        btn.setBorderPainted(false);
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(0, 102, 204));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupFrame() {
        setTitle("CoCar - Connexion");
        pack(); // Ajuste la taille automatiquement selon les composants
        setSize(450, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void login() {
    String email = txtEmail.getText().trim();
    String password = new String(txtPassword.getPassword());

    // 1. Vérification des champs vides
    if (email.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs", "Champs vides", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // 2. Validation Email (@ et format)
    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    if (!email.matches(emailRegex)) {
        JOptionPane.showMessageDialog(this, "Format d'email invalide (ex: utilisateur@domaine.com)", "Erreur Email", JOptionPane.ERROR_MESSAGE);
        txtEmail.requestFocus();
        return;
    }

    // 3. Validation Mot de Passe (Min 4 caractères, lettres + chiffres)
    // (?=.*[0-9]) : doit contenir au moins un chiffre
    // (?=.*[a-zA-Z]) : doit contenir au moins une lettre
    // .{4,} : au moins 4 caractères au total
    String passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z]).{4,}$";
    
    if (!password.matches(passwordRegex)) {
        JOptionPane.showMessageDialog(this, 
            "Le mot de passe doit contenir au moins 4 caractères,\n" +
            "incluant au moins une lettre et un chiffre.", 
            "Sécurité Mot de Passe", 
            JOptionPane.WARNING_MESSAGE);
        txtPassword.requestFocus();
        return;
    }

    // 4. Authentification
    if (authService == null) {
        JOptionPane.showMessageDialog(this, "Service d'authentification indisponible.");
        return;
    }

    Utilisateur utilisateur = authService.login(email, password);
    if (utilisateur != null) {
        openMainFrame(utilisateur);
        this.dispose();
    } else {
        JOptionPane.showMessageDialog(this, "Identifiants incorrects", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}

    private void openRegisterFrame() {
    // 1. Créer une instance de la nouvelle fenêtre
    InscriptionFrame inscriptionFrame = new InscriptionFrame();
    
    // 2. Rendre la nouvelle fenêtre visible
    inscriptionFrame.setVisible(true);
    
    // 3. Fermer (ou cacher) la fenêtre de connexion actuelle
    this.dispose(); 
}

    private void openMainFrame(Utilisateur utilisateur) {
        // Fermer cette fenêtre
        this.dispose();
        
        // Ouvrir la fenêtre principale en fonction du type d'utilisateur
        SwingUtilities.invokeLater(() -> {
            try {
                if ("PASSAGER".equals(utilisateur.getTypeUtilisateur())) {
                    // Essayer d'ouvrir PassagerFrame
                    try {
                        PassagerFrame passagerFrame = new PassagerFrame(utilisateur);
                        passagerFrame.setVisible(true);
                        System.out.println("✅ PassagerFrame ouverte pour: " + utilisateur.getEmail());
                    } catch (Exception e) {
                        System.err.println("Erreur ouverture PassagerFrame: " + e.getMessage());
                        // Fallback: MainFrame
                        MainFrame mainFrame = new MainFrame(utilisateur);
                        mainFrame.setVisible(true);
                    }
                    
                } else if ("CONDUCTEUR".equals(utilisateur.getTypeUtilisateur())) {
                    // Essayer d'ouvrir ConducteurFrame
                    try {
                        ConducteurFrame conducteurFrame = new ConducteurFrame(utilisateur);
                        conducteurFrame.setVisible(true);
                        System.out.println("✅ ConducteurFrame ouverte pour: " + utilisateur.getEmail());
                    } catch (Exception e) {
                        System.err.println("Erreur ouverture ConducteurFrame: " + e.getMessage());
                        // Fallback: MainFrame
                        MainFrame mainFrame = new MainFrame(utilisateur);
                        mainFrame.setVisible(true);
                    }
                    
                } else {
                    // Par défaut: MainFrame
                    MainFrame mainFrame = new MainFrame(utilisateur);
                    mainFrame.setVisible(true);
                }
                
            } catch (Exception e) {
                System.err.println("❌ Erreur critique: " + e.getMessage());
                e.printStackTrace();
                
                // En cas d'erreur, afficher un message et réouvrir le login
                JOptionPane.showMessageDialog(null,
                    "Erreur d'ouverture de l'application: " + e.getMessage() + 
                    "\nVeuillez redémarrer l'application.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                
                new LoginFrame().setVisible(true);
            }
        });
    }

    // Version de secours si MainFrame n'existe pas encore
    private void openMainFrameSimple(Utilisateur utilisateur) {
        this.dispose();
        
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Application Covoiturage");
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            
            JLabel label = new JLabel(
                "<html><center>" +
                "<h1 style='color: #0066cc;'>🎉 Connexion réussie !</h1>" +
                "<h3>Bienvenue " + utilisateur.getPrenom() + " " + utilisateur.getNom() + "</h3>" +
                "<p>Type: <b>" + utilisateur.getTypeUtilisateur() + "</b></p>" +
                "<p>Email: " + utilisateur.getEmail() + "</p>" +
                "<p>Solde: <span style='color: green;'>" + utilisateur.getSolde() + " €</span></p>" +
                "<br><p>L'interface principale est en cours de développement...</p>" +
                "</center></html>",
                SwingConstants.CENTER
            );
            
            JButton btnDeconnexion = new JButton("Se déconnecter");
            btnDeconnexion.addActionListener(e -> {
                frame.dispose();
                new LoginFrame().setVisible(true);
            });
            
            panel.add(label, BorderLayout.CENTER);
            panel.add(btnDeconnexion, BorderLayout.SOUTH);
            
            frame.add(panel);
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        // Look and Feel Système pour une meilleure apparence
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            
            // Afficher les comptes de test disponibles
            SwingUtilities.invokeLater(() -> {
                String testAccounts = "<html><div style='padding: 10px;'>" +
                    "<h3>🎯 Comptes de test disponibles</h3>" +
                    "<p><b>Passager:</b> sophie.martin@email.com / password123</p>" +
                    "<p><b>Conducteur:</b> jean.dupont@email.com / password123</p>" +
                    "<p><b>Conducteur:</b> pierre.bernard@email.com / demo123</p>" +
                    "<p><b>Passager:</b> marie.dubois@email.com / demo123</p>" +
                    "</div></html>";
                
                JOptionPane.showMessageDialog(loginFrame, testAccounts,
                    "Identifiants de test", JOptionPane.INFORMATION_MESSAGE);
            });
        });
    }
}