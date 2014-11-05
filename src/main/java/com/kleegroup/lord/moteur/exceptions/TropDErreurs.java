package com.kleegroup.lord.moteur.exceptions;

/**
 * Excpetion lev�e si le nombre d'erreur rencontr�e est sup�rieur � un seuil d�fini. 
 *
 */
public class TropDErreurs extends ExceptionMoteur {

	private static final long serialVersionUID = -4967815460942236191L;

	protected long numLigne=0;

	/**
	 * @param numligne num�ro de la ligne de l'erreur
	 */
	public TropDErreurs (long numligne){
		numLigne=numligne;	
	}
	/**
	 * @return le num�ro de la ligne de l'erreur
	 */
	public long getLigneErreur(){
		return numLigne;
	}

}
