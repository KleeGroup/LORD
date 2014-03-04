package spark.reprise.outil.ui.utilisateur.model;

import java.text.NumberFormat;
import java.util.ResourceBundle;

import spark.reprise.outil.moteur.Categories;
import spark.reprise.outil.moteur.Categories.Categorie;
import spark.reprise.outil.moteur.Fichier;
import spark.reprise.outil.moteur.Schema;


/**
 * Modele pour la classe FrameProgressionTraitement.<br>
 * <br>
 * renvoie la liste des fichiers aisni que l'etat de chaque fichier.
 * 
 */
public class FrameProgressionTraitementModel extends Model {
    private class TableModel extends javax.swing.table.AbstractTableModel {
	private static final long serialVersionUID = 4157087119873576473L;

	Schema schema;

	private String nomColonne[] = new String[] { "Nom", "Etat", };

	private NumberFormat nf = NumberFormat.getInstance();

	private Categories categoriesActives = new Categories();

	private int typeAffiche[];
	private int indiceAffiche[];
	private int categorieEnCours[];
	private int nbFichiersActifs;
	
	TableModel(Schema schema) {
	    this.schema = schema;
	}

	private void createListFromSchema(Schema pschema) {
	    categoriesActives = new Categories();
	    nbFichiersActifs=0;
	    for (Fichier f : pschema.getFichiers()) {
		if (f.getEtatFichier() == Fichier.ETAT.EN_ATTENTE) {
		    f.setModeAffichage(0);
		    categoriesActives.put(f, f.getNomCategorie());
		    nbFichiersActifs++;
		}
	    }
	    if(schema.getCategories().getNbCategories()>1){
		construitIndicesAvecCategories();
	    }else{
		construitIndicesSansCategories();
	    }
	}

	private void construitIndicesAvecCategories() {
	    typeAffiche=new int[getRowCount()];
	    indiceAffiche=new int[getRowCount()];
	    categorieEnCours=new int[getRowCount()];
	    int j=0;
	    int indiceCat=0,indiceFichiers=0;
	    for(int i=0;i< categoriesActives.getChildCount();i++){
		Categorie c= categoriesActives.getCategorie(i);
		typeAffiche[j]=0;//affiche une categorie
		indiceAffiche[j]=indiceCat;
		categorieEnCours[j]=indiceCat;
		j++;
		for(int k=0;k<c.size();k++){
		    typeAffiche[j]=1;//affiche les fichiers d'une categorie
		    indiceAffiche[j]=indiceFichiers;
		    categorieEnCours[j]=indiceCat;
		    indiceFichiers++;
		    j++;
		}
		indiceFichiers=0;
		indiceCat++;
	    }
	}
	private void construitIndicesSansCategories() {
            typeAffiche=new int[getRowCount()];
            indiceAffiche=new int[getRowCount()];
            categorieEnCours=new int[getRowCount()];
            Categorie c= categoriesActives.getRootCategorie();
            for(int k=0;k<c.size();k++){
                typeAffiche[k]=1;//affiche les fichiers d'une categorie
                indiceAffiche[k]=k;
                categorieEnCours[k]=0;
            }
	}
	void clean() {
	    createListFromSchema(schema);// */
	}

	/** {@inheritDoc} */
	@Override
	public int getRowCount() {
	    int res=nbFichiersActifs+categoriesActives.getChildCount();
	    if(schema.getCategories().getNbCategories()>1){
		return res;
	    }
	    return nbFichiersActifs; //Sans root(root sera tout seul)
	}

	/** {@inheritDoc} */
	@Override
	public int getColumnCount() {
	    return nomColonne.length;
	}

	/** {@inheritDoc} */
	@Override
	public String getColumnName(int columnIndex) {
	    return nomColonne[columnIndex];
	}
	
	/** {@inheritDoc} */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
	    
	    switch(typeAffiche[rowIndex]){
	    case 0:
		return afficheCategorie(columnIndex, indiceAffiche[rowIndex]);
	    case 1:
		return afficheFichier(columnIndex, 
			categoriesActives.getCategorie(categorieEnCours[rowIndex])
				.get(indiceAffiche[rowIndex]));
	    case 2:
		return afficheFichier(columnIndex, categoriesActives.getRootCategorie().get(indiceAffiche[rowIndex]));
	    default:
		return "";    
	    }
	}
	    

	private Object afficheCategorie(int columnIndex, int i) {
	    if (columnIndex == 0 ) {
		return categoriesActives.getCategorie(i);
		
	    }
	   
	    return "";
	}

	private Object afficheFichier(int columnIndex, Fichier fichier) {
	    switch (columnIndex) {
	    case 0:
		return fichier;
	    case 1:
		long nbErr = fichier.getNbErreurs();
		String msg = "";
		if (nbErr == 1) {
		    msg = "(1 " + resourceMap.getString("SingleError") + ")";
		} else if (nbErr > 1) {
		    msg = "(" + nf.format(nbErr) + " "
			    + resourceMap.getString("Errors") + ")";
		}

		return fichier.getEtatFichier().toString() + " " + msg;

	    default:
		return "";
	    }
	}

    }

    protected TableModel tableModel;

    final ResourceBundle resourceMap = ResourceBundle
	    .getBundle("resources.FrameProgressionTraitement");

    /**
         * Construit un model de progrssion a partir d'un schema s.
         * 
         * @param s
         *                le schema dont on suit la progression.
         */
    public FrameProgressionTraitementModel(Schema s) {
	tableModel = new TableModel(s);
    }

    /**
         * @return la liste des fichiers ainsi que leur etat.
         */
    public TableModel getTableModel() {
	return tableModel;
    }

    /**
         * remet le modele à 0.
         */
    public void clean() {
	tableModel.clean();
    }

    /**
     * raffraichit les données du modele.
     */
    public void refresh() {
	tableModel.fireTableDataChanged();

    }

}
