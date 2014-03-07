package spark.reprise.outil.ui.admin.view;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import spark.reprise.outil.moteur.Fichier;
import spark.reprise.outil.moteur.util.IHierarchieSchema;
import spark.reprise.outil.ui.admin.common.view.ColonneChooser;
import spark.reprise.outil.ui.admin.controller.FenetrePrincipaleAdminController;

/**
 * Fenetre Principale de l'interface administrateur.
 */
public class FenetrePrincipaleAdmin extends JFrame {

    private static final long serialVersionUID = 7518219448243697823L;

    private static final String ORD_TITLE = "Outil de Reprise - Interface Administrateur";

    TableCellEditor currentCellEditor;

    final FenetrePrincipaleAdminController controller;

    private class ActnMoveFileUp extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnMoveFileUp() {
	    super("", new ImageIcon(ClassLoader.
			getSystemResource("resources/images/go-up.png")));
	    putValue(SHORT_DESCRIPTION,"Déplacer le fichier ou la catégorie séléctionnés vers le haut.");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    moveFileUp();
	}

    }

    private class ActnMoveFileDn extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnMoveFileDn() {
	    super("", new ImageIcon(ClassLoader.
			getSystemResource("resources/images/go-down.png")));
	    putValue(SHORT_DESCRIPTION,"Déplacer le fichier ou la catégorie séléctionnés vers le bas.");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    moveFileDn();
	}

    }

    private class ActnDelFileCat extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnDelFileCat() {
	    super("", new ImageIcon(ClassLoader.
			getSystemResource("resources/images/delete.gif")));
	    putValue(SHORT_DESCRIPTION,"Supprimer le fichier ou la catégorie séléctionnés.");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    delFileCat();
	}

    }

    private class ActnMoveColDn extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnMoveColDn() {
	    super("", new ImageIcon(ClassLoader.
			getSystemResource("resources/images/go-down.png")));
	    putValue(SHORT_DESCRIPTION,"Déplacer la colonne séléctionée vers la droite dans le fichier.");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    moveColDn();
	}

    }

    private class ActnMoveColUp extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnMoveColUp() {
	    super("", new ImageIcon(ClassLoader.
			getSystemResource("resources/images/go-up.png")));
	    putValue(SHORT_DESCRIPTION,"Déplacer la colonne séléctionée vers la gauche dans le fichier.");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    moveColUp();
	}

    }

    private class ActnDeleteCol extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnDeleteCol() {
	    super("", new ImageIcon(ClassLoader.
			getSystemResource("resources/images/delete.gif")));
	    putValue(SHORT_DESCRIPTION,"Supprimer la colonne séléctionée.");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteCol();
	}

    }

    private class ActnNewCol extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnNewCol() {
	    super("", new ImageIcon(ClassLoader.
			getSystemResource("resources/images/add.gif")));
	    putValue(SHORT_DESCRIPTION,"Ajouter une nouvelle colonne au fichier.");

	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    newCol();
	}

    }

    private class ActnNewFile extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnNewFile() {
	    super("", new ImageIcon(ClassLoader.
			getSystemResource("resources/images/add.gif")));
	    putValue(SHORT_DESCRIPTION,"Ajouter un nouveau fichier au schéma.");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    newFile();
	}

    }
    private class ActnNewCat extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnNewCat() {
	    super("", new ImageIcon(ClassLoader.
			getSystemResource("resources/images/add_cat.gif")));
	    putValue(SHORT_DESCRIPTION,"Ajouter une nouvelle catégorie au schéma.");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    newCat();
	}

    }

    private class ActnShowGeneralOptions extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnShowGeneralOptions() {
	    this.putValue(NAME, "Options générales...");
	    putValue(MNEMONIC_KEY, KeyEvent.VK_G);
	    putValue(SHORT_DESCRIPTION,"Afficher les options générales du schéma.");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    showGeneralOptions();
	}

    }

    private class ActnShowFileOptions extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnShowFileOptions() {
	    this.putValue(NAME, "Options des fichiers ...");
	    putValue(MNEMONIC_KEY, KeyEvent.VK_F);
	    putValue(SHORT_DESCRIPTION,"Afficher les options de lecture des fichiers.");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    showFileOptions();
	}

    }

    private class ActnShowMultiColConstraints extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnShowMultiColConstraints() {
	    this.putValue(NAME, "...");
	    putValue(SHORT_DESCRIPTION,"Modifier les vérifications spécifiques du fichier.");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    showMultiColConstraints();
	}

    }

    private class ActnExit extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnExit() {
	    super("Quitter", new ImageIcon(ClassLoader.
			getSystemResource("resources/images/exit.png")));
	    putValue(MNEMONIC_KEY, KeyEvent.VK_Q);
	    putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
	    putValue(SHORT_DESCRIPTION,"Quitter l'application.");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    exit();
	}

    }

    private class ActnOpen extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnOpen() {
	    super("Ouvrir", new ImageIcon(ClassLoader.
			getSystemResource("resources/images/document-open.png")));
	    putValue(MNEMONIC_KEY, KeyEvent.VK_O);
	    putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
	    putValue(SHORT_DESCRIPTION,"Ouvrir un nouveau schéma.");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    openFile();
	}

    }

    private class ActnSave extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnSave() {
	    super("Sauvegarder", new ImageIcon(ClassLoader
		    .getSystemClassLoader().getResource(
			    "resources/images/document-save.png")));
	    putValue(MNEMONIC_KEY, KeyEvent.VK_S);
	    putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
	    putValue(SHORT_DESCRIPTION,"Sauvegarder le schéma actuel.");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    saveFile();
	}

    }

    private class ActnSaveAs extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnSaveAs() {
	    super("Sauvegarder sous...", new ImageIcon(ClassLoader
		    .getSystemClassLoader().getResource(
			    "resources/images/document-save-as.png")));
	    putValue(MNEMONIC_KEY, KeyEvent.VK_A);
	    putValue(SHORT_DESCRIPTION,"Sauvegarder le schéma actuel dans un fichier différent.");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    saveAs();
	}

    }

    private class ActnDuplicateFile extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnDuplicateFile() {
	    super("", new ImageIcon(ClassLoader.
			getSystemResource("resources/images/duplicate-file.png")));
	    putValue(SHORT_DESCRIPTION,"Dupliquer le fichier séléctionné.");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    duplicateFile();
	}

    }

    private class ActnNewSchema extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnNewSchema() {
	    super("Nouveau Schema", new ImageIcon(ClassLoader
		    .getSystemClassLoader().getResource(
			    "resources/images/document-new.png")));
	    putValue(MNEMONIC_KEY, KeyEvent.VK_N);
	    putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(Character
		    .valueOf('n'), java.awt.event.InputEvent.CTRL_MASK));
	    putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
	    putValue(SHORT_DESCRIPTION,"Créer un nouveau schéma.");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    newSchema();
	}

    }

    private class ActnVerifSchema extends AbstractAction {

	private static final long serialVersionUID = -2551078572811820430L;

	ActnVerifSchema() {
	    super("Verifier le schema", new ImageIcon(ClassLoader
		    .getSystemClassLoader().getResource(
			    "resources/images/validate.gif")));
	    putValue(MNEMONIC_KEY, KeyEvent.VK_V);
	    putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("F5"));
	    putValue(SHORT_DESCRIPTION,"Vérifier que le schéma est correct.");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
	    verifySchema();
	}

    }

    javax.swing.JTree jtreeFileList;

    private javax.swing.JButton jbtnFileNew;

    private javax.swing.JButton jbtnShowMultiColConstraints;

    private javax.swing.JButton jbtnDuplicateFile;

    private javax.swing.JButton jbtnCatNew;
    
    private javax.swing.JButton jbtnFileDel;

    private javax.swing.JButton jbtnFileTreeMoveUp;

    private javax.swing.JButton jbtnFileTreeMoveDn;

    private javax.swing.JButton jbtnColNew;

    private javax.swing.JButton jbtnColDel;

    private javax.swing.JButton jbtnColMoveUp;

    private javax.swing.JButton jbtnColMoveDn;

    private javax.swing.JLabel jlblNomFichier;

    private javax.swing.JLabel jmbmExtensionFichier;

    private javax.swing.JLabel jlblGroupeFichier;

    private javax.swing.JLabel jlblEnteteFichier;

    private javax.swing.JLabel jlblSeuilErreurFichier;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JMenuBar jMenuBar;

    private javax.swing.JPanel jpnlTree;

    private javax.swing.JPanel jpnlContent;

    private javax.swing.JPanel jpnlLine1;

    private javax.swing.JPanel jpnlLine2;

    private javax.swing.JPanel jpnlTable;

    private javax.swing.JScrollPane jScrollPaneTable;

    private javax.swing.JScrollPane jScrollPaneTree;

    private javax.swing.JSpinner jspnFileGroup;

    private javax.swing.JSpinner jspnFileHeader;

    private javax.swing.JSpinner jspnFileErrorLimit;

    private javax.swing.JTable jtblFileDetails;

    private javax.swing.JTextField jtxtNamePrefix;

    private javax.swing.JTextField jtxtExtension;
    
    private final int defaultRowHeight;

    /**
         * Cree la fenetre principale de l'interface administrateur.
         * 
         * @param controller
         *                le controlleur de la fenetre
         */
    public FenetrePrincipaleAdmin(FenetrePrincipaleAdminController controller) {
	this.controller = controller;
	initComponents();
	defaultRowHeight=(int)jtreeFileList.getCellRenderer().
	getTreeCellRendererComponent(jtreeFileList, new Fichier("ko","ko"), false, false, false, 0, false)
	.getPreferredSize().getHeight();
    }

    void newCat() {
	stopCurrentCellEditing();
	controller.addCategorie();
	expandFiles();
    }

    private void initComponents() {
	createComponents();

	setIconImage((new ImageIcon(ClassLoader.
		getSystemResource("resources/images/logo-admin.png"))).getImage());
	
//	setIconImage((new ImageIcon(ClassLoader.getSystemClassLoader()
//		.getResource("resources/images/logo-admin.png"))).getImage());

	jpnlTree.setBorder(javax.swing.BorderFactory
		.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
	
	jpnlContent.setBorder(javax.swing.BorderFactory
		.createLineBorder(new java.awt.Color(0, 0, 0)));

	jScrollPaneTree.setViewportView(jtreeFileList);
	jScrollPaneTable.setViewportView(jtblFileDetails);

	jtxtExtension.setPreferredSize(new java.awt.Dimension(50, 20));

	final DefaultTreeCellRenderer dtcr = new DefaultTreeCellRenderer();
	dtcr.setLeafIcon(new ImageIcon(ClassLoader.
		getSystemResource("resources/images/text.png")));
	jtreeFileList.setCellRenderer(dtcr);
	jtreeFileList.getSelectionModel().setSelectionMode
        	(TreeSelectionModel.SINGLE_TREE_SELECTION);

	setNames();
	setText();

	createLayout();
	createMenu();

	this.attachActions();

	setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
	addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent w) {
		controller.exit();
	    }

	});

	jtreeFileList.setModel(controller.getTreeModel());
	jtreeFileList.setRootVisible(false);
	jtreeFileList.addTreeSelectionListener(new TreeSelectionListener() {

	    @Override
		public void valueChanged(TreeSelectionEvent e) {
		stopCurrentCellEditing();
		final IHierarchieSchema selectedObj=
		    (IHierarchieSchema)jtreeFileList.getLastSelectedPathComponent();

		if (selectedObj == null) {
		    return;
		}
		if( selectedObj.isFichier()){
		    controller.select((Fichier)selectedObj);
		}else{
		    controller.selectNothing();
		}

	    }
	});
	jtreeFileList.addTreeWillExpandListener(new MyTreeWillExpandListener());
	jtreeFileList.setEditable(true);

	jScrollPaneTable
		.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

	jspnFileGroup
		.setModel(new SpinnerNumberModel(0, 0, Short.MAX_VALUE, 1));
	jspnFileHeader
		.setModel(new SpinnerNumberModel(0, 0, Short.MAX_VALUE, 1));
	jspnFileErrorLimit.setModel(new SpinnerNumberModel(-1, -1,
		Short.MAX_VALUE, 1));

	pack();
	setLocationRelativeTo(null);
    }

    private void setNames() {
	jpnlTree.setName("jPanel1");
	jScrollPaneTree.setName("jScrollPane2");
	jtreeFileList.setName("jTree2");
	jbtnFileNew.setName("jButton1");
	jbtnDuplicateFile.setName("jButton2");
	jbtnFileDel.setName("jButton3");
	jbtnFileTreeMoveUp.setName("jButton4");
	jbtnFileTreeMoveDn.setName("jButton5");
	jpnlContent.setName("jPanel2");
	jpnlLine1.setName("jPanel3");
	jlblNomFichier.setName("jLabel1");
	jtxtNamePrefix.setName("jTextField1");
	jmbmExtensionFichier.setName("jLabel2");
	jtxtExtension.setName("jTextField2");
	jpnlLine2.setName("jPanel4");
	jlblGroupeFichier.setName("jLabel3");
	jspnFileGroup.setName("jSpinner1");
	jlblEnteteFichier.setName("jLabel4");
	jlblSeuilErreurFichier.setName("jLabel5");
	jpnlTable.setName("jPanel5");
	jspnFileHeader.setName("jSpinner2");
	jScrollPaneTable.setName("jScrollPane3");
	jtblFileDetails.setName("jTable1");
	jbtnColNew.setName("jButton6");
	jbtnColDel.setName("jButton7");
	jbtnColMoveUp.setName("jButton8");
	jbtnColMoveDn.setName("jButton9");
	jbtnShowMultiColConstraints.setName("jButton10");

    }

    private void setText() {
	setTitle(ORD_TITLE);
	jlblNomFichier.setText("D�but du nom du fichier");
	jmbmExtensionFichier.setText("Extension");
	jlblGroupeFichier.setText("N Groupe du fichier");
	jlblEnteteFichier.setText("Nombre de ligne d'ent�te");
	jlblSeuilErreurFichier.setText("Seuil d'erreurs");
    }

    private void createLayout() {
	java.awt.GridBagConstraints gridBagConstraints;
	getContentPane().setLayout(new java.awt.GridBagLayout());
	jpnlTree.setLayout(new java.awt.GridBagLayout());
	jpnlContent.setLayout(new java.awt.GridBagLayout());
	jpnlLine1.setLayout(new java.awt.GridBagLayout());
	jpnlLine2.setLayout(new java.awt.GridBagLayout());
	jpnlTable.setLayout(new java.awt.GridBagLayout());

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
	gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
	getContentPane().add(jpnlTree, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 1;
	gridBagConstraints.gridy = 0;
	gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
	gridBagConstraints.weightx = 1.0;
	gridBagConstraints.weighty = 1.0;
	gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
	getContentPane().add(jpnlContent, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 0;
	gridBagConstraints.gridy = 0;
	gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
	gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
	gridBagConstraints.weightx = 1.0;
	gridBagConstraints.weighty = 1.0;
	jpnlTree.add(jScrollPaneTree, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 0;
	gridBagConstraints.gridy = 1;
	gridBagConstraints.weightx = 1.0;
	jpnlTree.add(jbtnCatNew, gridBagConstraints);
	
	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 1;
	gridBagConstraints.gridy = 1;
	gridBagConstraints.weightx = 1.0;
	jpnlTree.add(jbtnFileNew, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 2;
	gridBagConstraints.gridy = 1;
	gridBagConstraints.weightx = 1.0;
	jpnlTree.add(jbtnDuplicateFile, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 3;
	gridBagConstraints.gridy = 1;
	gridBagConstraints.weightx = 1.0;
	jpnlTree.add(jbtnFileDel, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 4;
	gridBagConstraints.gridy = 1;
	gridBagConstraints.weightx = 1.0;
	jpnlTree.add(jbtnFileTreeMoveUp, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 5;
	gridBagConstraints.gridy = 1;
	gridBagConstraints.weightx = 1.0;
	jpnlTree.add(jbtnFileTreeMoveDn, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
	jpnlContent.add(jpnlLine1, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 0;
	gridBagConstraints.gridy = 1;
	gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
	jpnlContent.add(jpnlLine2, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 0;
	gridBagConstraints.gridy = 2;
	gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
	gridBagConstraints.weightx = 1.0;
	gridBagConstraints.weighty = 1.0;
	gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
	jpnlContent.add(jpnlTable, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
	jpnlLine1.add(jlblNomFichier, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints.weightx = 1.0;
	jpnlLine1.add(jtxtNamePrefix, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
	jpnlLine1.add(jmbmExtensionFichier, gridBagConstraints);

	jpnlLine1.add(jtxtExtension, new java.awt.GridBagConstraints());

	createLayoutPart2();
    }

    private void createLayoutPart2() {
	java.awt.GridBagConstraints gridBagConstraints;
	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
	jpnlLine2.add(jlblGroupeFichier, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints.weightx = 1.0;
	jpnlLine2.add(jspnFileGroup, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
	jpnlLine2.add(jlblEnteteFichier, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints.weightx = 1.0;
	jpnlLine2.add(jspnFileHeader, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
	jpnlLine2.add(jlblSeuilErreurFichier, gridBagConstraints);

	jspnFileErrorLimit.setName("jSpinner3");
	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints.weightx = 1.0;
	jpnlLine2.add(jspnFileErrorLimit, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 0;
	gridBagConstraints.gridy = 0;
	gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
	gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
	gridBagConstraints.ipadx = 1;
	gridBagConstraints.ipady = 1;
	gridBagConstraints.weightx = 1.0;
	gridBagConstraints.weighty = 1.0;
	jpnlTable.add(jScrollPaneTable, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 0;
	gridBagConstraints.gridy = 1;
	gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	jpnlTable.add(jbtnColNew, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 1;
	gridBagConstraints.gridy = 1;
	gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	jpnlTable.add(jbtnColDel, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 2;
	gridBagConstraints.gridy = 1;
	gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	jpnlTable.add(jbtnColMoveUp, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 3;
	gridBagConstraints.gridy = 1;
	gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
	gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	jpnlTable.add(jbtnColMoveDn, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 4;
	gridBagConstraints.gridy = 1;
	gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
	gridBagConstraints.weightx = 1.0;
	jpnlTable.add(jLabel6, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 5;
	gridBagConstraints.gridy = 1;
	gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
	jpnlTable.add(jbtnShowMultiColConstraints, gridBagConstraints);
    }

    private void createMenu() {
	final javax.swing.JMenu jmnuFile = new javax.swing.JMenu();
	final javax.swing.JMenu jmnuTools = new javax.swing.JMenu();

	jmnuFile.setText("Fichier");
	jmnuFile.setMnemonic('F');
	jMenuBar.add(jmnuFile);

	jmnuTools.setText("Outils");
	jMenuBar.add(jmnuTools);
	jmnuTools.setMnemonic('T');

	final javax.swing.JMenuItem jmiNew = new javax.swing.JMenuItem(
		new ActnNewSchema());
	final javax.swing.JMenuItem jmiOpen = new javax.swing.JMenuItem(
		new ActnOpen());
	final javax.swing.JMenuItem jmiSave = new javax.swing.JMenuItem(
		new ActnSave());
	final javax.swing.JMenuItem jmiSaveAs = new javax.swing.JMenuItem(
		new ActnSaveAs());
	final javax.swing.JMenuItem jmiExit = new javax.swing.JMenuItem(
		new ActnExit());

	jmnuFile.add(jmiNew);
	jmnuFile.add(jmiOpen);
	jmnuFile.add(jmiSave);
	jmnuFile.add(jmiSaveAs);
	jmnuFile.addSeparator();
	jmnuFile.add(jmiExit);

	final javax.swing.JMenuItem jmiGeneralOptions = new javax.swing.JMenuItem(
		new ActnShowGeneralOptions());
	final javax.swing.JMenuItem jmiFileOptions = new javax.swing.JMenuItem(
		new ActnShowFileOptions());
	final javax.swing.JMenuItem jmiVerifSchema = new javax.swing.JMenuItem(
		new ActnVerifSchema());

	jmnuTools.add(jmiGeneralOptions);
	jmnuTools.add(jmiFileOptions);
	jmnuTools.addSeparator();
	jmnuTools.add(jmiVerifSchema);

	setJMenuBar(jMenuBar);
    }

    private void createComponents() {
	jpnlTree = new javax.swing.JPanel();
	jScrollPaneTree = new javax.swing.JScrollPane();
	jtreeFileList = new javax.swing.JTree();
	jbtnFileNew = new javax.swing.JButton();
	jbtnDuplicateFile = new javax.swing.JButton();
	jbtnFileDel = new javax.swing.JButton();
	jbtnFileTreeMoveUp = new javax.swing.JButton();
	jbtnFileTreeMoveDn = new javax.swing.JButton();
	jpnlContent = new javax.swing.JPanel();
	jpnlLine1 = new javax.swing.JPanel();
	jlblNomFichier = new javax.swing.JLabel();
	jtxtNamePrefix = new javax.swing.JTextField();
	jmbmExtensionFichier = new javax.swing.JLabel();
	jtxtExtension = new javax.swing.JTextField();
	jpnlLine2 = new javax.swing.JPanel();
	jlblGroupeFichier = new javax.swing.JLabel();
	jspnFileGroup = new javax.swing.JSpinner();
	jlblEnteteFichier = new javax.swing.JLabel();
	jspnFileHeader = new javax.swing.JSpinner();
	jlblSeuilErreurFichier = new javax.swing.JLabel();
	jspnFileErrorLimit = new javax.swing.JSpinner();
	jpnlTable = new javax.swing.JPanel();
	jScrollPaneTable = new javax.swing.JScrollPane();

	jbtnColNew = new javax.swing.JButton();
	jbtnColDel = new javax.swing.JButton();
	jbtnColMoveUp = new javax.swing.JButton();
	jbtnColMoveDn = new javax.swing.JButton();
	jLabel6 = new javax.swing.JLabel();
	jbtnShowMultiColConstraints = new javax.swing.JButton();
	jMenuBar = new javax.swing.JMenuBar();
	
	jbtnCatNew=new javax.swing.JButton();

	initColTable();

    }

    private void initColTable() {
	jtblFileDetails = new MyJTable(currentCellEditor, controller);
	jtblFileDetails.setModel(controller.getTableModel());
	jtblFileDetails.setColumnSelectionAllowed(true);
	jtblFileDetails.setRowSelectionAllowed(true);
	jtblFileDetails.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

	resetTableEditors();

	adjustTableColumnSize();

    }

    private void resetTableEditors() {
	jtblFileDetails.getColumnModel().getColumn(2).setCellEditor(
		new DefaultCellEditor(
			new JComboBox(controller.getObFacValues())));
	jtblFileDetails.getColumnModel().getColumn(5)
		.setCellEditor(
			new DefaultCellEditor(new JComboBox(controller
				.getTypeValues())));

    }

    private void adjustTableColumnSize() {
	// Calcul des tailles optimales des colonnes
	// on utilise unqiuement les donn�es du premier fichier (selectionn� par
	// defaut)
	for (int i = 0; i < jtblFileDetails.getColumnCount(); i++) {
	    final TableColumn column = jtblFileDetails.getColumnModel()
		    .getColumn(i);

	    Component comp = jtblFileDetails.getTableHeader()
		    .getDefaultRenderer().getTableCellRendererComponent(null,
			    column.getHeaderValue(), false, false, 0, 0);
	    final int headerWidth = comp.getPreferredSize().width + 20;

	    int cellWidth = 0;
	    for (int j = 0; j < jtblFileDetails.getRowCount(); j++) {

		comp = jtblFileDetails.getDefaultRenderer(
			controller.getTableModel().getColumnClass(i))
			.getTableCellRendererComponent(jtblFileDetails,
				controller.getTableModel().getValueAt(j, i),
				false, false, 0, i);
		cellWidth = Math.max(cellWidth,
			comp.getPreferredSize().width + 20);
	    }

	    column.setPreferredWidth(Math.max(headerWidth, cellWidth));
	}
    }

    void stopCurrentCellEditing() {
	if (currentCellEditor != null) {// corrige un bug de jtable
	    currentCellEditor.cancelCellEditing();
	}
    }

    /**
         * @param model
         *                le mod�le de la table des colonnes.
         */
    public void setTableModel(TableModel model) {
	jtblFileDetails.setModel(model);
	if(model.getColumnCount()>9){
	jtblFileDetails.getColumnModel().getColumn(9).setCellEditor(
		new ColonneChooser(controller.getSchema(), controller
			.getCurrentFichier()));
	}
    }

    private void attachActions() {
	jbtnCatNew.setAction(new ActnNewCat());
	jbtnFileNew.setAction(new ActnNewFile());
	jbtnFileDel.setAction(new ActnDelFileCat());
	jbtnFileTreeMoveUp.setAction(new ActnMoveFileUp());
	jbtnFileTreeMoveDn.setAction(new ActnMoveFileDn());
	jbtnDuplicateFile.setAction(new ActnDuplicateFile());

	jbtnColNew.setAction(new ActnNewCol());
	jbtnColMoveUp.setAction(new ActnMoveColUp());
	jbtnColMoveDn.setAction(new ActnMoveColDn());

	jbtnShowMultiColConstraints
		.setAction(new ActnShowMultiColConstraints());

	jbtnColDel.setAction(new ActnDeleteCol());
	jtxtNamePrefix.getDocument().addDocumentListener(
		new DocumentListener() {

		    @Override
			public void changedUpdate(DocumentEvent e) {
			setFileNamePrefix();
		    }

		    @Override
			public void insertUpdate(DocumentEvent e) {
			setFileNamePrefix();
		    }

		    @Override
			public void removeUpdate(DocumentEvent e) {
			setFileNamePrefix();
		    }
		});

	jtxtExtension.getDocument().addDocumentListener(new DocumentListener() {

	    @Override
		public void changedUpdate(DocumentEvent e) {
		setFileExtension();
	    }

	    @Override
		public void insertUpdate(DocumentEvent e) {
		setFileExtension();

	    }

	    @Override
		public void removeUpdate(DocumentEvent e) {
		setFileExtension();

	    }

	});

	jspnFileGroup.addChangeListener(new ChangeListener() {

	    @Override
		public void stateChanged(ChangeEvent e) {
		controller.setFileGroup(getFileGroup());
	    }
	});

	jspnFileHeader.addChangeListener(new ChangeListener() {

	    @Override
		public void stateChanged(ChangeEvent e) {
		controller.setFileHeaderLinesCount(getFileHeaderLinesCount());
	    }
	});
	jspnFileErrorLimit.addChangeListener(new ChangeListener() {

	    @Override
		public void stateChanged(ChangeEvent e) {
		controller.setFileErrorLimit(getFileErrorLimit());
	    }
	});

    }

    /**
         * @return la valeur que l'utilisateur a choisi pour PrefixNom
         */
    public String getNamePrefix() {
	return jtxtNamePrefix.getText();
    }

    /**
         * @param nom
         *                la valeur de PrefixNom à afficher à l'utilisateur
         */
    public void setNamePrefix(String nom) {
	jtxtNamePrefix.setText(nom);
    }

    /**
         * @return l'extension que l'utilisateur a choisi
         */
    public String getFileExtension() {
	return jtxtExtension.getText();
    }

    /**
         * @param extension
         *                la valeur de l'extension à afficher à l'utilisateur
         */
    public void setFileExtension(String extension) {
	jtxtExtension.setText(extension);
    }

    /**
         * @return le num�ro du groupe du fichier que l'utilisateur a choisi
         */
    public int getFileGroup() {
	return (Integer) jspnFileGroup.getValue();
    }

    /**
         * @return le nombre de ligne d'entete que l'utilisateur a choisi
         */
    public int getFileHeaderLinesCount() {
	return (Integer) jspnFileHeader.getValue();
    }

    /**
         * @return le seuil d'erreurs que l'utilisateur a choisi
         */
    public int getFileErrorLimit() {
	return (Integer) jspnFileErrorLimit.getValue();
    }

    /**
         * @param value
         *                le numéro du groupe du fichier à afficher à
         *                l'utilisateur
         */
    public void setFileGroupNumber(int value) {
	jspnFileGroup.setValue(value);
    }

    /**
         * @param value
         *                le nombre de ligne d'entête à afficher à l'utilisateur
         */
    public void setFileHeaderLinesCount(int value) {
	jspnFileHeader.setValue(value);
    }

    /**
         * @param value
         *                le seuil d'erreurs à afficher à l'utilisateur
         */
    public void setFileErrorLimit(int value) {
	jspnFileErrorLimit.setValue(value);
    }

    /**
         * synchronisre l'affichage avec le controlleur.<br>
         * Utilis� suite � des modifications, pour r�fl�ter ces modifications
         * dans l'affichage. par exemple, lors de la selection d'un nouveau
         * fichier
         */
    public void refresh() {
	jtreeFileList.setModel(controller.getTreeModel());
	expandFiles();
	

	jtblFileDetails.setModel(controller.getTableModel());
	resetTableEditors();
	adjustTableColumnSize();
	
	showFileDetails(false);
	refreshTitle();
    }

    private void expandFiles() {
	for(int i=0;i<jtreeFileList.getRowCount();i++){
	    jtreeFileList.expandRow(i);
	}
	
    }

    /**
         * synchronise le titre de la fenetre suite a une modification.
         */
    public void refreshTitle() {
	setTitle(controller.getTitle() + " - " + ORD_TITLE);
    }

    /**
         * @param currentFileMultiColContraintesCount
         *                le nombre de v�rifications � afficher � l'utilisateur
         */
    public void setFileContraintesMultiColCount(
	    int currentFileMultiColContraintesCount) {
	jLabel6.setText(currentFileMultiColContraintesCount
		+ " V�rification(s) sp�cifique(s)");

    }

    void moveFileUp() {
	final TreePath selectedFile=jtreeFileList.getSelectionPath();
	final TreePath res=controller.moveFileUp(selectedFile);
	expandFiles();
	jtreeFileList.setSelectionPath(res);
	scrollToVisibleRow();
    }

    void moveFileDn() {
	final TreePath selectedFile=jtreeFileList.getSelectionPath();
	final TreePath res=controller.moveFileDn(selectedFile);
	expandFiles();
	jtreeFileList.setSelectionPath(res);
	scrollToVisibleRow();
    }
    
    

    private void scrollToVisibleRow() {
	if(jtreeFileList.getSelectionRows()!=null){
	    final Rectangle rectVis = new Rectangle(0, (jtreeFileList
		.getSelectionRows()[0] -1 )
		* defaultRowHeight, 0,
		3* defaultRowHeight);
	jtreeFileList.scrollRectToVisible(rectVis);
	}
    }

    void delFileCat() {
	final TreePath tp=controller.deleteFile(jtreeFileList.getSelectionPath());
	System.out.println(tp);
	jtreeFileList.setSelectionPath(tp);
	refreshTitle();
	expandFiles();
    }

    void moveColDn() {
	final int selectedRow = jtblFileDetails.getSelectedRow();
	if (selectedRow < jtblFileDetails.getRowCount() - 1) {
	controller.moveColDn(selectedRow);
	
	    jtblFileDetails
		    .setRowSelectionInterval(
			    jtblFileDetails.getSelectedRow() + 1,
			    jtblFileDetails.getSelectedRow() + 1);
	
	final Rectangle rectVis = new Rectangle(0, (jtblFileDetails
		.getSelectedRow() + 2)
		* jtblFileDetails.getRowHeight(), 0, 0);
	jtblFileDetails.scrollRectToVisible(rectVis);
	}
    }

    void moveColUp() {
	final int selectedRow = jtblFileDetails.getSelectedRow();
	
	if (selectedRow > 0) {
	    controller.moveColUp(selectedRow);
	    jtblFileDetails
		    .setRowSelectionInterval(
			    jtblFileDetails.getSelectedRow() - 1,
			    jtblFileDetails.getSelectedRow() - 1);
	
	final Rectangle rectVis = new Rectangle(0, (jtblFileDetails
		.getSelectedRow() - 1)
		* jtblFileDetails.getRowHeight(), 0, 0);
	jtblFileDetails.scrollRectToVisible(rectVis);
	}
    }

    void deleteCol() {
	final int selectedRow = jtblFileDetails.getSelectedRow();
	controller.deleteColonne(selectedRow);
	if (jtblFileDetails.getRowCount() > 0
		&& selectedRow <= jtblFileDetails.getRowCount()) {
	    if (selectedRow == jtblFileDetails.getRowCount()) {
		jtblFileDetails.setRowSelectionInterval(selectedRow - 1,
			selectedRow - 1);
	    } else if (selectedRow >= 0) {
		jtblFileDetails.setRowSelectionInterval(selectedRow,
			selectedRow);
	    }
	}
    }

    void newCol() {
	final int selectedRow = jtblFileDetails.getSelectedRow();
	stopCurrentCellEditing();
	controller.addColonne(selectedRow);
	if (selectedRow >= 0) {
	    jtblFileDetails.setRowSelectionInterval(selectedRow + 1,
		    selectedRow + 1);
	    jtblFileDetails.requestFocusInWindow();
	    jtblFileDetails.editCellAt(selectedRow, 0);

	}
    }

    void newFile() {
	stopCurrentCellEditing();
	final TreePath select=jtreeFileList.getSelectionPath();
	controller.addFichier(select);
	jtreeFileList.setSelectionPath(select);
	expandFiles();
    }

    void showGeneralOptions() {
	controller.showGeneralOptions();
    }

    void showFileOptions() {
	controller.showFileOptions();
    }

    void showMultiColConstraints() {
	controller.showMultiColConstraints();
    }

    void exit() {
	controller.exit();
    }

    void openFile() {
	controller.openFile();
    }

    void saveFile() {
	controller.save();
    }

    void saveAs() {
	controller.saveAs();
    }

    void duplicateFile() {
	jtreeFileList.setSelectionPath(
		controller.duplicateFile(jtreeFileList.getSelectionPath()));
	expandFiles();
    }

    void newSchema() {
	controller.newSchema();
    }

    void verifySchema() {
	controller.verifySchema();
    }

    void setFileExtension() {
	controller.setFileExtension(jtxtExtension.getText());
    }

    void setFileNamePrefix() {
	controller.setFileNamePrefix(jtxtNamePrefix.getText());
    }

    /**
     * Affiche la zone "détails des fichiers".  Cette zone contient la table
     * des colonnes ainsi que le reste des paramètres du fichier.
     * @param etat true s'il faut l'afficher/
     */
    public void showFileDetails(boolean etat) {
	jpnlTable.setVisible(etat);
	jpnlLine1.setVisible(etat);
	jpnlLine2.setVisible(etat);
	
    }

    static class MyTreeWillExpandListener implements TreeWillExpandListener {

	    @Override
		public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
		throw new ExpandVetoException(event);
	    }

	    @Override
		public void treeWillExpand(TreeExpansionEvent event)  {
	    /*fais rien*/
	    }
	}
    
    static class MyJTable extends JTable {
	    private static final long serialVersionUID = 1L;
	    private TableCellEditor currentCellEditor;
	    private final FenetrePrincipaleAdminController controller;
	    
	    /**
	     * Constructeur MyJTable
	     * 
	     * @param currentCellEditor paramètre à définir
	     * @param controller paramètre à définir
	     */
	    public MyJTable(TableCellEditor currentCellEditor, FenetrePrincipaleAdminController controller) {
	    	super();
	    	this.currentCellEditor = currentCellEditor;
	    	this.controller = controller;
	    }
	    
	    @Override
	    public TableCellRenderer getCellRenderer(int row, int column) {
		return super.getCellRenderer(row, column);
	    }

	    @Override
	    public TableCellEditor getCellEditor(int row, int column) {
		currentCellEditor = super.getCellEditor(row, column);
		return currentCellEditor;
	    }

	    @Override
	    public boolean isCellEditable(int row, int col) {
		return true;
	    }

	    @Override
	    public void setValueAt(Object value, int row, int col) {
		controller.changeFileValue(value, row, col);
	    }
	}
}
