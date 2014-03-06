__Warning, this project is still under developement.__

# L'Outil de Reprise de Données (LORD)

Ce programme a pour objectif de valider le format et le contenu de fichiers CSV d'après une spécification XML. Il propose aussi une interface pour définir ces règles.

##1. Pourquoi utiliser LORD ?

La reprise de données est un problème récurrent pour lequel le plus souvent des solutions jetables sont mises en place.

LORD a pour objectifs principaux
* formaliser le contrat du format attendu
* ne pas recoder les contrôles pour chaque reprise
* limiter les itérations (données fiables)
Mais aussi :
* assurer la confidentialité des données (le contrôle peut être fait chez le client, par des équipes habilités)
* commencer le chantier d’extraction des données plus tôt
* valider d’autres données (interfaces, jeux de test)

##2. Fonctionnalités

###	Gestion des fichiers

LORD permet de définir des catégories de fichiers. Il peut s'agir de catégories fonctionnelles (ex: référentiel, finance, stock) ou techniques (ex: source_A, source_B) selon le besoin.

Pour chaque fichier il est possible de définir
* le nombre de lignes d'entête
* un seuil d'erreur au-delà duquel arrêter le traitement
* une catégorie
* le nom du fichier    
	
### Contraintes sur le format

LORD est capable de tester les points suivants :
* Caractère obligatoire, facultatif ou interdit (vide)
* Taille maximale (en caractères)
* Type de données  : chaîne, entier, date (masque), décimal (précision)

### Contraintes sur les données

LORD est capable de tester les points suivants :
* Unicité de la valeur au sein d'un fichier
* Liste de contrôle (explicite)
* Intégrité référentielle vers une colonne
	
##2. Usage

Lancer l'interface administrateur afin de générer un fichier "schema.xml".
```
java -cp klee-reprise-1.0-shaded.jar spark.reprise.outil.ui.admin.AdminUI 
```

Lancer l'interface utilisateur afin de procéder au contrôle.
```
java -cp klee-reprise-1.0-shaded.jar spark.reprise.outil.ui.utilisateur.UserUI
```
