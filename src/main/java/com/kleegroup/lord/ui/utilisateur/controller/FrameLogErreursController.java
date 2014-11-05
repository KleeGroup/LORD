package com.kleegroup.lord.ui.utilisateur.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

import com.kleegroup.lord.moteur.Fichier;
import com.kleegroup.lord.moteur.logs.LoggueurRam;
import com.kleegroup.lord.ui.utilisateur.model.FrameLogErreursModel;
import com.kleegroup.lord.ui.utilisateur.view.FrameLogErreurs;

/**
 * Controlleur du FrameLogErreurs.
 */
public class FrameLogErreursController extends FrameController<FrameLogErreurs, FrameLogErreursModel> {
	// protected FrameLogErreursModel model = new FrameLogErreursModel();

	/**
	 * Construit un controlleur du FrameLogErreurs.
	 * 
	 * @param fpc
	 *            Controlleur de la fenetre principale.
	 */
	public FrameLogErreursController(FenetrePrincipaleUtilisateurController fpc) {
		super(fpc);
		fenetrePrincipaleController = fpc;
		resourceMap = ResourceBundle.getBundle("resources.FrameLogErreurs");
	}

	/** {@inheritDoc} */
	@Override
	public void activate() {

		fenetrePrincipale.setEtape(4);
		final Map<String, LoggueurRam> mapErrs = new HashMap<>();
		for (final String nom : fenetrePrincipaleController.model.getSchema().getLoggeurs().keySet()) {
			mapErrs.put(nom, (LoggueurRam) fenetrePrincipaleController.model.getSchema().getLoggeurs().get(nom));
		}
		model.setLoggueurs(mapErrs);
		view.setTableModel(new DefaultTableModel());
		view.setTreeModel(model.getListFichier(fenetrePrincipaleController.model.getSchema()));
		fenetrePrincipale.setEnabledSuivant(false);
		if (fenetrePrincipaleController.model.getSchema().isAfficherExportLogs()) {
			fenetrePrincipale.setVisibleMiscButton(true);
			fenetrePrincipale.setEnabledMiscButton(true);
			fenetrePrincipale.setMiscButtonCaption(resourceMap.getString("miscBtn.Caption"));
		}

	}

	/**
	 * Affiche les erreurs du fichier selectionn�.
	 * 
	 * @param nodeInfo
	 *            le fichier selectionn�.
	 */
	public void select(Fichier nodeInfo) {
		view.setTableModel(model.getErreurs(nodeInfo));
	}

	@Override
	protected FrameLogErreurs createView() {
		return new FrameLogErreurs(this);
	}

	@Override
	String getName() {
		return "LogErreurs";
	}

	/** {@inheritDoc} */
	@Override
	public void executeMiscAction() {
		boolean ecraseFichierExistant = false, annuleExport = false, fichierEstUnique = false;
		File res;
		final JFileChooser jfc = new JFileChooser();
		jfc.setFileFilter(new MyFileFilter());
		jfc.setSelectedFile(new File("logs.zip"));

		do {
			final int returnVal = jfc.showSaveDialog(fenetrePrincipale);
			res = jfc.getSelectedFile();
			if (res == null || returnVal == JFileChooser.CANCEL_OPTION) {
				annuleExport = true;
				res = null;
			} else {
				if (res.exists()) {
					final int answer = JOptionPane.showConfirmDialog(fenetrePrincipale, resourceMap.getString("erreur.fichierExistant"), "", JOptionPane.YES_NO_OPTION);
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
				fenetrePrincipaleController.model.getSchema().zipLogFiles(res);
			} catch (final IOException e) {
				JOptionPane.showMessageDialog(fenetrePrincipale, e.getLocalizedMessage(), resourceMap.getString("erreur.echecEcritureLog"), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	@Override
	protected void deactivate() {
		fenetrePrincipale.setVisibleMiscButton(false);
		fenetrePrincipale.setEnabledSuivant(true);
	}

	@Override
	protected FrameLogErreursModel createModel() {
		return new FrameLogErreursModel();
	}

	static class MyFileFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			return f.isDirectory() || f.getName().endsWith(".zip");
		}

		@Override
		public String getDescription() {
			return "Fichier Zip(*.zip)";
		}
	}
}
