package spark.reprise.outil.moteur.contraintes;

import java.util.Arrays;
import java.util.List;

import spark.reprise.outil.moteur.ContrainteUniCol;

/**
 * Cette contrainte v�rifie que la valeur est parmi une liste pr�d�termin�e.
 * Elle l�ve une erreur si ce n'est pas le cas.
 * @author maazreibi
 */
public class ContrainteListeValeursPermises extends ContrainteUniCol {
	private static final String SEPARATEUR = " , ";
	protected String [] valeursPermises;
	protected String listeValeurs="";

	/**
	 * Constructeur de la contrainte
	 * <br><br>
	 * La liste de valeurs permises peut �tre un array de String (<code> String[]</code>)
	 * ou bien les valeurs s�par�s par une virgule.
	 * <br><br>
	 * Les deux exemples suivants sont correctes et �quivalents:<br>
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
		/*utilisation d'une recherche lin�aire au lieu d'une recherche par dichotomie
		 * parce que c'est plus simple et plus efficace, surtout que la liste de valeur permises
		 * contient peu d'elements (moins de 5)
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
	 *La v�rification est sensible aux majuscles.
	 *@return la liste des valeurs permises s�par�es par une virgule.
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

