package spark.reprise.outil.moteur.contraintes;

import java.util.ArrayList;
import java.util.List;

import spark.reprise.outil.moteur.ContrainteUniCol;



/**
 * Pseudo-contrainte qui n'effectue pas de vérification. 
 * 
 * @author maazreibi
 *
 */
public class ContrainteTRUE extends ContrainteUniCol {

	

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public  boolean estConforme(final String valeur) {
		return true;
	}
	/**{@inheritDoc}*/
	@Override
	public List<String> getListeParam() {
		return new ArrayList<String>();
	}
	
	/**{@inheritDoc}*/
	@Override
	public ContrainteTRUE copy() {
	    return new ContrainteTRUE();
	}
	
	
}

