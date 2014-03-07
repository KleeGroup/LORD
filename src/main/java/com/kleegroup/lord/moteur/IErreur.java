package com.kleegroup.lord.moteur;

import java.util.List;

/**
 * Interface qui fournit les m�thode communes � tous les objets Erreur.
 *
 */
public interface IErreur {


	/**
	 * Indique le nom de la colonne d'erreur .
	 * @return le nom de la colonne d'erreur 
	 */
	String getErrColonne();

	/**
	 * Indique le num�ro de la ligne d'erreur .
	 * @return le num�ro de la ligne d'erreur 
	 */
	long getErrLigne();

	/**
	 * Indique le message d'erreur .
	 * @return le message d'erreur 
	 */
	String getErrMessage();

	/**
	 * Indique l'identifiant de la contrainte d'origine de l'erreur .
	 * @return l'identifiant de la contrainte d'origine de l'erreur 
	 */
	String getErrOrigine();

	/**
	 * Indique la valeur qui a caus� l'erreur .
	 * @return la valeur qui a caus� l'erreur 
	 */
	String getErrValeur();


	/**
	 * @return le nom du fichier de l'erreur
	 */
	String getNomFichier();
	
	/**
	 * @return la liste des valeurs des colonnes de reference.
	 */
	List<String> getReference();

}
