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

## Déploiement (Oracle Cloud Free Tier + GitLab CI/CD)

### 1. Créer un compte Oracle Cloud

1. Aller sur [oracle.com/cloud](https://oracle.com/cloud) → **Free Tier**
2. Renseigner une carte bancaire (pas de frais, juste vérification)
3. Après activation, créer une **VM (Compute Instance)** :
   - **Image** : Ubuntu 24.04 LTS (ARM ou AMD)
   - **Shape** : `VM.Standard.A1.Flex` (4 ARM OCPU, 24 GB RAM — gratuit) ou `VM.Standard.E2.1.Micro` (1 AMD OCPU, 1 GB RAM — gratuit)
   - **SSH** : Télécharger ou copier la clé privée
   - Ouvrir les ports **4173** et **22** dans le security list (Ingress Rules)

### 2. Préparer la VM

```bash
ssh ubuntu@<IP_DE_LA_VM>
sudo apt update && sudo apt install -y docker.io docker-compose-v2
sudo usermod -aG docker $USER
# Déconnexion/reconnexion pour appliquer le groupe
exit

# Installer Portainer (optionnel — interface web pour gérer Docker)
docker volume create portainer_data
docker run -d --name portainer --restart unless-stopped \
  -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock \
  -v portainer_data:/data portainer/portainer-ce:latest
```

### 3. Créer un token de déploiement GitLab

1. Projet → **Settings** → **Repository** → **Deploy Tokens**
2. Créer un token avec :
   - **Name** : `gitlab-deploy`
   - **Expires at** : laisser vide (pas d'expiration)
   - **Scopes** : `read_registry`
3. Copier le **username** et le **token** générés

### 4. Variables CI/CD (Settings → CI/CD → Variables)

| Variable | Valeur | Type |
|----------|--------|------|
| `VPS_HOST` | IP publique de la VM Oracle | Variable |
| `VPS_SSH_USER` | `ubuntu` | Variable |
| `VPS_SSH_KEY` | Clé privée SSH (fichier `.pem`) | File, Masked |
| `REGISTRY_USER` | Username du Deploy Token GitLab | Variable |
| `REGISTRY_PASSWORD` | Token du Deploy Token GitLab | Masked |
| `DB_PASSWORD` | Mot de passe PostgreSQL (choisir un sûr) | Masked |
| `JWT_SECRET` | Clé secrète JWT (64+ caractères) | Masked |
| `POSTGRES_DB` | `localadvisor` | Variable |
| `POSTGRES_USER` | `postgres` | Variable |

### 5. Pipeline

À chaque `git push` sur `main` :
1. **test-backend** — `./mvnw verify`
2. **test-frontend** — `npm run type-check && npm run test:unit`
3. **build-images** — Construit et pousse les images Docker vers le GitLab Container Registry
4. **deploy** — SSH vers la VM, pull les nouvelles images, redémarre avec Docker Compose

### 6. Accès

- Frontend : `http://<VPS_HOST>:4173`
- Backend : `http://<VPS_HOST>:8080` (interne, pas exposé publiquement par défaut)
- Portainer : `http://<VPS_HOST>:9000`

---
