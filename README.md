[![Java CI](https://github.com/Thana-Toast/Loc-MNS_Back-End/actions/workflows/maven.yml/badge.svg)](https://github.com/Thana-Toast/Loc-MNS_Back-End/actions)
# Loc-MNS – API Back-End
API RESTful développée avec Java 21 et Spring Boot pour la plateforme de gestion et de suivi du parc matériel informatique et audiovisuel **Loc-MNS** (Metz Numeric School). Ce projet a été réalisé dans le cadre de la préparation au Titre Professionnel **Concepteur Développeur d'Applications (CDA)**.

---

## 🛠️ Stack Technique
![Java](https://img.shields.io/badge/Java-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)
* **Langage & Framework** : Java 21, Spring Boot 4.0.5 (Spring MVC, Spring Data JPA, Spring Security)
* **Sécurité & Authentification** : JSON Web Token (JJWT 0.13.0), BCrypt Password Encoder
* **Base de données** : PostgreSQL 18
* **Documentation API** : OpenAPI 3.0 / Swagger UI (`springdoc-openapi-starter-webmvc-ui`)
* **Build & Gestionnaire de dépendances** : Maven (Wrapper `mvnw`)
* **Tests & Qualité** : JUnit 5, Mockito, MockMvc, Maven Surefire & Failsafe
* **DevOps & Conteneurisation** : Docker, Docker Compose, GitHub Actions (CI/CD)

---

## 📋 Fonctionnalités Principales

* **Authentification & Autorisations** : Inscription, connexion sécurisée par JWT, contrôle d'accès basé sur les rôles (`ROLE_ADMIN`, `ROLE_INSTRUCTOR`, `ROLE_USER`).
* **Gestion du Parc Matériel** : CRUD des catégories, modèles d'équipements et unités physiques (numéros de série, états d'usure).
* **Workflow d'Emprunts** :
  * Soumission de demandes de prêt (`PENDING`) avec sélection de dates.
  * Algorithme de détection de conflits et chevauchements intégrant 1 jour de carence de maintenance.
  * Validation (`VALIDATED`) ou refus motivé (`REJECTED`) par un administrateur.
  * Gestion automatique des retards et réattribution dynamique des matériels disponibles.
* **Gestion des Incidents & Tickets** : Signalement de pannes, demandes de retour anticipé et demandes de prolongation de prêt.
* **Système d'Exportation Paramétrable** : Exportation de la liste des utilisateurs aux formats **CSV** (avec BOM UTF-8 pour Excel) et **XML** pour l'intégration SI.
* **Gestion Documentaire & Fichiers** : Téléversement et stockage sécurisé des notices PDF et images d'équipements.

---

## 📂 Architecture du Projet

L'application suit une architecture n-tiers propre (Layered Architecture) :

```text
src/main/java/fr/ctanchot/locmns/
├── config/         # Configuration Spring Security, CORS, Swagger et exceptions globales
├── controller/     # Contrôleurs REST (Endpoints API)
├── dao/            # Repositories Spring Data JPA
├── dto/            # Data Transfer Objects & Mappers
├── model/          # Entités JPA (AppUser, Loan, Equipment, Ticket, etc.)
├── security/       # Filtres JWT, Annotations de sécurité (@IsAdmin) et UserDetails
└── service/        # Couche métier (Gestion des prêts, conflits, exports CSV/XML, etc.)
```

---

## 🚀 Prérequis & Installation

### Prérequis
* **JDK 21** ou supérieur
* **Maven 3.9+** (ou utiliser le wrapper `./mvnw`)
* **Docker & Docker Compose** (pour le lancement par conteneurs)
* **PostgreSQL 18** (si exécution hors Docker)

### Configuration des variables d'environnement
Créez un fichier `.env` à la racine du projet en vous basant sur la structure suivante :

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=db

DB_USER=admin
DB_PASSWORD=votre_mot_de_passe

DDL_AUTO=update
SQL_FILE=init
SQL_INIT_MODE=never

JWT_SECRET=votre_cle_secrete_jwt_suffisamment_longue

DOCKER_USERNAME=votre_identifiant_dockerhub
```

### Lancement en local (Mode Développement)

1. **Cloner le dépôt** :
   ```bash
   git clone https://github.com/Thana-Toast/Loc-MNS_Back-End.git
   cd Loc-MNS_Back-End
   ```

2. **Lancer la base de données PostgreSQL via Docker** :
   ```bash
   docker compose up -d postgres
   ```

3. **Compiler et démarrer le serveur Spring Boot** :
   ```bash
   ./mvnw spring-boot:run
   ```

L'API est accessible sur `http://localhost:8080/api` (ou selon le port configuré).

---

## 🐳 Lancement complet via Docker Compose

Pour démarrer l'ensemble des services (PostgreSQL, Back-End, Front-End et Nginx Proxy Manager) :

```bash
docker compose up -d
```

Pour reconstruire l'image locale après une modification :
```bash
docker compose up -d --build
```

---

## 📖 Documentation API (Swagger UI)

Une fois l'application lancée, la documentation interactive OpenAPI / Swagger UI est disponible aux adresses suivantes :

* **Interface UI** : `http://localhost:8080/api/swagger-ui.html`
* **Spécification JSON** : `http://localhost:8080/api/v3/api-docs`

---

## 🧪 Tests & Qualité

Le projet dispose d'une suite de tests automatisés couvrant les services métier, la sécurité JWT, les contrôleurs API et l'intégration Spring Boot :

* **Exécuter les tests unitaires** :
  ```bash
  ./mvnw test
  ```

* **Exécuter les tests d'intégration (Failsafe)** :
  ```bash
  ./mvnw failsafe:integration-test
  ```

---

## 🔄 CI/CD & Déploiement

Le dépôt intègre un workflow **GitHub Actions** (`.github/workflows/maven.yml`) :
* **Sur les branches `main` et `dev`** : Exécution automatique des tests automatisés Maven à chaque `push`.
* **Sur la branche `main`** : Build de l'image Docker, publication sur Docker Hub, transfert du `compose.yml` via SCP et déploiement automatique sur le serveur VPS.

---

## 👤 Auteur

* **Clément TANCHOT** – Apprenant Concepteur Développeur d'Applications (MNS)
