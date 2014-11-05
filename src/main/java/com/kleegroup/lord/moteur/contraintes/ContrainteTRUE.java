package com.kleegroup.lord.moteur.contraintes;

import java.util.ArrayList;
import java.util.List;

import com.kleegroup.lord.moteur.ContrainteUniCol;

/**
 * Pseudo-contrainte qui n'effectue pas de v�rification. 
 * 
 * @author maazreibi
 *
 */
public class ContrainteTRUE extends ContrainteUniCol {

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean estConforme(final String valeur) {
		return true;
	}

	/**{@inheritDoc}*/
	@Override
	public List<String> getListeParam() {
		return new ArrayList<>();
	}

	/**{@inheritDoc}*/
	@Override
	public ContrainteTRUE copy() {
		return new ContrainteTRUE();
	}

}
