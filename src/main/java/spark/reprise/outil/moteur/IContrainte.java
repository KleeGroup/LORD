package spark.reprise.outil.moteur;

import spark.reprise.outil.moteur.exceptions.ExceptionMoteur;



/**
 *Implémenté par  tout objet qui effectue des vérifications.
 *Elle contient des fonctions qui permettent de lancer la vérification sur sun String,
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
	 * @return NO_ERR si aucune erreur n'est détéctée <br>
	 * Sinon, un objet Erreur contenant les informations nécessaires.
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
	 * renvoie une interpretation de la balise. voir {@link spark.reprise.outil.moteur.Interprete}
	 * @param balise la balise à interpreter 
	 * @param indice l'indice (ignoré dans ce cas)
	 * @return balise
	 * */
	String interprete(String balise, int indice);
	
	/**nettoie l'objet.
	 * Remet à zero les données spécifique utilisée lors de la dernière vérification
	 * pour pouvoir  réutiliser cet objet pour une nouvelle vérification 
	 * */
	void clean();
	
	
}
