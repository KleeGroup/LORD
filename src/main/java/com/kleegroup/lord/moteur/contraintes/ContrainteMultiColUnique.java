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
	 * Construit une contrainte Unique sur plusieurs colonnes.
	 * 
	 * @param id l'identifiant de la contrainte
	 * @param errTemplate le template du message d'erreur
	 * @param cols les colonnes définies uniques
	 */
	public ContrainteMultiColUnique(String id, String errTemplate, String nomFonction, String[] cols) {
		super(id, errTemplate, cols);
	}

	@Override
	protected boolean estConforme(String[] valeurs) {
		return dict.add(Arrays.asList(valeurs));
	}

	/**
	 * renvoie le nom de la fonction de vérification.
	 * @return le nom de la fonction de vérification
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
	
	/**
	 * Teste si la fonction est valide. La fonction est valide, si elle existe, si tous
	 * ses paramètres sont de type String et si le nombre de colonnes désignés paramètres
	 * est égale au nombre des paramètres accepté par la fonction.
	 * @param nomFonction le nom de la fonction
	 * @param cols les colonnes désignées paramètres de la fonction
	 * @return True si la fonction est valide, false sinon.
	 */
	public static boolean isValide(String nomFonction, String... cols) {
		// Requiert au moins deux colonnes
		return (cols.length > 1);
	}
		
}
