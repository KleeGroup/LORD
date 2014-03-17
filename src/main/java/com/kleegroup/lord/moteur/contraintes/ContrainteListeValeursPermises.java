package com.kleegroup.lord.moteur.contraintes;

import java.util.Arrays;
import java.util.List;

import com.kleegroup.lord.moteur.ContrainteUniCol;

/**
 * Cette contrainte vérifie que la valeur est parmi une liste prédéterminée.
 * Elle lève une erreur si ce n'est pas le cas.
 * @author maazreibi
 */
public class ContrainteListeValeursPermises extends ContrainteUniCol {
	private static final String SEPARATEUR = " , ";
	protected String [] valeursPermises;
	protected String listeValeurs="";

	/**
	 * Constructeur de la contrainte
	 * <br><br>
	 * La liste de valeurs permises peut être un array de String (<code> String[]</code>)
	 * ou bien les valeurs séparées par une virgule.
	 * <br><br>
	 * Les deux exemples suivants sont corrects et équivalents:<br>
	 * 1
	 * <code> String a[]=new String(){"1","2","3"}; <br>
	 * new ContrainteListeValeursPermises(a);<br></code>
	 * <br>
	 * 2
	 * <code>
	 * new ContrainteListeValeursPermises("1","2","3");<br></code>
	 * 
	 * @param valeursPermises liste des valeurs permises
	 */
	public ContrainteListeValeursPermises(String ... valeursPermises){
		super();
		this.valeursPermises=valeursPermises.clone();
		listeValeurs=valeursPermises[0];
		for(int i =1;i<valeursPermises.length;i++){
			listeValeurs+=SEPARATEUR+valeursPermises[i];
		}
		Arrays.sort(this.valeursPermises);
		
	}
	

	/**{@inheritDoc}*/
	@Override
	public  String interprete(String balise, int indice){
		if("liste_valeur".equals(balise)){
		    return listeValeurs;		
		}
		return super.interprete(balise, indice);
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public  boolean estConforme(final String valeur) {
		/* utilisation d'une boucle au lieu d'une recherche par dichotomie
		 * parce que c'est plus simple et aussi efficace, étant donné que la liste de valeur permises
		 * contient peu d'éléments (moins de 5 en général)
		 */

	/*	if (Arrays.binarySearch(valeur_permise, valeur)<0){					
			return false;
		}	
		return true;
		*/	
		for(int i=0;i<valeursPermises.length;i++){
			if (valeursPermises[i].equals(valeur)){
				return true;
			}
		}
		return false;
	}
	/**{@inheritDoc}*/
	@Override
	public List<String> getListeParam() {
		return Arrays.asList(valeursPermises);
	}


	/**renovie la liste des valeurs permises.La valeur v�rifi�e doit appartenir � cette liste.
	 *La vérification est sensible aux majuscles.
	 *@return la liste des valeurs permises séparées par une virgule.
	 */
	public String getSimpleListeValeurs() {
	    return listeValeurs;
	}

	/**{@inheritDoc}*/
	@Override
	public ContrainteUniCol copy() {
	    return new ContrainteListeValeursPermises(valeursPermises);
	}


}

