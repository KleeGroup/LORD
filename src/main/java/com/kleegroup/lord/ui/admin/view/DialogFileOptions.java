package com.kleegroup.lord.ui.admin.view;

import java.awt.event.ActionEvent;
import java.nio.charset.Charset;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;

import com.kleegroup.lord.ui.admin.controller.DialogFileOptionsController;

/**
 * Fenêtre d'option des fichiers.
 */
public final class DialogFileOptions extends JDialog {

	private static final long serialVersionUID = -6828356803936809959L;

	final ResourceBundle resourceMap = ResourceBundle.getBundle("resources.Administration");

	private class ActnOk extends AbstractAction {
		private static final long serialVersionUID = -2551078572811820430L;

		ActnOk() {
			this.putValue(NAME, resourceMap.getString("action.ok"));
		}

		/**{@inheritDoc}*/
		@Override
		public void actionPerformed(ActionEvent e) {
			setFileOptions();
		}
	}

	private class ActnCancel extends AbstractAction {
		private static final long serialVersionUID = 8418816007160731744L;

		ActnCancel() {
			this.putValue(NAME, resourceMap.getString("action.cancel"));
		}

		/**{@inheritDoc}*/
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}

	private javax.swing.JButton jbtnOk;
	private javax.swing.JButton jbtnCancel;
	private javax.swing.JComboBox<String> jcbEncoding;
	private javax.swing.JLabel jlblEncoding;
	private javax.swing.JLabel jlblSeparateurChamp;
	private javax.swing.JLabel jlblSeparateurDecimales;
	private javax.swing.JPanel jPanel;
	private javax.swing.JComboBox<String> jcbFieldSeparator;
	private javax.swing.JComboBox<String> jcbDecimalSeparator;

	private final DialogFileOptionsController controller;

	/**
	 * @param cntrl contrÃ´leur de la fenÃªtre
	 */
	public DialogFileOptions(DialogFileOptionsController cntrl) {
		this.controller = cntrl;
		initComponents();
		setEncoding(controller.getEncoding());
		setFieldSeparator(controller.getFieldSeparator());
		setDecimalSeparator(controller.getDecimalSeparator());
		setTitle(resourceMap.getString("window.options.files.title"));
	}

	private void setDecimalSeparator(String decimalSeparator) {
		jcbDecimalSeparator.setSelectedItem(decimalSeparator);
	}

	private void setFieldSeparator(String fieldSeparator) {
		jcbFieldSeparator.setSelectedItem(fieldSeparator);
	}

	private void setEncoding(String encoding) {
		jcbEncoding.setSelectedItem(encoding);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 */
	private void initComponents() {

		createComponents();

		setResizable(false);
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setText();
		final Set<String> s = Charset.availableCharsets().keySet();
		jcbEncoding.setModel(new DefaultComboBoxModel<String>((String[]) s.toArray()));
		jcbEncoding.setSelectedItem("ISO-8859-15");

		jcbFieldSeparator.setModel(new DefaultComboBoxModel<String>(new String[] { ",", ";" }));
		jcbEncoding.setSelectedIndex(1);
		jcbDecimalSeparator.setModel(new DefaultComboBoxModel<String>(new String[] { ",", "." }));
		jcbEncoding.setSelectedIndex(0);

		jbtnOk.setAction(new ActnOk());
		jbtnCancel.setAction(new ActnCancel());

		createLayout();
		pack();
		setLocationRelativeTo(null);
	}

	private void setText() {
		jlblEncoding.setText(resourceMap.getString("label.options.encoding"));
		jlblSeparateurChamp.setText(resourceMap.getString("label.options.field.separator"));
		jlblSeparateurDecimales.setText(resourceMap.getString("label.options.decimal.separator"));
	}

	private void createLayout() {
		java.awt.GridBagConstraints gridBagConstraints;
		getContentPane().setLayout(new java.awt.GridBagLayout());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		getContentPane().add(jlblEncoding, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		getContentPane().add(jlblSeparateurChamp, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		getContentPane().add(jlblSeparateurDecimales, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		getContentPane().add(jcbEncoding, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		getContentPane().add(jcbFieldSeparator, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		getContentPane().add(jcbDecimalSeparator, gridBagConstraints);

		jPanel.setLayout(new java.awt.GridLayout());
		jPanel.add(Box.createHorizontalGlue());
		jPanel.add(jbtnOk);
		jPanel.add(Box.createHorizontalGlue());
		jPanel.add(jbtnCancel);
		jPanel.add(Box.createHorizontalGlue());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		getContentPane().add(jPanel, gridBagConstraints);
	}

	private void createComponents() {
		jlblEncoding = new javax.swing.JLabel();
		jlblSeparateurChamp = new javax.swing.JLabel();
		jlblSeparateurDecimales = new javax.swing.JLabel();
		jcbEncoding = new javax.swing.JComboBox<String>();
		jcbFieldSeparator = new javax.swing.JComboBox<String>();
		jcbDecimalSeparator = new javax.swing.JComboBox<String>();
		jPanel = new javax.swing.JPanel();
		jbtnOk = new javax.swing.JButton();
		jbtnCancel = new javax.swing.JButton();
	}

	void setFileOptions() {
		controller.setEncoding(jcbEncoding.getSelectedItem().toString());
		controller.setDecimalSeparator(jcbDecimalSeparator.getSelectedItem().toString());
		controller.setFieldSeparator(jcbFieldSeparator.getSelectedItem().toString());

		dispose();
	}

}
