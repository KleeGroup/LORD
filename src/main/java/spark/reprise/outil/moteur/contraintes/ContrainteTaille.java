package spark.reprise.outil.moteur.contraintes;

import java.util.ArrayList;
import java.util.List;

import spark.reprise.outil.moteur.ContrainteUniCol;



/**
 * Cette contrainte sert à vérifier que les valeurs du champ possèdent une
 *  taille maximale inférieure à une taille limite déterminée.
 * @author maazreibi
 *
 */
public class ContrainteTaille extends ContrainteUniCol {
	protected int tailleMax=0;

	/**
	 * Construit une ContrainteTaille.
	 * @param taille la taille limite à ne pas dépasser
	 */
	public ContrainteTaille(int taille){
		super();//pour PMD
		tailleMax=taille;
	}
	/**{@inheritDoc}*/
	@Override
	public  String interprete(String balise, int indice){
		if("taille_champ".equals(balise)){
		    return Integer.toString(tailleMax);		
		}
		return super.interprete(balise, indice);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public  boolean estConforme(final String valeur) {
		return valeur.length()<=tailleMax;
	}
	/**{@inheritDoc}*/
	@Override
	public List<String> getListeParam() {
		List<String> params= new ArrayList<String>();
		params.add(Integer.toString(tailleMax));		
		return params;
	}

	/**
	 *La taille de la valeur vérifiée doit ête inférieure à la taille maximale définie ici.
	 * @return la taille maximale permise. 
	 */
	public int getTailleMax() {
	    return tailleMax;
	}
	/**
	 * La taille de la valeur vérifiée doit ête inférieure à la taille maximale définie ici.
	 * @param tailleMax la taille maximale permise. 
	 */
	public void setTailleMax(int tailleMax) {
	    this.tailleMax = tailleMax;
	}
	
	/**{@inheritDoc}*/
	@Override
	public ContrainteTaille copy() {
	    return new ContrainteTaille(tailleMax);
	}
	
}

