package com.kleegroup.lord.moteur;

import com.kleegroup.lord.moteur.exceptions.ExceptionMoteur;



/**
 *Implémentée par  tout objet qui effectue des vérifications.
 *Elle contient des fonctions qui permettent de lancer la vérification sur un String,
 *des fonctions qui récupèrent les messages d'erreurs.<br>
 *Elle définit aussi des constantes à utiliser pour identifier les erreurs	
 *
 * @author maazreibi
 *
 
 */
public interface IContrainte  {
	
	/**
	 * Vérifie que la valeur est valide selon les contraintes prédéfinies.
	 * @param numLigne le numero de la ligne a verifier
	 * @param valeurs les valeurs à vérifier
	 * @return NO_ERR si aucune erreur n'est détectée <br>
	 * Sinon, un objet Erreur contenant les informations nécessaires.
	 * @throws ExceptionMoteur si la vérification detecte une anomalie 
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
	 * @param balise la balise à interpreter 
	 * @param indice l'indice (ignoré dans ce cas)
	 * @return balise
	 * */
	String interprete(String balise, int indice);
	
	/**nettoie l'objet.
	 * Remet � zero les donn�es sp�cifique utilis�e lors de la derni�re v�rification
	 * pour pouvoir  r�utiliser cet objet pour une nouvelle v�rification 
	 * */
	void clean();
	
}
