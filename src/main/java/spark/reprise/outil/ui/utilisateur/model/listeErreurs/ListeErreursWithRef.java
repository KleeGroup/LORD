package spark.reprise.outil.ui.utilisateur.model.listeErreurs;

import java.util.List;

import spark.reprise.outil.moteur.IErreur;

/**
 * Affiche une liste d'erreurs qui contient des colonnes de reference.
 */
public class ListeErreursWithRef extends ListeErreurs{

    private static final long serialVersionUID = 1L;

    /**
     * @param listeErreurs la liste d'erreurs  à afficher.
     */
    public ListeErreursWithRef(List<IErreur> listeErreurs) {
	super(listeErreurs);
	nomColonne=new String[]{
        	RESOURCEMAP.getString("TableModel.Ligne"),
        	RESOURCEMAP.getString("TableModel.Reference"),
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
        	return err.getReference().toString();
            case 2:
                return err.getErrColonne();
            case 3:
                return err.getErrValeur();
            case 4:
                return err.getErrMessage();
            default:
                return "";
        }
    }

}
