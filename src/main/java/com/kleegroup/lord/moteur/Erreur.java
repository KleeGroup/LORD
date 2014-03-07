package com.kleegroup.lord.moteur;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite, representante de toutes les erreurs.
 *
 */
public abstract class Erreur implements IErreur {

	/**
	 * Un objet "Constante" qui repr�sente un �tat sans erreur.
	 * Utilis� pour tester si une erreur est lev�e ou pas.
	 */
	private static final ErreurUniCol NOERR = null;
	protected IContrainte errOrigine;
	protected String[] errValeurs;
	protected long errLigne;
	protected String errMessage = "";
	protected List<String> refColonnes = new ArrayList<>();

	protected Erreur() {
		super();
	}

	/**
	 * @param contrainteParent La contrainte (unicol/multicol) qui g�n�re l'erreur
	 * @param numLigne le num�ro de la ligne de l'erreur
	 * @param valeurs les valeurs de l'erreur
	 */
	public Erreur(IContrainte contrainteParent, long numLigne, String[] valeurs) {
		errOrigine = contrainteParent;
		errLigne = numLigne;
		errValeurs = valeurs;
		Fichier parent = contrainteParent.getFichier();
		if (parent.hasColonneReference()) {
			refColonnes = parent.getValeursColRef(valeurs);
		}

	}

	/**
	 * Renvoie un objet "Constante" qui repr�sente un �tat sans erreur.
	 * Utilis� pour tester si une erreur est lev�e ou pas.
	 * @return l'erreur Constante qui reprs�sente l'�tat pas d'erreur
	 */
	public static ErreurUniCol pasDErreur() {
		return NOERR;
	}

	/** {@inheritDoc}*/
	@Override
	public long getErrLigne() {
		return errLigne;
	}

	/** {@inheritDoc}*/
	@Override
	public String getErrOrigine() {
		return errOrigine.getID();
		//return errOrigine;
	}

	/** renvoie les valeurs de la ligne.
	 * @return les valeurs de la ligne*/
	public String[] getErrValeurs() {
		return errValeurs.clone();//clone pour faire taire findbugs
	}

	/** {@inheritDoc}*/
	@Override
	public String getNomFichier() {
		return getContrainteParent().getNomFichier();
	}

	/** renvoie le fichier de l'erreur.
	 * @return le fichier de l'erreur*/
	public Fichier getFichier() {
		return getContrainteParent().getFichier();
	}

	/** 
	 * @return la contrainte parent*/
	public abstract IContrainte getContrainteParent();

	/**
	 * @return la liste des colonnes de l'erreur
	 */
	public abstract List<Colonne> getColonnes();

	/**{@inheritDoc}*/
	@Override
	public List<String> getReference() {
		return refColonnes;
	}

}
