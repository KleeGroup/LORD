package com.kleegroup.lord.ui.admin.controller;

import com.kleegroup.lord.moteur.util.SeparateurChamps;
import com.kleegroup.lord.moteur.util.SeparateurDecimales;
import com.kleegroup.lord.ui.admin.view.DialogFileOptions;

/**
 * Contrôleur pour la fenêtre {@link DialogFileOptions}.
 */
public class DialogFileOptionsController {

    private final DialogFileOptions view;
    private final FenetrePrincipaleAdminController fpc;
    
    /**
     * Le contrôleur des fenêtres de dialogue se limite à communiquer 
     * les actions de l'utilisateur au contrôleur de la fenêtre principale 
     * qui lui se charge d'effectuer véritablement les modifications.
     * @param fpc controlleur de la fenêtre principale.
     */
    public DialogFileOptionsController(FenetrePrincipaleAdminController fpc){
	this.fpc=fpc;
	view=new DialogFileOptions(this);
    }


    /**
     * Affiche la fenêtre.
     */
    public void showWindow() {
	view.setVisible(true);
    }


    /**
     * @return l'encodage des fichiers du schema.
     */
    public String getEncoding() {
	return fpc.getEncoding();
    }


    /**
     * @return le séparateur de champs.
     */
    public SeparateurChamps getFieldSeparator() {
	return fpc.getFieldSeparator();
    }


    /**
     * @return le séparateur de décimales.
     */
    public SeparateurDecimales getDecimalSeparator() {
	return fpc.getDecimalSeparator();
    }


    /**
     * @param string l'encodage des fichiers du schema.
     */
    public void setEncoding(String string) {
	fpc.setEncoding(string);
    }


    /**
     * @param text le séparateur de décimales.
     */
    public void setDecimalSeparator(SeparateurDecimales sep) {
	fpc.setDecimalSeparator(sep);
    }


    /**
     * @param text le séparateur de champs ("," ou ";").
     */
    public void setFieldSeparator(SeparateurChamps sep) {
    	fpc.setFieldSeparator(sep);
    }
    

}
