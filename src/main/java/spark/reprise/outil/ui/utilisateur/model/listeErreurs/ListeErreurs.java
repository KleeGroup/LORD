/**
 * 
 */
package spark.reprise.outil.ui.utilisateur.model.listeErreurs;

import java.util.List;
import java.util.ResourceBundle;

import spark.reprise.outil.moteur.IErreur;

/**
 *Classe publique qui sert à afficher la liste des erreurs dans le FrameLogErreur.
 *
 */
public abstract class ListeErreurs extends javax.swing.table.AbstractTableModel {
    protected static final ResourceBundle RESOURCEMAP = ResourceBundle
    .getBundle("resources.GeneralUIMessages");
    
    private static final long serialVersionUID = 4157087119873576473L;
    
    protected String nomColonne[];

    List<IErreur> listeErreurs;

   
   
    
    
    protected ListeErreurs(List<IErreur> listeErreurs) {
	this.listeErreurs = listeErreurs;
    }

    /**{@inheritDoc}*/
    @Override
	public int getRowCount() {
	return listeErreurs.size();
    }

    /**{@inheritDoc}*/
    @Override
	public int getColumnCount() {
	return nomColonne.length;
    }

    /**{@inheritDoc}*/
    @Override
    public String getColumnName(int columnIndex) {
	return nomColonne[columnIndex];
    }

    /**{@inheritDoc}*/
    @Override
    public Class<?> getColumnClass(int columnIndex) {
	if (columnIndex == 0) {
	    return Long.class;
	}
	return String.class;
    }

    /**{@inheritDoc}*/
    @Override
	public abstract Object getValueAt(int rowIndex, int columnIndex);
}
