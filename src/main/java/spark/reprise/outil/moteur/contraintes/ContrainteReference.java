package spark.reprise.outil.moteur.contraintes;

import java.util.ArrayList;
import java.util.List;

import spark.reprise.outil.moteur.ContrainteUniCol;

/**
 *Cette contrainte permet de s'assurer que l'ensemble ses valeur du champ référant sont
 * incluses dans l'ensemble des valeurs du champ de référence.
 * <br><br>
 *  Elle utilise une contrainte ContrainteReferenceLookup pour obtenir les valeurs
 *   de référence.
 * @author maazreibi
 *
 */
public class ContrainteReference extends ContrainteUniCol {
	protected ContrainteReferenceLookup dict;

	/**
	 * Construit la contrainte de référence en utilisant une contrainte auxiliaire 
	 * de référence dict de type ContrainteReferenceLookup.<br><br>
	 * 
	 * Il vaut mieux évite de créer cette contrainte à la main.Il est préférable de passer par
	 * la méthode
	 * {@link spark.reprise.outil.moteur.Fichier#addReference(String, spark.reprise.outil.moteur.Colonne)}
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
	 * @return le nom du fichier référencé par cette contrainte.
	 */
	public String getFichierRef() {
		return dict.getNomFichier();
	}

	/**
	 * @return le nom de la colonne référencée par cette contrainte.
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
