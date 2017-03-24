/*
 * DesktopApplication1.java
 */

package com.kleegroup.lord.ui.admin;

import com.kleegroup.lord.ui.UILauncher;
import com.kleegroup.lord.ui.admin.controller.FenetrePrincipaleAdminController;

/**
 * Application main class for administrative usage (schema editing).
 */
public class AdminUI extends UILauncher {
	/** {@inheritDoc}. */
	@Override
	protected void montrerFenetrePrincipale() {
		new FenetrePrincipaleAdminController().showFenetre();
	}

	/**
	 *Point d'entrée du programme en mode administrateur.
	 * @param args paramètres du programme
	 */
	public static void main(String[] args) {
		new AdminUI().run();
	}
}

