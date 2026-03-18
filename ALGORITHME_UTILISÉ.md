Voici une version plus courte, avec juste l’essentiel :

# Méthode de résolution algorithmique de Sokoban


## Principe général

Le problème est modélisé comme un **graphe d’états** :

* un **état** correspond à une configuration complète du niveau ;
* un **passage** d’un état à un autre correspond à une **poussée de caisse**.

Résoudre le niveau revient donc à parcourir ce graphe jusqu’à trouver un état gagnant.

## Représentation d’un état

Chaque état est représenté par la classe `Niveau`, qui contient la grille, la position du pousseur, des caisses, des murs et des buts.

Pour éviter d’explorer plusieurs fois le même état, chaque niveau est transformé en un code unique avec la méthode `code()`, utilisé dans une `HashMap`.

## Clonage du personnage

Avant de tester les poussées possibles, on considère que le joueur peut se déplacer librement sur toutes les cases accessibles sans pousser de caisse.

On “clone” donc le personnage sur toute cette zone accessible.

Cela permet de ne pas calculer tous les déplacements du joueur un par un, mais seulement les poussées de caisses réellement possibles.

## Génération des états suivants

À partir d’un état :

* on repère toutes les caisses ;
* on teste pour chaque caisse les 4 directions possibles ;
* si une caisse peut être poussée, on crée un nouvel état ;
* ce nouvel état est ajouté à la liste des états accessibles.

## Parcours du graphe

La classe `Solveur` parcourt les états avec deux structures :

* `actifs` : les états à explorer ;
* `dejavu` : les états déjà visités.

À chaque étape :

1. on prend un état ;
2. on vérifie s’il est gagnant ;
3. sinon, on génère ses successeurs ;
4. on ajoute seulement ceux qui n’ont pas encore été vus.

## Reconstruction de la solution

Pour chaque état découvert, on mémorise aussi son **parent**.

Ainsi, lorsqu’un état gagnant est trouvé, on peut remonter les parents jusqu’à l’état initial pour reconstruire le chemin complet de résolution.

## Résumé

La méthode utilisée consiste donc à :

* représenter Sokoban comme un graphe d’états ;
* générer les états accessibles par poussée de caisse ;
* parcourir ces états automatiquement ;
* mémoriser les états déjà vus ;
* reconstruire le chemin quand une solution est trouvée.
