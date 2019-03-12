package com.kleegroup.lord.ui.utilisateur.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.apache.log4j.Logger;

import com.kleegroup.lord.ui.utilisateur.controller.FrameSelectionCheminsFichiersController;

/**
 * Frame qui permet de selectionner les chemins des fichier , aisni que les
 * activer/d�scativer.
 */
public class FrameSelectionCheminsFichiers extends javax.swing.JPanel {

	private static org.apache.log4j.Logger logger = Logger.getLogger(FrameSelectionCheminsFichiers.class);

	private static final long serialVersionUID = -3028875209077812559L;

	protected int nbFichier = 0;

	protected FrameSelectionCheminsFichiersController controller;

	final ResourceBundle resourceMap = ResourceBundle.getBundle("resources." + getClass().getSimpleName());

	String lastPath = "";
	final List<TreeCellLineEditor> editeursFichier = new ArrayList<>();

	private javax.swing.JLabel jLblDataDir;

	private javax.swing.JLabel jLblLogDir;

	private javax.swing.JScrollPane jScrollPane1;

	private javax.swing.JButton jSelectDirData;

	private javax.swing.JButton jSelectDirLog;

	private javax.swing.JTextField jtxtDataDir;

	private javax.swing.JTextField jtxtLogDir;

	private javax.swing.JPanel listFichier1;

	private javax.swing.JPanel jpnlListeFichiers;

	/** Cr�e une nouvelle fenetre.
	 * @param controller controlleur de la frame. */
	public FrameSelectionCheminsFichiers(FrameSelectionCheminsFichiersController controller) {
		super();
		initComponents();
		this.controller = controller;

	}

	/**
	 * Rajoute des contrôles pour modifier les propriétés d'un fichier.
	 * @param nom le nom du fichier.
	 * @param extension l'extension du fichier.
	 */
	public void addFichier(final String nom, final String extension, final String categorie) {

		final TreeCellLineEditor fichier = new TreeCellLineEditor(listFichier1, nom, extension, categorie);

		editeursFichier.add(fichier);
		fichier.setNom(nom);
		fichier.addEnabledListener(new ActionListener() {
			int i = nbFichier;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.setEnabled(nom, editeursFichier.get(i).getFichierEnabled());

			}
		});
		fichier.addCheminListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				try {
					if (nom != null && controller != null) {
						final String text = arg0.getDocument().getText(0, arg0.getDocument().getLength());
						controller.setChemin(nom, text);
					}
				} catch (final BadLocationException e) {
					/* ne devrais pas se produire puisqu'on demande du texte
					* dont on est certain qu'il est dans les limites du
					* documents */
				}
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {// Ne fais rien
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {// Ne fais rien
			}
		});
		nbFichier++;
	}

	private void categorieSelection(final String categorie, boolean enable) {
		logger.info(((enable)?"Sélectionner ":"Désélectionner ") + "les fichiers de la catégorie \"" + categorie + "\"");
		// Traitement
		for (TreeCellLineEditor fic : editeursFichier) {
			if (fic.hasCategorie(categorie)) {
				controller.setEnabled(fic.getNom(), enable);
			}
		}
		
	}
	
	/**
	 * Ajoute une zone qui indique le début d'une catégorie.
	 * Deux actions sont possibles pour (dé)sélectionner les fichiers.
	 * 
	 * @param nom le nom de la catégorie.
	 */
	public void addCategorie(String nom) {
		
		// Libellé de catégorie
		final JLabel lblCat = new JLabel(nom);
		lblCat.setFont(new Font(getFont().getName(), Font.BOLD, getFont().getSize() + 2));
		lblCat.setBorder(new EmptyBorder(0,20,0,0)); // Remplace les espaces en tête

		// Bloc de boutons (sélectionner Aucun / tous)
		final javax.swing.JPanel buttonBar = new javax.swing.JPanel(); 
		buttonBar.setOpaque(false);
		buttonBar.setLayout(new FlowLayout());

		Font font = new Font(getFont().getName(), Font.PLAIN, getFont().getSize() + 2);
		Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		Font underlinedFont = font.deriveFont(attributes);
				
		final JLabel selectTous = new JLabel(resourceMap.getString("labelSelectAll.text"));
		selectTous.setLabelFor(lblCat);
		selectTous.setFont(underlinedFont);
		selectTous.setCursor(new Cursor(Cursor.HAND_CURSOR));
		selectTous.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getComponent() instanceof JLabel) {
                	categorieSelection(((JLabel)((JLabel)e.getComponent()).getLabelFor()).getText(), true);
                }
            }
        });
		
		final JLabel selectAucun = new JLabel(resourceMap.getString("labelSelectNone.text"));
		selectAucun.setLabelFor(lblCat);
		selectAucun.setFont(underlinedFont);
		selectAucun.setCursor(new Cursor(Cursor.HAND_CURSOR));
		selectAucun.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getComponent() instanceof JLabel) {
                	categorieSelection(((JLabel)((JLabel)e.getComponent()).getLabelFor()).getText(), false);
                }
            }
        });
	
		buttonBar.add(selectAucun);
		buttonBar.add(new JLabel(" / "));
		buttonBar.add(selectTous);
		
		// Ligne d'entête : libellé et boutons
		final javax.swing.JPanel entete = new javax.swing.JPanel(); 
		entete.setBackground(java.awt.Color.LIGHT_GRAY);
		entete.setOpaque(true);
		entete.setLayout(new BorderLayout());

		entete.add(lblCat, BorderLayout.LINE_START);
		entete.add(buttonBar, BorderLayout.LINE_END);
		
		// Ajout à la Frame
		final java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new java.awt.Insets(8, 0, 3, 0);
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

		listFichier1.add(entete, gridBagConstraints);
	}

	/**
	 * Ajoute une zone vide.
	 */
	public void addEmptyArea() {
		final Component area = Box.createVerticalStrut(40);
		final java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

		listFichier1.add(area, gridBagConstraints);
	}

	/**
	 * Affiche la valeur chemin dans la case chemin du fichier.
	 * @param nom le nom du fichier
	 * @param chemin le chemin � afficher
	 */
	public void setChemin(String nom, String chemin) {
		getEditor(nom).setChemin(chemin);
	}

	private TreeCellLineEditor getEditor(String nom) {
		for (int i = 0; i < editeursFichier.size(); i++) {
			if (nom.equals(editeursFichier.get(i).getNom())) {
				return editeursFichier.get(i);
			}
		}
		return null;
	}

	/**
	 * Active/désactive le fichier.
	 * @param nom le nom du fichier.
	 * @param etat true s'il faut l'activer.
	 */
	public void setEnable(String nom, boolean etat) {
		getEditor(nom).setFichierEnabled(etat);
	}

	private void initComponents() {

		jLblDataDir = new javax.swing.JLabel();
		jLblLogDir = new javax.swing.JLabel();
		jtxtDataDir = new javax.swing.JTextField();
		jtxtLogDir = new javax.swing.JTextField();
		jSelectDirData = new javax.swing.JButton();
		jSelectDirLog = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		listFichier1 = new javax.swing.JPanel();
		jpnlListeFichiers = new javax.swing.JPanel();

		jLblDataDir.setText(resourceMap.getString("jLblDataDir.text"));
		jLblLogDir.setText(resourceMap.getString("jLblLogDir.text"));

		jSelectDirData.setAction(new ActnSelectDataDir());
		jSelectDirLog.setAction(new ActnSelectLogDir());

		jtxtDataDir.setEditable(false);

		jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		jScrollPane1.setName("jScrollPane1");
		jScrollPane1.setBackground(java.awt.Color.WHITE);

		jScrollPane1.getViewport().setBackground(java.awt.Color.WHITE);
		jScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		listFichier1.setBackground(java.awt.Color.WHITE);
		listFichier1.setBackground(java.awt.Color.WHITE);

		createLayout();

	}

	private void createLayout() {
		setLayout(new GridBagLayout());
		jpnlListeFichiers.setLayout(new GridBagLayout());

		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		jpnlListeFichiers.add(jLblDataDir, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		jpnlListeFichiers.add(jLblLogDir, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
		jpnlListeFichiers.add(jtxtDataDir, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
		gridBagConstraints.weightx = 1.0;
		jpnlListeFichiers.add(jtxtLogDir, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		jpnlListeFichiers.add(jSelectDirData, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		jpnlListeFichiers.add(jSelectDirLog, gridBagConstraints);
		jpnlListeFichiers.setBorder(new LineBorder(java.awt.Color.BLACK));

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.0;
		gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
		add(jpnlListeFichiers, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
		jScrollPane1.setBorder(new LineBorder(java.awt.Color.BLACK));
		add(jScrollPane1, gridBagConstraints);

		jScrollPane1.getViewport().add(listFichier1);

		listFichier1.setLayout(new GridBagLayout());
		final javax.swing.JLabel l1 = new javax.swing.JLabel(resourceMap.getString("columnFile.header"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		final Font headerFont = new Font(getFont().getName(), Font.BOLD, getFont().getSize() + 1);

		l1.setFont(headerFont);
		listFichier1.add(l1, gridBagConstraints);

		final javax.swing.JLabel l2 = new javax.swing.JLabel(resourceMap.getString("columnPath.header"));
		l2.setFont(headerFont);
		l2.setBackground(java.awt.Color.GRAY);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.gridy = 0;
		listFichier1.add(l2, gridBagConstraints);

	}

	/**
	 * Rajoute un label à la fin de la liste des fichiers pour pousser les contrôles vers le haut.
	 */
	public void fin() {
		final javax.swing.JLabel l3 = new javax.swing.JLabel("");
		l3.setBackground(java.awt.Color.GRAY);
		final java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.weighty = 1;
		listFichier1.add(l3, gridBagConstraints);
	}

	void jSelectDirDataActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jSelectDirDataActionPerformed
		controller.changeDataDir();
	}

	void jSelectDirLogActionPerformed(java.awt.event.ActionEvent evt) {
		controller.changeLogDir();
	}

	/**
	 * @param path le r�pertoire du LogDir � afficher.
	 */
	public void setLogDir(String path) {
		jtxtLogDir.setText(path);
	}

	/**
	 * @param path le r�pertoire des donn�es.
	 */
	public void setDataDir(String path) {
		jtxtDataDir.setText(path);
	}

	private class ActnSelectDataDir extends AbstractAction {
		private static final long serialVersionUID = 1L;

		ActnSelectDataDir() {
			putValue(NAME, "...");
		}

		/** {@inheritDoc} */
		@Override
		public void actionPerformed(ActionEvent e) {
			jSelectDirDataActionPerformed(e);
		}
	}

	private class ActnSelectLogDir extends AbstractAction {
		private static final long serialVersionUID = 1L;

		ActnSelectLogDir() {
			putValue(NAME, "...");
		}

		/** {@inheritDoc} */
		@Override
		public void actionPerformed(ActionEvent e) {
			jSelectDirLogActionPerformed(e);
		}
	}

	private class TreeCellLineEditor {
		protected javax.swing.JTextField jtxtfldEmplacementFichier;

		protected javax.swing.JPanel myPanel;

		protected List<ActionListener> tcleListeEnabledListener = new ArrayList<>();

		protected List<DocumentListener> tcleListeCheminListener = new ArrayList<>();

		//	protected int pos;
		private String nom = "";
		
		private String categorie = "";

		private javax.swing.JCheckBox jChckFichierActif;

		private javax.swing.JButton jBtnChangerEmplacement;

		private javax.swing.JButton jBtnEffacerEmaplacement;

		private class ActionEffacerEmplacement extends AbstractAction {
			private static final long serialVersionUID = 1L;

			ActionEffacerEmplacement() {
				putValue(NAME, "X");
			}

			/** {@inheritDoc} */
			@Override
			public void actionPerformed(ActionEvent e) {
				jtxtfldEmplacementFichier.setText("");
			}
		}

		private class ActionChangerEmplacement extends AbstractAction {
			private static final long serialVersionUID = 1L;

			ActionChangerEmplacement() {
				putValue(NAME, "...");
			}

			/** {@inheritDoc} */
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changerEmplacement();
			}
		}

		/** Creates new form line. */
		TreeCellLineEditor(javax.swing.JPanel panel1, String nom, String extension, String categorie) {
			myPanel = panel1;
			//	    this.pos = pos;
			this.nom = nom;
			this.categorie = categorie;
			initComponents();
		}

		protected String getNom() {
			return nom;
		}

		protected boolean hasCategorie(final String cat) {
			return (categorie != null)?categorie.equals(cat):false;
		}

		/**
		     * This method is called from within the constructor to initialize the
		     * form. WARNING: Do NOT modify this code. The content of this method is
		     * always regenerated by the Form Editor.
		     */
		// <editor-fold defaultstate="collapsed" desc="Generated
		// Code">//GEN-BEGIN:initComponents
		private void initComponents() {

			jChckFichierActif = new javax.swing.JCheckBox();
			jtxtfldEmplacementFichier = new javax.swing.JTextField();

			// jtxtfldEmplacementFichier.setMinimumSize(new Dimension(0,0));
			jtxtfldEmplacementFichier.setPreferredSize(new Dimension(0, 0));
			jBtnChangerEmplacement = new javax.swing.JButton();
			jBtnEffacerEmaplacement = new javax.swing.JButton();

			jChckFichierActif.setBackground(Color.WHITE);
			jChckFichierActif.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					disableLine(arg0);
				}
			});
			jChckFichierActif.setSelected(true);

			jtxtfldEmplacementFichier.setBorder(null);
			jtxtfldEmplacementFichier.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void changedUpdate(DocumentEvent arg0) {
					for (final DocumentListener al : tcleListeCheminListener) {
						al.changedUpdate(arg0);
					}
				}

				@Override
				public void insertUpdate(DocumentEvent arg0) {
					changedUpdate(arg0);
				}

				@Override
				public void removeUpdate(DocumentEvent arg0) {
					changedUpdate(arg0);
				}

			});

			jBtnChangerEmplacement.setText("...");
			jBtnChangerEmplacement.setAction(new ActionChangerEmplacement());

			jBtnEffacerEmaplacement.setAction(new ActionEffacerEmplacement());
			jBtnEffacerEmaplacement.setText("X");

			jtxtfldEmplacementFichier.setBorder(new LineBorder(java.awt.Color.BLACK));

			createLayout();
		}

		private void createLayout() {
			java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			myPanel.add(jChckFichierActif, gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
			gridBagConstraints.gridx = 2;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.insets = new java.awt.Insets(3, 1, 3, 1);
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			jtxtfldEmplacementFichier.setPreferredSize(new Dimension(20, 20));
			myPanel.add(jtxtfldEmplacementFichier, gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
			gridBagConstraints.gridx = 3;
			gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
			myPanel.add(jBtnChangerEmplacement, gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
			gridBagConstraints.gridx = 4;
			gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
			myPanel.add(jBtnEffacerEmaplacement, gridBagConstraints);

		}

		void setNom(String nom) {
			jChckFichierActif.setText(nom);
		}

		private void disableLine(boolean etat) {
			jBtnChangerEmplacement.setEnabled(!etat);
			jBtnEffacerEmaplacement.setEnabled(!etat);
			jtxtfldEmplacementFichier.setEditable(!etat);
			jChckFichierActif.setBackground(jtxtfldEmplacementFichier.getBackground());
		}

		boolean getFichierEnabled() {
			return jChckFichierActif.isSelected();
		}

		void setFichierEnabled(boolean etat) {
			logger.trace("Modification de " + nom + " ("+etat+")");
			jChckFichierActif.setSelected(etat);
			disableLine(!etat);
		}

		void addEnabledListener(ActionListener al) {
			tcleListeEnabledListener.add(al);
		}

		void addCheminListener(DocumentListener al) {
			tcleListeCheminListener.add(al);
		}

		void setChemin(String chemin) {
			jtxtfldEmplacementFichier.setText(chemin);
		}

		void changerEmplacement() {
			controller.changerEmplacement(nom);
			jtxtfldEmplacementFichier.setText(controller.getChemin(nom));
		}

		void disableLine(ActionEvent arg0) {
			disableLine(!jChckFichierActif.isSelected());
			for (final ActionListener al : tcleListeEnabledListener) {
				al.actionPerformed(arg0);
			}
		}

	}

}
