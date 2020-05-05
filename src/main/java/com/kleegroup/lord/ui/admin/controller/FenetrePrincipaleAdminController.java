package com.kleegroup.lord.ui.admin.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.xml.bind.JAXBException;

import com.kleegroup.lord.moteur.Categories.Categorie;
import com.kleegroup.lord.moteur.Fichier;
import com.kleegroup.lord.moteur.Schema;
import com.kleegroup.lord.moteur.util.IHierarchieSchema;
import com.kleegroup.lord.moteur.util.SeparateurChamps;
import com.kleegroup.lord.moteur.util.SeparateurDecimales;
import com.kleegroup.lord.ui.admin.model.FenetrePrincipaleAdminModel;
import com.kleegroup.lord.ui.admin.view.FenetrePrincipaleAdmin;

/**
 * Controlleur de la fenetre principale de l'interface administrateur.
 */
public class FenetrePrincipaleAdminController {

    private FenetrePrincipaleAdminModel model = new FenetrePrincipaleAdminModel();

    private final FenetrePrincipaleAdmin view;

    // le rÃ©pertoire du dernier fichier sÃ©lectionnÃ©
    private String lastPath = ".";

    /**
     * Constructeur par dÃ©faut du controlleur.
     */
    public FenetrePrincipaleAdminController() {
	view = new FenetrePrincipaleAdmin(this);
	try {
	    model.loadSchema("config.xml");
	} catch (final FileNotFoundException e) {
	    model = new FenetrePrincipaleAdminModel();
	} catch (final JAXBException e) {
	    model = new FenetrePrincipaleAdminModel();
	}
    }

    /**
     * @return le modele utilise par la table pour afficher les colonnes
     */
    public TableModel getTableModel() {
	return model.getTableModel();
    }

    /**
     * @return le modele utilise par le TreeView de la fenetrepour afficher
     *         la liste des fichiers.
     */
    public TreeModel getTreeModel() {
	return model.getTreeModel();
    }

    /**
     * Affiche les details du fichier selectionnï¿½.
     * 
     * @param node
     *                le fichier selectionnï¿½.
     */
    public void select(Fichier node) {
	if (node != null) {
	    view.showFileDetails(true);
	    model.saveModified();
	    view.setTableModel(model.getTableModel(node));
	    view.setNamePrefix(model.getCurrentFileNamePrefix());
	    view.setFileExtension(model.getCurrentFileExtension());
	    view.setFileErrorLimit(model.getCurrentFileErrorLimit());
	    view.setFileGroupNumber(model.getCurrentFileGroupNumber());
	    view.setFileHeaderLinesCount(model.getCurrentFileHeaderLinesCount());
	    view.setCheckHeaderName(model.getCurrentFileCheckHeaderName());
	    view.setCheckHeaderNameCaseSensitive(model.getCurrentFileCheckHeaderNameCaseSensitive());
	    view.setFileContraintesMultiColCount(model.getCurrentFileMultiColContraintesCount());
	    model.revertModified();
	    view.refreshTitle();
	}

    }

    //    /**
    //         * Deplace le fichier d'une position vers le haut.
    //         *
    //         * @param node
    //         *                le fichier ï¿½ deplacer
    //         */
    //    public void moveFileUp(Fichier node) {
    //	model.moveFileUp(node);
    //	view.refreshTitle();
    //    }

    /**
     * methode appelï¿½e quand l'utilisateur demande de quitter l'application.
     */
    public void exit() {
	if (model.isSchemaModified()){
	   final int res=JOptionPane.showConfirmDialog(view,
		    "Le schéma a été modifié. Voulez-vous sauvegarder vos modifications ?",
		    "Interface administrateur",
		    JOptionPane.YES_NO_CANCEL_OPTION);
	    if (res==JOptionPane.CANCEL_OPTION){
		return;
	    }
	    if(res==JOptionPane.YES_OPTION){
		save();
	    }
	}
	view.dispose();
    }

    /**
     * Deplace la colonne d'une position vers le haut.
     * 
     * @param selectedRow
     *                la position de la colonne ï¿½ deplacer.
     */
    public void moveColUp(int selectedRow) {
	model.moveColUp(selectedRow);
	view.refreshTitle();

    }

    /**
     * Deplace la colonne d'une position vers le bas.
     * 
     * @param selectedRow
     *                la position de la colonne ï¿½ deplacer.
     */
    public void moveColDn(int selectedRow) {
	model.moveColDn(selectedRow);
	view.refreshTitle();

    }

    /**
     * Supprime la colonne a la position selectedRow.
     * 
     * @param selectedRow
     *                la position de la colonne ï¿½ supprimer.
     */
    public void deleteColonne(int selectedRow) {
	model.deleteColonne(selectedRow);
	view.refreshTitle();

    }

    /**
     * modifie les attributs des colonnes d'un fichier. voir
     * {@link FenetrePrincipaleAdminModel#changeFileValue(Object, int, int)}.
     * 
     * @param value
     *                la nouvelle valeur.
     * @param row
     *                la position de la colonne ï¿½ modifier.
     * @param col
     *                la valeur ï¿½ modifier.
     */
    public void changeFileValue(Object value, int row, int col) {
	model.changeFileValue(value, row, col);
	view.refreshTitle();

    }

    /**
     * @return la liste des valeur disponible pour presenceColonne
     *         (OBLIGATOIRE,INTERDITE, FACULTATIVE)
     */
    public String[] getObFacValues() {
	return model.getObFacValues();
    }

    /**
     * @return la liste des valeur disponible pour le type d'une colonne
     *         entier , decimal, chaine de caractere, date
     */
    public String[] getTypeValues() {
	return model.getTypeValues();
    }

    /**
     * Affiche une fenetre d'ouverture d'un fichier, et essaie de charger le
     * schema sï¿½lï¿½ctionnï¿½.
     */
    public void openFile() {
	final JFileChooser f = new JFileChooser(new File(lastPath));
	f.setFileFilter(new MyFileFilter());
	f.showOpenDialog(null);
	if (f.getSelectedFile() != null) {
	    lastPath = f.getSelectedFile().getAbsolutePath();
	    try {
		model.loadSchema(f.getSelectedFile().getAbsolutePath());
	    } catch (final FileNotFoundException e) {
		JOptionPane
		.showMessageDialog(view,
			"Fichier non trouvÃ©.");
	    } catch (final JAXBException e) {
		JOptionPane
			.showMessageDialog(view,
				"Le fichier ne suit pas le format d'un fichier de configuration");
	    }
	    view.refresh();
	}
    }

    /**
     * Sauvegarde le schï¿½ma actuel sous un autre nom.
     */
    public void saveAs() {
	boolean ecraseFichierExistant = false, annuleExport = false, fichierEstUnique = false;
	File res;
	final JFileChooser jfc = new JFileChooser();
	jfc.setSelectedFile(new File("configNew.xml"));

	do {
	    final int returnVal = jfc.showSaveDialog(view);
	    res = jfc.getSelectedFile();
	    if (res == null || returnVal == JFileChooser.CANCEL_OPTION) {
		annuleExport = true;
		res = null;
	    } else {
		if (res.exists()) {
		    final int answer = JOptionPane.showConfirmDialog(view,
			    "erreurfichierExistant", "",
			    JOptionPane.YES_NO_OPTION);
		    if (answer == JOptionPane.YES_OPTION) {
			ecraseFichierExistant = true;
		    }
		} else {
		    fichierEstUnique = true;
		}
	    }
	} while (!ecraseFichierExistant && !annuleExport && !fichierEstUnique);
	if (res != null) {
	    try {
		model.saveFile(res);
	    } catch (final IOException e) {
		JOptionPane.showMessageDialog(view, e.getLocalizedMessage(),
			"erreur.echecEcritureLog", JOptionPane.ERROR_MESSAGE);
	    }
	}
	view.refreshTitle();
    }

    /**
     * Supprime un fichier.
     * 
     * @param selection le treepath du fichier à supprimer
     * @return le TreePath de l'objet a selectionner apres suppression
     */
    public TreePath deleteFile(TreePath selection) {
	if (selection == null) {
	    return null;
	}
	final IHierarchieSchema lastSelected =
	    (IHierarchieSchema)selection.getLastPathComponent();
	if (lastSelected.isFichier()){
	    return model.deleteFile(selection);
	}
	if (lastSelected.isCategorie()
		&& ((Categorie) lastSelected).size() == 0) {
	    return model.deleteCat(selection, true);
	}

	final int res = JOptionPane.showConfirmDialog(view,
		"Voulez-vous garder les fichiers de ce groupe ?",
		"Suppresion de groupe", JOptionPane.YES_NO_CANCEL_OPTION);
	if (res == JOptionPane.CANCEL_OPTION) {
	    return selection;
	} else if (res == JOptionPane.NO_OPTION) {
	    return model.deleteCat(selection, false);
	} else {
	    return model.deleteCat(selection, true);
	}
    }

    /**
     * modifie le prefixNom d'un fichier.
     * 
     * @param text
     *                le nouveau prefixNom
     */
    public void setFileNamePrefix(String text) {
	model.setCurrentFileNamePrefix(text);
	view.refreshTitle();

    }

    /**
     * modifie l'extension du fichier.
     * 
     * @param ext
     *                la nouvelle extension
     */
    public void setFileExtension(String ext) {
	model.setCurrentFileExtension(ext);
	view.refreshTitle();

    }

    /**
     * modifie le groupe du fichier.
     * 
     * @param grp
     *                le nouveau groupe du fichier.
     */
    public void setFileGroup(int grp) {
	model.setCurrentFileGroup(grp);
	view.refreshTitle();
    }

    /**
     * modifie le nombre de lignes d'entï¿½te du fichier.
     * 
     * @param nb
     *                le nombre de lignes d'entï¿½te du fichier.
     */
    public void setFileHeaderLinesCount(int nb) {
	model.setCurrentFileHeaderLinesCount(nb);
	view.refreshTitle();
    }

    /**
     * Modifie s'il faut contrôler le libellés des entêtes
     * 
     * @param value état du contrôle des libellés d'entêtes
     */
    public void setCheckHeaderName(boolean value) {
		model.setCurrentFileCheckHeaderName(value);
		if (value) {
			view.refreshTitle();
		} else {
			setCheckHeaderNameCaseSensitive(false);
		}
    }

    /**
     * Modifie s'il faut contrôler le libellés des entêtes
     * 
     * @param value état du contrôle des libellés d'entêtes
     */
    public void setCheckHeaderNameCaseSensitive(boolean value) {
	model.setCurrentFileCheckHeaderNameCaseSensitive(value);
	view.refreshTitle();
    }

    /**
     * modifie le seuil d'abandon du fichier.
     * 
     * @param nb
     *                le seuil d'abandon du fichier.
     */
    public void setFileErrorLimit(int nb) {
	model.setCurrentFileErrorLimit(nb);
	view.refreshTitle();
    }

    /**
     * Rajoute une colonne ï¿½ la position selectedRow.
     * 
     * @param selectedRow
     *                la position de la colonne ï¿½ rajouter
     */
    public void addColonne(int selectedRow) {
	model.addColonne(selectedRow);
	view.refreshTitle();

    }

    /**
     * Rajoute une colonne ï¿½ la position selectedRow.
     * 
     * @param position
     *                la position du fichier ï¿½ rajouter
     */
    public void addFichier(TreePath position) {
	model.addFichier(position);
	view.refreshTitle();
    }

    /**
     * @return true si on doit afficher le bouton exporter les logs.
     */
    public boolean getLogExportStatus() {
	return model.isSchemaAfficherExportLogs();
    }

    /**
     * @param b
     *                true si on doit afficher le bouton exporter les logs.
     */
    public void setLogExportStatus(boolean b) {
	model.setSchemaAfficherExportLogs(b);
	view.refreshTitle();

    }

    /**
     * affiche la feneter des options generales.
     */
    public void showGeneralOptions() {
	(new DialogGeneralOptionsController(this)).showWindow();

    }

    /**
     * @return l'encodage des fichiers du schema.
     */
    public String getEncoding() {
	final String s = model.getSchemaEncoding();
	if (s == null) {
	    return "";
	}
	return s;
    }

    /**
     * @param encoding
     *                l'encodage des fichiers du schema.
     */
    public void setEncoding(String encoding) {
	model.setSchemaEncoding(encoding);
	view.refreshTitle();
    }

    /**
     * @return le separateur de champ.
     */
    public SeparateurChamps getFieldSeparator() {
	return model.getSchemaSeparateurChamp();
    }

    /**
     * @return le separateur de decimales
     */
    public SeparateurDecimales getDecimalSeparator() {
	 return model.getSchemaSeparateurDecimales();
    }

    /**
     * @param sep SÃ©parateur de dÃ©cimales ({@link SeparateurDecimales}).
     */
    public void setDecimalSeparator(SeparateurDecimales sep) {
	    model.setSchemaSeparateurDecimales(sep);
    }

    /**
     * @param separateurChamp SÃ©parateur de champs ({@link SeparateurChamps}).
     */
    public void setFieldSeparator(SeparateurChamps sep) {
	model.setSchemaSeparateurChamp(sep);
	view.refreshTitle();

    }

    /**
     * affiche la feneter des options de fichier.
     */
    public void showFileOptions() {
	(new DialogFileOptionsController(this)).showWindow();

    }

    /**
     * affiche la fenetre de modification des contraintes multi-colonne.
     */
    public void showMultiColConstraints() {
	(new DialogMultiColumnConstraintsController(this)).showWindow();
	view.setFileContraintesMultiColCount(model
		.getCurrentFileMultiColContraintesCount());

    }

    /**
     * @return le fichier sï¿½lï¿½ctionnï¿½ actuellement dans le treeview, et dont
     *         les dï¿½tails sont affichï¿½s dans la table.
     */
    public Fichier getCurrentFichier() {
	return model.getCurrentFichier();
    }

    /**
     * @return le schema du model.
     */
    public Schema getSchema() {
	return model.getSchema();
    }

    /**
     * Cree un nouveau schema et le charge.
     */
    public void newSchema() {
	model = new FenetrePrincipaleAdminModel();
	view.refresh();
    }

    /**
     * @return le titre de la fenetre principale.
     */
    public String getTitle() {
	return model.getTitle();
    }

    /**
     * sauvegarde les modifications effectuï¿½es sur le schï¿½ma.
     */
    public void save() {
	if (model.isNewSchema()) {
	    saveAs();
	} else {
	    try {
		model.saveFile();
	    } catch (final IOException e) {
		JOptionPane.showMessageDialog(view,
			"Le fichier n'a pas Ã©tÃ© sauvegardÃ©");
	    }
	}
	view.refreshTitle();
    }

    /**
     * Verifie que le schema est valide.
     */
    public void verifySchema() {

	JOptionPane.showMessageDialog(view, model.verifySchema());
    }

    /**
     * Duplique le fichier selectionnï¿½.
     * 
     * @param path
     *                la position du fichier Ã  dupliquer
     * @return le path du fichier Ã  sÃ©lectionner (dans le view)
     */
    public TreePath duplicateFile(TreePath path) {
	return model.duplicateFile(path);
    }

    /**
     * Affiche la fenetre principale.
     */
    public void showFenetre() {
	view.refresh();
	view.setVisible(true);
    }

    /**
     * Masque la partie "dï¿½tail fichier".
     */
    public void selectNothing() {
	view.showFileDetails(false);
    }

    /**
     * Dï¿½place le fichier vers le bas.
     * @param selectionPath Le path du fichier ï¿½ dï¿½placer.
     * @return le nouveau path du fichier.
     */
    public TreePath moveFileDn(TreePath selectionPath) {
	final TreePath res = model.moveFileDn(selectionPath);
	view.refreshTitle();
	return res;

    }

    /**
     * DÃ©place le fichier vers le haut.
     * @param selectionPath Le chemin du fichier Ã  dÃ©placer.
     * @return le nouveau path du fichier.
     */
    public TreePath moveFileUp(TreePath selectionPath) {
	final TreePath res = model.moveFileUp(selectionPath);
	view.refreshTitle();
	return res;
    }

    /**
     * Ajoute une catï¿½gorie au schï¿½ma.
     */
    public void addCategorie() {
	model.addCategorie();
	view.refreshTitle();
    }
    
    static class MyFileFilter extends FileFilter {
    	@Override
	    public boolean accept(File file) {
		return file.isDirectory() || file.getName().endsWith(".xml");
	    }

	    @Override
	    public String getDescription() {
		return "Fichier de configuration (*.XML)";
	    }
    }
}
