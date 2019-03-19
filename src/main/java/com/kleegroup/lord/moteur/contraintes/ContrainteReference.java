package com.kleegroup.lord.moteur.contraintes;

import java.util.ArrayList;
import java.util.List;

import com.kleegroup.lord.moteur.ContrainteUniCol;

/**
 *Cette contrainte permet de s'assurer que l'ensemble ses valeur du champ référant sont
 * incluses dans l'ensemble des valeurs du champ de référence.
 * <br><br>
 *  Elle utilise une contrainte ContrainteReferenceLookup pour obtenir les valeurs
 *   de rÃ©fÃ©rence.
 * @author maazreibi
 *
 */
public class ContrainteReference extends ContrainteUniCol {
	protected ContrainteReferenceLookup dict;

	/**
	 * Construit la contrainte de référence en utilisant une contrainte auxiliaire 
	 * de référence dict de type ContrainteReferenceLookup.<br><br>
	 * 
	 * Il vaut mieux éviter de créer cette contrainte Ã  la main.Il est préférable de passer par
	 * la méthode
	 * {@link com.kleegroup.lord.moteur.Fichier#addReference(String, com.kleegroup.lord.moteur.Colonne)}
	 * 
	 * @param dict  contrainte auxiliaire de référence
	 */
	public ContrainteReference(ContrainteReferenceLookup dict) {
		super();
		this.dict = dict;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean estConforme(final String valeur) {
		return dict.lookup(valeur);
	}

	/**{@inheritDoc}*/
	@Override
	public String interprete(String balise, int indice) {
		if ("fichier_reference".equals(balise)) {
			return dict.getColonneParent().getFichierParent().getNom();
		}
		return super.interprete(balise, indice);
	}

	/**{@inheritDoc}*/
	@Override
	public List<String> getListeParam() {
		List<String> params = new ArrayList<>();
		params.add(dict.getColonneParent().getFichierParent().getNom());
		params.add(dict.getColonneParent().getNom());
		return params;
	}

	/**
	 * @return le nom du fichier rÃ©fÃ©rencÃ© par cette contrainte.
	 */
	public String getFichierRef() {
		return dict.getNomFichier();
	}

	/**
	 * @return le nom de la colonne rÃ©fÃ©rencÃ©e par cette contrainte.
	 */
	public String getColonneRef() {
		return dict.getColonneParent().getNom();
	}

	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return "[" + getFichierRef() + "].[" + getColonneRef() + "]";
	}

	/**{@inheritDoc}*/
	@Override
	public ContrainteReference copy() {
		return new ContrainteReference(dict);
	}

}
