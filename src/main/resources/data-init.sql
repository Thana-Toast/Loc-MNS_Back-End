-- Suppression des données existantes pour repartir à zéro (ordre respectant les contraintes de clés étrangères)
DELETE FROM loan;
DELETE FROM loan_request;
DELETE FROM ticket;
DELETE FROM equipment;
DELETE FROM model;
DELETE FROM category;
DELETE FROM user_role;
DELETE FROM app_user;
DELETE FROM role;

-- 1. ROLES
INSERT INTO role (id_role, label) VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER');

-- 2. CATEGORIES
INSERT INTO category (id_category, label, description) VALUES 
(1, 'Informatique', 'Matériel informatique : ordinateurs, écrans, périphériques'),
(2, 'Mobilier', 'Bureaux, chaises, armoires');

-- 3. UTILISATEURS
INSERT INTO app_user (id_appuser, last_name, first_name, email, password, creation_date, is_anonymized) VALUES
(1, 'DOE', 'John', 'admin@locmns.fr', '$2a$10$f9pXjjFLacTk1k7RdVtXEerArq1GyiMEaN31S3oxyPdIrNfyWmkse', NOW(), false),
(2, 'HAMMETT', 'Kirk', 'kirk.h@gmail.com', '$2a$10$Jzic1KLskFPH.LwJje3ixuHBkrWMllTXh4anzFvKvpQhIslzl9C2G', NOW(), false),
(3, 'ULRICH', 'Lars', 'lars.u@gmail.com', '$2a$10$Jzic1KLskFPH.LwJje3ixuHBkrWMllTXh4anzFvKvpQhIslzl9C2G', NOW(), false);

-- 4. ATTRIBUTION DES ROLES
INSERT INTO user_role (id_appuser, id_role) VALUES (1, 1), (2, 2), (3, 1), (3, 2);

-- 5. MODELES D'EQUIPEMENT
INSERT INTO model (id_model, label, brand, description, technical_specs, id_category) VALUES
(1, 'MacBook Pro 16', 'Apple', 'Ordinateur portable puissant pour le développement', 'M2 Max, 32GB RAM, 1TB SSD', 1),
(2, 'Ecran 27"', 'Dell', 'Ecran 4K pour le confort visuel', 'Résolution 3840x2160, IPS', 1),
(3, 'Bureau Assis-Debout', 'Ikea', 'Bureau ergonomique réglable en hauteur', 'Dimensions 160x80cm, Motorisé', 2);

-- 6. EQUIPEMENTS
INSERT INTO equipment (id_equipment, serial_number, id_model) VALUES
(1, 'SN-MBP-001', 1),
(2, 'SN-MBP-002', 1),
(3, 'SN-DELL-001', 2),
(4, 'SN-IKEA-001', 3);

-- 7. DEMANDES DE PRET
INSERT INTO loan_request (id_loan_request, date, description, id_appuser, id_equipment) VALUES
(1, NOW(), 'Besoin d''un ordinateur portable pour une mission client', 2, 1),
(2, NOW(), 'Demande d''écran supplémentaire', 3, 3);

-- 8. PRETS
INSERT INTO loan (id_loan, start_date, end_date, id_loan_request) VALUES
(1, '2026-05-01', '2026-05-30', 1);

-- 9. TICKETS
INSERT INTO ticket (id_ticket, date, label, description, id_equipment, id_appuser) VALUES
(1, NOW(), 'Ecran fissuré', 'L''écran est arrivé avec une fissure dans le coin supérieur gauche', 3, 3);

-- Réalignement des séquences d'ID pour PostgreSQL (évite l'erreur de clé dupliquée après les insertions manuelles de seed data)
SELECT setval('app_user_id_appuser_seq', (SELECT COALESCE(MAX(id_appuser), 1) FROM app_user));
SELECT setval('role_id_role_seq', (SELECT COALESCE(MAX(id_role), 1) FROM role));
SELECT setval('category_id_category_seq', (SELECT COALESCE(MAX(id_category), 1) FROM category));
SELECT setval('model_id_model_seq', (SELECT COALESCE(MAX(id_model), 1) FROM model));
SELECT setval('equipment_id_equipment_seq', (SELECT COALESCE(MAX(id_equipment), 1) FROM equipment));
SELECT setval('loan_request_id_loan_request_seq', (SELECT COALESCE(MAX(id_loan_request), 1) FROM loan_request));
SELECT setval('loan_id_loan_seq', (SELECT COALESCE(MAX(id_loan), 1) FROM loan));
SELECT setval('ticket_id_ticket_seq', (SELECT COALESCE(MAX(id_ticket), 1) FROM ticket));
