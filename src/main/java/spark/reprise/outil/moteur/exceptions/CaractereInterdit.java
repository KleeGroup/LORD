package spark.reprise.outil.moteur.exceptions;

/**
 * Une exception levée si un champ contient une valeur interdite (" ou \n normalement).
 *
 */
public class CaractereInterdit extends ExceptionMoteur {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2896947029106163551L;
	
	protected String errColonne="",errValeur="";
	protected long numLigne=0,errColonnePos=0;
	/**
	 * 
	 * @param numLigne le numéro de la ligne de l'erreur
	 * @param errColonnePos position de la colonne de l'erreur
	 * @param errValeur valeur qui a déclanché l'erreur
	 */
	public CaractereInterdit(long numLigne,long errColonnePos,String errValeur ){
		this.errColonnePos=errColonnePos;
		this.errValeur=errValeur;
		this.numLigne=numLigne;
	}
	/**
	 * @return le nom de la colonne de l'erreur
	 */
	public long getErrColonne(){
		return errColonnePos;
	}
	/**
	 * @return la valeur qui a déclenché l'erreur
	 */
	public String getErrValeur(){
		return errValeur;
	}
	/**
	 * @return la ligne de l'erreur
	 */
	public long getNumLigne() {
	    return numLigne;
	}

}
