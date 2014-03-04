package spark.reprise.outil.ui.common.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import spark.reprise.outil.moteur.Categories;
import spark.reprise.outil.moteur.Categories.Categorie;
import spark.reprise.outil.moteur.util.IHierarchieSchema;
import spark.reprise.outil.moteur.Fichier;
import spark.reprise.outil.moteur.Schema;


/**
 * Constuit un modèle d'un schéma utilisable par un {@link javax.swing.JTree}.
 */
public class FileTreeModel implements javax.swing.tree.TreeModel {

    private List<TreeModelListener> tml = new ArrayList<TreeModelListener>();

    private Categories categories;

    private Schema schema=null;

    private int numCatAjoutee = 0;

    /**
         * Constuit un modèle d'un schéma utilisable par un
         * {@link javax.swing.JTree}.
         * 
         * @param s
         *                le schéma affiché.
         */
    public FileTreeModel(Schema s) {
	this(s,false);
    }
    /**
     * Constuit un modèle d'un schéma utilisable par un
     * {@link javax.swing.JTree}.
     * 
     * @param s
     *                le schéma affiché.
     * @param fichiersErronesUniquement  true s'il faut afficher uniquement les 
     * fichiers qui contiennent des erreurs.
     */

    public FileTreeModel(Schema s,boolean fichiersErronesUniquement){
	schema = s;
	for (Fichier f : s.getFichiers()) {
	    f.setModeAffichage(1);
	}
	Categories res;
	
	if(fichiersErronesUniquement){
	    res=new Categories();
	    for (Fichier f : s.getFichiers()) {
		if (f.getNbErreurs()>0){
		    res.put(f, f.getNomCategorie());
		}
	    }
	    
	}else{
	    res=s.getCategories();
	}
	this.categories = res;
	
    }
    

    /** {@inheritDoc} */
    @Override
	public Object getChild(Object parent, int index) {
	if (!(index >= 0 && index < getChildCount(parent))) {
	    return null;
	}
	return ((IHierarchieSchema)parent).getChild(index);
    }

    /** {@inheritDoc} */
    @Override
	public int getChildCount(Object parent) {
	return ((IHierarchieSchema)parent).getChildCount();
    }

    /** {@inheritDoc} */
    @Override
	public Object getRoot() {
	return categories;
    }

    /** {@inheritDoc} */
    @Override
	public int getIndexOfChild(Object parent, Object child) {
	return ((IHierarchieSchema)child).getPosition();
	    
    }

    /** {@inheritDoc} */
    @Override
	public boolean isLeaf(Object node) {
	return node != getRoot() && 
	((IHierarchieSchema)node).isFichier();

    }

    /**
         * @param node
         *                l'objet testé
         * @return true s'il faut affiché l'objet node d'une manière spéciale.
         */
    public boolean isBold(Object node) {
	return isLeaf(node) && (((Fichier) node).getNbErreurs() > 0);
    }

    /** {@inheritDoc} */
    @Override
	public void valueForPathChanged(TreePath path, Object newValue) {
	// Ne fais rien
	IHierarchieSchema selected = (IHierarchieSchema)path.getLastPathComponent();
	String newName = (String) newValue;
	if (selected.isFichier()){
	    ((Fichier) selected).setNom(newName);
	} else {
	    if (categories.getPosCategorie(newName) < 0) {
		categories.renameCategorie(((Categorie) selected).getNom(), (String) newValue);
	    }
	}
	TreeModelEvent tme = new TreeModelEvent(this, path);
	for (TreeModelListener tm : tml) {
	    tm.treeNodesChanged(tme);
	}
    }

    /**
         * déplace le fichier vers le haut.
         * 
         * @param f
         *                le fichier à déplacer.
         */

    /**
         * déplace le fichier vers le bas.
         * 
         * @param f
         *                le fichier à déplacer.
         */

    /**
         * supprime le fichier du schéma.
         * 
         * @param selectedElement
         *                le path du fichier à supprimer.
     * 	@return le nouveau path à séléctionner. 
         */
    public TreePath removeFile(TreePath selectedElement) {
	Fichier f = (Fichier) selectedElement.getLastPathComponent();
	Categorie parent = f.getCategorie();
	int posFichier = parent.getPosFichier(f.getNom());

	f.getCategorie().remove(f);
	if (schema != null) {
	    schema.removeFichier(f);
	}
	notifyListeners();
	TreePath tp;
	if(parent.size()>0){
	 tp= new TreePath(new Object[] { getRoot(), parent,
			parent.get(posFichier), });
	}else{
	     tp= new TreePath(new Object[] { getRoot(), parent, });
	}
	System.out.println(tp);
	notifyListenersOfRemoval(tp);
	return tp;

    }

    private void notifyListenersOfRemoval(TreePath parent) {
	TreeModelEvent tme = new TreeModelEvent(this, parent);
	for (TreeModelListener tm : tml) {
	    tm.treeNodesRemoved(tme);
	}
    }

    /**
         * ajoute un fichier au schéma.
     * @param position la position du fichier.
         * 
         * @param f le fichier à ajouter
         */
    public void addFichier(TreePath position, Fichier f) {
	if (position != null) {
	    IHierarchieSchema path = (IHierarchieSchema)position.getLastPathComponent();
	    IHierarchieSchema parent=null;
	    if (path!=null && position.getParentPath()!=null){
		parent = (IHierarchieSchema)position.getParentPath().getLastPathComponent();
		
	    }

	    if (parent!=null && parent.isCategorie()){
		f.setNomCategorie(((Categorie) parent).getNom());
	    }
	    if (path!=null && path.isCategorie()){
		f.setNomCategorie(((Categorie) path).getNom());
	    }
	}
	if (schema != null) {

	    schema.addFichier(f);
	}
	notifyListeners();
    }

    /** {@inheritDoc} */
    @Override
	public void addTreeModelListener(TreeModelListener l) {
	tml.add(l);

    }

    /** {@inheritDoc} */
    @Override
	public void removeTreeModelListener(TreeModelListener l) {
	tml.remove(l);

    }

    /**
     * Déplace un élément(fichier ou catégorie) vers le bas.
     * @param selectionPath le path de l'élément.
     * @return le nouveau path de l'élément.
     */
    public TreePath moveDn(TreePath selectionPath) {
	IHierarchieSchema toMove = (IHierarchieSchema)selectionPath.getLastPathComponent();
	if (toMove.isFichier()){
	    return moveFileDn(selectionPath, (Fichier) toMove);
	} else if (toMove.isCategorie()){
	    return moveCategorieDn(selectionPath, (Categorie) toMove);
	}
	return new TreePath(getRoot());

    }

    private TreePath moveCategorieDn(TreePath selectionPath, Categorie categorie) {
	int pos = categories.getPosCategorie(categorie.getNom());
	if (pos == categories.getNbCategories() - 1) {
	    return selectionPath;
	}
	categories.moveDn(categorie);
	notifyListeners();
	return selectionPath;
    }

    private void notifyListeners() {
	TreeModelEvent tme = new TreeModelEvent(this, new TreePath(getRoot()));
	for (TreeModelListener tm : tml) {
	    tm.treeStructureChanged(tme);
	}
    }

    private TreePath moveFileDn(TreePath selectionPath, Fichier f) {
	int pos = f.getCategorie().getPosFichier(f.getNom());
	if (pos < f.getCategorie().size() - 1) {// on ne deplace
                                                                // pas le
                                                                // dernier
                                                                // fichier
	    categories.moveDn(f);
	    notifyListeners();
	    return selectionPath;
	}
	// on change de categorie
	int posCat = categories.getPosCategorie(f.getCategorie().getNom());

	Categorie dest;// la categorie de destination
	Object parentPaths[];// le chemin vers l'element

	if (posCat >= 0 && posCat + 1 < categories.getNbCategories()) {
	    dest = categories.getCategorie(posCat + 1);
	    parentPaths = new Object[] { getRoot()  , dest, f };
	} else {
//	    dest = root;
//	    parentPaths = new Object[] { root, f };
//	    if ((f.getCategorie() == dest)
//		    && root.getPosFichier(f.getNom()) == root.size() - 1) {
//		// arrivé à la fin de la liste , on ne déplace plus le fichier
//                // en bas
//		return selectionPath;
//	    }
	    return selectionPath;
	}
	f.getCategorie().remove(f);
	dest.add(f, 0);// ajouter au debut de la liste
	notifyListeners();
	return new TreePath(parentPaths);
    }
    /**
     * Déplace un élément(fichier ou catégorie) vers le haut.
     * @param selectionPath le path de l'élément.
     * @return le nouveau path de l'élément.
     */
    public TreePath moveUp(TreePath selectionPath) {
	IHierarchieSchema toMove = (IHierarchieSchema)selectionPath.getLastPathComponent();
	if (toMove.isFichier()){
	    return moveFileUp(selectionPath, (Fichier) toMove);
	} else if (toMove.isCategorie()){
	    return moveCategorieUp(selectionPath, (Categorie) toMove);
	}
	return new TreePath(getRoot());
    }

    private TreePath moveCategorieUp(TreePath selectionPath, Categorie categorie) {
	int pos = categories.getPosCategorie(categorie.getNom());
	if (pos == 0) {
	    return selectionPath;
	}
	categories.moveUp(categorie);
	notifyListeners();
	return selectionPath;
    }

    private TreePath moveFileUp(TreePath selectionPath, Fichier f) {
	int pos = f.getCategorie().getPosFichier(f.getNom());
	if (pos > 0) {// on ne deplace pas le premier fichier, on change de
                        // categorie
	    categories.moveUp(f);
	    notifyListeners();
	    return selectionPath;
	}
	// on change de categorie
	int posCat = categories.getPosCategorie(f.getCategorie().getNom());

	Categorie dest;// la categorie de destination

	if (posCat == 0) {
	    // arrivé au debut de la 1ere categorie , on ne déplace plus le
                // fichier
	    notifyListeners();
	    return selectionPath;
	}
	dest = categories.getCategorie(posCat - 1);
	f.getCategorie().remove(f);
	dest.add(f);// ajouter au debut de la liste
	notifyListeners();
	return new TreePath(new Object[] { getRoot(), dest, f });
    }

    /**
     * Supprime une catégorie.
     * @param selection le path de la catégorie.
     * @param conserverLesFichier true s'il faut conserver les fichiers.
     * @return le nouveau path à séléctionner.
     */
    public TreePath deleteCat(TreePath selection, boolean conserverLesFichier) {
	String nomCategorie = ((Categorie) (selection.getLastPathComponent()))
		.getNom();
	if (conserverLesFichier) {
	    categories.deleteCat(nomCategorie);
	} else {
	    categories.deleteCatAndFiles(nomCategorie);
	}
	notifyListeners();
	return null;
    }

    /**
     * Ajoute une nouvelle catégorie au schéma.
     */
    public void addCategorie() {
	categories.createCategorie("Nouvelle Categorie" + numCatAjoutee);
	notifyListeners();
	numCatAjoutee++;
    }

}
