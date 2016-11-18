# L'Outil de Reprise de Données (LORD)

Ce programme a pour objectif de valider qu'un ensemble de fichiers CSV respecte un format défini par une spécification XML.
Il propose une interface utilisateur pour aider à la définition de ces règles (mode admin), et une autre interface pour l'exécution des contrôles.
Il a été conçut afin de pouvoir anticiper la validation de la qualité des données sans attendre que les développements (reprise, interfaces) soient réalisés.

##1. Pourquoi utiliser LORD ?

La reprise de données est un problème récurrent pour lequel, le plus souvent, des solutions jetables sont mises en place.
LORD est un outil qui permet de limiter les parties spécifiques (et donc jetées) lors de reprises de données.
Mais il peut aussi contrôler n'importe quel jeu de fichiers CSV et est donc aussi adapté au contrôle de format sur des interfaces par fichiers plats.

LORD a pour objectifs principaux de :
* formaliser le contrat du format attendu (xml)
* ne pas recoder les contrôles de format pour chaque reprise
* limiter les itérations (données fiables en entrée)

Et par conséquence :
* assurer la confidentialité des données (le contrôle peut être fait chez le client, par des équipes habilités)
* commencer le chantier d’extraction des données plus tôt, bien avant que les programmes de reprise ou d'import soient prêts
* valider d’autres données (interfaces, jeux de test)

##2. Fonctionnalités

Rendez-vous sur le [Wiki](https://github.com/KleeGroup/LORD/wiki) pour en apprendre plus !

###	Gestion des fichiers

LORD permet de définir des catégories de fichiers. Il peut s'agir de catégories fonctionnelles (ex: référentiel, finance, stock) ou techniques (ex: source_A, source_B) selon le besoin.

Pour chaque fichier il est possible de 
* l'associer à une catégorie
* définir le nombre de lignes d'entête
* choisir un seuil d'erreur au-delà duquel arrêter le traitement sur le fichier
* définir le nom du fichier (préfixe)
Les dépendances entre fichiers pour la validation seront déduites des règles de contrôle.
	
### Contraintes sur le format

LORD est capable de tester les points suivants :
* Caractère obligatoire, facultatif ou interdit (vide)
* Taille maximale (en caractères)
* Type de données  : chaîne, entier, date (masque), décimal (précision)

### Contraintes sur les données

LORD est capable de tester les points suivants :
* Unicité de la valeur au sein d'un fichier : colonne unique et multi-colonne
* Test de non nullité : colonne unique ou multi-colonne
* Liste de contrôle (explicite)
* Intégrité référentielle vers une donnée du même fichier ou d'un autre
* Validation d'une expression régulière

Il permet aussi d'ajouter des types de contraintes supplémentaires (simple colonne ou multi-colonne).

##2. Usage

###2.1. Construction du JAR

N'oubliez pas d'inclure "target/generated-sources/jaxb" dans votre build path.

###2.2. Exécution du programme

Lancer l'interface administrateur afin de générer un fichier "schema.xml".
```
java -cp LORD-1.1.jar spark.reprise.outil.ui.admin.AdminUI 
```

Lancer l'interface utilisateur afin de procéder au contrôle.
```
java -cp LORD-1.1.jar spark.reprise.outil.ui.utilisateur.UserUI
```
