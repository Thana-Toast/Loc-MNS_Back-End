-- Suppression des données existantes pour repartir à zéro (ordre respectant les contraintes de clés étrangères)
DELETE FROM loan;
DELETE FROM loan_request;
DELETE FROM ticket;
DELETE FROM equipment;
DELETE FROM model;
DELETE FROM role_category_access;
DELETE FROM category;
DELETE FROM user_role;
DELETE FROM app_user;
DELETE FROM role;

-- 1. ROLES
INSERT INTO role (id_role, label) VALUES (1, 'ROLE_ADMIN'), (2,'ROLE_INSTRUCTOR'), (3, 'ROLE_USER');

-- 2. CATEGORIES
INSERT INTO category (id_category, label, description, id_parent_category) VALUES 
(1, 'Ordinateurs', 'Ordinateurs portables, MacBook & Mini PC', NULL),
(2, 'Tablettes', 'Tablettes Android, iOS & Surface Pro', NULL),
(3, 'Matériel informatique', 'Claviers, souris, hub USB, câbles', NULL),
(4, 'Matériel audiovisuel', 'Écrans, vidéoprojecteurs, sonorisation', NULL),
(5, 'Réalité virtuelle', 'Casques VR', NULL),
(6, 'Ordinateurs portables', 'Ordinateurs portables Windows', 1),
(7, 'MacBook', 'Ordinateurs MacBook', 1),
(8, 'Mini PC', 'Mini PC & Mac Mini', 1),
(9,'iPad','Tablettes iOS',2),
(10,'Tablettes Android','Tablettes Android',2),
(11,'Tablettes Surface Pro', 'Tablettes Windows',2),
(12,'Claviers', 'Claviers filaires & sans fil', 3),
(13,'Souris', 'Souris filaires & sans fil', 3),
(14,'Hub USB','Hub USB',3),
(15,'Switch réseau','Switch RJ45',3),
(16,'Écrans','Écrans',4),
(17,'Vidéoprojecteurs','Vidéoprojecteurs',4),
(18,'Sonorisation','Enceintes, casques, écouteurs',4),
(19,'Casques VR','Casques VR',5);

-- 3. UTILISATEURS
INSERT INTO app_user (id_appuser, last_name, first_name, email, password, creation_date, is_anonymized) VALUES
(1,'FRIEDMAN','Marty', 'admin@loc-alhost.com', '$2a$10$fh3NJAc286nAUmIagXYOTubaxkbDPILtyWEXEXPN5giaVNcluKrzO', NOW(), false),
(2,'RHOADS','Randy', 'randy.r@gmail.com', '$2a$10$fh3NJAc286nAUmIagXYOTubaxkbDPILtyWEXEXPN5giaVNcluKrzO', NOW(), false),
(3,'HAMMETT','Kirk', 'kirk.h@gmail.com', '$2a$10$fh3NJAc286nAUmIagXYOTubaxkbDPILtyWEXEXPN5giaVNcluKrzO', NOW(), false),
(4,'MUSTAINE','Dave','dave.m@gmail.com','$2a$10$fh3NJAc286nAUmIagXYOTubaxkbDPILtyWEXEXPN5giaVNcluKrzO',NOW(),false),
(5,'IOMMI','Tony','tony.i@gmail.com','$2a$10$fh3NJAc286nAUmIagXYOTubaxkbDPILtyWEXEXPN5giaVNcluKrzO',NOW(),false);

-- 4. ATTRIBUTION DES ROLES
INSERT INTO user_role (id_appuser, id_role) VALUES (1, 1), (2, 2), (3, 3), (4, 3),(5,3);

-- 5. MODELES D'EQUIPEMENT
INSERT INTO model (id_model, label, brand, description, technical_specs, image_path, id_category) VALUES
(1, 'Dell Pro 16', 'Dell', 'Ordinateur portable professionnel 16 pouces pour le développement et la bureautique', 'Ecran: 16" FHD, CPU: Intel Core i7-13700H, RAM: 16Go DDR5, Stockage: 512Go SSD, OS: Windows 11 Pro', NULL, 6),
(2, 'Dell Latitude 5540', 'Dell', 'Ordinateur portable professionnel léger de 15.6 pouces', 'Ecran: 15.6" FHD, CPU: Intel Core i5-1335U, RAM: 16Go DDR4, Stockage: 256Go SSD, OS: Windows 11 Pro', NULL, 6),
(3, 'Lenovo ThinkPad L15 G4', 'Lenovo', 'Ordinateur portable robuste et fiable pour l''enseignement et le travail administratif', 'Ecran: 15.6" FHD IPS, CPU: AMD Ryzen 5 7530U, RAM: 16Go DDR4, Stockage: 512Go SSD, OS: Windows 11 Pro', NULL, 6),
(4, 'HP 450 G10', 'HP', 'Ordinateur portable professionnel polyvalent et abordable', 'Ecran: 15.6" FHD, CPU: Intel Core i5-1335U, RAM: 8Go DDR4, Stockage: 512Go SSD, OS: Windows 11 Pro', NULL, 6),
(5, 'MacBook Pro M4 Pro 16"', 'Apple', 'Station de travail mobile ultra-performante pour les développeurs et créateurs de contenu', 'Ecran: 16.2" Liquid Retina XDR, CPU: Apple M4 Pro (12 coeurs CPU / 16 coeurs GPU), RAM: 24Go Unified Memory, Stockage: 512Go SSD, OS: macOS Sequoia', NULL, 7),
(6, 'MacBook Air M3 15"', 'Apple', 'MacBook portable fin et léger avec la puissance de la puce M3', 'Ecran: 15.3" Liquid Retina, CPU: Apple M3 (8 coeurs CPU / 10 coeurs GPU), RAM: 16Go Unified Memory, Stockage: 512Go SSD, OS: macOS Sonoma', NULL, 7),
(7, 'MacBook Air M2 13"', 'Apple', 'MacBook ultra-portable avec autonomie d''une journée complète', 'Ecran: 13.6" Liquid Retina, CPU: Apple M2 (8 coeurs CPU / 8 coeurs GPU), RAM: 8Go Unified Memory, Stockage: 256Go SSD, OS: macOS Ventura', NULL, 7),
(8, 'Mac Mini M2', 'Apple', 'Ordinateur de bureau compact et performant', 'CPU: Apple M2 (8 coeurs CPU / 10 coeurs GPU), RAM: 8Go Unified Memory, Stockage: 256Go SSD, OS: macOS Sonoma, Connecteurs: 2x Thunderbolt 4, 1x HDMI', NULL, 8),
(9, 'Intel NUC 12 Pro', 'Intel', 'Mini PC de bureau ultra-compact idéal pour le multi-affichage', 'CPU: Intel Core i5-1240P, RAM: 16Go DDR4, Stockage: 512Go SSD, OS: Windows 11 Pro, Graphisme: Intel Iris Xe', NULL, 8),
(10, 'iPad Pro 11" M4', 'Apple', 'Tablette tactile haut de gamme avec écran OLED de dernière génération', 'Ecran: 11" Ultra Retina XDR OLED, CPU: Apple M4, RAM: 8Go, Stockage: 256Go SSD, Connectivité: Wi-Fi 6E, OS: iPadOS 17', NULL, 9),
(11, 'iPad Air 6 11"', 'Apple', 'Tablette tactile polyvalente et équilibrée avec puce M2', 'Ecran: 11" Liquid Retina, CPU: Apple M2, RAM: 8Go, Stockage: 128Go, Connectivité: Wi-Fi 6E, OS: iPadOS 17', NULL, 9),
(12, 'Galaxy Tab S9 Ultra', 'Samsung', 'Tablette Android grand format idéale pour le dessin et le multitâche', 'Ecran: 14.6" Dynamic AMOLED 2X, CPU: Snapdragon 8 Gen 2, RAM: 12Go, Stockage: 256Go, Connectivité: Wi-Fi 6E, OS: Android 13', NULL, 10),
(13, 'Galaxy Tab S10', 'Samsung', 'Tablette multimédia réactive sous Android', 'Ecran: 12.4" Dynamic AMOLED, CPU: MediaTek Dimensity 9300+, RAM: 12Go, Stockage: 256Go, Connectivité: Wi-Fi 7, OS: Android 14', NULL, 10),
(14, 'Surface Pro 11', 'Microsoft', 'Ordinateur hybride 2-en-1 avec écran tactile haute résolution', 'Ecran: 13" PixelSense Flow OLED, CPU: Snapdragon X Plus, RAM: 16Go LPDDR5x, Stockage: 256Go SSD, OS: Windows 11 Home', NULL, 11),
(15, 'Surface Pro 10', 'Microsoft', 'Tablette Windows hybride pour professionnels', 'Ecran: 13" PixelSense Flow, CPU: Intel Core Ultra 5, RAM: 8Go LPDDR5x, Stockage: 256Go SSD, OS: Windows 11 Pro', NULL, 11),
(16, 'MX Keys S', 'Logitech', 'Clavier sans fil confortable avec touches concaves et rétroéclairage intelligent', 'Connexion: Bluetooth / Logi Bolt, Disposition: AZERTY, Type: Membrane, Rétroéclairage: Intelligent, Batterie: Rechargeable USB-C', NULL, 12),
(17, 'M750L', 'Logitech', 'Souris sans fil ergonomique et silencieuse pour grandes mains', 'Connexion: Bluetooth / Logi Bolt, Boutons: 5, Molette: SmartWheel, Ergonomie: Droitier Taille L', NULL, 13),
(18, 'Connect Station', 'Belkin', 'Station d''accueil multi-ports USB-C pour ordinateurs portables', 'Connecteurs: 2x USB-A 3.0, 1x USB-C PD 100W, 1x HDMI 4K @ 30Hz, 1x Ethernet RJ45 Gigabit, 1x SD Card Reader', NULL, 14),
(19, 'TL-SF1005D', 'TP-LINK', 'Switch réseau de bureau à 5 ports RJ45', 'Ports: 5x RJ45 10/100 Mbps, Technologie: Auto-MDI/MDIX, Normes: IEEE 802.3/3u/3x', NULL, 15),
(20, 'Moniteur Dell SE2422H', 'Dell', 'Écran de bureau 24 pouces antireflet idéal pour le double écran', 'Ecran: 23.8" VA, Résolution: 1920x1080 @ 75Hz, Temps de réponse: 5ms, Connecteurs: 1x HDMI 1.4, 1x VGA', NULL, 16),
(21, 'MSI MAG 244F', 'MSI', 'Écran de bureau fluide avec dalle IPS rapide', 'Ecran: 23.8" Rapid IPS, Résolution: 1920x1080 @ 170Hz, Temps de réponse: 1ms, Connecteurs: 2x HDMI 2.0, 1x DisplayPort 1.2a', NULL, 16),
(22, 'BenQ TH575i', 'BenQ', 'Vidéoprojecteur Full HD haute luminosité pour salles de réunion', 'Résolution: 1080p (1920x1080), Luminosité: 3800 ANSI Lumens, Contraste: 15 000:1, Technologie: DLP, Entrées: 2x HDMI, Android TV', NULL, 17),
(23, 'Logitech Multimedia Speakers Z207', 'Logitech', 'Haut-parleurs stéréo avec son riche et double connexion sans fil/filaire', 'Puissance: 10W Crête / 5W RMS, Connexion: Bluetooth 5.0 et Jack 3.5mm, Portée: 20 mètres', NULL, 18),
(24, 'JBL Quantum 200', 'JBL', 'Casque audio circum-aural avec micro pour visioconférence', 'Haut-parleurs: 50mm, Connexion: Jack 3.5mm, Micro: Directionnel rabattable avec mousse', NULL, 18),
(25, 'Meta Quest 3', 'Meta', 'Casque autonome de réalité mixte de dernière génération', 'Stockage: 128Go, Résolution: 2064x2208 par oeil, CPU: Snapdragon XR2 Gen 2, Suivi: 6 DOF sans capteur externe, Passthrough couleur', NULL, 19),
(26, 'Oculus Rift', 'Microsoft', 'Casque VR filaire de bureau pour démonstrations 3D', 'Résolution: 1080x1200 par oeil, Rafraîchissement: 90Hz, Connectique: HDMI + USB 3.0, Suivi: 3 capteurs Constellation externes', NULL, 19);

-- 6. EQUIPEMENTS
INSERT INTO equipment (id_equipment, serial_number, id_model) VALUES
-- Dell Pro 16 (Modèle 1) - 3 exemplaires
(1, 'SN-DELL-PRO16-001', 1),
(2, 'SN-DELL-PRO16-002', 1),
(3, 'SN-DELL-PRO16-003', 1),
-- Dell Latitude 5540 (Modèle 2) - 3 exemplaires
(4, 'SN-DELL-LAT5540-001', 2),
(5, 'SN-DELL-LAT5540-002', 2),
(6, 'SN-DELL-LAT5540-003', 2),
-- Lenovo ThinkPad L15 G4 (Modèle 3) - 3 exemplaires
(7, 'SN-LENOVO-L15-001', 3),
(8, 'SN-LENOVO-L15-002', 3),
(9, 'SN-LENOVO-L15-003', 3),
-- HP 450 G10 (Modèle 4) - 3 exemplaires
(10, 'SN-HP-450G10-001', 4),
(11, 'SN-HP-450G10-002', 4),
(12, 'SN-HP-450G10-003', 4),
-- MacBook Pro M4 Pro 16" (Modèle 5) - 2 exemplaires
(13, 'SN-MAC-PRO-M4-001', 5),
(14, 'SN-MAC-PRO-M4-002', 5),
-- MacBook Air M3 15" (Modèle 6) - 2 exemplaires
(15, 'SN-MAC-AIR-M3-001', 6),
(16, 'SN-MAC-AIR-M3-002', 6),
-- MacBook Air M2 13" (Modèle 7) - 2 exemplaires
(17, 'SN-MAC-AIR-M2-001', 7),
(18, 'SN-MAC-AIR-M2-002', 7),
-- Mac Mini M2 (Modèle 8) - 2 exemplaires
(19, 'SN-MAC-MINI-M2-001', 8),
(20, 'SN-MAC-MINI-M2-002', 8),
-- Intel NUC 12 Pro (Modèle 9) - 2 exemplaires
(21, 'SN-INTEL-NUC12-001', 9),
(22, 'SN-INTEL-NUC12-002', 9),
-- iPad Pro 11" M4 (Modèle 10) - 2 exemplaires
(23, 'SN-IPAD-PRO-M4-001', 10),
(24, 'SN-IPAD-PRO-M4-002', 10),
-- iPad Air 6 11" (Modèle 11) - 2 exemplaires
(25, 'SN-IPAD-AIR-6-001', 11),
(26, 'SN-IPAD-AIR-6-002', 11),
-- Galaxy Tab S9 Ultra (Modèle 12) - 2 exemplaires
(27, 'SN-SAM-S9ULTRA-001', 12),
(28, 'SN-SAM-S9ULTRA-002', 12),
-- Galaxy Tab S10 (Modèle 13) - 2 exemplaires
(29, 'SN-SAM-S10-001', 13),
(30, 'SN-SAM-S10-002', 13),
-- Surface Pro 11 (Modèle 14) - 2 exemplaires
(31, 'SN-MS-SURF11-001', 14),
(32, 'SN-MS-SURF11-002', 14),
-- Surface Pro 10 (Modèle 15) - 2 exemplaires
(33, 'SN-MS-SURF10-001', 15),
(34, 'SN-MS-SURF10-002', 15),
-- MX Keys S (Modèle 16) - 5 exemplaires
(35, 'SN-LOG-KEYS-001', 16),
(36, 'SN-LOG-KEYS-002', 16),
(37, 'SN-LOG-KEYS-003', 16),
(38, 'SN-LOG-KEYS-004', 16),
(39, 'SN-LOG-KEYS-005', 16),
-- M750L (Modèle 17) - 5 exemplaires
(40, 'SN-LOG-M750L-001', 17),
(41, 'SN-LOG-M750L-002', 17),
(42, 'SN-LOG-M750L-003', 17),
(43, 'SN-LOG-M750L-004', 17),
(44, 'SN-LOG-M750L-005', 17),
-- Connect Station (Modèle 18) - 4 exemplaires
(45, 'SN-BELKIN-CONN-001', 18),
(46, 'SN-BELKIN-CONN-002', 18),
(47, 'SN-BELKIN-CONN-003', 18),
(48, 'SN-BELKIN-CONN-004', 18),
-- TL-SF1005D (Modèle 19) - 3 exemplaires
(49, 'SN-TPLINK-SW-001', 19),
(50, 'SN-TPLINK-SW-002', 19),
(51, 'SN-TPLINK-SW-003', 19),
-- Moniteur Dell SE2422H (Modèle 20) - 4 exemplaires
(52, 'SN-DELL-MON-001', 20),
(53, 'SN-DELL-MON-002', 20),
(54, 'SN-DELL-MON-003', 20),
(55, 'SN-DELL-MON-004', 20),
-- MSI MAG 244F (Modèle 21) - 4 exemplaires
(56, 'SN-MSI-MON-001', 21),
(57, 'SN-MSI-MON-002', 21),
(58, 'SN-MSI-MON-003', 21),
(59, 'SN-MSI-MON-004', 21),
-- BenQ TH575i (Modèle 22) - 2 exemplaires
(60, 'SN-BENQ-PROJ-001', 22),
(61, 'SN-BENQ-PROJ-002', 22),
-- Logitech Multimedia Speakers Z207 (Modèle 23) - 3 exemplaires
(62, 'SN-LOG-SPK-001', 23),
(63, 'SN-LOG-SPK-002', 23),
(64, 'SN-LOG-SPK-003', 23),
-- JBL Quantum 200 (Modèle 24) - 3 exemplaires
(65, 'SN-JBL-HEAD-001', 24),
(66, 'SN-JBL-HEAD-002', 24),
(67, 'SN-JBL-HEAD-003', 24),
-- Meta Quest 3 (Modèle 25) - 1 exemplaire
(68, 'SN-META-QUEST3-001', 25),
-- Oculus Rift (Modèle 26) - 1 exemplaire
(69, 'SN-OCULUS-RIFT-001', 26);

-- 7. DEMANDES DE PRET
INSERT INTO loan_request (id_loan_request, date, description, id_appuser, id_equipment) VALUES
(1, '2026-06-01 09:00:00', 'Demande de prêt d''ordinateur Dell Pro pour développement de code', 3, 1),
(2, '2026-06-02 10:15:00', 'Besoin d''un MacBook Pro performant pour les cours de développement mobile iOS', 2, 13),
(3, '2026-06-03 11:30:00', 'iPad Pro requis pour effectuer des maquettes graphiques et du prototypage UI', 4, 23),
(4, '2026-06-04 14:00:00', 'Casque VR Meta Quest 3 pour une démonstration de réalité mixte lors d''un cours', 2, 68),
(5, '2026-06-05 16:45:00', 'Demande d''un second écran pour travailler en double affichage', 3, 52);

-- 8. PRETS
INSERT INTO loan (id_loan, start_date, end_date, actual_return_date, id_loan_request) VALUES
(1, '2026-06-02', '2026-06-16', NULL, 1),
(2, '2026-06-03', '2026-06-30', NULL, 2),
(3, '2026-06-06', '2026-06-20', NULL, 5);

-- 9. TICKETS
INSERT INTO ticket (id_ticket, date, label, description, id_equipment, id_appuser) VALUES
(1, '2026-06-10 11:00:00', 'Touche de clavier bloquée', 'La touche de lettre E du Dell Pro 16 est bloquée et ne répond plus de façon fluide.', 1, 3),
(2, '2026-06-12 15:30:00', 'Surchauffe anormale', 'Le MacBook Pro chauffe énormément après 10 minutes d''utilisation et fait un bruit de ventilation saccadé.', 13, 2),
(3, '2026-06-15 09:45:00', 'Fissure écran', 'L''iPad Pro présente une légère fissure sur la vitre de l''écran dans la partie inférieure gauche.', 23, 4);

-- 10. ACCÈS AUX CATEGORIES PAR ROLE (RESTRICTIONS CIBLÉES UNIQUEMENT)
-- Par défaut, toutes les catégories sans association sont PUBLIQUES.
-- On restreint ici "Matériel audiovisuel" (id 4, 16, 17, 18) et "Réalité virtuelle" (id 5, 19) aux ADMINS (1) et INSTRUCTORS (2).
INSERT INTO role_category_access (id_role, id_category) VALUES
-- ROLE_ADMIN (id_role = 1) a accès aux catégories privées
(1, 4), (1, 5), (1, 16), (1, 17), (1, 18), (1, 19),
-- ROLE_INSTRUCTOR (id_role = 2) a accès aux catégories privées
(2, 4), (2, 5), (2, 16), (2, 17), (2, 18), (2, 19);

-- Réalignement des séquences d'ID pour PostgreSQL (évite l'erreur de clé dupliquée après les insertions manuelles de seed data)
SELECT setval('app_user_id_appuser_seq', (SELECT COALESCE(MAX(id_appuser), 1) FROM app_user));
SELECT setval('role_id_role_seq', (SELECT COALESCE(MAX(id_role), 1) FROM role));
SELECT setval('category_id_category_seq', (SELECT COALESCE(MAX(id_category), 1) FROM category));
SELECT setval('model_id_model_seq', (SELECT COALESCE(MAX(id_model), 1) FROM model));
SELECT setval('equipment_id_equipment_seq', (SELECT COALESCE(MAX(id_equipment), 1) FROM equipment));
SELECT setval('loan_request_id_loan_request_seq', (SELECT COALESCE(MAX(id_loan_request), 1) FROM loan_request));
SELECT setval('loan_id_loan_seq', (SELECT COALESCE(MAX(id_loan), 1) FROM loan));
SELECT setval('ticket_id_ticket_seq', (SELECT COALESCE(MAX(id_ticket), 1) FROM ticket));
