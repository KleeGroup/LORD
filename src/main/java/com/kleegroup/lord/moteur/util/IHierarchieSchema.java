package com.kleegroup.lord.moteur.util;

/**
 * Definit la hierarchie du schema de donnees.<br>
 * la hierarchie est definie de la maniere suivante
 * Categorie-&gt;Fichier-&gt;Colonne-&gt;Contraintes.<br><br>
 * Cette interface permet de d�finir des fonctions qui aident
 * lors de l'affichage des ces elements dans un arbre.
 */
public interface IHierarchieSchema {
    /**
     * @return true si c'est une categorie
     */
    boolean isCategorie();
    /**
     * @return true si c'est un fichier
     */
    boolean isFichier();
    /**
     * @return true si c'est une colonne
     */
    boolean isColonne();
    /**
     * @return true si c'est une contrainte
     */
    boolean isContrainte();
    /**
     * Categorie=0, fichier =1, colonne=2, contrainte=3.
     * @return le niveau de l'object
     */
    int getNiveau();
    /**
     * @return le nom a afficher dans la hierarchie
     */
    String getNomHierarchie();
    /**
     * @return le nombre de fils.
     */
    int getChildCount();
    /**
     * @param index la position du fils.
     * @return le fils demand�.
     */
    IHierarchieSchema getChild(int index);
    /**
     * @return la position dans le parent de cet element.
     */
    int getPosition();
    /**
     * @return le parent de cet element
     */
    IHierarchieSchema getParent();

}
