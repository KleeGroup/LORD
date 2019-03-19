package com.kleegroup.lord.ui.utilisateur.controller;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import com.kleegroup.lord.ui.utilisateur.model.FenetrePrincipaleUtilisateurModel;
import com.kleegroup.lord.ui.utilisateur.model.Model;
import com.kleegroup.lord.ui.utilisateur.view.FenetrePrincipaleUtilisateur;

/**
 * Controlleur de la fenetre principale.<br>
 * S'occupe de r�aliser les demandes de l'utilisateur. Dans la majorit� des cas,
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
	 *             si le fichier n'est pas trouv�.
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
	 * Action quand l'utilisateur a cliqu� sur annul�. d�l�gu�e au controlleur
	 * de la frame actuellement affich�e.
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
	 * pr�cedent.
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
	 * Action quand l'utilisateur a cliqu� sur le bouton Misc(exporter les
	 * logs). d�l�gu�e au controlleur de la frame actuellement affich�e.
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
