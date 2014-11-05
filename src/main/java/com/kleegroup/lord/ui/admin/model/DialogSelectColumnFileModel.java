package com.kleegroup.lord.ui.admin.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.kleegroup.lord.moteur.Colonne;
import com.kleegroup.lord.moteur.Fichier;

/**
 *  Modele de la fenetre DialogSelectColumn en mode fichier.
 */
public class DialogSelectColumnFileModel implements IColumnSeclect {
	private class FileTreeModel implements javax.swing.tree.TreeModel {

		private List<TreeModelListener> tml = new ArrayList<>();
		private List<Colonne> colonneFiltre = new ArrayList<>();

		private Fichier root;

		private String filter = "";

		FileTreeModel(Fichier f) {
			root = f;
			colonneFiltre.addAll(f.getColonnes());
		}

		/**{@inheritDoc}*/
		@Override
		public void addTreeModelListener(TreeModelListener l) {
			tml.add(l);

		}

		/**{@inheritDoc}*/
		@Override
		public Colonne getChild(Object parent, int index) {
			if (parent == getRoot()) {
				return colonneFiltre.get(index);
			}
			return null;
		}

		/**{@inheritDoc}*/
		@Override
		public int getChildCount(Object parent) {
			if (parent == getRoot()) {
				return colonneFiltre.size();
			}
			return 0;
		}

		/**{@inheritDoc}*/
		@Override
		public int getIndexOfChild(Object parent, Object child) {
			if (parent == getRoot()) {

				return colonneFiltre.indexOf(child);
			}
			return 0;

		}

		/**{@inheritDoc}*/
		@Override
		public Fichier getRoot() {
			return root;
		}

		/**{@inheritDoc}*/
		@Override
		public boolean isLeaf(Object node) {
			return !(node == getRoot());
		}

		/**{@inheritDoc}*/
		@Override
		public void removeTreeModelListener(TreeModelListener l) {
			tml.remove(l);

		}

		/**{@inheritDoc}*/
		@Override
		public void valueForPathChanged(TreePath path, Object newValue) {
			// ne fais rien
		}

		/**
		 * filtre la liste des colonnes affich�s.
		 * @param s le filtre des colonnes.
		 */
		public void setFilter(String s) {
			/*
			 *  ameliorer l'algorithme du filtre
			 *  
			 *l'algorithme est un simple algorithme brute qui devrait fonctionner pour les cas
			 *usuels.
			 *Sinon voir les trie , search trees 
			 * http://en.wikipedia.org/wiki/Trie
			 * http://en.wikipedia.org/wiki/Ternary_search_tree
			 * 
			 * */
			filter = s;
			colonneFiltre.clear();
			if ("".equals(filter)) {
				colonneFiltre.addAll(fichier.getColonnes());
			} else {
				for (Colonne c : fichier.getColonnes()) {
					if (c.getNom().toLowerCase().contains(filter) || (c.getDescription() != null && c.getDescription().toLowerCase().contains(filter))) {
						colonneFiltre.add(c);
					}

				}
			}
			notifyListeners();
		}

		private void notifyListeners() {
			TreeModelEvent tme = new TreeModelEvent(root, new TreePath(getRoot()));
			for (TreeModelListener tm : tml) {
				tm.treeStructureChanged(tme);
			}
		}

	}

	Fichier fichier;
	private FileTreeModel model;
	private List<Colonne> chosenElements = new ArrayList<>();

	/**
	 * @param f le fichier dont on veut afficher les colonnes.
	 */
	public DialogSelectColumnFileModel(Fichier f) {
		fichier = f;
		model = new FileTreeModel(fichier);
	}

	/**{@inheritDoc}*/
	@Override
	public TreeModel getTreeModel() {
		return model;
	}

	/**{@inheritDoc}*/
	@Override
	public void setFilter(String text) {
		model.setFilter(text.toLowerCase());

	}

	/**{@inheritDoc}*/
	@Override
	public void selectElement(TreePath tp) {
		if (tp == null) {
			return;
		}
		if (tp.getPathCount() == 2) {
			Colonne nomDesc = (Colonne) tp.getLastPathComponent();
			chosenElements.add(nomDesc);
		}
	}

	/**{@inheritDoc}*/
	@Override
	public String getSelectedElements() {

		return chosenElements.toString();
	}

	/**{@inheritDoc}*/
	@Override
	public List<Colonne> getSelectedEltsList() {
		return new ArrayList<>(chosenElements);
	}

	/**{@inheritDoc}*/
	@Override
	public void clear() {
		chosenElements.clear();

	}

	/**{@inheritDoc}*/
	@Override
	public void setInitialValue(List<?> value) {
		clear();
		for (Object s : value) {
			String nom = (String) s;
			Colonne c = fichier.getColonne(nom);

			if (c != null) {
				chosenElements.add(c);
			}
		}
	}

}
