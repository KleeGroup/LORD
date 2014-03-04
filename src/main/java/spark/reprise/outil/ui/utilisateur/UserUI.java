/*
 * DesktopApplication1.java
 */

package spark.reprise.outil.ui.utilisateur;

import spark.reprise.outil.ui.UILauncher;
import spark.reprise.outil.ui.utilisateur.controller.FenetrePrincipaleUtilisateurController;

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
	 *Point d'entrée du programme en mode utilisateur.
	 * @param args parametres du programme
	 */
	public static void main(String[] args) {
		new UserUI().run();
	}
}
