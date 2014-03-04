package spark.reprise.outil.ui.admin.model;

import java.util.List;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import spark.reprise.outil.moteur.Colonne;

/**
 * Interface que les modèle de la boite de selection des colonnes doivent implémenter.
 */
public interface IColumnSeclect {

   /**
     * @return le modèle qu'affichera l'arbre des colonnes.
     */
    TreeModel getTreeModel();

     /**
     * filtrer la liste des colonnes affichés.
     * @param text le filtre des colonnes.
     */
    void setFilter(String text);

      /**
     * @param tp ajouter l'élément dont le treepath est tp à la sélection.
     */
    void selectElement(TreePath tp);

      /**
     * @return une chaine qui represente les elements selectionnés.
     */
    String getSelectedElements();

      /**
     * @return la liste des colonnes selectionnées.
     */
    List<Colonne> getSelectedEltsList();

      /**
     * mettre à 0 la séléction.
     */
    void clear();

      /**
     * @param value la valeur initiale de la séléction.
     */
    void setInitialValue(List<?> value);

}
