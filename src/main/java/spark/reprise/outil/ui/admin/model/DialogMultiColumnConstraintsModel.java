package spark.reprise.outil.ui.admin.model;

import java.util.ArrayList;
import java.util.List;

import spark.reprise.outil.moteur.Colonne;
import spark.reprise.outil.moteur.ContrainteMultiCol;
import spark.reprise.outil.moteur.Fichier;
import spark.reprise.outil.moteur.contraintes.ContrainteMultiColFonctionsSpecifiques;

/**
 * Cette classe sert à représenter, construire et détruire les contraintes d'un fichier.<br>
 * <br>
 * Comme l'utilisateur peut annuler ses modifications, et que la construction d'une contrainte
 * se fait par étape, cette classe mémorise les choix de l'utilisateur, dans des objets
 * "commandes de contrainte" sans  réellement construire de contrainte.<br><br>
 * 
 * <br>
 *  La construction des contraintes a lieu dans la fonction {@link #createConstraints()},
 *  qui est appelée une fois que l'utilisateur a fini ses modifications et cliqué sur OK.
 * 
 */
public class DialogMultiColumnConstraintsModel {
	private static class CommandeConstraint {

		String id = "";
		String method = "";
		List<String> cols = new ArrayList<>();
		String errorMessage = "";

		CommandeConstraint() {
			id = "";
		}

		boolean isValide() {
			final boolean test = id != null && !("".equals(id)) && method != null && errorMessage != null && cols != null && cols.size() > 0;
			if ("Unique".equals(method)) {
				return test;
			}
			return test && ContrainteMultiColFonctionsSpecifiques.isValide(method, cols.toArray(new String[cols.size()]));

		}
	}

	private class TableModel extends javax.swing.table.AbstractTableModel {

		private static final long serialVersionUID = 4157087119873576473L;

		List<CommandeConstraint> commandes = new ArrayList<>();

		private final String nomColonne[] = new String[] { "Id verif", "Fonction", "Colonnes", "Message d'erreur", };

		private Fichier fichier = new Fichier("", "");
		private int constraintCount = 0;

		TableModel(Fichier f) {
			if (f != null) {
				setFichier(f);

			}

		}

		private void buildCommandesFromConstrainstList(Fichier f2) {
			commandes.clear();
			for (final ContrainteMultiCol cm : f2.getContraintesMultiCol()) {
				commandes.add(transform(cm));
			}
		}

		private CommandeConstraint transform(ContrainteMultiCol cm) {
			final CommandeConstraint cc = new CommandeConstraint();
			cc.id = cm.getID();
			cc.cols = new ArrayList<>(cm.getNomColonnes());
			cc.method = cm.getNomFonction();
			cc.errorMessage = cm.getErrTemplate();
			return cc;
		}

		/**{@inheritDoc}*/
		@Override
		public int getRowCount() {
			if (fichier != null) {
				return commandes.size();
			}
			return 0;
		}

		/**{@inheritDoc}*/
		@Override
		public int getColumnCount() {
			return nomColonne.length;
		}

		/**{@inheritDoc}*/
		@Override
		public String getColumnName(int columnIndex) {
			return nomColonne[columnIndex];
		}

		/**{@inheritDoc}*/
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		/**{@inheritDoc}*/
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowIndex < 0 || rowIndex >= commandes.size()) {
				return "";
			}
			final CommandeConstraint cc = commandes.get(rowIndex);
			switch (columnIndex) {
				case 0:
					return cc.id;
				case 1:
					return cc.method;
				case 2:
					return cc.cols;
				case 3:
					return cc.errorMessage;
				default:
					return "";
			}
		}

		/**{@inheritDoc}*/
		@SuppressWarnings("unchecked")
		@Override
		public void setValueAt(Object value, int row, int col) {
			final CommandeConstraint cc = commandes.get(row);
			switch (col) {
				case 0:
					cc.id = (String) value;
					break;
				case 1:
					cc.method = (String) value;
					break;
				case 2:
					final List<String> c = trans((List<Colonne>) value);
					if (c.size() > 0) {
						cc.cols = c;
					}
					break;
				case 3:
					cc.errorMessage = (String) value;
					break;
				default:
			}
			fireTableCellUpdated(row, col);

		}

		private List<String> trans(List<Colonne> value) {
			final List<String> liste = new ArrayList<>(value.size());
			for (final Object c : value) {
				liste.add(((Colonne) c).getNom());
			}
			return liste;
		}

		private void setFichier(Fichier f2) {
			fichier = f2;
			buildCommandesFromConstrainstList(fichier);
			fireTableDataChanged();
		}

		/**
		 * Ajoute une commande de contrainte.
		 */
		public void addConstraint() {
			constraintCount++;
			final CommandeConstraint cc = new CommandeConstraint();
			cc.id = "id" + constraintCount;
			commandes.add(cc);
			fireTableRowsInserted(getRowCount(), getRowCount());

		}

		/**
		 * @return la position de la première commande de contrainte invalide.
		 */
		public int getFirstInvalideConstrainte() {
			for (int j = 0; j < commandes.size(); j++) {
				if (!(commandes.get(j).isValide())) {
					return j;
				}
			}
			return -1;
		}

		void createConstraints() {
			createConstraint();
		}

		/**
		 * supprime la commande de contrainte à la postion pos.
		 * @param pos la position de la commande à supprimer.
		 */
		public void deleteConstraint(int pos) {
			if (pos >= 0 && pos < commandes.size()) {
				commandes.remove(pos);
				fireTableRowsDeleted(pos, pos);
			}

		}

	}

	private final TableModel tableModel;
	private final Fichier f;

	/**
	 * @param f le fichier modèle
	 */
	public DialogMultiColumnConstraintsModel(Fichier f) {
		tableModel = new TableModel(f);
		this.f = f;
	}

	/**
	 * @return la liste des contraintes du fichier.
	 */
	public TableModel getTableModel() {
		return tableModel;
	}

	/**
	 * Crée une nouvelle "Commande" de contrainte. la commande est initialement vide.
	 */
	public void addConstraintCommand() {
		tableModel.addConstraint();

	}

	/**
	 * modifie la contrainte à la ligne row. le row détermine la propriété de la contrainte qui sera
	 * modifiée( id, message d'erreur , nom de fonction, ...).
	 * @param value la nouvelle valeur de la colonne.
	 * @param row la position de la contrainte à modifier.
	 * @param col la propriété de la contrainte à modifier.
	 */
	public void changeFileValue(Object value, int row, int col) {
		tableModel.setValueAt(value, row, col);

	}

	/**
	 * @return la liste des noms de méthode disponibles.
	 */
	public String[] getPossibleMethodNames() {
		final List<String> pmn = new ArrayList<>(ContrainteMultiColFonctionsSpecifiques.getMethods());
		pmn.add("Unique");
		return pmn.toArray(new String[pmn.size()]);
	}

	/**
	 * @return le fichier dont la modification est en cours.
	 */
	public Fichier getCurrentFichier() {
		return f;
	}

	/**
	 * L'utilisateur a cliqué sur OK. on prend les "commandes" de l'utilisateur
	 * et on construit.
	 */
	public void createConstraints() {
		tableModel.createConstraints();
	}

	/**
	 * @return la ligne de la premiere contrainte invalide
	 * (nbre de parametre incorrect, méthode inexsitante, ...) ou -1 si
	 * toutes les contraintes sont valides.
	 */
	public int getFirstInvalidConstraint() {
		return tableModel.getFirstInvalideConstrainte();
	}

	/**
	 * supprime une contrainte.
	 * @param selectedRow la position de la contrainte à supprimer.
	 */
	public void deleteConstraint(int selectedRow) {
		tableModel.deleteConstraint(selectedRow);

	}

	void createConstraint() {
		f.getContraintesMultiCol().clear();
		for (final CommandeConstraint com : tableModel.commandes) {
			if (com.isValide()) {
				String cols[] = new String[0];
				cols = com.cols.toArray(cols);
				f.addContrainteMultiCol(com.id, com.errorMessage, com.method, cols);

			}
		}
	}

}
