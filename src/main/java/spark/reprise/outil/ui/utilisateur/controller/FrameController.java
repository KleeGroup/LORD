package spark.reprise.outil.ui.utilisateur.controller;

import java.awt.Component;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import spark.reprise.outil.ui.utilisateur.model.Model;
import spark.reprise.outil.ui.utilisateur.view.FenetrePrincipaleUtilisateur;

/**
 * Classe mère des controlleur de frame.
 * @param <C> Controlleur de la frame à mettre dans la fenetre Principale
 * @param <M> le modele de la frame
 */
public abstract class FrameController<C extends Component, M extends Model> extends Controller<C, M> {
	protected FenetrePrincipaleUtilisateur fenetrePrincipale;

	protected FenetrePrincipaleUtilisateurController fenetrePrincipaleController;

	protected ResourceBundle resourceMap = ResourceBundle.getBundle("resources.GeneralUIMessages");

	protected FrameController<? extends Component,? extends Model> next = this;

	protected FrameController<? extends Component,? extends Model> previous = this;

	protected FrameController(FenetrePrincipaleUtilisateurController fpc) {
		fenetrePrincipaleController = fpc;
		fenetrePrincipale = fpc.view;
		this.model = createModel();
		this.view = createView();
		fenetrePrincipale.addFrame(view, getName());
	}

	protected abstract M createModel();

	protected abstract C createView();

	/**
	 * Demande à lu'tilisateur s'il veut vraiment quitter, et si oui quitte le
	 * programme.
	 */
	public void annuler() {
		if (askQuestionDefaultNo(resourceMap.getString("Quit.message")) == javax.swing.JOptionPane.YES_OPTION) {
			quitter();
		}
	}

	/**
	 * affiche la fenetre suivante.
	 */
	public void suivant() {
		fenetrePrincipale.showFrame(getNext().getName());
	}

	/**
	 * @return true s'il est permis de revenir en arriere.
	 */
	public boolean canGoBack() {
		return true;
	}

	/**
	 * Pour faire des vérifications de validité des données, il est conseillé de
	 * surcharger cette fonction.
	 * 
	 * @return true s'il est permis d'avancer.
	 */
	public boolean canGoNext() {
		return true;
	}

	/**
	 * affiche le fenetre precedante.
	 */
	public void precedent() {
		fenetrePrincipale.showFrame(getPrevious().getName());
	}

	/**
	 * @return le frame a afficher.
	 */
	public Component getFrame() {
		return view;
	}

	/**
	 * @return le controller de la fenetre suivante.
	 */
	public FrameController<? extends Component, ? extends Model>  getNext() {
		return next;
	}

	/**
	 * @param next
	 *            le controller de la fenetre suivante.
	 */
	public void setNext(FrameController<? extends Component, ? extends Model> next) {
		this.next = next;
		next.fenetrePrincipale = fenetrePrincipale;
		next.previous = this;
	}

	/**
	 * @return le controller de la fenetre precedante.
	 */
	public FrameController<? extends Component, ? extends Model> getPrevious() {
		return previous;
	}

	/**
	 * @return le frame de la fenetre suivante.
	 */
	public Component getNextFrame() {
		return next.getFrame();
	}

	/**
	 * @return le frame de la fenetre precedante.
	 */
	public Component getPreviousFrame() {
		return previous.getFrame();
	}

	abstract String getName();

	protected void activate() {
		// apppelé lorsque le frame correspondant est affiche dans la fenetre
		// principale
	}

	protected void deactivate() {
		// apppelé avant que le frame correspondant ne soit remplace par un
		// autre
		// dans la fenetre principale
	}

	/**
	 * action du bouton "Exporter les logs".
	 */
	public void executeMiscAction() {
		// action du bouton afficher Erreur
	}

	/**
	 * ferme l'application.
	 */
	public void quitter() {
		fenetrePrincipaleController.quitter();
	}

	protected int askQuestionDefaultNo(String message) {
		final String yes = resourceMap.getString("CancelTest.YES"), no = resourceMap.getString("CancelTest.NO");
		return JOptionPane.showOptionDialog(null, message, "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, new String[] { yes, no }, no);
	}

}
