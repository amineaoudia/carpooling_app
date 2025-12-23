package com.covoiturage.presentation.frames;

import com.covoiturage.business.models.Utilisateur;
import com.covoiturage.business.services.TrajetService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ConducteurFrame extends JFrame {
    private Utilisateur conducteur;
    private TrajetService trajetService;
    
    // Composants UI
    private JLabel lblWelcome, lblSolde, lblNbTrajets, lblRevenus, lblPassagers, lblNote;
    private JTable tableTrajets;
    private DefaultTableModel tableModel;
    
    // Style
    private final Color PRIMARY_BLUE = new Color(0, 102, 204);
    private final Color SUCCESS_GREEN = new Color(34, 139, 34);
    private final Color DANGER_RED = new Color(220, 20, 60);

    public ConducteurFrame(Utilisateur conducteur) {
        this.conducteur = conducteur;
        this.trajetService = new TrajetService();
        
        setupFrame();
        initComponents();
        chargerMesTrajets();
    }

    private void setupFrame() {
        setTitle("CoCar - Espace Conducteur");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // --- 1. TOOLBAR (Style cohérent) ---
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(PRIMARY_BLUE);
        
        toolBar.add(createToolbarBtn("🚪 Déconnexion", e -> deconnecter()));
        toolBar.add(createToolbarBtn("🔄 Actualiser", e -> chargerMesTrajets()));
        
        JButton btnPub = createToolbarBtn("➕ Publier un trajet", e -> publierTrajet());
        btnPub.setBackground(new Color(255, 255, 255, 40)); // Légère surbrillance
        toolBar.add(btnPub);
        
        add(toolBar, BorderLayout.NORTH);

        // --- 2. PANEL CENTRAL ---
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Header (Bienvenue + Solde + Profil)
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        lblWelcome = new JLabel("Espace Conducteur - " + conducteur.getPrenom());
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblWelcome.setForeground(PRIMARY_BLUE);

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightHeader.setOpaque(false);
        
        lblSolde = new JLabel("Revenus : " + (int)conducteur.getSolde() + " DA");
        lblSolde.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSolde.setForeground(new Color(46, 125, 50));
        
        JButton btnProfil = new JButton("👤 Profil");
        styleSecondaryButton(btnProfil);
        btnProfil.addActionListener(e -> voirProfil());
        
        rightHeader.add(lblSolde);
        rightHeader.add(btnProfil);

        header.add(lblWelcome, BorderLayout.WEST);
        header.add(rightHeader, BorderLayout.EAST);

        // Table des trajets
        String[] columns = {"ID", "Départ", "Arrivée", "Date", "Heure", "Prix", "Places", "Statut", "Action"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 8; }
        };
        tableTrajets = new JTable(tableModel);
        tableTrajets.setRowHeight(40);
        
        // Custom Renderer pour le bouton ANNULER (Lisibilité Max)
        tableTrajets.getColumnModel().getColumn(8).setCellRenderer(new ActionButtonRenderer());
        tableTrajets.getColumnModel().getColumn(8).setCellEditor(new ActionButtonEditor(new JCheckBox()));

        // --- 3. PANEL STATISTIQUES (BAS) ---
        JPanel footerPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        footerPanel.setOpaque(false);
        footerPanel.setPreferredSize(new Dimension(0, 90));

        lblNbTrajets = new JLabel("0", JLabel.CENTER);
        lblPassagers = new JLabel("0", JLabel.CENTER);
        lblRevenus = new JLabel("0 DA", JLabel.CENTER);
        lblNote = new JLabel("4.8/5", JLabel.CENTER);

        footerPanel.add(createStatCard("Trajets Créés", lblNbTrajets, "📤"));
        footerPanel.add(createStatCard("Passagers", lblPassagers, "👥"));
        footerPanel.add(createStatCard("Revenus", lblRevenus, "💰"));
        footerPanel.add(createStatCard("Note Pilote", lblNote, "⭐"));

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(tableTrajets), BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }

    private void chargerMesTrajets() {
        tableModel.setRowCount(0);
        // Exemples avec format de date 2025
        tableModel.addRow(new Object[]{101, "Alger", "Oran", "2025-11-05", "08:00", "1200 DA", "3", "Actif", "Annuler"});
        tableModel.addRow(new Object[]{102, "Alger", "Sétif", "2025-11-10", "17:30", "600 DA", "0", "Complet", "Annuler"});
        refreshStats();
    }

    private void publierTrajet() {
        JDialog dialog = new JDialog(this, "Publier un trajet", true);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 15));
        form.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        form.setBackground(Color.WHITE);

        JTextField tDep = new JTextField();
        JTextField tArr = new JTextField();
        JTextField tDate = new JTextField("2025-11-05");
        JTextField tHeure = new JTextField("08:00");
        JTextField tPrix = new JTextField();
        JSpinner sPlaces = new JSpinner(new SpinnerNumberModel(4, 1, 8, 1));

        form.add(new JLabel("📍 Ville Départ:")); form.add(tDep);
        form.add(new JLabel("🏁 Ville Arrivée:")); form.add(tArr);
        form.add(new JLabel("📅 Date (AAAA-MM-JJ):")); form.add(tDate);
        form.add(new JLabel("⏰ Heure (HH:MM):")); form.add(tHeure);
        form.add(new JLabel("💵 Prix (DA):")); form.add(tPrix);
        form.add(new JLabel("💺 Places:")); form.add(sPlaces);

        // BOUTON PUBLIER CORRIGÉ (Lisibilité Totale)
        JButton btnSubmit = new JButton("PUBLIER L'ANNONCE");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSubmit.setBackground(SUCCESS_GREEN);
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setOpaque(true);
        btnSubmit.setBorderPainted(false);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setPreferredSize(new Dimension(0, 60));

        btnSubmit.addActionListener(e -> {
            if(tDep.getText().isEmpty() || tArr.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Veuillez remplir les destinations.");
                return;
            }
            tableModel.addRow(new Object[]{200+tableModel.getRowCount(), tDep.getText(), tArr.getText(), 
                             tDate.getText(), tHeure.getText(), tPrix.getText()+" DA", sPlaces.getValue(), "Actif", "Annuler"});
            refreshStats();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "✅ Annonce publiée !");
        });

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnSubmit, BorderLayout.SOUTH);
        
        dialog.setSize(480, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void voirProfil() {
        // Design identique à PassagerFrame
        String info = "<html><body style='width: 280px; font-family: Segoe UI; padding: 10px;'>" +
                "<h2 style='color: #0066CC; border-bottom: 2px solid #0066CC;'>Profil Conducteur</h2>" +
                "<table style='margin-top: 10px;'>" +
                "<tr><td><b>Nom :</b></td><td>" + conducteur.getNom().toUpperCase() + "</td></tr>" +
                "<tr><td><b>Prénom :</b></td><td>" + conducteur.getPrenom() + "</td></tr>" +
                "<tr><td><b>Email :</b></td><td>" + conducteur.getEmail() + "</td></tr>" +
                "<tr><td><b>Téléphone :</b></td><td>" + conducteur.getTelephone() + "</td></tr>" +
                "<tr><td><b>Type :</b></td><td><b>CONDUCTEUR</b></td></tr>" +
                "</table><br>" +
                "<div style='background: #f4f4f4; padding: 12px; border-radius: 8px; border-left: 5px solid #2E7D32;'>" +
                "<b>💰 Revenus :</b> <span style='color: #2E7D32; font-weight: bold;'>" + (int)conducteur.getSolde() + " DA</span><br>" +
                "<b>📊 Note :</b> <span style='color: #FFD700; font-weight: bold;'>★★★★</span>★ (4.8)</span>" +
                "</div></body></html>";
        JOptionPane.showMessageDialog(this, info, "Mon Profil", JOptionPane.PLAIN_MESSAGE);
    }

    private void refreshStats() {
        lblNbTrajets.setText(String.valueOf(tableModel.getRowCount()));
        lblSolde.setText("Solde : " + (int)conducteur.getSolde() + " DA");
        lblRevenus.setText((int)conducteur.getSolde() + " DA");
    }

    private void deconnecter() {
        if (JOptionPane.showConfirmDialog(this, "Se déconnecter ?") == JOptionPane.YES_OPTION) dispose();
    }

    // --- CLASSES POUR BOUTON ANNULER ---
    class ActionButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ActionButtonRenderer() {
            setText("Annuler");
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setBackground(DANGER_RED);
            setForeground(Color.WHITE);
            setOpaque(true);
            setBorderPainted(false);
        }
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) { return this; }
    }

    class ActionButtonEditor extends DefaultCellEditor {
        private JButton button; private int row;
        public ActionButtonEditor(JCheckBox cb) {
            super(cb);
            button = new JButton("Annuler");
            button.setBackground(DANGER_RED); button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.addActionListener(e -> {
                fireEditingStopped();
                if(JOptionPane.showConfirmDialog(null, "Annuler ce trajet ?") == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(row);
                    refreshStats();
                }
            });
        }
        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) { this.row = r; return button; }
    }

    // --- HELPERS UI ---
    private JButton createToolbarBtn(String t, java.awt.event.ActionListener a) {
        JButton b = new JButton(t);
        b.setForeground(Color.WHITE); b.setBackground(PRIMARY_BLUE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBorderPainted(false); b.addActionListener(a);
        return b;
    }

    private void styleSecondaryButton(JButton b) {
        b.setBackground(Color.WHITE); b.setForeground(PRIMARY_BLUE);
        b.setBorder(BorderFactory.createLineBorder(PRIMARY_BLUE));
    }

    private JPanel createStatCard(String title, JLabel valueLabel, String icon) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(248, 249, 250));
        p.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        JLabel lblTitle = new JLabel(icon + " " + title, JLabel.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(PRIMARY_BLUE);
        p.add(lblTitle, BorderLayout.NORTH); p.add(valueLabel, BorderLayout.CENTER);
        return p;
    }
}