# Cadrage — LocalAdvisor

> Document de référence à remplir **avant** toute nouvelle fonctionnalité.
> Les valeurs marquées **(hypothèse)** sont à confirmer : elles sont portantes pour
> les décisions d'architecture et doivent être revues si elles changent.

## 1. Problème en une phrase

LocalAdvisor est une plateforme web collaborative où les utilisateurs découvrent,
partagent et évaluent les bons endroits d'une ville (restaurants, cafés, salles de sport,
coiffeurs, etc.).

## 2. Exigences fonctionnelles (cœur)

1. **Authentification** — un visiteur peut s'inscrire et se connecter ; un utilisateur
   authentifié obtient un JWT et accède à ses actions.
2. **Catalogue de lieux** — consulter, rechercher et filtrer les lieux par catégorie,
   note et mot-clé ; consulter la fiche d'un lieu (avis, images, favoris).
3. **Avis et notes** — un utilisateur authentifié publie, modifie et supprime ses avis.
4. **Favoris** — un utilisateur authentifié ajoute et retire des lieux de ses favoris.

Le reste (notifications, carte interactive, modération, suggestion de catégorie) est
explicitement hors périmètre pour l'instant — voir §4.

## 3. Exigences non fonctionnelles

| Axe | Cible | Statut |
|---|---|---|
| Échelle | 10 000 utilisateurs, 500 lieux, 5 000 avis **(hypothèse)** | À confirmer |
| Ratio lecture/écriture | 10:1 (lecture dominante) **(hypothèse)** | À confirmer |
| Latence | p99 < 500 ms sur les lectures, p99 < 800 ms sur les écritures **(hypothèse)** | À confirmer |
| Disponibilité | 99,5 % (une instance, environnement unique) **(hypothèse)** | À confirmer |
| Cohérence | Forte (PostgreSQL ACID) | Imposé par le stack |
| Durabilité | Volume Docker `dbdata` ; RPO 24 h / RTO 4 h **(hypothèse)** | À confirmer |
| Sécurité | JWT HS256, durée 2 h, mots de passe BCrypt, routes d'écriture authentifiées | Partiel (voir audit) |
| Données personnelles | Email, nom, mot de passe haché ; aucune donnée sensible | À documenter |

## 4. Hors périmètre (explicite)

- Notifications (e-mail / push) sur les nouveaux avis.
- Carte interactive et géolocalisation.
- Application mobile native.
- Modération et tableau de bord administrateur.
- Upload de fichiers image / stockage objet (aujourd'hui : URL uniquement).
- Suggestion de catégorie par les utilisateurs.
- Multi-région, haute disponibilité, réplication.
- Refresh token et révocation de session.

> Ces éléments peuvent revenir plus tard, mais **ne doivent pas apparaître comme livrés**
> dans le README ni dans les diagrammes.

## 5. Hypothèses porteuses

- Un seul serveur, un seul environnement (développement local via Docker Compose).
- Aucun système externe à ce jour ; pas de fournisseur d'identité, de mail ou de stockage.
- Le frontend et l'API sont servis derrière nginx (`/api` → `backend:8080`).
- Le rôle `ADMIN` existe en base mais n'est pas exploité par les règles d'accès.
- La note d'un lieu (`Place.rating`) est saisie, pas calculée depuis les avis.
- Les listes ne sont pas paginées : acceptable à 500 lieux, à corriger avant 10×.

## 6. Chiffres clés (à passer au dimensionnement)

| Indicateur | Estimation (hypothèse) |
|---|---|
| Utilisateurs actifs | 10 000 |
| Lieux | 500 |
| Avis | 5 000 |
| Lectures / jour | ~50 000 (≈ 0,6 lecture/s en moyenne, ~6/s en pic) |
| Écritures / jour | ~5 000 (≈ 0,06 écriture/s en moyenne, ~1/s en pic) |
| Stockage | < 100 Mo de données métier / an |
| Bande passante | Faible (JSON texte) |

**Conclusion de dimensionnement :** le volume ne justifie ni cache, ni file de messages,
ni sharding. Un monolithe Spring Boot + PostgreSQL suffit largement. Les priorités sont la
**sécurité**, l'**intégrité des données** et la **pagination**, pas la scalabilité horizontale.

## 7. Validation

- [x] Au moins un élément est hors périmètre (§4).
- [x] Chaque exigence non fonctionnelle a un nombre ou une mention « n/a ».
- [x] Les hypothèses sont écrites et identifiées comme telles.
- [x] Le cœur fonctionnel est limité à 4 fonctions (§2).

---

### Références

- C4 niveau 1 : `c4-niveau1-contexte.html`
- C4 niveau 2 : `c4-niveau2-conteneurs.html`
- Audit UML : `../../Conception/UML/AUDIT-UML.md`
