/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewJFrame.java
 *
 * Created on 27 mai 2009, 15:02:00
 */

package com.kleegroup.lord.ui.utilisateur.view;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import com.kleegroup.lord.ui.utilisateur.controller.FenetrePrincipaleUtilisateurController;

/**
 * Fenetre principale de l'interface utilsiateur.
 */
public class FenetrePrincipaleUtilisateur extends javax.swing.JFrame {

	private static final long serialVersionUID = -8573621447889189932L;

	protected CardLayout contentPaneLayout;

	final ResourceBundle resourceMap = ResourceBundle.getBundle("resources.FenetrePrincipale");

	ActnMisc miscAction = new ActnMisc();

	private final FenetrePrincipaleUtilisateurController controller;

	private javax.swing.JButton jBtnMisc;

	private javax.swing.JButton jBtnNext;

	private javax.swing.JButton jBtnQuit;

	private javax.swing.JButton jBtnBack;

	private javax.swing.JLabel jLblLogoKLEE;

	private javax.swing.JLabel jLblLogoSPARK;

	private javax.swing.JPanel jPnlBottom;

	private javax.swing.JPanel jPnlCenter;

	private javax.swing.JPanel jPnlContent;

	private javax.swing.JPanel jPnlLeft;

	private javax.swing.JPanel jPnlTop;

	private final List<JComponent> labelsEtapes = new ArrayList<>();

	private class ActnMisc extends javax.swing.AbstractAction {
		private static final long serialVersionUID = 1L;

		ActnMisc() {
			putValue(NAME, "");
		}

		/**{@inheritDoc}*/
		/**{@inheritDoc}*/
		@Override
		public void actionPerformed(ActionEvent e) {
			miscAction();
		}

		void setName(String caption) {
			putValue(NAME, caption);
		}

	}

	private class ActnNext extends javax.swing.AbstractAction {

		private static final long serialVersionUID = 1L;

		ActnNext() {
			putValue("Name", resourceMap.getString("ActnNext.text"));
			putValue("MnemonicKey", Integer.valueOf(KeyEvent.VK_S));
		}

		/**{@inheritDoc}*/
		@Override
		public void actionPerformed(ActionEvent e) {
			next();
		}

	}

	private class ActnBack extends javax.swing.AbstractAction {
		private static final long serialVersionUID = 1L;

		ActnBack() {
			putValue("Name", resourceMap.getString("ActnBack.text"));
			putValue("MnemonicKey", Integer.valueOf(KeyEvent.VK_P));

		}

		/**{@inheritDoc}*/
		@Override
		public void actionPerformed(ActionEvent e) {
			back();

		}

	}

	private class ActnQuit extends AbstractAction {
		private static final long serialVersionUID = 1L;

		ActnQuit() {
			putValue(NAME, resourceMap.getString("ActnQuit.text"));
			putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_A));
		}

		/**{@inheritDoc}*/
		@Override
		public void actionPerformed(ActionEvent e) {
			cancel();
		}

	}

	/**
	 * @param controller controlleur de la fenetre
	 */
	public FenetrePrincipaleUtilisateur(FenetrePrincipaleUtilisateurController controller) {
		setTitle(resourceMap.getString("Title") + resourceMap.getString("Version"));
		initComponents();
		this.controller = controller;
	}

	/**
	 * ajoute une frame � afficher. Elle sera affich�e par {@link #showFrame(String)}
	 * @param frame la frame � ajouter
	 * @param name le nom de la frame (doit eter unique)
	 */
	public void addFrame(Component frame, String name) {
		jPnlContent.add(frame, name);
	}

	/**
	 * affiche la frame suivante.
	 */
	public void nextFrame() {
		contentPaneLayout.next(jPnlContent);

	}

	/**
	 * affiche la frame pr�c�dente.
	 */
	public void previousFrame() {
		contentPaneLayout.previous(jPnlContent);
	}

	private void initComponents() {

		createComponents();
		createLayout();

		setIconImage((new ImageIcon(ClassLoader.getSystemResource("resources/images/logo_appli.png"))).getImage());

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent w) {
				cancel();
			}
		});
		setMinimumSize(new java.awt.Dimension(500, 300));
		setName("Form");

		jPnlLeft.setMinimumSize(new java.awt.Dimension(125, 200));
		jPnlLeft.setName("jPnlLeft");

		createLabel("Step1");
		createLabel("Step2");
		createLabel("Step3");
		createLabel("Step4");
		createLabel("Step5");

		jPnlTop.setBackground(java.awt.Color.WHITE);
		jPnlTop.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		jPnlTop.setName("jPnlTop");

		jLblLogoKLEE.setIcon(new javax.swing.ImageIcon(ClassLoader.getSystemClassLoader().getResource("resources/images/logo_klee.png")));
		jLblLogoKLEE.setName("jLabel5");

		jLblLogoSPARK.setIcon(new javax.swing.ImageIcon(ClassLoader.getSystemClassLoader().getResource("resources/images/logo_lord.png")));
		jLblLogoSPARK.setName("jLabel6");

		jPnlBottom.setMinimumSize(new java.awt.Dimension(331, 60));
		jPnlBottom.setName("jPnlBottom");

		jBtnMisc.setName("btnMisc");
		jBtnMisc.setAction(miscAction);

		jBtnQuit.setName("btnCancel");
		jBtnQuit.setAction(new ActnQuit());

		jBtnBack.setName("btnBack");
		jBtnBack.setAction(new ActnBack());

		jBtnNext.setName("btnNext");
		jBtnNext.setAction(new ActnNext());

		jPnlContent.setName("jPnlContent");

		jBtnMisc.setVisible(false);
		jBtnMisc.setEnabled(true);

		pack();
		centerWindow();

	}

	private void createLabel(String name) {
		javax.swing.JTextArea jLabel1 = new javax.swing.JTextArea();

		jLabel1.setText(resourceMap.getString(name + ".text"));
		jLabel1.setName(name);
		jLabel1.setEditable(false);
		jLabel1.setBackground(jPnlLeft.getBackground());

		labelsEtapes.add(jLabel1);
		jPnlLeft.add(jLabel1);
		jPnlLeft.add(Box.createGlue());
	}

	private void centerWindow() {
		setSize(800, 600);
		setLocationRelativeTo(null);
	}

	private void createLayout() {

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

		jPnlTop.setLayout(new BoxLayout(jPnlTop, BoxLayout.LINE_AXIS));
		jPnlTop.add(jLblLogoKLEE);
		jPnlTop.add(Box.createHorizontalGlue());
		jPnlTop.add(jLblLogoSPARK);

		jPnlBottom.setLayout(new BoxLayout(jPnlBottom, BoxLayout.LINE_AXIS));
		jPnlBottom.add(Box.createRigidArea(new Dimension(15, 0)));
		jPnlBottom.add(jBtnMisc);
		jPnlBottom.add(Box.createHorizontalGlue());
		jPnlBottom.add(jBtnBack);
		jPnlBottom.add(jBtnNext);
		jPnlBottom.add(jBtnQuit);
		jPnlBottom.add(Box.createRigidArea(new Dimension(13, 1)));

		contentPaneLayout = new java.awt.CardLayout();
		jPnlContent.setLayout(contentPaneLayout);

		add(jPnlTop);
		add(Box.createRigidArea(new Dimension(10, 10)));
		add(jPnlCenter);
		add(Box.createRigidArea(new Dimension(10, 10)));
		add(jPnlBottom);
		add(Box.createRigidArea(new Dimension(10, 10)));

		setMinimumSize(new Dimension(800, 600));

		jPnlCenter.setLayout(new BoxLayout(jPnlCenter, BoxLayout.LINE_AXIS));
		jPnlCenter.add(Box.createRigidArea(new Dimension(10, 10)));
		jPnlCenter.add(jPnlLeft);
		jPnlCenter.add(Box.createRigidArea(new Dimension(10, 10)));
		jPnlCenter.add(jPnlContent);
		jPnlCenter.add(Box.createRigidArea(new Dimension(10, 10)));

		jPnlLeft.setLayout(new GridLayout(0, 1));
		jPnlLeft.add(Box.createGlue());
		//voir createLabel

	}

	private void createComponents() {
		jPnlLeft = new javax.swing.JPanel();
		jPnlTop = new javax.swing.JPanel();
		jLblLogoKLEE = new javax.swing.JLabel();
		jLblLogoSPARK = new javax.swing.JLabel();
		jPnlBottom = new javax.swing.JPanel();
		jBtnMisc = new javax.swing.JButton();
		jBtnQuit = new javax.swing.JButton();
		jBtnBack = new javax.swing.JButton();
		jBtnNext = new javax.swing.JButton();
		jPnlContent = new javax.swing.JPanel();
		jPnlCenter = new javax.swing.JPanel();
	}

	/**
	 * Active/descative le bouton Suivant.
	 * @param etat true pour activer.
	 */
	public void setEnabledSuivant(boolean etat) {
		jBtnNext.setEnabled(etat);
	}

	/**
	 * Active/descative le bouton Precedent.
	 * @param etat true pour activer.
	 */
	public void setEnabledPrecedent(boolean etat) {
		jBtnBack.setEnabled(etat);
	}

	/**
	 * Active/descative le bouton Suivant.
	 * @param etat true pour activer.
	 */
	public void setEnabledMiscButton(boolean etat) {
		jBtnMisc.setEnabled(etat);
	}

	/**
	 * Affiche/cache le bouton Misc(exporter les logs).
	 * @param etat true pour afficher.
	 */
	public void setVisibleMiscButton(boolean etat) {
		jBtnMisc.setVisible(etat);
	}

	/**
	 * @param caption la texte du bouton Misc
	 */
	public void setMiscButtonCaption(String caption) {
		miscAction.setName(caption);
	}

	/**
	 * @param name le nom de la frame � afficher.
	 */
	public void showFrame(String name) {
		contentPaneLayout.show(jPnlContent, name);
	}

	/**
	 * @param i la position de l'�tape en mettre en bold.
	 */
	public void setEtape(int i) {
		if (i >= labelsEtapes.size() || i < 0) {
			return;
		}

		final Font nonBold, bold, f = getFont();
		bold = new Font(f.getName(), Font.BOLD, f.getSize());
		nonBold = new Font(f.getName(), Font.PLAIN, f.getSize());
		for (int j = 0; j < i; j++) {
			labelsEtapes.get(j).setFont(nonBold);
		}
		labelsEtapes.get(i).setFont(bold);
		for (int j = i + 1; j < labelsEtapes.size(); j++) {
			labelsEtapes.get(j).setFont(nonBold);
		}

	}

	/**
	 * Change le texte du bouton Annuler.
	 * @param status true s'il faut afficher "Quitter" , sinon affiche Annuler
	 */
	public void setAnnuler(boolean status) {
		if (status) {
			jBtnQuit.getAction().putValue(Action.NAME, resourceMap.getString("ActnCancel.text"));
		} else {
			jBtnQuit.getAction().putValue(Action.NAME, resourceMap.getString("ActnQuit.text"));
		}
	}

	void miscAction() {
		controller.executeMiscAction();
	}

	void next() {
		controller.suivant();
	}

	void back() {
		controller.precedent();
	}

	void cancel() {
		controller.annuler();
	}

	/**
	 * Vide les frames de la fenetre principale.
	 */
	public void clear() {
		jPnlContent.removeAll();
	}

	/**
	 * change le curseur en curseur d'attente si etat est true .
	 * @param etat true pour le changer, false pour le curseur normal
	 */
	public void setWait(boolean etat) {
		if (etat) {
			try {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			} catch (HeadlessException e) {
				/**Ne fais rien*/
			}
		} else {
			setCursor(Cursor.getDefaultCursor());
		}
	}

}
