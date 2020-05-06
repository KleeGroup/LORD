package com.kleegroup.lord.moteur.exceptions;

/**
 * Excpetion levee si le libell� des ent�tes ne correspond pas � celui du sch�ma
 *
 */
public class EnteteIncorrectException extends ExceptionMoteur {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4967815460942236191L;
	
	protected String expectedValue;
	protected String actualValue;
	protected long numLigne=0;

	/**
	 * 
	 * @param numLigne le num�ro de la ligne de l'erreur
	 * @param expected valeur attendue en ent�te
	 * @param errValeur valeur pr�sente dans le fichier
	 */
	public EnteteIncorrectException(long numLigne, String expected, String actual ){
		this.expectedValue = expected;
		this.actualValue = actual;
		this.numLigne = numLigne;
	}
	/**
	 * @return le nom de la colonne de l'erreur
	 */
	public String getExpectedvalue(){
		return expectedValue;
	}
	
	/**
	 * @return la valeur qui a d�clench� l'erreur
	 */
	public String getAcutalValue(){
		return actualValue;
	}
	
	/**
	 * @return la ligne de l'erreur
	 */
	public long getNumLigne() {
	    return numLigne;
	}


}
