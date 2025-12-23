-- Supprimer la base si elle existe
DROP DATABASE IF EXISTS covoiturage_db;

-- Créer la base de données
CREATE DATABASE covoiturage_db;
USE covoiturage_db;

-- Table UTILISATEUR
CREATE TABLE Utilisateur (
    idUtilisateur INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    motDePasse VARCHAR(255) NOT NULL,
    telephone VARCHAR(20),
    dateInscription DATETIME DEFAULT CURRENT_TIMESTAMP,
    solde DECIMAL(10,2) DEFAULT 0.00,
    typeUtilisateur ENUM('CONDUCTEUR', 'PASSAGER') NOT NULL,
    INDEX idx_email (email),
    INDEX idx_type (typeUtilisateur)
);

-- Table CONDUCTEUR
CREATE TABLE Conducteur (
    idConducteur INT PRIMARY KEY AUTO_INCREMENT,
    idUtilisateur INT UNIQUE NOT NULL,
    numeroPermis VARCHAR(50) UNIQUE NOT NULL,
    modeleVoiture VARCHAR(50),
    couleurVoiture VARCHAR(30),
    plaqueImmatriculation VARCHAR(20) UNIQUE,
    noteMoyenne DECIMAL(3,2) DEFAULT 0.00,
    FOREIGN KEY (idUtilisateur) REFERENCES Utilisateur(idUtilisateur) ON DELETE CASCADE,
    INDEX idx_utilisateur (idUtilisateur)
);

-- Table PASSAGER
CREATE TABLE Passager (
    idPassager INT PRIMARY KEY AUTO_INCREMENT,
    idUtilisateur INT UNIQUE NOT NULL,
    preferences TEXT,
    noteMoyenne DECIMAL(3,2) DEFAULT 0.00,
    FOREIGN KEY (idUtilisateur) REFERENCES Utilisateur(idUtilisateur) ON DELETE CASCADE,
    INDEX idx_utilisateur (idUtilisateur)
);

-- Table TRAJET
CREATE TABLE Trajet (
    idTrajet INT PRIMARY KEY AUTO_INCREMENT,
    idConducteur INT NOT NULL,
    villeDepart VARCHAR(100) NOT NULL,
    villeArrivee VARCHAR(100) NOT NULL,
    dateDepart DATE NOT NULL,
    heureDepart TIME NOT NULL,
    prix DECIMAL(6,2) NOT NULL,
    placesDisponibles INT NOT NULL,
    statut ENUM('PLANIFIE', 'EN_COURS', 'TERMINE', 'ANNULE') DEFAULT 'PLANIFIE',
    datePublication DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idConducteur) REFERENCES Conducteur(idConducteur) ON DELETE CASCADE,
    INDEX idx_depart (villeDepart),
    INDEX idx_arrivee (villeArrivee),
    INDEX idx_date (dateDepart),
    INDEX idx_conducteur (idConducteur),
    INDEX idx_statut (statut)
);

-- Table RESERVATION
CREATE TABLE Reservation (
    idReservation INT PRIMARY KEY AUTO_INCREMENT,
    idTrajet INT NOT NULL,
    idPassager INT NOT NULL,
    dateReservation DATETIME DEFAULT CURRENT_TIMESTAMP,
    nombrePlaces INT NOT NULL,
    statut ENUM('CONFIRMEE', 'EN_ATTENTE', 'ANNULEE') DEFAULT 'EN_ATTENTE',
    FOREIGN KEY (idTrajet) REFERENCES Trajet(idTrajet) ON DELETE CASCADE,
    FOREIGN KEY (idPassager) REFERENCES Passager(idPassager) ON DELETE CASCADE,
    INDEX idx_trajet (idTrajet),
    INDEX idx_passager (idPassager),
    INDEX idx_statut (statut),
    UNIQUE KEY unique_trajet_passager (idTrajet, idPassager)
);

-- Table PAIEMENT
CREATE TABLE Paiement (
    idPaiement INT PRIMARY KEY AUTO_INCREMENT,
    idReservation INT UNIQUE NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    datePaiement DATETIME DEFAULT CURRENT_TIMESTAMP,
    methodePaiement ENUM('SOLDE', 'CARTE') DEFAULT 'SOLDE',
    statut ENUM('PAYE', 'EN_ATTENTE', 'REFUSE') DEFAULT 'EN_ATTENTE',
    FOREIGN KEY (idReservation) REFERENCES Reservation(idReservation) ON DELETE CASCADE,
    INDEX idx_reservation (idReservation),
    INDEX idx_statut (statut)
);

-- Table CARTEBANCAIRE
CREATE TABLE CarteBancaire (
    idCarte INT PRIMARY KEY AUTO_INCREMENT,
    idPassager INT NOT NULL,
    numeroCarte VARCHAR(16) NOT NULL,
    titulaire VARCHAR(100) NOT NULL,
    dateExpiration DATE NOT NULL,
    cryptogramme VARCHAR(3) NOT NULL,
    FOREIGN KEY (idPassager) REFERENCES Passager(idPassager) ON DELETE CASCADE,
    INDEX idx_passager (idPassager),
    INDEX idx_numero (numeroCarte)
);

-- Table AVIS
CREATE TABLE Avis (
    idAvis INT PRIMARY KEY AUTO_INCREMENT,
    idReservation INT UNIQUE NOT NULL,
    idDonneur INT NOT NULL,
    idReceveur INT NOT NULL,
    note INT CHECK (note >= 1 AND note <= 5),
    commentaire TEXT,
    dateAvis DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idReservation) REFERENCES Reservation(idReservation) ON DELETE CASCADE,
    FOREIGN KEY (idDonneur) REFERENCES Passager(idPassager) ON DELETE CASCADE,
    FOREIGN KEY (idReceveur) REFERENCES Conducteur(idConducteur) ON DELETE CASCADE,
    INDEX idx_receveur (idReceveur),
    INDEX idx_reservation (idReservation)
);

-- Insertion des données de démonstration
INSERT INTO Utilisateur (nom, prenom, email, motDePasse, telephone, solde, typeUtilisateur) VALUES
('Dupont', 'Jean', 'jean.dupont@email.com', SHA2('password123', 256), '0612345678', 150.00, 'CONDUCTEUR'),
('Martin', 'Sophie', 'sophie.martin@email.com', SHA2('password123', 256), '0623456789', 75.50, 'PASSAGER'),
('Bernard', 'Pierre', 'pierre.bernard@email.com', SHA2('password123', 256), '0634567890', 200.00, 'CONDUCTEUR'),
('Dubois', 'Marie', 'marie.dubois@email.com', SHA2('password123', 256), '0645678901', 50.00, 'PASSAGER');

INSERT INTO Conducteur (idUtilisateur, numeroPermis, modeleVoiture, couleurVoiture, plaqueImmatriculation) VALUES
(1, '123456789012', 'Renault Clio', 'Bleue', 'AB-123-CD'),
(3, '987654321098', 'Peugeot 208', 'Rouge', 'EF-456-GH');

INSERT INTO Passager (idUtilisateur, preferences) VALUES
(2, 'Non fumeur, bagage léger, musique douce'),
(4, 'Peu importe, horaires flexibles');

INSERT INTO Trajet (idConducteur, villeDepart, villeArrivee, dateDepart, heureDepart, prix, placesDisponibles) VALUES
(1, 'Paris', 'Lyon', '2024-12-15', '08:00:00', 25.00, 3),
(1, 'Paris', 'Marseille', '2024-12-16', '14:30:00', 40.00, 4),
(2, 'Lyon', 'Nice', '2024-12-17', '09:15:00', 35.00, 2),
(2, 'Lyon', 'Bordeaux', '2024-12-18', '16:45:00', 30.00, 3);

INSERT INTO Reservation (idTrajet, idPassager, nombrePlaces, statut) VALUES
(1, 1, 2, 'CONFIRMEE'),
(3, 2, 1, 'EN_ATTENTE');

INSERT INTO Paiement (idReservation, montant, methodePaiement, statut) VALUES
(1, 50.00, 'SOLDE', 'PAYE');

INSERT INTO Avis (idReservation, idDonneur, idReceveur, note, commentaire) VALUES
(1, 1, 1, 5, 'Très bon conducteur, ponctuel et sympathique');

-- Procédures stockées utiles
DELIMITER $$

CREATE PROCEDURE sp_GetTrajetsDisponibles(IN p_depart VARCHAR(100), IN p_arrivee VARCHAR(100), IN p_date DATE)
BEGIN
    SELECT t.*, c.noteMoyenne, u.nom, u.prenom
    FROM Trajet t
    JOIN Conducteur c ON t.idConducteur = c.idConducteur
    JOIN Utilisateur u ON c.idUtilisateur = u.idUtilisateur
    WHERE t.villeDepart LIKE CONCAT('%', p_depart, '%')
      AND t.villeArrivee LIKE CONCAT('%', p_arrivee, '%')
      AND t.dateDepart = p_date
      AND t.statut = 'PLANIFIE'
      AND t.placesDisponibles > 0
    ORDER BY t.prix ASC, t.heureDepart ASC;
END$$

CREATE PROCEDURE sp_GetReservationsUtilisateur(IN p_idUtilisateur INT)
BEGIN
    -- Récupère toutes les réservations d'un utilisateur (conducteur ou passager)
    SELECT r.*, t.villeDepart, t.villeArrivee, t.dateDepart, t.heureDepart, t.prix
    FROM Reservation r
    JOIN Trajet t ON r.idTrajet = t.idTrajet
    WHERE r.idPassager IN (SELECT idPassager FROM Passager WHERE idUtilisateur = p_idUtilisateur)
       OR t.idConducteur IN (SELECT idConducteur FROM Conducteur WHERE idUtilisateur = p_idUtilisateur)
    ORDER BY r.dateReservation DESC;
END$$

DELIMITER ;

-- Triggers
DELIMITER $$

CREATE TRIGGER tr_AfterInsertAvis
AFTER INSERT ON Avis
FOR EACH ROW
BEGIN
    -- Mettre à jour la note moyenne du conducteur
    UPDATE Conducteur 
    SET noteMoyenne = (
        SELECT AVG(note) 
        FROM Avis 
        WHERE idReceveur = NEW.idReceveur
    )
    WHERE idConducteur = NEW.idReceveur;
END$$

CREATE TRIGGER tr_AfterInsertReservation
AFTER INSERT ON Reservation
FOR EACH ROW
BEGIN
    -- Créer automatiquement un paiement en attente
    INSERT INTO Paiement (idReservation, montant, statut)
    SELECT NEW.idReservation, (t.prix * NEW.nombrePlaces), 'EN_ATTENTE'
    FROM Trajet t
    WHERE t.idTrajet = NEW.idTrajet;
END$$

DELIMITER ;