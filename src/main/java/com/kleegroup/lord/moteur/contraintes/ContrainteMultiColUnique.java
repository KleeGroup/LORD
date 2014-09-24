package com.kleegroup.lord.moteur.contraintes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kleegroup.lord.moteur.ContrainteMultiCol;

/**
 * Cette contrainte vérifie l'unicité de plusieurs colonne d'un même fichier. 
 */
public class ContrainteMultiColUnique extends ContrainteMultiCol {

	protected Set<List<String>> dict = new HashSet<>();

	/**
	 * Construint une contrainte Unique sur plusieurs colonnes.
	 * @param id l'identifiant de la contrainte
	 * @param errTemplate le template du message d'erreur
	 * @param cols les colonnes définies uniques
	 */
	public ContrainteMultiColUnique(String id, String errTemplate, String[] cols) {

		super(id, errTemplate, cols);
	}

	@Override
	protected boolean estConforme(String[] valeurs) {
		return dict.add(Arrays.asList(valeurs));
	}

	/**
	 * renvoie le nom de la fonction de verification.
	 * @return le nom de la fonction de verification
	 */
	@Override
	public String getNomFonction() {
		return "Unique";
	}

	/**{@inheritDoc}*/
	@Override
	public void clean() {
		dict.clear();
	}
}
