package com.covoiturage.presentation.frames;

import com.covoiturage.business.models.Utilisateur;
import com.covoiturage.business.services.TrajetService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PassagerFrame extends JFrame {
    private Utilisateur passager;
    private TrajetService trajetService;
    
    // Composants UI
    private JLabel lblWelcome, lblSolde, lblNbReservations, lblDepenses;
    private JTable tableTrajets;
    private DefaultTableModel tableModel;
    
    // Gestion des données
    private int totalDepense = 0; 
    private List<String> historiqueReservations = new ArrayList<>(); // Pour stocker les destinations
    private final Color PRIMARY_BLUE = new Color(0, 102, 204);
    
    public PassagerFrame(Utilisateur passager) {
        this.passager = passager;
        this.trajetService = new TrajetService();
        
        setupFrame();
        initComponents();
        chargerTrajetsDisponibles();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // --- 1. TOOLBAR ---
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(PRIMARY_BLUE);
        toolBar.add(createToolbarBtn("🚪 Déconnexion", e -> deconnecter()));
        toolBar.add(createToolbarBtn("🔄 Actualiser", e -> chargerTrajetsDisponibles()));
        toolBar.add(createToolbarBtn("💰 Recharger", e -> rechargerSolde()));
        toolBar.add(createToolbarBtn("🎒 Mes Réservations", e -> voirReservations())); 
        add(toolBar, BorderLayout.NORTH);
        
        // --- 2. PANEL CENTRAL ---
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Header (Bienvenue + Solde Principal + Profil)
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        lblWelcome = new JLabel("Espace Passager - " + passager.getPrenom());
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblWelcome.setForeground(PRIMARY_BLUE);

        // Zone Droite du Header (Solde + Bouton Profil)
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightHeader.setOpaque(false);
        
        lblSolde = new JLabel("Solde : " + (int)passager.getSolde() + " DA");
        lblSolde.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSolde.setForeground(new Color(46, 125, 50));
        
        JButton btnProfil = new JButton("👤 Profil");
        styleSecondaryButton(btnProfil);
        btnProfil.addActionListener(e -> voirProfil());
        
        rightHeader.add(lblSolde);
        rightHeader.add(btnProfil);

        header.add(lblWelcome, BorderLayout.WEST);
        header.add(rightHeader, BorderLayout.EAST);

        // Barre de Recherche
        JTextField txtSearch = new JTextField();
        txtSearch.setBorder(BorderFactory.createTitledBorder("Filtrer par destination (Alger, Oran...)"));
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                String q = txtSearch.getText().toLowerCase();
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
                tableTrajets.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + q));
            }
        });

        // Table des trajets
        String[] columns = {"ID", "Départ", "Arrivée", "Date", "Heure", "Prix", "Places", "Conducteur", "Action"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 8; }
        };
        tableTrajets = new JTable(tableModel);
        tableTrajets.setRowHeight(35);
        
        // Rareté des places (Couleurs)
        tableTrajets.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, s, f, r, c);
                try {
                    int p = Integer.parseInt(v.toString().split(" ")[0]);
                    if (p == 1) l.setForeground(Color.RED);
                    else if (p <= 2) l.setForeground(Color.ORANGE);
                    else l.setForeground(new Color(0, 128, 0));
                    l.setFont(new Font("Segoe UI", Font.BOLD, 12));
                } catch (Exception e) {}
                return l;
            }
        });

        tableTrajets.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
        tableTrajets.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JPanel centerContent = new JPanel(new BorderLayout(0, 10));
        centerContent.setOpaque(false);
        centerContent.add(txtSearch, BorderLayout.NORTH);
        centerContent.add(new JScrollPane(tableTrajets), BorderLayout.CENTER);

        // --- 3. PANEL STATISTIQUES (BAS) ---
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new Dimension(0, 80));

        lblNbReservations = new JLabel("0", JLabel.CENTER);
        lblDepenses = new JLabel("0 DA", JLabel.CENTER);

        statsPanel.add(createStatCard("Total Réservations", lblNbReservations, "📅"));
        statsPanel.add(createStatCard("Dépenses Totales", lblDepenses, "💸"));

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(centerContent, BorderLayout.CENTER);
        mainPanel.add(statsPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }

    private void chargerTrajetsDisponibles() {
        tableModel.setRowCount(0);
        // Ajout de nombreuses destinations à travers l'Algérie
        tableModel.addRow(new Object[]{301, "Alger", "Oran", "22/12", "08:00", "1200 DA", "3 places", "Mehdi ⭐⭐⭐⭐", "Réserver"});
        tableModel.addRow(new Object[]{302, "Constantine", "Setif", "23/12", "10:30", "450 DA", "1 places", "Imane ⭐⭐⭐⭐⭐", "Réserver"});
        tableModel.addRow(new Object[]{303, "Annaba", "Alger", "24/12", "06:00", "1800 DA", "4 places", "Reda ⭐⭐⭐", "Réserver"});
        tableModel.addRow(new Object[]{304, "Tlemcen", "Oran", "25/12", "09:00", "400 DA", "3 places", "Yacine ⭐⭐⭐⭐", "Réserver"});
        tableModel.addRow(new Object[]{305, "Alger", "Bejaia", "22/12", "14:00", "700 DA", "2 places", "Amine ⭐⭐⭐", "Réserver"});
        tableModel.addRow(new Object[]{306, "Batna", "Biskra", "23/12", "07:00", "300 DA", "4 places", "Sami ⭐⭐⭐⭐⭐", "Réserver"});
        tableModel.addRow(new Object[]{307, "Ghardaia", "Alger", "26/12", "20:00", "2500 DA", "2 places", "Omar ⭐⭐⭐⭐", "Réserver"});
        tableModel.addRow(new Object[]{308, "Sidi Bel Abbes", "Oran", "22/12", "08:30", "350 DA", "3 places", "Fouad ⭐⭐⭐", "Réserver"});
        tableModel.addRow(new Object[]{309, "Blida", "Alger", "24/12", "07:30", "150 DA", "4 places", "Karim ⭐⭐⭐⭐", "Réserver"});
        tableModel.addRow(new Object[]{310, "Skikda", "Annaba", "25/12", "11:00", "250 DA", "1 places", "Hichem ⭐⭐⭐⭐⭐", "Réserver"});
        tableModel.addRow(new Object[]{311, "Ouargla", "Hassi Messaoud", "26/12", "06:00", "500 DA", "2 places", "Zino ⭐⭐⭐", "Réserver"});
        tableModel.addRow(new Object[]{312, "Tizi Ouzou", "Alger", "23/12", "06:30", "300 DA", "3 places", "Mourad ⭐⭐⭐⭐", "Réserver"});
    }

    private void reserverTrajet(int id, int row) {
        String destination = tableModel.getValueAt(row, 2).toString();
        String depart = tableModel.getValueAt(row, 1).toString();
        int prixUnitaire = Integer.parseInt(tableModel.getValueAt(row, 5).toString().replace(" DA", ""));
        int placesDispo = Integer.parseInt(tableModel.getValueAt(row, 6).toString().split(" ")[0]);

        Integer[] choix = new Integer[placesDispo];
        for(int i=0; i<placesDispo; i++) choix[i] = i+1;
        Integer nbr = (Integer) JOptionPane.showInputDialog(this, "Réserver vers " + destination + "\nNombre de places :", "Réservation", JOptionPane.QUESTION_MESSAGE, null, choix, 1);

        if (nbr != null) {
            int total = prixUnitaire * nbr;
            if (passager.getSolde() >= total) {
                if (JOptionPane.showConfirmDialog(this, "Confirmer le paiement de " + total + " DA ?") == JOptionPane.YES_OPTION) {
                    passager.setSolde(passager.getSolde() - total);
                    totalDepense += total;
                    
                    // Ajout à l'historique avec destination
                    historiqueReservations.add("📍 De " + depart + " vers " + destination + " (" + nbr + " places)");
                    
                    // MAJ UI
                    refreshStats();
                    tableModel.setValueAt((placesDispo - nbr) + " places", row, 6);
                    JOptionPane.showMessageDialog(this, "✅ Réservation confirmée !");
                }
            } else JOptionPane.showMessageDialog(this, "Solde insuffisant !");
        }
    }

    private void voirReservations() {
        if (historiqueReservations.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vous n'avez pas encore de réservations.", "Mes Voyages", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder sb = new StringBuilder("<html><body style='width: 300px;'>");
        sb.append("<h3 style='color: #0066CC;'>Mes Réservations</h3><hr>");
        for (String res : historiqueReservations) {
            sb.append(res).append("<br><br>");
        }
        sb.append("</body></html>");
        
        JOptionPane.showMessageDialog(this, sb.toString(), "🎒 Historique de voyages", JOptionPane.PLAIN_MESSAGE);
    }

    private void refreshStats() {
        lblSolde.setText("Solde : " + (int)passager.getSolde() + " DA");
        lblDepenses.setText(totalDepense + " DA");
        lblNbReservations.setText(String.valueOf(historiqueReservations.size()));
    }

    private void voirProfil() {
        String info = "<html><body style='width: 280px; font-family: Segoe UI; padding: 10px;'>" +
                "<h2 style='color: #0066CC; border-bottom: 2px solid #0066CC;'>Profil Utilisateur</h2>" +
                "<table style='margin-top: 10px;'>" +
                "<tr><td><b>Nom :</b></td><td>" + passager.getNom().toUpperCase() + "</td></tr>" +
                "<tr><td><b>Prénom :</b></td><td>" + passager.getPrenom() + "</td></tr>" +
                "<tr><td><b>Email :</b></td><td>" + passager.getEmail() + "</td></tr>" +
                "<tr><td><b>Téléphone :</b></td><td>" + passager.getTelephone() + "</td></tr>" +
                "<tr><td><b>Type :</b></td><td><b>PASSAGER</b></td></tr>" +
                "</table><br>" +
                "<div style='background: #f4f4f4; padding: 12px; border-radius: 8px; border-left: 5px solid #2E7D32;'>" +
                "<b>💰 Solde :</b> <span style='color: #2E7D32; font-weight: bold;'>" + (int)passager.getSolde() + " DA</span><br>" +
                "<b>💸 Dépenses :</b> <span style='color: #C62828; font-weight: bold;'>" + totalDepense + " DA</span>" +
                "</div></body></html>";
        JOptionPane.showMessageDialog(this, info, "Détails Profil", JOptionPane.PLAIN_MESSAGE);
    }

    private void rechargerSolde() {
        String m = JOptionPane.showInputDialog(this, "Montant à recharger (DA) :");
        try {
            if (m != null) {
                passager.setSolde(passager.getSolde() + Integer.parseInt(m));
                refreshStats();
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Montant invalide."); }
    }

    private void deconnecter() {
        if (JOptionPane.showConfirmDialog(this, "Déconnecter ?", "CoCar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }

    // --- HELPERS UI ---
    private JButton createToolbarBtn(String t, java.awt.event.ActionListener a) {
        JButton b = new JButton(t);
        b.setForeground(Color.WHITE); b.setBackground(PRIMARY_BLUE);
        b.setBorderPainted(false); b.setFocusPainted(false);
        b.addActionListener(a);
        return b;
    }

    private void styleSecondaryButton(JButton b) {
        b.setBackground(Color.WHITE); b.setForeground(PRIMARY_BLUE);
        b.setBorder(BorderFactory.createLineBorder(PRIMARY_BLUE));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel createStatCard(String title, JLabel valueLabel, String icon) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(248, 249, 250));
        p.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        JLabel lblTitle = new JLabel(icon + " " + title, JLabel.CENTER);
        lblTitle.setForeground(Color.GRAY);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(PRIMARY_BLUE);
        p.add(lblTitle, BorderLayout.NORTH); p.add(valueLabel, BorderLayout.CENTER);
        return p;
    }

    private void setupFrame() {
        setTitle("CoCar - Passager");
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // CLASSES TABLEAU
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() { setText("Réserver"); setBackground(new Color(0, 153, 76)); setForeground(Color.WHITE); setOpaque(true); setBorderPainted(false); }
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) { return this; }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button; private int row;
        public ButtonEditor(JCheckBox cb) {
            super(cb);
            button = new JButton("Réserver");
            button.setBackground(new Color(0, 153, 76)); button.setForeground(Color.WHITE);
            button.addActionListener(e -> { reserverTrajet((int)tableModel.getValueAt(row,0), row); fireEditingStopped(); });
        }
        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) { this.row = r; return button; }
    }
}