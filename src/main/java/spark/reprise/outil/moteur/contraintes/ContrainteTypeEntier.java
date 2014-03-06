package spark.reprise.outil.moteur.contraintes;

import java.util.ArrayList;
import java.util.List;

import spark.reprise.outil.moteur.ContrainteUniCol;

/**
 * Vérifie que les valeurs du champ sont des entiers.
 * @author maazreibi
 *
 */
public class ContrainteTypeEntier extends ContrainteUniCol {

	/** {@inheritDoc} */
	@Override
	public boolean estConforme(final String valeur) {
		try {
			Integer.parseInt(valeur);

		} catch (NumberFormatException e) {
			return false;
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
	public ContrainteUniCol copy() {
		return new ContrainteTypeEntier();
	}

}
