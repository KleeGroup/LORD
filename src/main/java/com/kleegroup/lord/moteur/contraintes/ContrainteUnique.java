package com.kleegroup.lord.moteur.contraintes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kleegroup.lord.moteur.ContrainteUniCol;

/**
 * Vérifie que toutes les valeurs d'une colonne sont uniques.
 * @author maazreibi
 *
 */
public class ContrainteUnique extends ContrainteUniCol {
	protected Set<String> dict = new HashSet<>();

	/** {@inheritDoc} */
	@Override
	public boolean estConforme(final String valeur) {
		return dict.add(valeur); //le hashset renvoie faux si la valeur est deja presente,true sinon
	}

	/**{@inheritDoc}*/
	@Override
	public List<String> getListeParam() {
		return new ArrayList<>();
	}

	/**{@inheritDoc}*/
	@Override
	public void clean() {
		dict.clear();
	}

	/**{@inheritDoc}*/
	@Override
	public ContrainteUniCol copy() {
		return new ContrainteUnique();
	}

}
