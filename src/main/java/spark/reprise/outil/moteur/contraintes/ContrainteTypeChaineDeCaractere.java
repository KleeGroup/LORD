package spark.reprise.outil.moteur.contraintes;

import java.util.ArrayList;
import java.util.List;

import spark.reprise.outil.moteur.ContrainteUniCol;

/**
 * Effectue des vérifications simple sur une chaine de caractères. Notamment, la 
 * chaine ne doit pas contenir les caractères '"' ou '\n'
 * @author maazreibi
 *
 */
public class ContrainteTypeChaineDeCaractere extends ContrainteUniCol {
	protected String[] caracteresInterdits = new String[] { "\n" };

	/** {@inheritDoc}*/
	@Override
	public boolean estConforme(final String valeur) {
		for (String c : caracteresInterdits) {
			if (valeur.contains(c)) {
				return false;
			}
		}
		return true;
	}

	/**{@inheritDoc}*/
	@Override
	public List<String> getListeParam() {
		return new ArrayList<>();
	}

	/**{@inheritDoc}*/
	@Override
	public boolean isContrainteType() {
		return true;
	}

	/**{@inheritDoc}*/
	@Override
	public ContrainteTypeChaineDeCaractere copy() {
		return new ContrainteTypeChaineDeCaractere();
	}

}
