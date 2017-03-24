/*
 * DesktopApplication1.java
 */

package com.kleegroup.lord.ui.utilisateur;

import com.kleegroup.lord.ui.UILauncher;
import com.kleegroup.lord.ui.utilisateur.controller.FenetrePrincipaleUtilisateurController;

/**
 * Application main class for data control usage (schema enforcement).
 */
public class UserUI extends UILauncher {
	/** {@inheritDoc}. */
	@Override
	protected void montrerFenetrePrincipale() {
		new FenetrePrincipaleUtilisateurController(getExecDir()).montrerFenetrePrincipale();
	}

	/**
	 *Point d'entrée du programme en mode utilisateur.
	 * @param args paramètres du programme
	 */
	public static void main(String[] args) {
		new UserUI().run();
	}
}
