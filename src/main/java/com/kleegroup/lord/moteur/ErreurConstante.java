package com.kleegroup.lord.moteur;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Repr�sentre une erreur rencntr�e. Contient toutes les informations n�cessaires sous
 * forme de string.
 * 
 * Cet objet est cr�e par une des classes contraintes, quand une erreur est detect�e.
 */
public class ErreurConstante extends Erreur {

	private static final ResourceBundle RESOURCE_MAP = ResourceBundle.getBundle("resources.ContraintesMessagesErreur");

	private String errColonne = "", errValeur = "", errFichier = "";

	private ErreurConstante(long numLigne, String errMessage) {
		this(numLigne, errMessage, "", "", "");
	}

	/**
	 * Cree un objet erreur specifique.
	 * @param numLigne numero de la ligne de l'erreur
	 * @param errMessage le message d'erreur
	 * @param errColonne la colonne de l'erreur
	 * @param errValeur la valeur qui a cr�e l'erreur
	 * @param errFichier le fichier de l'erreur
	 */
	private ErreurConstante(long numLigne, String errMessage, String errColonne, String errValeur, String errFichier) {
		this.errLigne = numLigne;
		this.errMessage = errMessage;
		this.errColonne = errColonne;
		this.errValeur = errValeur;
		this.errFichier = errFichier;
	}

	/**
	 * Cree un objet erreur specifique.
	 * @param numLigne numero de la ligne de l'erreur
	 * @param errMessage le message d'erreur
	 * @param errColonne la colonne de l'erreur
	 * @param errValeur la valeur qui a cr�e l'erreur
	 * @param errFichier le fichier de l'erreur
	 * @param errReference reference d'erreur
	 */
	public ErreurConstante(long numLigne, String errMessage, String errColonne, String errValeur, String errFichier, List<String> errReference) {
		this.errLigne = numLigne;
		this.errMessage = errMessage;
		this.errColonne = errColonne;
		this.errValeur = errValeur;
		this.errFichier = errFichier;
		if (errReference != null) {
			this.refColonnes = errReference;
		}
	}

	/**
	 * Construit une ErreurConstante � partir d'une autre erreur. Les valeurs de
	 * l'autres erreurs sont �valu�es � ce moment et stock�es dans l'objet ErreurConstante
	 *  actuel.
	 * @param err l'erreur � convertir.
	 */
	public ErreurConstante(IErreur err) {
		this.errLigne = err.getErrLigne();
		this.errColonne = err.getErrColonne().intern();
		this.errFichier = err.getNomFichier().intern();
		this.errValeur = err.getErrValeur().intern();
		this.errMessage = err.getErrMessage().intern();
		this.refColonnes = err.getReference();
	}

	/**
	 * Si on rencontre un caractere interdit, on renvoie cette erreur.
	 * @param numLigne la ligne d'erreur
	 * @param errColonne la colonne de l'erreur
	 * @param errValeur la valeur contenant le caract�re interdit
	 * @return un objet ErreurConstante
	 */
	public static ErreurConstante errCaractereInterdit(long numLigne, String errColonne, String errValeur) {
		final String errMsg = RESOURCE_MAP.getString("errCaractereInterdit");
		return new ErreurConstante(numLigne, errMsg, errColonne, errValeur, "");
	}

	/**
	 * @param nomFichier le nom du fichier de l'erreur
	 * @param numLigne le num�ro de la ligne de l'erreur
	 * @return un objet ErreurConstante
	 */
	public static ErreurConstante errTropDErreurs(String nomFichier, long numLigne) {
		final String errMsg = RESOURCE_MAP.getString("errTropDErreurs");
		return new ErreurConstante(numLigne, errMsg, "", "", nomFichier);
	}

	/**
	 * @param numLigne num�ro de la ligne d'erreur
	 * @param rencontre le nombre de colonnes rencontres
	 * @param attendu le nombre de colonne attendu
	 * @return un objet ErreurConstante
	 */
	public static ErreurConstante errNbreColonnesIncorrect(long numLigne, int rencontre, int attendu) {
		String errMsg = RESOURCE_MAP.getString("errNbreColonnesIncorrect");
		errMsg = errMsg.replaceAll("_rencontre_", "(" + Integer.toString(rencontre) + ")");
		errMsg = errMsg.replaceAll("_attendu_", "(" + Integer.toString(attendu) + ")");
		return new ErreurConstante(numLigne, errMsg);
	}

	/**
	 * Cette erreur ne devrait pas etre rencontr�e. C'est un message d'erreur
	 * lev� si une exception de type MoteurException n'a pas �t� capt�e.
	 * @param nomFichier nom du fichier de l'erreur
	 * @return un objet ErreurConstante
	 */
	public static ErreurConstante errExceptionNonGeree(String nomFichier) {
		String errMsg = RESOURCE_MAP.getString("errExceptionNonGeree");
		errMsg = errMsg.replaceAll("_nom_fichier_", nomFichier);
		return new ErreurConstante(-1, errMsg);
	}

	/**
	 * Cette erreur ne devrait pas etre rencontr�e. C'est un message d'erreur
	 * lev� si une exception de type MoteurException n'a pas �t� capt�e.
	 * @param numLigne numero de ligbe
	 * @param message message d'erreur
	 * @param nomFichier nom du fichier de l'erreur
	 * @return un objet ErreurConstante
	 */
	public static ErreurConstante errExceptionMoteur(int numLigne, String message, String nomFichier) {
		String errMsg = RESOURCE_MAP.getString("errExceptionMoteur");
		errMsg = errMsg.replaceAll("_nom_fichier_", nomFichier);
		return new ErreurConstante(numLigne, errMsg, message, "", nomFichier);
	}

	/**
	 * non applicable pour une erreur constante.
	 * @return null
	 */
	@Override
	public IContrainte getContrainteParent() {
		return null;
	}

	/**{@inheritDoc}*/
	@Override
	public long getErrLigne() {
		return errLigne;
	}

	/**{@inheritDoc}*/
	@Override
	public String getErrColonne() {
		return errColonne;
	}

	/**{@inheritDoc}*/
	@Override
	public String getErrMessage() {
		return errMessage;
	}

	/**{@inheritDoc}*/
	@Override
	public String getErrValeur() {
		return errValeur;
	}

	/**{@inheritDoc}*/
	@Override
	public String getNomFichier() {
		return errFichier;
	}

	/**{@inheritDoc}*/
	@Override
	public List<Colonne> getColonnes() {
		//pour les erreurs constantes, on ne conserve pas les parents.
		//on renvoie donc une liste vide.
		return new ArrayList<>();
	}

	/**
	 * renvoie une erreur qui indique que le fichier n'a pas pu �tre lu.
	 * @param nomFichier le nom de fichier illisible.
	 * @return une erreur qui indique que le fichier n'a pas pu �tre lu.
	 */
	public static IErreur errLectureFicher(String nomFichier) {
		String errMsg = RESOURCE_MAP.getString("errLectureFicher");
		errMsg = errMsg.replaceAll("_nom_fichier_", nomFichier);
		return new ErreurConstante(-1, errMsg);
	}
}
