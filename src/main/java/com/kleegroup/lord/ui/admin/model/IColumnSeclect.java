package com.kleegroup.lord.ui.admin.model;

import java.util.List;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.kleegroup.lord.moteur.Colonne;

/**
 * Interface que les mod�le de la boite de selection des colonnes doivent impl�menter.
 */
public interface IColumnSeclect {

   /**
     * @return le mod�le qu'affichera l'arbre des colonnes.
     */
    TreeModel getTreeModel();

     /**
     * filtrer la liste des colonnes affich�s.
     * @param text le filtre des colonnes.
     */
    void setFilter(String text);

      /**
     * @param tp ajouter l'�l�ment dont le treepath est tp � la s�lection.
     */
    void selectElement(TreePath tp);

      /**
     * @return une chaine qui represente les elements selectionn�s.
     */
    String getSelectedElements();

      /**
     * @return la liste des colonnes selectionn�es.
     */
    List<Colonne> getSelectedEltsList();

      /**
     * mettre � 0 la s�l�ction.
     */
    void clear();

      /**
     * @param value la valeur initiale de la s�l�ction.
     */
    void setInitialValue(List<?> value);

}
