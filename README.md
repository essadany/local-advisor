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

## API REST

Toutes les routes sont versionnées sous `/api/v1`. Les lectures publiques (`places`, `categories`, `reviews`, `images`)
sont accessibles sans jeton ; les écritures et les routes `users` exigent `Authorization: Bearer <token>`.

| Méthode | Route | Description |
|---------|-------|-------------|
| POST | `/api/v1/auth/register` | Inscription (renvoie un JWT) |
| POST | `/api/v1/auth/login` | Connexion (renvoie un JWT) |
| GET / POST | `/api/v1/categories` | Lister / créer |
| GET / PUT / DELETE | `/api/v1/categories/{id}` | Lire / modifier / supprimer |
| GET | `/api/v1/categories/{id}/places` | Lieux d'une catégorie |
| GET / POST | `/api/v1/places` | Lister / créer |
| GET / PUT / DELETE | `/api/v1/places/{id}` | Lire / modifier / supprimer |
| GET / POST | `/api/v1/reviews` | Lister (`?placeId=` ou `?userId=`) / créer |
| GET / PUT / DELETE | `/api/v1/reviews/{id}` | Lire / modifier / supprimer |
| GET / POST | `/api/v1/images` | Lister (`?placeId=` ou `?userId=`) / créer |
| GET / DELETE | `/api/v1/images/{id}` | Lire / supprimer |
| GET | `/api/v1/users` | Lister les utilisateurs |
| GET | `/api/v1/users/me` | Utilisateur connecté |
| GET / PUT / DELETE | `/api/v1/users/{id}` | Lire / modifier / supprimer |
| GET / POST | `/api/v1/users/{userId}/favorites` | Lister / ajouter un favori (`{ "placeId": 1 }`) |
| DELETE | `/api/v1/users/{userId}/favorites/{placeId}` | Retirer un favori |

Les erreurs respectent une enveloppe unique :

```json
{ "error": { "status": 404, "code": "not_found", "message": "...", "path": "...", "retryable": false } }
```

---
