package com.kleegroup.lord.ui.admin.common.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.kleegroup.lord.moteur.Colonne;
import com.kleegroup.lord.moteur.Fichier;
import com.kleegroup.lord.moteur.Schema;
import com.kleegroup.lord.ui.admin.controller.DialogSelectColumnController;

/**
 * Classe qui lance un dialogue de selection de colonne lorsque l'utilisateur clique sur une 
 * cellule d'une table qui affiche les détails d'un fichier.
 */
public class ColonneChooser extends AbstractCellEditor implements TableCellEditor, ActionListener {

	/**
	     * 
	     */
	private static final long serialVersionUID = 1L;
	private static final String EDIT = "edit";

	private JButton button;

	private DialogSelectColumnController dialogController;

	private boolean modeSchema = false;

	private Fichier fichier;

	private Schema schema;

	private Fichier exclu;

	private List<Colonne> result = new ArrayList<>();
	private Object oldValue;
	private String colonneExclue = "";

	private ColonneChooser() {
		button = new JButton();
		button.setVisible(false);
		button.setActionCommand(EDIT);
		button.addActionListener(this);
	}

	/**
	 * Construit un ColonneChooser pour afficher les colonnes du fichier f.
	 * @param f le fichier dont on affiche les colonnes.
	 */
	public ColonneChooser(Fichier f) {
		this();
		fichier = f;

	}

	/**
	 * Construit un ColonneChooser pour afficher les colonnes du sch�ma sch�ma.
	 * @param schema le schéma dont affiche les colonnes.
	 * @param exclu fichier exclu de l'affichage des colonnes.
	 */
	public ColonneChooser(Schema schema, Fichier exclu) {
		this();
		this.schema = schema;
		this.exclu = exclu;
		modeSchema = true;

	}

	private void createDialogController() {
		if (modeSchema) {
			dialogController = new DialogSelectColumnController(schema, exclu, colonneExclue);
			List<Object> val = new ArrayList<>();
			if (oldValue != null) {
				val.add(oldValue);
				dialogController.setInitialValue(val);
			}
		} else {
			dialogController = new DialogSelectColumnController(fichier);
			dialogController.setInitialValue((List<?>) oldValue);
		}

	}

	/**{@inheritDoc}*/
	@Override
	public void actionPerformed(ActionEvent e) {
		if (EDIT.equals(e.getActionCommand())) {
			// The user has clicked the cell, so
			// bring up the dialog.
			createDialogController();
			dialogController.showWindow();
			if (!dialogController.isUserClickedCancel()) {
				result = dialogController.getSelectElements();
			}

			fireEditingStopped();

		}
	}

	/**{@inheritDoc}*/
	@Override
	public Object getCellEditorValue() {
		return result;
	}

	/**{@inheritDoc}*/
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		oldValue = value;
		colonneExclue = table.getModel().getValueAt(row, 0).toString();
		return button;
	}

}
