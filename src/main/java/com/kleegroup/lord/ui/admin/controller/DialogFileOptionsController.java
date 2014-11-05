package com.kleegroup.lord.ui.admin.controller;

import com.kleegroup.lord.ui.admin.view.DialogFileOptions;

/**
 * Controlleur pour la fenetre {@link DialogFileOptions}.
 */
public class DialogFileOptionsController {

    private final DialogFileOptions view;
    private final FenetrePrincipaleAdminController fpc;
    
    /**
     * Le controlleur des fenetre de dialgue se limite a communiquer 
     * les actions de l'utilisateur au controlleur de la fenetre principale 
     * qui lui se charge d'effectuer veritablement les modifications.
     * @param fpc controlleur de la fenetre principale.
     */
    public DialogFileOptionsController(FenetrePrincipaleAdminController fpc){
	this.fpc=fpc;
	view=new DialogFileOptions(this);
    }


    /**
     * affiche la fenetre.
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
     * @return le seprateur de champs.
     */
    public String getFieldSeparator() {
	return fpc.getFieldSeparator();
    }


    /**
     * @return le separateur de d�cimales.
     */
    public String getDecimalSeparator() {
	return fpc.getDecimalSeparator();
    }


    /**
     * @param string l'encodage des fichiers du schema.
     */
    public void setEncoding(String string) {
	fpc.setEncoding(string);
    }


    /**
     * @param text le separateur de d�cimales.
     */
    public void setDecimalSeparator(String text) {
	fpc.setDecimalSeparator(text);
    }


    /**
     * @param text le seprateur de champs ("," ou ";").
     */
    public void setFieldSeparator(String text) {
	if(",".equals(text)){
	    fpc.setFieldSeparator(',');
	}else{
	    fpc.setFieldSeparator(';');
	}
    }
    

}
