package com.kleegroup.lord.moteur.contraintes;

import java.util.ArrayList;
import java.util.List;

import com.kleegroup.lord.moteur.ContrainteUniCol;

/**
 * v�rifie que les valeurs d'un champ sont conforme � une expression r�guli�re.
 * @author maazreibi
 *
 */
public class ContrainteRegex extends ContrainteUniCol {
	protected String regex = "";

	/**
	 * Construit la contrainte.
	 * @param regex l'expression r�guli�re qui valide les valeurs du champ
	 */
	public ContrainteRegex(String regex) {
		super();//ne fais rien.pour faire taire PMD
		this.regex = regex;
	}

	//	/** {@inheritDoc} */
	//	@Override
	//	public  String getMessageErreur() {
	//		return "La valeur ne suit pas le format d�fini.";		
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
	 * L'expression r�guli�re de test sert � tester les valeurs. Elle doit �tre
	 * une expression r�guli�re java valide. voir {@link java.util.regex.Pattern} 
	 * @return renvoie l'expression r�guli�re de test de la fonction.
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
