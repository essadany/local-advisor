# Conception — LocalAdvisor

Ce dossier contient les artefacts de conception à maintenir **avant** et **pendant** le codage.
Ils sont la référence en revue : si le code diverge, on met à jour le document dans le même PR.

## Ordre de lecture

1. `cadrage.md` — exigences fonctionnelles, non fonctionnelles, hors périmètre et hypothèses.
2. `c4-niveau1-contexte.html` — qui utilise le système, et de quoi il dépend.
3. `c4-niveau2-conteneurs.html` — les unités déployables (SPA, API, base) et leurs flux.
4. `../../Conception/UML/AUDIT-UML.md` — écarts constatés entre les anciens diagrammes et le code.
5. `../../Conception/UML/*.puml` — cas d'utilisation, classes et séquences (source versionnable).

## Ouvrir les diagrammes C4

Les fichiers HTML sont autonomes (CSS + SVG inline). Ouvrir directement dans un navigateur :

```bash
xdg-open docs/design/c4-niveau1-contexte.html
xdg-open docs/design/c4-niveau2-conteneurs.html
```

Les boutons `⋯` permettent d'exporter en PNG ou PDF.

## Règle de maintien

- Toute nouvelle fonctionnalité commence par une mise à jour de `cadrage.md` (périmètre + hypothèses).
- Tout changement de conteneur (ex. ajout d'un stockage d'images) met à jour le C4 niveau 2.
- Tout changement du modèle de données met à jour `diagramme-classes.puml`.
- Aucun diagramme ne doit être livré uniquement en image : la source texte est obligatoire.
