package com.kleegroup.lord.moteur.contraintes;

import java.util.HashSet;
import java.util.Set;

// import org.apache.log4j.Logger;

import com.kleegroup.lord.moteur.ContrainteMultiCol;

/**
 * Cette contrainte vérifie l'unicité de plusieurs colonne d'un même fichier.
 * S'il y a des valeurs nulles, elles interdisent la répétition de la partie non
 * nulle. Autrement dit, une valeur nulle agit comme un joker (wildcard)
 * vis-à-vis de la "clef" de la ligne.
 */
public class ContrainteMultiColUniqueNullEqAny extends ContrainteMultiCol {

	// private static org.apache.log4j.Logger logAppli = Logger.getLogger(ContrainteMultiColUniqueNullEqAny.class);

	private class Value {
		private String[] values;
		
		public Value(String[] values) {
			this.values = new String[values.length];
			for (int i = 0; i < values.length; i++) {
				this.values[i] = values[i];
			}
		}
		
		public String[] getValues() {
			return values;
		}

		public int length() {
			return values.length;
		}
		
		@Override
		/**
		 * Returns a constant (force use of equals).
		 * Java manages hashcode collision by calling equals.
		 */
		public int hashCode() {
			return 0;
		}

		@Override
		/**
		 * This method consider null or empty values as a wildcard.
		 * Called with a N^2 cost.
		 */
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Value other = (Value) obj;
			if (other.length() != values.length)
				return false;
			boolean result = true;
			int i = 0;
			while (result && i < values.length) {
				String otherV = other.getValues()[i];
				String localV = values[i];
				result = result && ( otherV == null || otherV.isEmpty() || localV == null || localV.isEmpty() || otherV.equals(localV));
				i++;
			}
			// logAppli.trace("Value equals = " + result);
			return result;
		}
		
	}
	
	// Dictionary for duplicate detection
	protected Set<Value> dict = new HashSet<>();

	/**
	 * Construit une contrainte Unique sur plusieurs colonnes.
	 * 
	 * @param id		  	l'identifiant de la contrainte
	 * @param errTemplate	le template du message d'erreur
	 * @param nomfonction	Le nom de la fonction (ici non ambigu)
	 * @param cols			les colonnes définies uniques
	 */
	public ContrainteMultiColUniqueNullEqAny(String id, String errTemplate, String nomFonction, String[] cols) {
		super(id, errTemplate, cols);
	}

	@Override
	protected boolean estConforme(String[] valeurs) {
		Value value = new Value(valeurs);
		return dict.add(value);
	}

	/**
	 * renvoie le nom de la fonction de vérification.
	 * 
	 * @return le nom de la fonction de vérification
	 */
	@Override
	public String getNomFonction() {
		return "UniqueNullEqAny";
	}

	/** {@inheritDoc} */
	@Override
	public void clean() {
		dict.clear();
	}

	/**
	 * Teste si la fonction est valide. La fonction est valide, si elle existe,
	 * si tous ses paramètres sont de type String et si le nombre de colonnes
	 * désignés paramètres est égale au nombre des paramètres accepté par la
	 * fonction.
	 * 
	 * @param nomFonction
	 *            le nom de la fonction
	 * @param cols
	 *            les colonnes désignées paramètres de la fonction
	 * @return True si la fonction est valide, false sinon.
	 */
	public static boolean isValide(String nomFonction, String... cols) {
		// Requiert au moins deux colonnes
		return (cols.length > 1);
	}

}
