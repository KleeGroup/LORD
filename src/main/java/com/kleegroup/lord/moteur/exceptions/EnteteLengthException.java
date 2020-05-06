package com.kleegroup.lord.moteur.exceptions;

/**
 * Excpetion levee si le libellé des entêtes ne correspond pas à celui du schéma
 *
 */
public class EnteteLengthException extends ExceptionMoteur {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4967815460942236191L;
	
	protected int expectedValue;
	protected int actualValue;
	protected long numLigne=0;

	/**
	 * 
	 * @param numLigne le numéro de la ligne de l'erreur
	 * @param expected valeur attendue en entête
	 * @param errValeur valeur présente dans le fichier
	 */
	public EnteteLengthException(long numLigne, int expected, int actual ){
		this.expectedValue = expected;
		this.actualValue = actual;
		this.numLigne = numLigne;
	}
	/**
	 * @return le nom de la colonne de l'erreur
	 */
	public int getExpectedvalue(){
		return expectedValue;
	}
	
	/**
	 * @return la valeur qui a déclenché l'erreur
	 */
	public int getAcutalValue(){
		return actualValue;
	}
	
	/**
	 * @return la ligne de l'erreur
	 */
	public long getNumLigne() {
	    return numLigne;
	}


}
