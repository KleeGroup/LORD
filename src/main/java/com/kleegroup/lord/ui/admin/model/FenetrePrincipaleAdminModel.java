package com.kleegroup.lord.ui.admin.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.xml.bind.JAXBException;

import com.kleegroup.lord.moteur.Colonne;
import com.kleegroup.lord.moteur.ContrainteUniCol;
import com.kleegroup.lord.moteur.Fichier;
import com.kleegroup.lord.moteur.Schema;
import com.kleegroup.lord.moteur.contraintes.ContrainteReference;
import com.kleegroup.lord.moteur.contraintes.ContrainteTypeChaineDeCaractere;
import com.kleegroup.lord.moteur.util.IHierarchieSchema;
import com.kleegroup.lord.moteur.util.SeparateurDecimales;
import com.kleegroup.lord.ui.common.model.FileTreeModel;

/**
 * Modele de la fenetre principale de la fenetre administrateur.
 */
public class FenetrePrincipaleAdminModel {

	private Schema schema = new Schema();
	private FileTreeModel treeModel = new FileTreeModel(schema);
	private boolean newSchema = true;
	private boolean isSchemaModified = false;
	private String fullSchemaPath = "";
	private final List<TableModel> tmlist = new ArrayList<>();
	private TableModel currentFile;
	private boolean savedModif;

	private static class TableModel extends javax.swing.table.AbstractTableModel {
		/**
		 *
		 */
		private static final long serialVersionUID = 4157087119873576473L;

		private final String nomColonne[] = new String[] { "Colonne", "Description", "O/F", "Ref", "Unique", "Type", "Format", "Taille Max", "Valeurs permises", "Champ de contr�le", };

		private Fichier f = new Fichier("", "");

		TableModel(Fichier f) {
			if (f != null) {
				this.f = f;
			}
		}

		/**{@inheritDoc}*/
		@Override
		public int getRowCount() {
			if (f != null) {
				return f.getNbColonnes();
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
			if (columnIndex == 3 || columnIndex == 4) {
				return Boolean.class;
			}
			return String.class;
		}

		/**{@inheritDoc}*/
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			//separ� en 2 methode pour eviter FanOutComplecity de CheckStyle
			final Colonne c = f.getColonne(rowIndex);
			switch (columnIndex) {
				case 0://nom
					return c.getNom();
				case 1://description
					return c.getDescription();
				case 2://obligatoire ou facultative
					return c.getPresenceValeur();
				case 3://vide ou pas
					return c.isColonneReference();
				case 4://unique
					return checkUnique(c);
				default://
					return returnPart2(rowIndex, columnIndex);

			}

		}

		private Object returnPart2(int rowIndex, int columnIndex) {
			//separ� en 2 methode pour eviter FanOutComplecity de CheckStyle
			final Colonne c = f.getColonne(rowIndex);

			switch (columnIndex) {
				case 5://return type
					return checkType(c);
				case 6://format
					return checkFormat(c);
				case 7://taille max
					return checkTaille(c);
				case 8://Valeurs permises
					return checkValeursPermises(c);
				case 9://champ de controle
					return checkReference(c);
				default://
					return "";
			}
		}

		private String checkFormat(Colonne c) {
			return c.getFormat();

		}

		private String checkTaille(Colonne c) {
			final int taille = c.getTaille();
			if (taille == Integer.MAX_VALUE) {
				return "";
			}
			return Integer.toString(taille);
		}

		private ContrainteReference checkReference(Colonne c) {
			return c.getContrainteReference();
		}

		private String checkValeursPermises(Colonne c) {
			return c.getListeValeursPermise();
		}

		private boolean checkUnique(Colonne c) {
			return c.isValeursUniques();

		}

		private String checkType(Colonne c) {
			return c.getType();
		}

		void moveUp(int selectedRow) {
			f.moveColonneUp(selectedRow);
			fireTableRowsUpdated(selectedRow - 1, selectedRow);
		}

		void moveDn(int selectedRow) {
			f.moveColonneDn(selectedRow);
			fireTableRowsUpdated(selectedRow, selectedRow + 1);

		}

		void deleteColonne(int selectedRow) {
			if (selectedRow >= 0 && selectedRow < getRowCount()) {
				f.deleteColonne(selectedRow);
				fireTableRowsDeleted(selectedRow, selectedRow);
			}

		}

		/**{@inheritDoc}*/
		@Override
		public void setValueAt(Object value, int row, int col) {
			final Colonne c = f.getColonne(row);
			switch (col) {
				case 0:
					final String nom = (String) value;
					if (f.getColonne(nom) == null) {
						c.setNom((String) value);
					}
					break;
				case 1:
					c.setDescription((String) value);
					break;
				case 2:
					c.setPresenceValeur(Colonne.PRESENCE.valueOf((String) value));
					fireTableCellUpdated(row, col + 1);
					break;

				case 3:
					c.setColonneReference((Boolean) value);
					fireTableCellUpdated(row, col - 1);
					break;

				case 4:
					createContrainteUnique((Boolean) value, c);
					break;
				default:
					setValueAtPart2(value, row, col);
			}
			fireTableCellUpdated(row, col);

		}

		private void setValueAtPart2(Object value, int row, int col) {
			final Colonne c = f.getColonne(row);
			switch (col) {
				case 5:
					createContrainteType((String) value, c);
					fireTableCellUpdated(row, col + 1);
					break;
				case 6:
					createContrainteFormat((String) value, c);
					break;
				case 7:
					createContrainteTaille((String) value, c);
					break;
				case 8:
					createContrainteListeValeursPermises((String) value, c);
					break;
				case 9:
					final List<Colonne> tmpCol = new ArrayList<>();
					for (final Colonne o : (List<Colonne>) value) {
						tmpCol.add(o);
					}
					createContrainteReference(tmpCol, c);
					break;
				default:
			}
		}

		private void createContrainteFormat(String format, Colonne c) {
			c.createContrainteFormat(format);
		}

		private void createContrainteReference(List<Colonne> liste, Colonne c) {
			c.removeContraintes(ContrainteReference.class);
			if (liste.size() > 0) {
				f.addReference(c.getNom(), liste.get(0));
			}
		}

		private void createContrainteListeValeursPermises(String value, Colonne c) {
			c.setListeValeursPermises(value);
		}

		private void createContrainteTaille(String val, Colonne c) {
			c.setTaille(val);
		}

		private void createContrainteType(String value, Colonne c) {
			c.setType(value);
		}

		private void createContrainteUnique(boolean value, Colonne c) {
			c.setUnique(value);
		}

		void setFichier(Fichier f2) {
			f = f2;
			fireTableDataChanged();
		}

		String getNamePrefix() {
			return f.getPrefixNom();
		}

		String getExtension() {
			return f.getExtension();
		}

		void setFileNamePrefix(String text) {
			f.setPrefixNom(text);

		}

		void setFileExtension(String text) {
			f.setExtension(text);

		}

		int getErrorLimit() {
			return f.getSeuilAbandon();
		}

		int getGroupNumber() {
			return f.getGroupe();
		}

		int getHeaderLinesCount() {
			return f.getNbLignesEntete();
		}

		void setFileGroup(int grp) {
			f.setGroupe(grp);

		}

		void setHeaderLinesCount(int nb) {
			f.setNbLignesEntete(nb);
		}

		void setFileErrorLimit(int nb) {
			f.setSeuilAbandon(nb);
		}

		void addColonne(int selectedRow) {
			final String nomCol = f.getNomColonneLibre();
			int pos = selectedRow;
			if (pos < 0) {
				pos = getRowCount();
			}
			f.addColonne(new Colonne(nomCol), pos);
			f.getColonne(nomCol).addContrainte(new ContrainteTypeChaineDeCaractere());
			fireTableRowsInserted(pos, pos);
		}

		Fichier getFichier() {
			return f;
		}

		int getMultiColContraintesCount() {
			return f.getContraintesMultiCol().size();
		}

	}

	/**
	* Cr�e un nouveau modele et lui ajoute un fichier.
	*/
	public FenetrePrincipaleAdminModel() {
		//      addFichier(null);
	}

	/**
	 * Affiche les details d'un fichier.
	 * @param f le fichier dont afficher les d�tails
	 * @return un objet {@link javax.swing.table.TableModel} qui represente le fichier.
	 */
	public TableModel getTableModel(Fichier f) {
		if (currentFile == null) {
			currentFile = new TableModel(f);
		}
		currentFile.setFichier(f);
		return currentFile;
	}

	/**
	 * @return les detail du fichier selectionn� par defaut (le premier fichier
	 *  du schema ou un fichier vide)
	 */
	public TableModel getTableModel() {
		if (schema != null && schema.getFichiers().size() > 0) {
			return getTableModel(schema.getFichiers().get(0));
		}
		return new TableModel(null);
	}

	/**
	 * Charge un fichier de configuration.
	 * @param path le chemin d'acc�s du fichier.
	 * @throws FileNotFoundException Exception si le fichier n'est pas trouv�.
	 * @throws JAXBException Exception si le fichier est invalide.
	 */
	public void loadSchema(String path) throws FileNotFoundException, JAXBException {
		final File in = new File(path);
		fullSchemaPath = in.getAbsolutePath();
		schema = Schema.fromXML(new java.io.FileInputStream(in));
		treeModel = new FileTreeModel(schema);
		tmlist.clear();
		getTableModel();
		newSchema = false;
		isSchemaModified = false;
	}

	/**
	 * @return liste des fichiers du schema.
	 */
	public TreeModel getTreeModel() {
		return treeModel;
	}

	//    /**
	//     * d�place le fichier selectionn� vers le haut.
	//     * @param node le fichier � d�placer.
	//     */
	//    public void moveFileUp(Fichier node) {
	//	setModified();
	//	treeModel.moveUp(node);
	//    }

	private void setModified() {
		isSchemaModified = true;
	}

	/**
	 * d�place la colonne � la position selectedRow vers le bas.
	 * @param selectedRow la posotion de la colonne � d�placer.
	 */
	public void moveColDn(int selectedRow) {
		if (currentFile != null) {
			setModified();
			currentFile.moveDn(selectedRow);
		}

	}

	/**
	 * d�place la colonne � la position selectedRow vers le haut.
	 * @param selectedRow la posotion de la colonne � d�placer.
	 */
	public void moveColUp(int selectedRow) {
		if (currentFile != null) {
			setModified();
			currentFile.moveUp(selectedRow);
		}
	}

	/**
	 * supprime la colonne � la position selectedRow.
	 * @param selectedRow la posotion de la colonne � supprimer.
	 */
	public void deleteColonne(int selectedRow) {
		if (currentFile != null) {
			setModified();
			currentFile.deleteColonne(selectedRow);
		}
	}

	/**
	 * modifie les propri�t� de la colonne � la position row.
	 * @param value la nouvelle valeur de la propri�t� � modifier.
	 * @param row la colonne � modifier.
	 * @param col la propri�t� � modifier.
	 */
	public void changeFileValue(Object value, int row, int col) {
		setModified();
		currentFile.setValueAt(value, row, col);

	}

	/**
	 * @return liste des valeurs � afficher pour la colonne pr�senceValeur.
	 */
	public String[] getObFacValues() {
		return new String[] { Colonne.PRESENCE.FACULTATIVE.toString(), Colonne.PRESENCE.OBLIGATOIRE.toString(), Colonne.PRESENCE.INTERDITE.toString(), };
	}

	/**
	 * @return liste des Contrainte de type disponibles.
	 */
	public String[] getTypeValues() {
		return Colonne.getTypesPossibles();

	}

	/**
	 * sauvegarde le schema dans le fichier res.
	 * @param res le fichier de configuration.
	 * @throws IOException en cas d'erreur d'�criture.
	 */
	public void saveFile(File res) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(res);
			schema.toXml(fos);
			isSchemaModified = false;
		} catch (final JAXBException e) {
			/**Ne fais rien*/
		} finally {
			if (fos != null) {
				fullSchemaPath = res.getAbsolutePath();
				fos.close();
			}
		}

	}

	/**
	 * supprime un fichier du sch�ma.
	 * @param selection le path du fichier � supprimer.
	 * @return le path de la nouvelle s�l�ction.
	 */
	public TreePath deleteFile(TreePath selection) {
		setModified();
		return treeModel.removeFile(selection);

	}

	/**
	 * @return le prefixNom du fichier s�lectionn�.
	 */
	public String getCurrentFileNamePrefix() {
		if (currentFile == null) {
			return "";
		}
		return currentFile.getNamePrefix();
	}

	/**
	 * @return l'extension du fichier s�lectionn�.
	 */
	public String getCurrentFileExtension() {

		if (currentFile == null) {
			return "";
		}
		return currentFile.getExtension();
	}

	/**
	 * @param prefixNom le prefixNom du fichier s�lectionn�.
	 */
	public void setCurrentFileNamePrefix(String prefixNom) {
		if (!prefixNom.equals(getCurrentFileNamePrefix())) {
			setModified();
		}
		currentFile.setFileNamePrefix(prefixNom);

	}

	/**
	 * @param ext l'extension du fichier s�lectionn�.
	 */
	public void setCurrentFileExtension(String ext) {
		if (!ext.equals(getCurrentFileExtension())) {
			setModified();
		}
		currentFile.setFileExtension(ext);

	}

	/**
	 * @return seuil d'erreur du fichier s�lectionn�.
	 */
	public int getCurrentFileErrorLimit() {
		return currentFile.getErrorLimit();
	}

	/**
	 * @return num�ro de groupe du fichier s�lectionn�.
	 */
	public int getCurrentFileGroupNumber() {
		return currentFile.getGroupNumber();
	}

	/**
	 * @return nbre de lignes d'entete du fichier s�lectionn�.
	 */
	public int getCurrentFileHeaderLinesCount() {
		return currentFile.getHeaderLinesCount();
	}

	/**
	 * @param grp numero de groupe du fichier s�lectionn�.
	 */
	public void setCurrentFileGroup(int grp) {
		if (grp != getCurrentFileGroupNumber()) {
			setModified();
		}
		currentFile.setFileGroup(grp);

	}

	/**
	 * @param nb nbre de lignes d'entete du fichier s�lectionn�.
	 */
	public void setCurrentFileHeaderLinesCount(int nb) {
		if (nb != getCurrentFileHeaderLinesCount()) {
			setModified();
		}
		currentFile.setHeaderLinesCount(nb);
	}

	/**
	 * @return nbre de contraintes multicolonnes du fichier s�lectionn�.
	 */
	public int getCurrentFileMultiColContraintesCount() {
		return currentFile.getMultiColContraintesCount();
	}

	/**
	 * @param nb le seuil d'erreur du fichier s�lectionn�.
	 */
	public void setCurrentFileErrorLimit(int nb) {
		setModified();
		currentFile.setFileErrorLimit(nb);
	}

	/**
	 * rajoute une colonne au fichier selectionn�.
	 * @param selectedRow la position de la colonne � rajouter.
	 */
	public void addColonne(int selectedRow) {
		if (currentFile != null) {
			setModified();
			currentFile.addColonne(selectedRow);
		}
	}

	/**
	 * rajoute un fichier au sch�ma.
	 * @param position la position du fichier dans le sch�ma.
	 */
	public void addFichier(TreePath position) {
		final String nom = getNewFileName("Nouveau Fichier");
		final Fichier f = new Fichier(nom, nom);
		addFichier(position, f);

	}

	private String getNewFileName(String nomSuggere) {
		String nom = nomSuggere;
		if (schema.getFichier(nom) == null) {
			return nom;
		}
		int pos = 1;
		while (schema.getFichier(nom + pos) != null) {
			pos++;
		}
		nom += pos;
		return nom;
	}

	private void addFichier(TreePath position, Fichier f) {
		setModified();
		treeModel.addFichier(position, f);
	}

	/**
	 * @return s'il faut afficher le bouton d'export de logs dans l'interface utilisateur.
	 */
	public boolean isSchemaAfficherExportLogs() {
		return schema.isAfficherExportLogs();
	}

	/**
	 * @param b true s'il faut afficher le bouton d'export de logs dans l'interface utilisateur.
	 */
	public void setSchemaAfficherExportLogs(boolean b) {
		setModified();
		schema.setAfficherExportLogs(b);
	}

	/**
	 * @return l'encodege des fichiers CSV.
	 */
	public String getSchemaEncoding() {
		return schema.getEncoding();
	}

	/**
	 * @return le separateur des champs.
	 */
	public char getSchemaSeparateurChamp() {
		return schema.getSeparateurChamp();
	}

	/**
	 * @param encoding l'ncodage des fichiers du schema.
	 */
	public void setSchemaEncoding(String encoding) {
		setModified();
		schema.setEncoding(encoding);
	}

	/**
	 * @param separateurChamp le separateur des champs.
	 */
	public void setSchemaSeparateurChamp(char separateurChamp) {
		setModified();
		schema.setSeparateurChamp(separateurChamp);
	}

	/**
	 * @return le fichier selectionn�, dont on affiche les dd�tails.
	 */
	public Fichier getCurrentFichier() {
		return currentFile.getFichier();
	}

	/**
	 * @return le sch�ma en cours de modifications.
	 */
	public Schema getSchema() {
		return schema;
	}

	/**
	 * @return un String qui donne le nom du fichier de conf et son chemin d'acc�s.
	 */
	public String getTitle() {
		String isModified = "";
		if (isSchemaModified) {
			isModified = "*";
		}
		if (newSchema) {
			return "Nouveau Schema" + isModified;
		}
		return fullSchemaPath + isModified;
	}

	/**
	 * sauvegarde la valeur variable qui indique que le sch�ma a �t� modifi�.
	 * utilis� par le controlleur quand il effectue des affichages.
	 *
	 * Dans certains cas, le controlleur demande une donn�e au model, qu'il
	 * affiche dans le view. cela declanche un evenement de modif des
	 * donnes dans le view qui fait un set dans le model.Le probleme est que
	 * savedModif qui indique s'il l'utilisateur a fait des modifs sera faussement
	 * modifi�e.
	 *
	 * Pour �viter ces situtions, le controlleur demande au model de
	 * sauvegarder la valeur de saveModified avant ces demandes de donn�es.
	 *
	 */
	public void saveModified() {
		savedModif = isSchemaModified;
	}

	/**
	* sauvegarde la valeur variable qui indique que le sch�ma a �t� modifi�.
	* utilis� par le controlleur quand il effectue des affichages
	*/
	public void revertModified() {
		isSchemaModified = savedModif;
	}

	/**
	 * @return true si le schema est modifi�.
	 */
	public boolean isSchemaModified() {
		return isSchemaModified;
	}

	/**
	 * @return true si le schema n'est pas lu d'un fichier disque.
	 */
	public boolean isNewSchema() {
		return newSchema;
	}

	/**
	 * sauvegarde le schema actuel.
	 * @throws IOException si une erreur d'ecriture a lieu
	 */
	public void saveFile() throws IOException {
		saveFile(new File(fullSchemaPath));
	}

	/**
	 * v�rifie que le schema actuel est valide.
	 * @return un message qui indique si le schema est valide , ou bien
	 * la cause d'invalidit� du sch�ma.
	 */
	public String verifySchema() {
		for (final Fichier f : schema.getFichiers()) {

			final Collection<Fichier> fichiersDontOnDepend = f.getFichiersDontOnDepend();
			final Collection<Fichier> fichiersQuiDependentDeNous = f.getFichiersQuiDependentDeNous();
			for (final Fichier fDep : fichiersQuiDependentDeNous) {
				if (fichiersDontOnDepend.contains(fDep)) {
					return "dependence circulaire entre >" + f.getNom() + "< et >" + fDep.getNom() + "<";
				}
			}

			for (final Colonne c : f.getColonnes()) {
				for (final ContrainteUniCol cuc : c.getContraintes()) {
					if ("ContrainteReference".equals(cuc.getID())) {
						final ContrainteReference cr = (ContrainteReference) cuc;
						final Fichier fref = schema.getFichier(cr.getFichierRef());
						if (fref == null) {
							return "La colonne >" + c.getNom() + "< du fichier >" + f.getNom() + "< fait r�f�rence � un fichier inexsistant.";
						}
						final Colonne cref = fref.getColonne(cr.getColonneRef());
						if (cref == null) {
							return "La colonne >" + c.getNom() + "< du fichier >" + f.getNom() + "< \n fait r�f�rence � une colonne inexsistante >" + cr.getColonneRef() + "< dans le fichier >" + fref.getNom() + "<.";
						}
						if (isIncompatible(c, cref)) {
							return "La colonne >" + c.getNom() + "< du fichier >" + f.getNom() + "< \n fait r�f�rence � une colonne incompatible >" + cr.getColonneRef() + "< dans le fichier >" + fref.getNom() + "<.";
						}
					}
				}
			}
		}
		return "Schema valide";
	}

	private boolean isIncompatible(Colonne c, Colonne cref) {
		//elles sont compatibles si elles sont strictement de meme typeet de tailles compatibles
		final String typeContrainteC = c.getContrainteType() == null ? "" : c.getContrainteType().getClass().getSimpleName(), typeContrainteCRef = cref.getContrainteType() == null ? "" : cref.getContrainteType().getClass().getSimpleName();
		return !(typeContrainteC.equals(typeContrainteCRef) && c.getTaille() <= cref.getTaille());
	}

	/**
	 * duplique le fichier � la position selectedRow.
	 * @param selectedRow la position du fichier � dupliquer.
	 * @return la nouvelle position � s�lectionner.
	 */
	public TreePath duplicateFile(TreePath selectedRow) {
		if (selectedRow == null) {
			return null;
		}
		final IHierarchieSchema selectedObj = (IHierarchieSchema) selectedRow.getLastPathComponent();
		if (!selectedObj.isFichier()) {
			return selectedRow;
		}
		final Fichier orig = (Fichier) selectedObj;
		final Fichier f = orig.copy();
		f.setNom(getNewFileName(orig.getNom()));
		treeModel.addFichier(selectedRow, f);
		return selectedRow;

	}

	/**
	 * doit �tre un toString() des enums de la classe {@link SeparateurDecimales}.
	 * @param text un String qui d�finit le s�parateur de d�cimales
	 */
	public void setSchemaSeparateurDecimales(String text) {
		schema.setSeparateurDecimales(text);

	}

	/**
	 * @return le separateur decimale du schema.
	 */
	public SeparateurDecimales getSchemaSeparateurDecimales() {
		return schema.getSeparateurDecimales();
	}

	/**
	 * D�place le fichier vers le bas.
	 * @param selectionPath la path du fichier � d�placer.
	 * @return le nouveau path du fichier.
	 */
	public TreePath moveFileDn(TreePath selectionPath) {
		if (selectionPath == null) {
			return null;
		}
		setModified();
		return treeModel.moveDn(selectionPath);

	}

	/**
	 * D�place le fichier vers le haut.
	 * @param selectionPath la path du fichier � d�placer.
	 * @return le nouveau path du fichier.
	 */
	public TreePath moveFileUp(TreePath selectionPath) {
		if (selectionPath == null) {
			return null;
		}
		setModified();
		return treeModel.moveUp(selectionPath);
	}

	/**
	 * Supprime une cat�gorie.
	 * @param selection le path de la cat�gorie.
	 * @param conserverLesFichier true s'il faut conserver les fichiers. Ils seront rattach�s � root.
	 * @return le path de la nouvelle s�l�ction.
	 */
	public TreePath deleteCat(TreePath selection, boolean conserverLesFichier) {
		return treeModel.deleteCat(selection, conserverLesFichier);
	}

	/**
	 * Ajoute une nouvelle cat�gorie au sch�ma.
	 */
	public void addCategorie() {
		treeModel.addCategorie();

	}

}
