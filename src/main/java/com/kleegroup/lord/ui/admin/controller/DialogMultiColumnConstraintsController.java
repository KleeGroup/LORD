package com.kleegroup.lord.ui.admin.controller;

import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import com.kleegroup.lord.moteur.Fichier;
import com.kleegroup.lord.ui.admin.model.DialogMultiColumnConstraintsModel;
import com.kleegroup.lord.ui.admin.view.DialogMultiColumnConstraints;

/**
 * Controlleur pour la class {@link DialogMultiColumnConstraints}.
 */
public class DialogMultiColumnConstraintsController {
    DialogMultiColumnConstraintsModel model;
    DialogMultiColumnConstraints view; 
    
    /**
     * @param fpc controlleur de la fenetre principale.
     */
    public DialogMultiColumnConstraintsController(FenetrePrincipaleAdminController fpc){
	fpc.getCurrentFichier(); 
	model =new DialogMultiColumnConstraintsModel(fpc.getCurrentFichier());
	 view =new DialogMultiColumnConstraints(this);
    }
    
    /**
     * affiche la fenetre.
     */
   
    public void showWindow() {
	view.setVisible(true);
    }
    /**
     * @return le model de la table.
     */
    public TableModel getTableModel() {
	return model.getTableModel();
    }
    
    /**
     * cree une nouvelle commande contrainte. Une fois l'utilisateur clique sur Ok,
     * les commandes sont v�rifi�es si elles sont valides et cr��es.
     */
    public void addConstraintCommand() {
	model.addConstraintCommand();
    }
    /**
     * @param value la nouvelle valeur
     * @param row la ligne de la valeur modifi�e
     * @param col la colonne de la valeur modifi�e
     */
    public void changeFileValue(Object value, int row, int col) {
	model.changeFileValue(value,row,col);
    }
    
    /**
     * la liste des noms de methode possibles.
     * @return la liste des noms de methode possibles. 
     */
    public String[] getPossibleMethodNames() {
	return model.getPossibleMethodNames();
    }
    
    /**
     * le fichier en cours est le fichier auquel appartient les 
     * contraintes en train d'etre modifi�es.
     * @return le fichier en cours de modification.
     */
    public Fichier getCurrentFichier() {
	return model.getCurrentFichier();
    }
    /**
     * L'utilisateur a cliqu� sur Ok.
     * 
     */
    public void ok() {
	//on verifie que toutes les commandes sont valide.
	final int pos=model.getFirstInvalidConstraint();
	if(pos<0){//elles sont valides
	
	    model.createConstraints();
	
	    view.dispose();
	    
	}else{//invalides: on indique la 1ere invalide
	    JOptionPane.showMessageDialog(view, 
		    "La contrainte qui est � la position "+(pos+1)+" est invalide");
	}
	
    }
    /**
     * L'utilisateur a clique sur annuler. On ne fait rien.
     */
    public void cancel() {
	view.dispose();
    }
    /**
     * @param selectedRow la psositon de la commande de contrainte a supprimer.
     */
    public void deleteConstraint(int selectedRow) {
	model.deleteConstraint(selectedRow);
	
    }
    
    
}
