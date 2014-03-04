/*
 * DesktopApplication1.java
 */

package spark.reprise.outil.ui.admin;

import spark.reprise.outil.ui.UILauncher;
import spark.reprise.outil.ui.admin.controller.FenetrePrincipaleAdminController;

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
	 *Point d'entrée du programme en mode utilisateur.
	 * @param args parametres du programme
	 */
	public static void main(String[] args) {
		new AdminUI().run();
	}
}

