package com.kleegroup.lord.moteur.contraintes;

import java.util.ArrayList;
import java.util.List;

import com.kleegroup.lord.moteur.ContrainteUniCol;

/**
 * vérifie que les valeurs d'un champ sont conforme à une expression régulière.
 * @author maazreibi
 *
 */
public class ContrainteRegex extends ContrainteUniCol {
	protected String regex = "";

	/**
	 * Construit la contrainte.
	 * @param regex l'expression régulière qui valide les valeurs du champ
	 */
	public ContrainteRegex(String regex) {
		super();//ne fais rien.pour faire taire PMD
		this.regex = regex;
	}

	//	/** {@inheritDoc} */
	//	@Override
	//	public  String getMessageErreur() {
	//		return "La valeur ne suit pas le format défini.";		
	//	}

	/** {@inheritDoc} */
	@Override
	public boolean estConforme(final String valeur) {
		return valeur.matches(regex);
	}

	/**{@inheritDoc}*/
	@Override
	public List<String> getListeParam() {
		List<String> params = new ArrayList<>();
		params.add(regex);
		return params;
	}

	/**
	 * L'expression régulière de test sert à tester les valeurs. Elle doit être
	 * une expression régulière java valide. voir {@link java.util.regex.Pattern} 
	 * @return renvoie l'expression régulière de test de la fonction.
	 * */
	public String getRegex() {
		return regex;
	}

	/**{@inheritDoc}*/
	@Override
	public ContrainteRegex copy() {
		return new ContrainteRegex(regex);
	}
}
