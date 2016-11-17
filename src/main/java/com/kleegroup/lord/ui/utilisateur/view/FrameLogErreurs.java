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

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeModel;

import com.kleegroup.lord.moteur.Fichier;
import com.kleegroup.lord.moteur.Schema;
import com.kleegroup.lord.moteur.util.IHierarchieSchema;
import com.kleegroup.lord.ui.common.model.FileTreeModel;
import com.kleegroup.lord.ui.utilisateur.controller.FrameLogErreursController;


/**
 * Frame qui affiche la liste des erreurs.
 */
public class FrameLogErreurs extends javax.swing.JPanel implements
	TreeSelectionListener {

    private static final DefaultTableModel EMPTY_TABLE_MODEL = new DefaultTableModel();

    private static final long serialVersionUID = 4261401487535832312L;
	
    protected DefaultMutableTreeNode topFileTreeNode = new DefaultMutableTreeNode(
	    "root");

    protected FileTreeModel fileTreeModel = null;

    protected FrameLogErreursController controller;

    private javax.swing.JScrollPane jScrollPaneTblErrorList;

    private javax.swing.JScrollPane jScrollPaneTreeFileList;

    private javax.swing.JSplitPane jSplitPane;

    private javax.swing.JTable jTblErrorList;

    private javax.swing.JTree jTreeFileList;

    /** Crée une nouvelle frame.
     * @param controller controlleur de la frame*/
    public FrameLogErreurs(FrameLogErreursController controller) {
	super();
	initComponents();
	this.controller = controller;
    }

    private void initComponents() {
	jSplitPane = new javax.swing.JSplitPane();
	jScrollPaneTreeFileList = new javax.swing.JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	jTreeFileList = new javax.swing.JTree();
	jTreeFileList.setModel(new FileTreeModel(new Schema()));
	
	jTreeFileList.addTreeWillExpandListener(new MyTreeWillExpander());
	
	
	jTreeFileList.setCellRenderer(new MyDefaultTreeCellRenderer());
	jScrollPaneTblErrorList = new javax.swing.JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	jTblErrorList = new javax.swing.JTable();
//	jTblErrorList.setAutoCreateRowSorter(true); // a remettre pour java 1.6

	jSplitPane.setContinuousLayout(true);

	jTreeFileList.setRootVisible(false);
	jTreeFileList.addTreeSelectionListener(this);
	jScrollPaneTreeFileList.setViewportView(jTreeFileList);

	jSplitPane.setLeftComponent(jScrollPaneTreeFileList);
	jScrollPaneTblErrorList.setViewportView(jTblErrorList);

	jSplitPane.setRightComponent(jScrollPaneTblErrorList);

	setLayout(new GridBagLayout());
	GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
	gridBagConstraints.weightx = 1.0;
	gridBagConstraints.weighty = 1.0;
	add(jSplitPane, gridBagConstraints);

	gridBagConstraints = new java.awt.GridBagConstraints();
	gridBagConstraints.gridx = 0;
	gridBagConstraints.gridy = 1;
	
	

    }

    /**
     * l'utilisateur a selectionné un fichier différent.
     * @param arg0 l'evenement de selection
     */
    @Override
	public void valueChanged(TreeSelectionEvent arg0) {
	final Object node = jTreeFileList
		.getLastSelectedPathComponent();

	// Nothing is selected.
	if (node==null || !((IHierarchieSchema)node).isFichier()) {
	    setTableModel(EMPTY_TABLE_MODEL);
	    return;
	}
	    controller.select((Fichier)node);
    }

    /**
     * @param tm le modèle de la table qui affiche la liste des erreurs
     */
    public void setTableModel(TableModel tm) {
	jTblErrorList.setModel(tm);
	final int size[];
	if(jTblErrorList.getColumnModel().getColumnCount()==4){
	    size=new int[]{5,15,15,65};
	}else{
	    size=new int[]{5,20,10,10,65};
	}
	for (int i=0;i<jTblErrorList.getColumnModel().getColumnCount();i++){
	    jTblErrorList.getColumnModel().getColumn(i).setPreferredWidth(size[i]*8);
	}
	jScrollPaneTblErrorList.getVerticalScrollBar().setValue(0);
    }


    /**
     * @param tm le mod�le de l'arbre qui affiche la liste des fichiers.
     */
    public void setTreeModel(TreeModel tm){
	jTreeFileList.setModel(tm);
	for(int i=0;i<jTreeFileList.getRowCount();i++){
	    jTreeFileList.expandRow(i);
	}
	int size=jTreeFileList.getPreferredSize().width+10;
	size=size<100?100:size;
	jSplitPane.setDividerLocation(size);
	
    }

    static class MyTreeWillExpander implements TreeWillExpandListener {

	    @Override
		public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
		throw new ExpandVetoException(event);
	    }
	    @Override
		public void treeWillExpand(TreeExpansionEvent event) {
		/*fais rien*/
	    }
    }
    
    static class MyDefaultTreeCellRenderer extends DefaultTreeCellRenderer {
	    private static final long serialVersionUID = 1L;
	 
	    @Override
	    public Component getTreeCellRendererComponent(JTree tree, Object value,
		    boolean sel,boolean expanded, boolean leaf,int row, boolean hasfocus){
		final Component c= super.getTreeCellRendererComponent
		(tree, value, sel, expanded, leaf, row, hasfocus);
		
		final Font nonBold=  new Font(c.getFont().getName(),Font.PLAIN,c.getFont().getSize()),
			bold= new Font(c.getFont().getName(),Font.BOLD,c.getFont().getSize());
		 
		if (((FileTreeModel)tree.getModel()).isBold(value)){
		    c.setFont(bold);
		}else{
		    c.setFont(nonBold);
		}
		return c;
	    }
	}
}
