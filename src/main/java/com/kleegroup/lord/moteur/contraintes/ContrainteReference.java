package com.kleegroup.lord.moteur.contraintes;

import java.util.ArrayList;
import java.util.List;

import com.kleegroup.lord.moteur.ContrainteUniCol;

/**
 *Cette contrainte permet de s'assurer que l'ensemble ses valeur du champ r�f�rant sont
 * incluses dans l'ensemble des valeurs du champ de r�f�rence.
 * <br><br>
 *  Elle utilise une contrainte ContrainteReferenceLookup pour obtenir les valeurs
 *   de r�f�rence.
 * @author maazreibi
 *
 */
public class ContrainteReference extends ContrainteUniCol {
	protected ContrainteReferenceLookup dict;

	/**
	 * Construit la contrainte de r�f�rence en utilisant une contrainte auxiliaire 
	 * de r�f�rence dict de type ContrainteReferenceLookup.<br><br>
	 * 
	 * Il vaut mieux �vite de cr�er cette contrainte � la main.Il est pr�f�rable de passer par
	 * la m�thode
	 * {@link com.kleegroup.lord.moteur.Fichier#addReference(String, com.kleegroup.lord.moteur.Colonne)}
	 * 
	 * @param dict  contrainte auxiliaire de r�f�rence
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
	 * @return le nom du fichier r�f�renc� par cette contrainte.
	 */
	public String getFichierRef() {
		return dict.getNomFichier();
	}

	/**
	 * @return le nom de la colonne r�f�renc�e par cette contrainte.
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
