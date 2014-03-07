package spark.reprise.outil.ui.admin.view;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import spark.reprise.outil.ui.admin.controller.DialogSelectColumnController;

/**
 * Fenetre qui permet de selectionner une(ou plusieurs) colonne parmi une liste de colonne.  
 */
public class DialogSelectColumn extends javax.swing.JDialog {
	private class ActnOK extends AbstractAction {

		private static final long serialVersionUID = 1L;

		ActnOK() {
			putValue(NAME, "OK");
		}

		/**{@inheritDoc}*/
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.ok();
		}

	}

	private class ActnCancel extends AbstractAction {

		private static final long serialVersionUID = 1L;

		ActnCancel() {
			putValue(NAME, "Cancel");
		}

		/**{@inheritDoc}*/
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.cancel();
		}

	}

	private class ActnClear extends AbstractAction {

		private static final long serialVersionUID = 1L;

		ActnClear() {
			super("", new ImageIcon(ClassLoader.getSystemResource("resources/images/clear.png")));
		}

		/**{@inheritDoc}*/
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.clear();
		}

	}

	private static final long serialVersionUID = 4949388399052250058L;

	DialogSelectColumnController controller;

	private javax.swing.JButton jbtnOK;

	private javax.swing.JButton jbtnClear;

	private javax.swing.JButton jbtnCancel;

	private javax.swing.JLabel jlblFiltre;

	private javax.swing.JLabel jlblListColonnes;

	private javax.swing.JLabel jlblColonnesSelectionnees;

	private javax.swing.JScrollPane jScrollPaneTree;

	private javax.swing.JTextField jtxtFilter;

	private javax.swing.JTextField jtxtSelected;

	private javax.swing.JTree jtreeCols;

	private final boolean singleSelectMode;

	/** Crée une nouvelle fenetre de selection de colonnes. 
	 * @param controller le controlleur de la fenetre
	 * @param multiSelectMode true pour permettre la selection de plusieure colonne */
	public DialogSelectColumn(DialogSelectColumnController controller, boolean multiSelectMode) {
		this.controller = controller;
		initComponents();
		this.singleSelectMode = multiSelectMode;
		jtreeCols.setRootVisible(false);
		if (singleSelectMode) {
			setTitle("Choisir une colonne");
		} else {
			setTitle("Choisir les colonnes");
		}
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 */
	private void initComponents() {

		createComponents();
		setModal(true);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new java.awt.GridBagLayout());

		setText();

		createLayout();

		jtreeCols.setModel(controller.getTreeModel());

		jtxtSelected.setEditable(false);
		jtxtFilter.setText("");
		jtxtFilter.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				setFilter();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				setFilter();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				setFilter();
			}
		});
		jtreeCols.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addSelectedElement(e);
			}

		});

		if (singleSelectMode) {
			jtreeCols.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		}

		jbtnOK.setAction(new ActnOK());
		jbtnCancel.setAction(new ActnCancel());
		jbtnClear.setAction(new ActnClear());
		jbtnOK.setEnabled(false);
		pack();
		setLocationRelativeTo(null);
	}

	private void setText() {
		jlblFiltre.setText("Filtre");
		jlblListColonnes.setText("Liste des Colonne");
		jlblColonnesSelectionnees.setText("Colonnes selectionnées");
	}

	/**
	 * Active/désactive le bouton ok.
	 * @param enabled true si le bouton doit être activé
	 */
	public void setOkEnabled(boolean enabled) {
		jbtnOK.setEnabled(enabled);
	}

	private void createLayout() {
		final javax.swing.JPanel jPanel;
		jPanel = new javax.swing.JPanel();

		java.awt.GridBagConstraints gridBagConstraints;
		final JPanel panelTop = new JPanel();
		panelTop.setLayout(new GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		//            gridBagConstraints.insets = new java.awt.Insets(16, 4, 4, 4);
		getContentPane().add(panelTop, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(16, 4, 4, 4);
		panelTop.add(jlblFiltre, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(16, 4, 4, 4);
		panelTop.add(jtxtFilter, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		getContentPane().add(jlblListColonnes, gridBagConstraints);

		jScrollPaneTree.setViewportView(jtreeCols);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		getContentPane().add(jScrollPaneTree, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		getContentPane().add(jlblColonnesSelectionnees, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		getContentPane().add(jtxtSelected, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		getContentPane().add(jbtnClear, gridBagConstraints);

		jPanel.setLayout(new java.awt.GridLayout());
		jPanel.add(Box.createHorizontalGlue());
		jPanel.add(jbtnOK);
		jPanel.add(Box.createHorizontalGlue());
		jPanel.add(jbtnCancel);
		jPanel.add(Box.createHorizontalGlue());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
		getContentPane().add(jPanel, gridBagConstraints);
	}

	private void createComponents() {
		jlblFiltre = new javax.swing.JLabel();
		jtxtFilter = new javax.swing.JTextField();
		jlblListColonnes = new javax.swing.JLabel();
		jScrollPaneTree = new javax.swing.JScrollPane();
		jtreeCols = new javax.swing.JTree();
		jlblColonnesSelectionnees = new javax.swing.JLabel();
		jtxtSelected = new javax.swing.JTextField();
		jbtnClear = new javax.swing.JButton();
		jbtnOK = new javax.swing.JButton();
		jbtnCancel = new javax.swing.JButton();
	}

	/**
	 * @param text affiche les colonnes selectionnées
	 */
	public void setSelectedColumns(String text) {
		jtxtSelected.setText(text);
	}

	/**
	 * vide la case du filtre.
	 */
	public void clearFilter() {
		jtxtFilter.setText("");
	}

	final void setFilter() {
		controller.setFilter(jtxtFilter.getText());
		for (int i = 0; i < jtreeCols.getRowCount(); i++) {
			jtreeCols.expandRow(i);
		}
	}

	void addSelectedElement(MouseEvent e) {
		if (!singleSelectMode && (e.getClickCount() == 2 || (e.getModifiers() & InputEvent.CTRL_MASK) != 0) || singleSelectMode) {
			final TreePath tp = jtreeCols.getPathForLocation(e.getX(), e.getY());
			controller.addElement(tp);
		}
	}

}
