package spark.reprise.outil.ui.utilisateur.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;

import spark.reprise.outil.moteur.Fichier;
import spark.reprise.outil.moteur.Fichier.ETAT;
import spark.reprise.outil.moteur.Schema;
import spark.reprise.outil.moteur.util.INotifiable;
import spark.reprise.outil.ui.utilisateur.model.FrameProgressionTraitementModel;
import spark.reprise.outil.ui.utilisateur.view.FrameProgressionTraitement;

/**
 * Controlleur de FrameProgressionTraitement.
 */
public class FrameProgressionTraitementController extends FrameController<FrameProgressionTraitement, FrameProgressionTraitementModel>
	implements PropertyChangeListener {
    protected String nomFichierEnCours;

    protected Fichier.ETAT etatFichierPrecedant;

    protected int nbFichiersTotal, nbFichiersVerifies;

    protected Task tacheVerifSchema;
    
    /**
     * L'etat de la vérification.
     */
    public enum Etat{
	/**
	 * Vérification du fichier n'a pas encore commencé.
	 */
	AVANT_VERIFICATION,
	/**
	 * Vérification du fichier en cours.
	 */
	VERIFICATION_EN_COURS,
	/**
	 * Vérification du fichier abandonnée.
	 */
	VERIFICATION_ABANDONNEE,
	/**
	 * Vérification du fichier achevée.
	 */
	VERIFICATION_ACHEVEE,
		
    }
    Etat etatActuel=Etat.AVANT_VERIFICATION;

    /**
     * Construit le controlleur.
     * @param fpc controlleur de la fenete principale
     */
    public FrameProgressionTraitementController(FenetrePrincipaleUtilisateurController fpc) {
	super(fpc);
	view.setTableModel(model.getTableModel());
	setNext(new FrameLogErreursController(fpc));
    }
  /**{@inheritDoc}*/
    @Override
    public boolean canGoBack() {
	if(etatActuel==Etat.AVANT_VERIFICATION){
	    return true;
	    
	}
	if(etatActuel==Etat.VERIFICATION_ABANDONNEE||
		etatActuel==Etat.VERIFICATION_ACHEVEE){
	cleanFrame();
	view.setVisibleBarresProgression(false);
	fenetrePrincipale.setEtape(2);
	return false;
	}
	return true;
    }
/**{@inheritDoc}*/
    @Override
    public boolean canGoNext() {
	if(etatActuel==Etat.AVANT_VERIFICATION){
	    view.setVisibleBarresProgression(true);
	    lancerVerif();
	    fenetrePrincipale.setEnabledSuivant(false);
	    
	    return false;
	}
	return true;
    }

    @Override
    String getName() {
	return "FrameProgressionTraitement";
    }

    @Override
    protected FrameProgressionTraitement createView() {
	return new FrameProgressionTraitement();
    }

    @Override
    protected FrameProgressionTraitementModel createModel() {
	return new FrameProgressionTraitementModel(fenetrePrincipaleController
		.model.getSchema());
    }

    @Override
    protected void activate() {
	if(etatActuel==Etat.AVANT_VERIFICATION){
	    cleanFrame();
	    view.setVisibleBarresProgression(false);
	}else{
	    fenetrePrincipale.setEtape(3);
	}
	
    }

    private void cleanFrame() {
	fenetrePrincipaleController.model.getSchema().clean();
	fenetrePrincipale.setAnnuler(false);
	model.clean();
	nbFichiersVerifies=0;
	nbFichiersTotal = fenetrePrincipaleController.model.getSchema().getNbFichiersActifs();
	
	fenetrePrincipale.setEtape(2);
	etatActuel=Etat.AVANT_VERIFICATION;
    }

    @Override
    protected void deactivate() {
	fenetrePrincipale.setEnabledSuivant(true);
    }

    private class Task extends javax.swing.SwingWorker<Void, Void> implements INotifiable {
	private long nbCaracteresTotal, nbCaracteresLus;
	private final Schema s = fenetrePrincipaleController.model.getSchema();
	
	Task(){
	    super();
	}
	/**{@inheritDoc}*/
	@Override
	public Void doInBackground() {

	    //Initialize progress property.
	    setProgress(0);
	    s.setEltANotifier(this);
	    s.verifie();
	    return null;
	}
/**{@inheritDoc}*/
	@Override
	public void finFichier(String nomFichier, ETAT etat) {
	    firePropertyChange("finFichier", null, etat);
	}
/**{@inheritDoc}*/
	@Override
	public void caractereTraites(long nbCaracteres) {
	    nbCaracteresLus = nbCaracteres;
	    setProgress((int) (nbCaracteresLus * 100.0F / nbCaracteresTotal));

	}
/**{@inheritDoc}*/
	@Override
	public void debutFichier(String nomFichier) {
	    nomFichierEnCours = nomFichier;
	    nbCaracteresLus = 0;
	    setProgress(0);
	    nbCaracteresTotal = s.getFichier(nomFichier).getNbCaracteresTotal();
	    firePropertyChange("debutFichier", null, nomFichier);

	}
/**{@inheritDoc}*/
	@Override
	public void done() {
	    setProgress(100);
	    firePropertyChange("finSchema", null, null);
	}

	void pause() {
	    s.pause();
	}

	void resume() {
	    s.resume();
	}
	void cancel(){
	    s.cancel();
	    cancel(true);
	}
    }

  
    private void lancerVerif() {
	etatActuel=Etat.VERIFICATION_EN_COURS;
	fenetrePrincipale.setEtape(3);
	fenetrePrincipale.setEnabledPrecedent(false);
	fenetrePrincipale.setEnabledMiscButton(false);
	
	tacheVerifSchema = new Task();
	tacheVerifSchema.addPropertyChangeListener(this);
	tacheVerifSchema.execute();
	fenetrePrincipale.setAnnuler(true);

    }

/**{@inheritDoc}*/
    @Override
	public void propertyChange(PropertyChangeEvent evt) {
	if ("progress".equals(evt.getPropertyName())) {
	    final int progress = (Integer) evt.getNewValue();
	    view.setFileProgressValue(progress);
	    refreshView();

	}
	if ("finFichier".equals(evt.getPropertyName())) {
	    nbFichiersVerifies++;
	    refreshView();

	}
	if ("finSchema".equals(evt.getPropertyName())) {
	    finVerif();
	}

    }

    private void finVerif() {
	etatActuel=Etat.VERIFICATION_ACHEVEE;
	fenetrePrincipale.setEnabledPrecedent(true);
	fenetrePrincipale.setEnabledSuivant(true);
	fenetrePrincipale.setAnnuler(false);

    }

    /**
     * Raffraichit le modèle ainsi que l'affichage.
     */
    public void refreshView() {
	model.refresh();
	view.setTotalProgressValue(
		(int) (nbFichiersVerifies * 100.0F / nbFichiersTotal));
    }
    
/**{@inheritDoc}*/
    @Override
    public void annuler() {
	if (etatActuel==Etat.VERIFICATION_EN_COURS){
	tacheVerifSchema.pause();
	
	    switch (askQuestionDefaultNo(
		    resourceMap.getString("CancelTest.message"))) {
	    			//demande s'il faut annuler la verif
	    
		    case JOptionPane.YES_OPTION:
		
			abandonneVerif();
			refreshView();
			break;
		
		    case JOptionPane.NO_OPTION:
			
			tacheVerifSchema.resume();
			break;
			
		    default:

	    }
	}else{
	super.annuler();
	}
    }
private void abandonneVerif() {
	tacheVerifSchema.cancel();
	etatActuel=Etat.VERIFICATION_ABANDONNEE;
	
	fenetrePrincipale.setEnabledPrecedent(true);
	fenetrePrincipale.setEnabledSuivant(true);
	fenetrePrincipale.setAnnuler(false);
	refreshView();
    }
    
    /**{@inheritDoc}*/
    @Override
    public void quitter(){
	if (tacheVerifSchema!=null && etatActuel==Etat.VERIFICATION_EN_COURS){
	tacheVerifSchema.cancel();
	}else{
	    super.quitter();
	}
    }
    
   }
