package com.kleegroup.lord.moteur.exceptions;

/**
 * Excpetion levée si le nombre d'erreur rencontré est supérieur à un seuil défini. 
 *
 */
public class TropDErreurs extends ExceptionMoteur {

	private static final long serialVersionUID = -4967815460942236191L;

	protected long numLigne=0;

	/**
	 * @param numligne numéro de la ligne de l'erreur
	 */
	public TropDErreurs (long numligne){
		numLigne=numligne;	
	}
	/**
	 * @return le numéro de la ligne de l'erreur
	 */
	public long getLigneErreur(){
		return numLigne;
	}

}
