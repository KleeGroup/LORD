package com.kleegroup.lord.ui.utilisateur.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.kleegroup.lord.moteur.Fichier;
import com.kleegroup.lord.ui.utilisateur.model.FrameSelectionCheminsFichiersModel;
import com.kleegroup.lord.ui.utilisateur.view.FrameSelectionCheminsFichiers;

/**
 * Frame qui permet à l'utilisateur de renseigner l'emplacement
 * des différents fichiers.
 */
public class FrameSelectionCheminsFichiersController extends FrameController<FrameSelectionCheminsFichiers, FrameSelectionCheminsFichiersModel> {
	private static final String USER_PREFS_FILEPATH = System.getProperty("java.io.tmpdir") + "/lord.properties";
	Properties userPrefs = new Properties();
	String lastPath = "";

	/**
	 * @param fpc controlleur de la fenetre principale.
	 */
	public FrameSelectionCheminsFichiersController(FenetrePrincipaleUtilisateurController fpc) {
		super(fpc);
		setNext(new FrameProgressionTraitementController(fpc));
		getUserPrefs();

	}

	/**
	 * change le repertoire de sauvegarde des fichiers de logs.
	 * @param pathSuggere le chemin d'accès du nouveau repertoire.
	 */
	public void setLogDir(String pathSuggere) {
		String path = pathSuggere;
		try {
			if (path == null) {
				path = "./logs/";
			}
			final String logDir = (new java.io.File(path)).getCanonicalPath();
			fenetrePrincipaleController.model.setEmplacementLogs(logDir + "/");
			view.setLogDir(logDir);
			userPrefs.setProperty("LogDir", path);
		} catch (final IOException e) {
			// Ne fais rien
		}
	}

	@Override
	String getName() {
		return "FrameSelectionCheminsFichiers";
	}

	@Override
	protected FrameSelectionCheminsFichiersModel createModel() {
		return new FrameSelectionCheminsFichiersModel(fenetrePrincipaleController.model.getSchema());
	}

	@Override
	protected FrameSelectionCheminsFichiers createView() {
		final FrameSelectionCheminsFichiers temp = new FrameSelectionCheminsFichiers(this);
		if (model.getNomsCategories().size() > 1) {
			creerListeCheminsAvecCategories(temp);
		} else {
			creerListeCheminsSansCategories(temp);
		}
		temp.fin();

		return temp;
	}

	private void creerListeCheminsAvecCategories(FrameSelectionCheminsFichiers temp) {
		for (final String nomCategorie : model.getNomsCategories()) {
			temp.addCategorie(nomCategorie);
			for (final Fichier f : model.getFichiers(nomCategorie)) {
				final String nom = f.getNom();
				temp.addFichier(nom, model.getExtension(nom), nomCategorie);
				temp.setChemin(nom, model.getChemin(nom));
			}
		}
	}

	private void creerListeCheminsSansCategories(FrameSelectionCheminsFichiers temp) {
		for (final String nomCategorie : model.getNomsCategories()) {
			for (final Fichier f : model.getFichiers(nomCategorie)) {
				final String nom = f.getNom();
				temp.addFichier(nom, model.getExtension(nom), nomCategorie);
				temp.setChemin(nom, model.getChemin(nom));
			}
		}
	}

	/**
	 * Change le chemin du fichier.
	 * @param nom le nom du fichier.
	 * @param chemin le chemin du fichier.
	 */
	public void setChemin(String nom, String chemin) {
		model.setChemin(nom, chemin);
		if (userPrefs != null) {
			userPrefs.put(nom, chemin);
		}
	}

	/**
	 * Active/désactive le fichier.
	 * @param nom le nom du fichier.
	 * @param etat true s'il faut activer le fichier.
	 */

	public void setEnabled(String nom, boolean etat) {
		model.setEnabled(nom, etat);
		refreshView();
	}

	/**{@inheritDoc}*/
	@Override
	public void activate() {
		fenetrePrincipale.setEtape(1);
		model.clean();
		refreshView();
	}

	/**{@inheritDoc}*/
	@Override
	public void deactivate() {
		saveUserPrefs();
	}

	/**{@inheritDoc}*/
	@Override
	public boolean canGoNext() {
		if (!model.isValideDirLog()) {
			JOptionPane.showMessageDialog(null, resourceMap.getString("InvalidLogDir"));
			return false;
		}

		if (!model.isAuMoinsUnfichierActif()) {
			final String errMsg = resourceMap.getString("NoActiveFiles");
			JOptionPane.showMessageDialog(null, errMsg);
			return false;
		}
		final String nomFichierInvalide = model.isValide();
		if (nomFichierInvalide == null) {
			return true;
		}
		String errMsg = resourceMap.getString("InvalidPath");
		errMsg = errMsg.replaceAll("_nom_fichier_", nomFichierInvalide);
		JOptionPane.showMessageDialog(null, errMsg);

		return false;
	}

	private void refreshView() {
		for (final String nom : model.getNomsFichiers()) {
			view.setEnable(nom, model.getEnabled(nom));
			view.setChemin(nom, model.getChemin(nom));
		}
	}

	private void setDirData(File selectedFile) {
		model.searchDir(selectedFile);
		refreshView();

	}

	private void getUserPrefs() {
		try (FileInputStream fis = new FileInputStream(USER_PREFS_FILEPATH)) {
			userPrefs.load(fis);
			for (final Object s : userPrefs.keySet()) {
				final String nom = (String) s;
				model.setChemin(nom, userPrefs.getProperty(nom));
			}
			setLogDir(userPrefs.getProperty("LogDir"));
		} catch (final FileNotFoundException e) {
			// Ne fais rien
		} catch (final IOException e) {
			// Ne fais rien
		}
	}

	private void saveUserPrefs() {
		try (FileOutputStream fos = new FileOutputStream(USER_PREFS_FILEPATH)) {
			userPrefs.store(fos, "");
		} catch (final IOException e) {
			// Ne fais rien
		}
	}

	/**
	 * @param nom la position du fichier dans la liste des fichiers.
	 * @return le chemin du fichier � la position pos
	 */
	public String getChemin(String nom) {
		return model.getChemin(nom);
	}

	/**
	 * Change l'emplacement fu fichier nom.<br>
	 * Demande à l'utilisateur le nouvel emplacaemnt. Mets à jour le view et le model.
	 * @param nom le nom du fichier dont on veut changer l'emplacement.
	 */
	public void changerEmplacement(String nom) {
		final JFileChooser f = new JFileChooser(lastPath);
		final String ext = model.getExtension(nom);
		f.setFileFilter(new MyFileFilter(ext, resourceMap));

		f.setDialogTitle(nom);
		f.showOpenDialog(null);
		if (f.getSelectedFile() != null) {
			setChemin(nom, f.getSelectedFile().getAbsolutePath());

			lastPath = f.getSelectedFile().getPath();
		}

	}

	/**
	 * Cherche  les fichiers CSV dans un répertoire choisi par l'utilisateur.
	 */
	public void changeDataDir() {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.showOpenDialog(view);
		if (fc.getSelectedFile() != null) {
			view.setDataDir(fc.getSelectedFile().getAbsolutePath());
			setDirData(fc.getSelectedFile());
		}
	}

	/**
	 * Change le répertoire des fichiers de logs.
	 */
	public void changeLogDir() {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.showOpenDialog(view);
		if (fc.getSelectedFile() != null) {
			view.setLogDir(fc.getSelectedFile().getAbsolutePath());
			setLogDir(fc.getSelectedFile().getAbsolutePath());
		}

	}

	static class MyFileFilter extends FileFilter {
		private final String extension;
		private final ResourceBundle resourceMap;

		/**
		 * Constructeur MyFileFilter
		 * 
		 * @param extension paramètre à définir
		 * @param resourceMap paramètre à définir
		 */
		public MyFileFilter(String extension, ResourceBundle resourceMap) {
			super();
			this.extension = extension;
			this.resourceMap = resourceMap;
		}

		@Override
		public boolean accept(File file) {
			return file.isDirectory() || "".equals(extension) || file.getName().endsWith(extension);
		}

		@Override
		public String getDescription() {
			return resourceMap.getString("fichiersSources");
		}
	}
}
