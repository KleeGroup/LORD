package spark.reprise.outil.ui.admin.controller;

import java.util.List;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import spark.reprise.outil.moteur.Colonne;
import spark.reprise.outil.moteur.Fichier;
import spark.reprise.outil.moteur.Schema;
import spark.reprise.outil.ui.admin.model.DialogSelectColumnFileModel;
import spark.reprise.outil.ui.admin.model.DialogSelectColumnSchemaModel;
import spark.reprise.outil.ui.admin.model.IColumnSeclect;
import spark.reprise.outil.ui.admin.view.DialogSelectColumn;

/**
 * Controlleur pour {@link DialogSelectColumn}
 * 
 * Cette classe ainsi que l'interface utilisateur correspondante {@link DialogSelectColumn}
 * , aident l'utilisateur à choisir
 * une (ou plusieurs) colonne(s) parmi une liste proposée.
 * 
 * Cette classe possède deux modes:
 * -mode schema: 
 * 	sert pour choisir la colonne de reference , <br>
 * 	affiche tout les fichiers du schema 
 * (sauf un en particulier pour éviter les références circulaires)
 *  ainsi que les colonnes de ces fichiers. <br>
 *  	permet de selectionner une seul colonne.
 *  
 *  
 * -mode fichier: 
 * 	sert pour choisir les colonnes pour les contraintes Multi Colonne(verifications specifiques)
 * 	affiche toutes les colonnes d'un fichier. 
 * 	permet de choisir plusieurs colonnes
 */
public class DialogSelectColumnController {
    DialogSelectColumn view;

    IColumnSeclect model;

    boolean userClickedCancel = true;


    /**
     * Demarre dans le mode Fichier. 
     * @param f le fichier dont on affiche les colonnes
     */
    public DialogSelectColumnController(Fichier f) {
	model = new DialogSelectColumnFileModel(f);
	view = new DialogSelectColumn(this, false);
	view.setOkEnabled(true);
    }

    /**
     * Demarre dans le mode schema .
     * @param schema le schema dont affiche les fichiers et les colonnes de ces fichiers
     * @param exclu le fichier qui contient la colonne a exclure de l'affichage
     * @param colonneExclue la colonne a exclure
     */
    public DialogSelectColumnController(Schema schema, Fichier exclu, String colonneExclue) {
	model = new DialogSelectColumnSchemaModel(schema, exclu,colonneExclue);
	view = new DialogSelectColumn(this, true);
    }
    /**
     * @param value la liste ou colonne initial selectionée
     */
    public void setInitialValue(List<?> value){
	clear();
	model.setInitialValue(value);
	view.setSelectedColumns(model.getSelectedElements());
    }

    /**
     * la liste des colonnes dépend du modele. c'est lui 
     * @return la liste des colonne
     */
    public TreeModel getTreeModel() {
	return model.getTreeModel();
    }

    /**
     * affiche la fenetre.
     */
   
    public void showWindow() {
	view.setOkEnabled(true);
	view.setVisible(true);
	
    }

    /**
     * @param text le filtre qui determine les colonne  a afficher. 
     */
    public void setFilter(String text) {
	model.setFilter(text);

    }

    /**
     * @param tp ajouter un element apres la position tp.
     */
    public void addElement(TreePath tp) {
	model.selectElement(tp);
	view.setSelectedColumns(model.getSelectedElements());
	if(model.getSelectedEltsList().size()>0){
	    view.setOkEnabled(true);
	}
	
    }

    /**
     * appelé par la fenetre quand l'utilisateur clique sur OK.
     */
    public void ok() {
	userClickedCancel = false;
	view.dispose();
    }

    /**
     * appelé par la fenetre quand l'utilisateur clique sur CANCEL.
     */
    public void cancel() {
	model.getSelectedEltsList().clear();
	view.dispose();
    }

    /**
     * @return une liste des elements selectionnés
     */
    public List<Colonne> getSelectElements() {
	return model.getSelectedEltsList();
    }

    /**
     * @return true si l'utilisateur a cliqué sur CANCEL.
     */
    public boolean isUserClickedCancel() {
	return userClickedCancel;
    }

    /**
     * efface la sélection de l'utilisateur.
     */
    public void clear() {
	model.clear();
	view.setSelectedColumns(model.getSelectedElements());
	view.clearFilter();
	setFilter("");
    }

}
