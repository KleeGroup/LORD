package spark.reprise.outil.ui.utilisateur.controller;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import spark.reprise.outil.ui.utilisateur.model.FenetrePrincipaleUtilisateurModel;
import spark.reprise.outil.ui.utilisateur.model.Model;
import spark.reprise.outil.ui.utilisateur.view.FenetrePrincipaleUtilisateur;

/**
 * Controlleur de la fenetre principale.<br>
 * S'occupe de réaliser les demandes de l'utilisateur. Dans la majorité des cas,
 * transmet les action de l'utilisateur au FrameController actuel.
 * 
 */
public class FenetrePrincipaleUtilisateurController extends Controller<FenetrePrincipaleUtilisateur, FenetrePrincipaleUtilisateurModel> {
    	File execDir;
	FrameController<? extends Component,? extends Model> controlleurActuel;

	/**
	 * Construit un Controlleur de la fenetre principale.
	 * @param execDir le repertoire du programme
	 */
	public FenetrePrincipaleUtilisateurController(File execDir) {
	    	this.execDir = execDir;
		view = new FenetrePrincipaleUtilisateur(this);
		controlleurActuel = new FrameBienvenueController(this);
		controlleurActuel.activate();

	}

	/**
	 * Charge le fichier de conf.
	 * 
	 * @throws FileNotFoundException
	 *             si le fichier n'est pas trouvé.
	 * @throws JAXBException Exception JAXB
	 */
	public void chargerFichierConf() throws FileNotFoundException, JAXBException {
		view.setWait(true);
		model = new FenetrePrincipaleUtilisateurModel(execDir);
		view.setWait(false);

	}

	/**
	 * Affiche la premiere fenetre de l'interface utilisateur.
	 */
	public void montrerFenetrePrincipale() {
		view.setVisible(true);
	}

	/**
	 * Action quand l'utilisateur a cliqué sur annulé. déléguée au controlleur
	 * de la frame actuellement affichée.
	 */
	public void annuler() {
		controlleurActuel.annuler();
	}

	/**
	 * demande la permission du controlleur actuel de changer de frame (
	 * {@link FrameController#canGoNext()} si autorise, appelle la methode
	 * {@link FrameController#suivant()}, et donne la main au controlleur
	 * suivant.
	 */
	public void suivant() {
		if (controlleurActuel.canGoNext()) {
			controlleurActuel.suivant();
			setControlleurActuel(controlleurActuel.getNext());
		}
	}

	/**
	 * demande la permission du controlleur actuel de changer de frame (
	 * {@link FrameController#canGoBack()} si autorise, appelle la methode
	 * {@link FrameController#precedent()}, et donne la main au controlleur
	 * précedent.
	 */
	public void precedent() {
		if (controlleurActuel.canGoBack()) {
			controlleurActuel.precedent();
			setControlleurActuel(controlleurActuel.getPrevious());
		}
	}

	protected void setControlleurActuel(FrameController<? extends Component,? extends Model> nouveauController) {
		controlleurActuel.deactivate();
		controlleurActuel = nouveauController;
		controlleurActuel.activate();
	}

	/**
	 * Action quand l'utilisateur a cliqué sur le bouton Misc(exporter les
	 * logs). déléguée au controlleur de la frame actuellement affichée.
	 */
	public void executeMiscAction() {
		controlleurActuel.executeMiscAction();

	}

	/**
	 * detruit la fenetre principale.
	 */
	public void quitter() {
		view.dispose();
//		view.setVisible(false);
	}

}
