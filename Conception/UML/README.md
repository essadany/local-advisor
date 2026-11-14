# Diagrammes UML — LocalAdvisor

Les diagrammes sont désormais écrits en **PlantUML** (`.puml`), c'est-à-dire en texte
versionnable, relisible en revue et régénérable à tout moment. Les anciens exports PNG
(`2025-03-11`) sont conservés uniquement à titre d'historique.

## Fichiers

| Fichier | Type | Scénario |
|---|---|---|
| `cas-utilisation.puml` | Cas d'utilisation | Acteurs et fonctions, avec éléments non implémentés en pointillés |
| `diagramme-classes.puml` | Classes | Modèle du domaine JPA réel |
| `sequence-inscription.puml` | Séquence | `POST /api/v1/auth/register` |
| `sequence-connexion.puml` | Séquence | `POST /api/v1/auth/login` |
| `sequence-creer-lieu.puml` | Séquence | `POST /api/v1/places` |
| `sequence-ajouter-avis.puml` | Séquence | `POST /api/v1/reviews` |
| `sequence-gerer-favoris.puml` | Séquence | `/api/v1/users/{userId}/favorites` |

## Générer les images

Option A — extension IDE : installer **PlantUML** (VS Code / IntelliJ) et ouvrir un `.puml`.

Option B — CLI (nécessite Java) :

```bash
# Récupérer le jar une fois
curl -L -o plantuml.jar https://github.com/plantuml/plantuml/releases/latest/download/plantuml.jar

# Vérifier la syntaxe sans générer d'image
java -jar plantuml.jar -checkonly *.puml

# Générer tous les PNG (nécessite Graphviz "dot")
java -jar plantuml.jar -tpng *.puml

# Variante sans Graphviz : moteur de layout Java intégré (Smetana)
java -jar plantuml.jar -Playout=smetana -tpng *.puml
```

Option C — serveur en ligne : coller le contenu d'un `.puml` sur <https://www.plantuml.com/plantuml>.

> Les PNG générés portent le nom du fichier source (`sequence-connexion.png`, etc.).
> Graphviz n'est requis que pour les diagrammes de classes et de cas d'utilisation ;
> `-Playout=smetana` fonctionne partout.

## Dépannage — « Dot Executable: /opt/local/bin/dot ... not found »

Les diagrammes de **classes** et de **cas d'utilisation** utilisent Graphviz par défaut.
Si le preview affiche une petite image avec ce message en vert, deux solutions :

1. **Recommandée (déjà en place)** : le pragma `!pragma layout smetana` en tête de
   `cas-utilisation.puml` et `diagramme-classes.puml` force le moteur Java intégré.
   Aucune installation nécessaire.
2. **Alternative** : installer Graphviz (`sudo apt install graphviz`) et vérifier que le
   chemin configuré dans l'extension correspond à `which dot`.
   Pour VS Code : `plantuml.render` = `"Smetana"` ou `plantuml.java` selon la version.

Les diagrammes de séquence n'utilisent pas Graphviz et ne sont pas concernés.

## Règle

Ne jamais commiter uniquement un export image. Si un PNG est publié, le `.puml`
correspondant doit être versionné dans le même commit.
