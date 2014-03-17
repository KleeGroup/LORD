package com.kleegroup.lord.ui.admin.view;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.table.TableColumn;

import com.kleegroup.lord.ui.admin.common.view.ColonneChooser;
import com.kleegroup.lord.ui.admin.controller.DialogMultiColumnConstraintsController;

/**
 * Fenetre qui permet de modifier les contraintes multicolonne du fichier.
 */
public class DialogMultiColumnConstraints extends JDialog {
    
    private static final long serialVersionUID = 2357228148372984990L;
    
    final DialogMultiColumnConstraintsController controller;
    
    private javax.swing.JButton jbtnAddContrainte;

    private javax.swing.JButton jbtnDelContrainte;

    private javax.swing.JButton jbtnOk;

    private javax.swing.JButton jbtnCancel;

    private javax.swing.JLabel jlblListeVerif;

   
    private javax.swing.JScrollPane jScrollPaneTable;

    private javax.swing.JTable jtblContraintes;
    
    private class ActnAddConstraint extends AbstractAction {

	private static final long serialVersionUID = 1L;

	ActnAddConstraint() {
	    putValue(NAME, "+");
	}

	/**{@inheritDoc}*/
	@Override
	public void actionPerformed(ActionEvent e) {
	    addConstraint();
	}

    }

    private class ActnDeleteConstraint extends AbstractAction {

	private static final long serialVersionUID = 1L;

	ActnDeleteConstraint() {
	    putValue(NAME, "-");
	}

	/**{@inheritDoc}*/
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteConstraint();
	}

    }

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

    /**
     * Cree une fenetre d'édition des contrainte MultiColonne.
     * @param controller controlleur de la fenetre
     */
    public DialogMultiColumnConstraints(
	    DialogMultiColumnConstraintsController controller) {
	this.controller = controller;
	setModal(true);
	initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
	createComponents();
	setText();
	setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	jScrollPaneTable.setViewportView(jtblContraintes);

	jbtnOk.setAction(new ActnOK());
	jbtnCancel.setAction(new ActnCancel());
	jbtnAddContrainte.setAction(new ActnAddConstraint());
	jbtnDelContrainte.setAction(new ActnDeleteConstraint());

	setTitle("Vérifications Spécifiques");
	createLayout();
	pack();
	setLocationRelativeTo(null);
    }

    private void setText() {
	jlblListeVerif.setText("Liste des vérifications");
	jbtnAddContrainte.setText("+");
	jbtnDelContrainte.setText("-");
	jbtnOk.setText("OK");
	jbtnCancel.setText("Annuler");
    }

    private void createLayout() {
	final javax.swing.JPanel jPanelPrincipal;
	final javax.swing.JPanel jPanelBtnDroite;
	final javax.swing.JPanel jPanelBtnCentreBas;

	
	jPanelPrincipal = new javax.swing.JPanel();
	jPanelBtnDroite = new javax.swing.JPanel();
	jPanelBtnCentreBas = new javax.swing.JPanel();
	
	
	java.awt.GridBagConstraints gridBagConstraints;
	getContentPane().setLayout(new java.awt.GridBagLayout());
	jPanelPrincipal.setLayout(new java.awt.GridBagLayout());

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	jPanelPrincipal.add(jlblListeVerif, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 0;
	gridBagConstraints.gridy = 1;
	gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
	gridBagConstraints.weightx = 1.0;
	gridBagConstraints.weighty = 1.0;
	jPanelPrincipal.add(jScrollPaneTable, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 0;
	gridBagConstraints.gridy = 0;
	gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
	gridBagConstraints.weightx = 1.0;
	gridBagConstraints.weighty = 1.0;
	gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 5);
	getContentPane().add(jPanelPrincipal, gridBagConstraints);

	jPanelBtnDroite.setLayout(new java.awt.GridBagLayout());

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints.weightx = 1.0;
	jPanelBtnDroite.add(jbtnAddContrainte, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 0;
	gridBagConstraints.gridy = 1;
	gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints.weightx = 1.0;
	jPanelBtnDroite.add(jbtnDelContrainte, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 1;
	gridBagConstraints.gridy = 0;
	gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
	getContentPane().add(jPanelBtnDroite, gridBagConstraints);

	jPanelBtnCentreBas.setLayout(new java.awt.GridLayout());
	jPanelBtnCentreBas.add(Box.createHorizontalGlue());
	jPanelBtnCentreBas.add(jbtnOk);
	jPanelBtnCentreBas.add(jbtnCancel);
	jPanelBtnCentreBas.add(Box.createHorizontalGlue());

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 0;
	gridBagConstraints.gridy = 1;
	gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
	gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints.weightx = 1.0;
	gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
	getContentPane().add(jPanelBtnCentreBas, gridBagConstraints);
    }

    private void createComponents() {
	
	jlblListeVerif = new javax.swing.JLabel();
	jScrollPaneTable = new javax.swing.JScrollPane();
	
	jbtnAddContrainte = new javax.swing.JButton();
	jbtnDelContrainte = new javax.swing.JButton();
	
	jbtnOk = new javax.swing.JButton();
	jbtnCancel = new javax.swing.JButton();
	initTable();
    }

    private void initTable() {
	jtblContraintes = new MyJTable();
	jtblContraintes.setModel(controller.getTableModel());
	jtblContraintes.getColumnModel().getColumn(1).setCellEditor(
		new DefaultCellEditor(new JComboBox<String>(new DefaultComboBoxModel<String>(
			controller.getPossibleMethodNames()))));

	jtblContraintes.getColumnModel().getColumn(2).setCellEditor(
		new ColonneChooser(controller.getCurrentFichier()));
	jtblContraintes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
	adjustTableColumnSize();
	pack();
    }

    private void adjustTableColumnSize() {
	//Calcul des tailles optimales des colonnes
	//on utilise unqiuement les données du premier fichier (selectionné par defaut)
	for (int i = 0; i < jtblContraintes.getColumnCount(); i++) {
	    final TableColumn column = jtblContraintes.getColumnModel().getColumn(i);

	    Component comp = jtblContraintes.getTableHeader().getDefaultRenderer()
		    .getTableCellRendererComponent(null,
			    column.getHeaderValue(), false, false, 0, 0);
	    final int headerWidth = comp.getPreferredSize().width + 20;

	    int cellWidth = 0;
	    for (int j = 0; j < jtblContraintes.getRowCount(); j++) {

		comp = jtblContraintes.getDefaultRenderer(
			controller.getTableModel().getColumnClass(i))
			.getTableCellRendererComponent(jtblContraintes,
				controller.getTableModel().getValueAt(j, i),
				false, false, 0, i);
		cellWidth = Math.max(cellWidth,
			comp.getPreferredSize().width + 20);
	    }

	    column.setPreferredWidth(Math.max(headerWidth, cellWidth));
	}
    }

    void deleteConstraint() {
	controller.deleteConstraint(jtblContraintes.getSelectedRow());
    }

    void addConstraint() {
	controller.addConstraintCommand();
    }

    static class MyJTable extends javax.swing.JTable {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public boolean isCellEditable(int row, int column) {
		return true;
	    }
	}

}
