# Audit des diagrammes UML — LocalAdvisor

> Audit réalisé le 14/11/2026 à partir du code source (`backend/localadvisor`, `frontend/src`).
> Les trois livrables existants sont des exports PNG non versionnables et n'ont pas pu être
> inspectés visuellement par l'outil. Le modèle réel a donc été reconstruit depuis le code, qui
> reste la source de vérité. Les diagrammes corrigés sont fournis en **PlantUML** dans ce dossier.

---

## 1. Livrables existants

| Fichier | Type | Format | Problème principal |
|---|---|---|---|
| `use case diagram-2025-03-11-134228.png` | Cas d'utilisation | PNG | Non versionnable, non modifiable, non diffable |
| `class diagram-2025-03-11-155413.png` | Classes | PNG | Idem ; aucun fichier source associé |
| `sequence diagram-2025-03-11-134316.png` | Séquence | PNG | Un seul scénario, non identifiable |

**Constat :** un diagramme sans source est un diagramme mort. Toute évolution du code le rend
immédiatement faux, sans possibilité de le corriger ni de le relire en revue.

**Correction :** les diagrammes sont désormais des fichiers `.puml` versionnés (voir `README.md`).
Les PNG restent dans le dossier à titre d'historique et pourront être supprimés après validation.

---

## 2. Écarts fonctionnels (cas d'utilisation)

| Fonction attendue (README / UI) | Réalité du code | Statut |
|---|---|---|
| Inscription / connexion | `AuthController`, JWT | Implémenté |
| CRUD lieux | `PlaceController` + `PlaceService` | Implémenté |
| CRUD catégories | `CategoryController` + `CategoryService` | Implémenté |
| CRUD avis | `ReviewController` + `ReviewService` | Implémenté |
| CRUD images | `ImageController` + `ImageService` | Implémenté (URL seulement) |
| Favoris | `FavoriteController` + `FavoriteService` | Implémenté |
| Profil utilisateur | `UserController` (`/users/me`) | Partiel |
| Rôle ADMIN / modération | `Role.ADMIN` existe, aucune règle `hasRole` | **Non implémenté** |
| Notifications sur les nouveaux avis | Aucun code back ni front | **Non implémenté** |
| Carte interactive | Aucun code back ni front | **Non implémenté** |
| Recherche multicritères | Filtrage **côté frontend uniquement**, `GET /places` renvoie tout | Partiel |
| Suggestion de catégorie | Modale frontend, aucun endpoint | **Non implémenté** |
| Upload de fichiers image | Seule une URL est stockée en base | **Non implémenté** |
| Moyenne des notes d'un lieu | `Place.rating` saisi manuellement, pas calculé depuis les avis | Écart de conception |

> Les cas d'utilisation non implémentés sont représentés en pointillés dans `cas-utilisation.puml`.
> Ils ne doivent pas être présentés comme livrés.

---

## 3. Écarts du diagramme de classes

| Élément | Réalité du code | Correction dans le diagramme |
|---|---|---|
| `Favorite` | Classe d'association `User` ↔ `Place` avec `dateAdded` | Modélisée comme entité porteuse de l'association |
| `Role` | Énumération `USER`, `ADMIN` | Représentée comme énumération |
| `Category.name` | `unique = true, nullable = false` | Contrainte indiquée |
| `User.email` | `unique = true` | Contrainte indiquée |
| `Place.category` | `@JoinColumn(nullable = false)` | Cardinalité `1..*` côté catégorie |
| `Image.place` / `Image.user` | `nullable = false` | Cardinalités `1..*` |
| `Review.user` / `Review.place` | `@JoinColumn` sans `nullable = false` | **Anomalie** : intégrité non garantie |
| `Favorite (user, place)` | Aucune contrainte d'unicité en base | **Anomalie** : doublons possibles hors service |
| `Category.Icon` | Champ Java `Icon` (majuscule) | Incohérence de nommage, exposé `icon` en JSON |
| `Place.rating` | Champ persisté, borné 0..5, non dérivé | Ne pas le dessiner comme un agrégat d'avis |
| `User.password` | `WRITE_ONLY`, jamais sérialisé | Indiqué comme non exposé |

---

## 4. Écarts de sécurité et de conception (impactant les séquences)

1. **Aucune autorisation par rôle.** `SecurityConfig` n'utilise jamais `hasRole("ADMIN")`.
   Le rôle `ADMIN` est donc décoratif.
2. **Aucun contrôle de propriété.** Un utilisateur authentifié peut modifier/supprimer les lieux,
   avis et images des autres. Seule l'authentification est vérifiée, pas l'autorisation.
3. **Usurpation possible sur les écritures.** `POST /reviews` lit `user.id` dans le corps,
   `POST /images` lit `userId` dans le corps, et `POST /users/{userId}/favorites` lit `userId`
   dans l'URL : aucun n'est comparé au principal authentifié.
4. **JWT sans révocation.** Token stateless, durée `app.token-duration` (2 h par défaut),
   pas de refresh token, pas de logout côté serveur.
5. **Pas de migrations versionnées.** `spring.jpa.hibernate.ddl-auto=update` : le schéma évolue
   implicitement, non réversible.
6. **`spring.jpa.show-sql=true`** activé en configuration par défaut.
7. **Secrets de développement** dans `stack_my_settings` (`change_me`, `change_me_too`).
8. **Aucune pagination.** Tous les `findAll()` renvoient la table entière.

> Ces points sont des constats d'audit, pas des corrections automatiques. Ils sont à traiter dans
> des tickets dédiés et à refléter dans les diagrammes de séquence (notes explicites).

---

## 5. Diagrammes régénérés

| Fichier | Contenu |
|---|---|
| `cas-utilisation.puml` | Acteurs, cas d'utilisation, frontière du système, éléments non implémentés |
| `diagramme-classes.puml` | Modèle du domaine réel avec cardinalités, contraintes et anomalies |
| `sequence-inscription.puml` | `POST /api/v1/auth/register` |
| `sequence-connexion.puml` | `POST /api/v1/auth/login` |
| `sequence-creer-lieu.puml` | `POST /api/v1/places` (authentifié) |
| `sequence-ajouter-avis.puml` | `POST /api/v1/reviews` (authentifié) |
| `sequence-gerer-favoris.puml` | `POST /api/v1/users/{userId}/favorites` |

---

## 6. Recommandations prioritaires

1. **Sécurité** : ajouter les contrôles d'autorisation (propriétaire ou `ADMIN`) sur les écritures.
2. **Intégrité** : contrainte unique `(user_id, place_id)` sur `favorites` ; `nullable = false`
   sur `reviews.user_id` / `reviews.place_id`.
3. **Migrations** : remplacer `ddl-auto=update` par Flyway ou Liquibase.
4. **Produit** : retirer du README et des diagrammes ce qui n'existe pas (notifications, carte,
   suggestion de catégorie), ou les planifier explicitement hors périmètre.
5. **Performance** : introduire la pagination et les filtres côté API avant d'augmenter le volume.
