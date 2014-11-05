/*
 * DesktopApplication1.java
 */

package com.kleegroup.lord.ui.admin;

import com.kleegroup.lord.ui.UILauncher;
import com.kleegroup.lord.ui.admin.controller.FenetrePrincipaleAdminController;

/**
 * The main class of the application.
 */
public class AdminUI extends UILauncher {
	/** {@inheritDoc}. */
	@Override
	protected void montrerFenetrePrincipale() {
		new FenetrePrincipaleAdminController().showFenetre();
	}

	/**
	 *Point d'entr�e du programme en mode utilisateur.
	 * @param args parametres du programme
	 */
	public static void main(String[] args) {
		new AdminUI().run();
	}
}

