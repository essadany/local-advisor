# LocalAdvisor

---

LocalAdvisor est une plateforme collaborative où les utilisateurs partagent, découvrent et évaluent les meilleurs endroits de leur ville (restaurants, cafés, salles de sport, coiffeurs…).

## Technologie principale

- 🖥 **Backend:** Spring Boot 3 (Java 21, Maven), Sécurité JWT, JPA/Hibernate (PostgreSQL)
- 🎨 **Frontend:** Vue 3, Vite, TypeScript, Pinia
- 🐘 **Base de données:** PostgreSQL

## Architecture & Conteneurisation

- Dockerfiles et `.dockerignore` sont rassemblés dans le dossier `/docker`.
- Orchestration via Docker Compose pour le développement local.
- Les variables d'environnement sont regroupées dans `stack_my_settings`.

---

## Démarrage rapide

1. **Ouvrir `stack_my_settings`** et renseigner les valeurs réelles pour `DB_PASSWORD` et `JWT_SECRET`.
2. **Lancer le projet en local :**
   ```bash
   docker-compose up --env-file stack_my_settings --build
   ```
3. **Interfaces :**
   - Frontend (SPA) : http://localhost:4173
   - Backend (API REST) : http://localhost:8080

---

## Fonctionnalités principales

- Inscription/connexion avec JWT
- Ajout et recommandation de lieux
- Recherche multicritères, carte interactive, favoris
- Système de notation/commentaires
- Notifications sur les nouveaux avis

---
