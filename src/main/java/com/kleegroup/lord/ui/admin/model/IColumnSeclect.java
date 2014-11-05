package com.kleegroup.lord.ui.admin.model;

import java.util.List;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.kleegroup.lord.moteur.Colonne;

/**
 * Interface que les modèles de la boîte de sélection des colonnes doivent implémenter.
 */
public interface IColumnSeclect {

   /**
     * @return le modèle qu'affichera l'arbre des colonnes.
     */
    TreeModel getTreeModel();

     /**
     * filtrer la liste des colonnes affichées.
     * @param text le filtre des colonnes.
     */
    void setFilter(String text);

      /**
     * @param tp ajouter l'élément dont le treepath est tp à la sélection.
     */
    void selectElement(TreePath tp);

      /**
     * @return une chaîne qui represente les éléments selectionnés.
     */
    String getSelectedElements();

      /**
     * @return la liste des colonnes selectionnées.
     */
    List<Colonne> getSelectedEltsList();

      /**
     * mettre à 0 la sélection.
     */
    void clear();

      /**
     * @param value la valeur initiale de la s�l�ction.
     */
    void setInitialValue(List<?> value);

}
