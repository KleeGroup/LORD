package com.kleegroup.lord.ui.utilisateur.model.listeErreurs;

import java.util.List;

import com.kleegroup.lord.moteur.IErreur;

/**
 * Affiche une liste d'erreurs sans de colonnes de reference.
 *
 */
public class ListeErreursNoRef extends ListeErreurs{

    private static final long serialVersionUID = 1L;

    /**
     * @param listeErreurs la liste d'erreur a afficher.
     */
    public ListeErreursNoRef(List<IErreur> listeErreurs) {
	super(listeErreurs);
	nomColonne=new String[]{
        	RESOURCEMAP.getString("TableModel.Ligne"),
        	RESOURCEMAP.getString("TableModel.Colonne"),
        	RESOURCEMAP.getString("TableModel.Valeur"),
        	RESOURCEMAP.getString("TableModel.MessageErreur"),
                };
    }
    /**{@inheritDoc}*/
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        IErreur err=listeErreurs.get(rowIndex);
        switch(columnIndex){
            case 0:
                return err.getErrLigne();
            case 1:
                return err.getErrColonne();
            case 2:
                return err.getErrValeur();
            case 3:
                return err.getErrMessage();
            default:
                return "";
        }
    }

}
