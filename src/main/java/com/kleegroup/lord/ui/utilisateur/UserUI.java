/*
 * DesktopApplication1.java
 */

package com.kleegroup.lord.ui.utilisateur;

import com.kleegroup.lord.ui.UILauncher;
import com.kleegroup.lord.ui.utilisateur.controller.FenetrePrincipaleUtilisateurController;

/**
 * The main class of the application.
 */
public class UserUI extends UILauncher {
	/** {@inheritDoc}. */
	@Override
	protected void montrerFenetrePrincipale() {
		new FenetrePrincipaleUtilisateurController(getExecDir()).montrerFenetrePrincipale();
	}

	/**
	 *Point d'entr�e du programme en mode utilisateur.
	 * @param args parametres du programme
	 */
	public static void main(String[] args) {
		new UserUI().run();
	}
}
