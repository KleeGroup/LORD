package spark.reprise.outil.moteur.contraintes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import spark.reprise.outil.moteur.ContrainteMultiCol;

/**
 * Cette contrainte v�rifie l'unicit� de plusieurs colonne d'un m�me fichier. 
 */
public class ContrainteMultiColUnique extends ContrainteMultiCol {

	protected Set<List<String>> dict = new HashSet<>();

	/**
	 * Construint une contrainte Unique sur plusieurs colonnes.
	 * @param id l'identifiant de la contrainte
	 * @param errTemplate le template du message d'erreur
	 * @param cols les colonnes d�finis uniques
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
