package com.kleegroup.lord.moteur;

import com.kleegroup.lord.moteur.exceptions.ExceptionMoteur;



/**
 *Impl�ment� par  tout objet qui effectue des v�rifications.
 *Elle contient des fonctions qui permettent de lancer la v�rification sur sun String,
 *des fonctions qui r�cup�rent les messages d'erreurs.<br>
 *Elle d�finit aussi des constantes � utiliser pour identifier les erreurs	
 *
 * @author maazreibi
 *
 
 */
public interface IContrainte  {
	
	/**
	 * V�rifie que la valeur est valide selon les contraintes pr�d�finies.
	 * @param numLigne le numero de la ligne a verifier
	 * @param valeurs les valeurs � v�rifier
	 * @return NO_ERR si aucune erreur n'est d�t�ct�e <br>
	 * Sinon, un objet Erreur contenant les informations n�cessaires.
	 * @throws ExceptionMoteur si la verification detecte une anomalie 
	 * bloquante(caractere interdit, trop d'erreurs,...)
	 * 
	 */
	IErreur verifie (long numLigne ,String [] valeurs) throws ExceptionMoteur;
	
	/**
	 * renvoie un identifiant unique de l'objet.
	 * @return l'identifiant de l'objet
	 */
	String getID();
	
	
	//void setColonneParent(Colonne c);
	//Colonne getColonneParent();
	//String getMessageErreur();
	
	/**
	 *@return le nom du fichier parent 
	 */
	String getNomFichier();

	/**
	 * @return le fichier parent de cette contrainte.
	 */
	Fichier getFichier();

	/**
	 * renvoie une interpretation de la balise. voir {@link com.kleegroup.lord.moteur.Interprete}
	 * @param balise la balise � interpreter 
	 * @param indice l'indice (ignor� dans ce cas)
	 * @return balise
	 * */
	String interprete(String balise, int indice);
	
	/**nettoie l'objet.
	 * Remet � zero les donn�es sp�cifique utilis�e lors de la derni�re v�rification
	 * pour pouvoir  r�utiliser cet objet pour une nouvelle v�rification 
	 * */
	void clean();
	
	
}
