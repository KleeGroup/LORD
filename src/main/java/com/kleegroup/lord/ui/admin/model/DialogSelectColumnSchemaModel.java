package com.kleegroup.lord.ui.admin.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.kleegroup.lord.moteur.Colonne;
import com.kleegroup.lord.moteur.Fichier;
import com.kleegroup.lord.moteur.Schema;
import com.kleegroup.lord.moteur.contraintes.ContrainteReference;
import com.kleegroup.lord.moteur.util.IHierarchieSchema;

/**
 * Modele pour la fenetre de selection des colonnes, dans le mode schema.
 */
public class DialogSelectColumnSchemaModel implements IColumnSeclect {

	private final List<Colonne> chosenElements = new ArrayList<>();
	private final SchemaTreeModel stm;

	private static class SchemaTreeModel implements javax.swing.tree.TreeModel {

		private final List<TreeModelListener> tml = new ArrayList<>();

		private List<Fichier> fichiers = new ArrayList<>();
		private final Map<String, List<Colonne>> listeColonnes = new HashMap<>();

		private final Fichier root = new Fichier("rooters", "");
		private final Fichier exclu;
		private final Colonne colonneExclue;
		private final Schema schema;

		SchemaTreeModel(Schema s, Fichier exclu, String colonneExclue) {
			this.schema = s;
			this.exclu = exclu;
			this.colonneExclue = exclu.getColonne(colonneExclue);
			buildColonneList(s, exclu);
		}

		private void buildColonneList(Schema s, Fichier f) {
			fichiers = new ArrayList<>(s.getFichiers());
			//		fichiers.remove(f);
			for (final Fichier fic : fichiers) {
				List<Colonne> listColonne = null;
				listColonne = new ArrayList<>(fic.getColonnes());
				if (fic == f) {
					listColonne.remove(colonneExclue);
				}
				listeColonnes.put(fic.getNom(), listColonne);
			}
		}

		/**{@inheritDoc}*/
		@Override
		public void addTreeModelListener(TreeModelListener l) {
			tml.add(l);

		}

		/**{@inheritDoc}*/
		@Override
		public Object getChild(Object parent, int index) {
			if (parent == getRoot()) {
				return getFichiers().get(index);
			}
			return ((IHierarchieSchema) parent).getChild(index);
		}

		/**{@inheritDoc}*/
		@Override
		public int getChildCount(Object parent) {
			if (parent == getRoot()) {
				return getFichiers().size();
			}
			return ((IHierarchieSchema) parent).getChildCount();
		}

		private List<Fichier> getFichiers() {
			return fichiers;
		}

		/**{@inheritDoc}*/
		@Override
		public int getIndexOfChild(Object parent, Object child) {
			if (parent == getRoot()) {
				return getFichiers().indexOf(child);
			}
			return ((IHierarchieSchema) parent).getChildCount();

		}

		/**{@inheritDoc}*/
		@Override
		public Object getRoot() {
			return root;
		}

		/**{@inheritDoc}*/
		@Override
		public boolean isLeaf(Object node) {
			return !(node == getRoot() || ((IHierarchieSchema) node).isFichier());
		}

		/**{@inheritDoc}*/
		@Override
		public void removeTreeModelListener(TreeModelListener l) {
			tml.remove(l);

		}

		/**{@inheritDoc}*/
		@Override
		public void valueForPathChanged(TreePath path, Object newValue) {/**/
		}

		/**
		 * filtre la liste des colonnes affich�s.
		 * @param filter le filtre des colonnes.
		 */

		public void setFilter(String filter) {
			buildColonneList(schema, exclu);
			if (!(filter == null)) {

				for (int j = 0; j < fichiers.size(); j++) {
					final Fichier f = fichiers.get(j);
					final List<Colonne> listeCol = listeColonnes.get(f.getNom());
					for (int i = 0; i < listeCol.size(); i++) {
						if (!(listeCol.get(i).getNom().toLowerCase().contains(filter) || listeCol.get(i).getDescription().toLowerCase().contains(filter))) {
							listeCol.remove(i);
							i--;
						}
					}
					if (listeCol.size() == 0) {
						fichiers.remove(f);
						j--;
					}

				}
			}

			notifyListeners();
		}

		private void notifyListeners() {
			final TreeModelEvent tme = new TreeModelEvent(getRoot(), new TreePath(getRoot()));
			for (final TreeModelListener tm : tml) {
				tm.treeStructureChanged(tme);
			}
		}

		/**
		 * D�finit le nom de la colonne � exclure de la liste des colonnes.
		 * @param columnName le nom de la colonne � exclure.
		 */
		/*
		public void setColonneExclue(String columnName) {
		colonneExclue=exclu.getColonne(columnName);
		
		}*/

	}

	/**
	 * Construit un modele qui affiche les colonnes des fichier du schema
	 * � l'exclusion d'une colonne d'un fichier particulier.
	 * @param s le schema dont on affiche les colonnes
	 * @param f le fichier en question
	 * @param colonneExclue  le nom de la colonne � exclure
	 */
	public DialogSelectColumnSchemaModel(Schema s, Fichier f, String colonneExclue) {
		stm = new SchemaTreeModel(s, f, colonneExclue);
	}

	/**{@inheritDoc}*/
	@Override
	public void selectElement(TreePath tp) {
		if (tp == null) {
			return;
		}
		if (tp.getPathCount() == 3) {
			final Colonne nomDesc = (Colonne) tp.getLastPathComponent();
			addColonne(nomDesc);
		}

	}

	private void addColonne(Colonne nomDesc) {
		if (chosenElements.size() == 0) {
			chosenElements.add(nomDesc);
		} else {
			chosenElements.set(0, nomDesc);
		}
	}

	/**{@inheritDoc}*/
	@Override
	public void clear() {
		chosenElements.clear();

	}

	/**{@inheritDoc}*/
	@Override
	public String getSelectedElements() {
		if (chosenElements.size() > 0) {
			return "[" + chosenElements.get(0).getFichierParent() + "].[" + chosenElements.get(0).getNom() + "]";
		}
		return "";
	}

	/**{@inheritDoc}*/
	@Override
	public List<Colonne> getSelectedEltsList() {
		return new ArrayList<>(chosenElements);
	}

	/**{@inheritDoc}*/
	@Override
	public TreeModel getTreeModel() {
		return stm;
	}

	/**{@inheritDoc}*/
	@Override
	public void setFilter(String text) {
		stm.setFilter(text.toLowerCase());

	}

	/**{@inheritDoc}*/
	@Override
	public void setInitialValue(List<?> value) {
		clear();
		if (value != null && value.size() > 0) {
			addColonne(((ContrainteReference) value.get(0)).getColonneParent());
		}
	}
}
