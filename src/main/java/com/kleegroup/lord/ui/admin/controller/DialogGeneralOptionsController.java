package com.kleegroup.lord.ui.admin.controller;

import com.kleegroup.lord.ui.admin.view.DialogGeneralOptions;

/**
 * Controlleur pour la fenetre {@link DialogGeneralOptions}.
 */
public class DialogGeneralOptionsController {
    private final DialogGeneralOptions view=new DialogGeneralOptions(this);
    private final FenetrePrincipaleAdminController fpc;
    
    /**
     * @param fpc controlleur de la fenetre principale.
     */
    public DialogGeneralOptionsController(FenetrePrincipaleAdminController fpc){
	this.fpc=fpc;
	view.setLogExportStatus(fpc.getLogExportStatus());
    }

    /**
     * @param b true s'il faut afficher le bouton 
     */
    public void setLogExportStatus(boolean b) {
	fpc.setLogExportStatus(b);
    }

    /**
     * affiche la fenetre.
     */
    public void showWindow() {
	view.setVisible(true);
    }
    

}
