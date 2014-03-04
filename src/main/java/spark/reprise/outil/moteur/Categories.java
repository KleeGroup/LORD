package spark.reprise.outil.moteur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import spark.reprise.outil.moteur.util.IHierarchieSchema;

/**
 * Représente les catégories du schéma. <br>
 * <br>
 * 
 * 
 * 
 */
public class Categories implements IHierarchieSchema {

    private static final String ROOT = "Catégorie non définie";

    /**
         * Une catégorie de fichier.
         */
    public static class Categorie implements IHierarchieSchema {
	protected List<Fichier> fichiers = new ArrayList<Fichier>();

	protected String nom;
	protected int position=0;
	protected  Categories parent;

	/**
         * @param nom
         *                le nom de la catégorie.
	 * @param position la position de la categorie
	 * @param parent le parent de la categorie.
         */
	public Categorie(String nom, int position , Categories parent) {
	    this.nom = nom;
	    this.position=position;
	    this.parent=parent;
	}

	/**
         * Ajoute un fichier à la catégorie. Si un fichier du même nom y existe
         * déjà, le fichier est déplacer à la position finale.
         * 
         * @param e
         *                le fichier à ajouter.
         */
	public void add(Fichier e) {
	    add(e, fichiers.size());
	}

	/**
         * Ajoute un fichier à la catégorie à la position pos. Si un fichier du
         * même nom y existe déjà, le fichier est déplacer à la position pos.
         * 
         * @param e
         *                le fichier à ajouter.
         * @param pos
         *                la position du fichier.
         */
	public void add(Fichier e, int pos) {
	    int posActuelle = getPosFichier(e.getNom());
	    if (posActuelle >= 0) {
		deplaceFichier(posActuelle, pos);
	    }
	    int posReel = pos;
	    if (posReel < 0) {
		posReel = 0;
	    }
	    if (posReel > fichiers.size()) {
		posReel = fichiers.size();
	    }
	    e.setCategorie(this);
	    if (!nom.equals(ROOT)) {
		e.setNomCategorie(nom);
	    } else {
		e.setNomCategorie("");
	    }

	    fichiers.add(posReel, e);
	}

	private void deplaceFichier(int oldPos, int newPos) {
	    int realNewPos = newPos;
	    if (realNewPos < 0) {
		realNewPos = 0;
	    }
	    if (realNewPos > fichiers.size() - 1) {
		realNewPos = fichiers.size() - 2;
	    }
	    int minPos = oldPos < realNewPos ? oldPos : realNewPos, maxPos = oldPos > realNewPos ? oldPos
		    : realNewPos;
	    /* http://java.sun.com/j2se/1.4.2/docs/api/java/util/Collections.html#rotate(java.util.List,%20int) */
	    Collections.rotate(fichiers.subList(minPos, maxPos + 1), -1);
	}

	/**
         * Renvoie le fichier à la position index.
         * 
         * @param index
         *                la position du fichier demandé.
         * @return le fichier demandé.
         */
	public Fichier get(int index) {
	    int posReelle = index;
	    if (posReelle < 0) {
		posReelle = 0;
	    }
	    if (posReelle >= fichiers.size()) {
		posReelle = fichiers.size() - 1;
	    }
	    return fichiers.get(posReelle);
	}

	/**
         * Rencoie le fichier dont le nom est nomFichier. Si le fihier n'est pas
         * trouvé, renvoie null.
         * 
         * @param nomFichier
         *                le nom du fichier demandé.
         * @return le fichier si trouvé, ou null sinon.
         */
	public Fichier get(String nomFichier) {
	    for (Fichier f : fichiers) {
		if (nomFichier.equals(f.getNom())) {
		    return f;
		}
	    }
	    return null;
	}

	/**
         * Renvoie la position du fichier dans la catégorie.
         * 
         * @param nomFichier
         *                le nom du fichier.
         * @return la position du fichier dans la catégorie.
         */
	public int getPosFichier(String nomFichier) {
	    for (int i = 0; i < fichiers.size(); i++) {
		if (nomFichier.equals(fichiers.get(i).getNom())) {
		    return i;
		}
	    }
	    return -1;
	}

	/**
         * @return le nombre de fichiers dans la catégorie.
         */
	public int size() {
	    return fichiers.size();
	}

	/**
         * @return le nom de la catégorie.
         */
	public String getNom() {
	    return nom;
	}

	/**
         * Modifie le nom de la categorie.
         * 
         * <br>
         * <br>
         * Attention de ne pas choisir un nom existant, sinon les catégories
         * seront mélangées au prochain chargement.
         * 
         * @param nom
         *                le nom de la catégorie.
         */
	public void setNom(String nom) {
	    this.nom = nom;
	    for (Fichier f : fichiers) {
		f.setNomCategorie(nom);
	    }
	}

	/**
         * @return la liste des fichiers de la categorie.
         */
	public List<Fichier> getFiles() {
	    return fichiers;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
	    return nom;
	}

	/**
         * Deplace le fichier vers le bas.
         * 
         * @param f
         *                le fichier à déplacer
         */
	public void moveDn(Fichier f) {
	    int pos = this.getPosFichier(f.getNom());
	    if (pos >= 0 && pos < fichiers.size()) {
		Fichier f2 = fichiers.get(pos + 1);
		fichiers.set(pos, f2);
		fichiers.set(pos + 1, f);
	    }

	}

	/**
         * @param f
         *                retire le fichier de la catégorie.
         */
	public void remove(Fichier f) {
	    fichiers.remove(f);

	}

	/**
         * Deplace le fichier vers le haut.
         * 
         * @param f
         *                le fichier à déplacer
         */
	public void moveUp(Fichier f) {
	    int pos = this.getPosFichier(f.getNom());
	    if (pos > 0 && pos < fichiers.size()) {
		Fichier f2 = fichiers.get(pos - 1);
		if (f2 == null) {
		    return;
		}
		fichiers.set(pos, f2);
		fichiers.set(pos - 1, f);
	    }
	}
/**{@inheritDoc}*/
	@Override
	public IHierarchieSchema getChild(int index) {
	    return fichiers.get(index);
	}
/**{@inheritDoc}*/
	@Override
	public int getChildCount() {
	    return fichiers.size();
	}
/**{@inheritDoc}*/
	@Override
	public int getNiveau() {
	    return 0;
	}
/**{@inheritDoc}*/
	@Override
	public String getNomHierarchie() {
	    return getNom();
	}
/**{@inheritDoc}*/
	@Override
	public IHierarchieSchema getParent() {
	    return parent;
	}
/**{@inheritDoc}*/
	@Override
	public int getPosition() {
	    return position;
	}
/**{@inheritDoc}*/
	@Override
	public boolean isCategorie() {
	    return true;
	}
/**{@inheritDoc}*/
	@Override
	public boolean isColonne() {
	    return false;
	}
/**{@inheritDoc}*/
	@Override
	public boolean isContrainte() {
	    return false;
	}
/**{@inheritDoc}*/
	@Override
	public boolean isFichier() {
	    return false;
	}

    }

    protected Categorie root = new Categorie(ROOT,-1,this);

    protected List<Categorie> categories = new ArrayList<Categorie>();

    /**
     * Construit des categories pour un schema.
     */
    public Categories(){
	categories.add(root);
    }
    private Categorie getCategorie(String nom) {
	if (nom.equals(ROOT) || "".equals(nom)) {
	    return root;
	}
	for (Categorie c : categories) {
	    if (nom.equals(c.getNom())) {
		return c;
	    }
	}
	return root;
    }

    /**
         * Crée une catégorie. Si une catégorie du même nom existe déjà, renvoie
         * cette dernière.
         * 
         * @param nom
         *                le nom de la catégorie.
         * @return la catégorie demandée.
         */
    public Categorie createCategorie(String nom) {
	Categorie res = getCategorie(nom);
	if (res == root && !nom.equals(ROOT)) {
	    res = new Categorie(nom,categories.size(),this);
	    categories.add(categories.size()-1,res);
	}
	return res;
    }

    /**
         * Renomme la catégorie.
         * 
         * @param oldName
         *                l'ancien nom.
         * @param newName
         *                le nouveau nom de la catégorie.
         */
    public void renameCategorie(String oldName, String newName) {
	Categorie c = getCategorie(oldName);
	Categorie inexist = getCategorie(newName);
	if (inexist == root && !newName.equals(ROOT)) {// on ne doit pas renommer root.
	    c.setNom(newName);
	}
    }

    /**
         * @return la liste des catégories.
         */
    public List<String> getCategories() {
	List<String> listeNom = new ArrayList<String>(categories.size());
	for (Categorie c : categories) {
	    listeNom.add(c.getNom());
	}
	return listeNom;
    }

    /**
         * Tout schéma contient forcément une catégorie, la catégorie root.Les
         * fichiers qui ne sont pas rattachés à une catégorie particulière sont
         * rajouté à cette catégorie. La catégorie Root est traité spécialement lors
         * de l'affichage. Ses fichiers sont affichés sans catégorie mère.
         * 
         * @return la catégorie root.
         */
    public Categorie getRootCategorie() {
	return root;
    }

    /**
     * Supprime la catégorie. Les fichiers de la catégorie seront rattachés à root.
     * @param nom le nom de la catégorie à supprimer.
     */
    public void deleteCat(String nom) {
	List<Fichier> liste = getFileList(nom);
	deleteCatAndFiles(nom);
	for (Fichier f : liste) {
	    root.add(f);
	}
    }

    /**
     * Supprime la categorie et ses fichiers.
     * 
     * @param nom le nom de la categorie.
     */
    public void deleteCatAndFiles(String nom) {
	Categorie aEffacer = getCategorie(nom);
	if (aEffacer != root) {
	    categories.remove(aEffacer);
	}
    }

    /**
     * Renvoie la position du fichier dans sa catégorie.
     * @param nomFichier le nom du fichier.
     * @return sa position dans la catégorie.
     */
    public int getPosFichier(String nomFichier) {
	for (Categorie c : categories) {
	    int res = c.getPosFichier(nomFichier);
	    if (res >= 0) {
		return res;
	    }
	}
	return -1;
    }

    /**
     * Renvoie la position de la catégorie parmi les autres catégories.
     * @param nomCategorie le nom de la catégorie.
     * @return la position parmi les autres catégories.
     */
    public int getPosCategorie(String nomCategorie) {
	for (int i = 0; i < categories.size(); i++) {// on evite le root
	    if (categories.get(i).getNom().equals(nomCategorie)) {
		return i;
	    }
	}
	return -1;
    }

    /**
     * Renvoie la catégorie auquel appartient le fichier f.
     * @param f le fichier dont on cherche la catégorie. 
     * @return la position du fichier.
     */
    public String getCategorieFichier(Fichier f) {
	for (Categorie c : categories) {
	    int res = c.getPosFichier(f.getNom());
	    if (res >= 0) {
		return c.getNom();
	    }
	}
	return "";
    }

    /**
     *Renvoie la liste des fichiers d'une catégorie.
     * @param categorie la catégorie demandée
     * @return la liste des fichiers de la catégorie. 
     */
    public List<Fichier> getFileList(String categorie) {
	return getCategorie(categorie).getFiles();
    }

    private void put(Fichier f, String categorie, int pos) {
	// si categorie existe pas,on rajoute fichier dans la categorie qui a la
        // position pos

	if (categorie == null || "".equals(categorie)) {
	    root.add(f, pos);
	    return;
	}
	Categorie c = getCategorie(categorie);
	if (c == root) {
	    createCategorie(categorie).add(f, pos);
	} else {
	    c.add(f, pos);
	}

    }

    /**
     * Rajoute le fichier f dans la catégorie categorie.
     * @param f le fichier à rajouter.
     * @param categorie la catégorie auquel il faut el rajouter.
     */
    public void put(Fichier f, String categorie) {
	put(f, categorie, Integer.MAX_VALUE);
    }

    /**
     * @return le nombre des catégories (root n'est pas compté).
     */
    public int getNbCategories() {
	return categories.size();
    }

    /**
     * Renvoie la catégorie ) la position index.
     * @param index la position de la catégorie.
     * @return la catégorie demandée.
     */
    public Categorie getCategorie(int index) {
	return categories.get(index);
    }

    /**
     * Déplace le fichier f vers le bas dans la catégorie.
     * @param f le fichier à déplacer.
     */
    public void moveDn(Fichier f) {
	f.getCategorie().moveDn(f);

    }

    /**
     * Déplace la Catégorie catégorie vers le bas dans le schéma.
     * @param categorie la Catégorie à déplacer.
     */
    public void moveDn(Categorie categorie) {
	int pos = getPosCategorie(categorie.getNom());
	if (pos >= 0 && pos < getNbCategories()-2) {
	    Categorie c2 = categories.get(pos + 1);
	    categories.set(pos + 1, categorie);
	    categorie.position++;
	    categories.set(pos, c2);
	}

    }

    /**
     * Déplace le fichier f vers le haut dans la catégorie.
     * @param f le fichier à déplacer.
     */
    public void moveUp(Fichier f) {
	f.getCategorie().moveUp(f);

    }

    /**
     * Déplace la Catégorie catégorie vers le haut dans le schéma.
     * @param categorie la Catégorie à déplacer.
     */
    public void moveUp(Categorie categorie) {
	int pos = getPosCategorie(categorie.getNom());
	if (pos > 0 && pos < getNbCategories()-1) {
	    Categorie c2 = categories.get(pos - 1);
	    categories.set(pos - 1, categorie);
	    categorie.position--;
	    categories.set(pos, c2);
	}
    }

    /**
     * @return la liste des catégories du schéma (autre que root)
     */
    public List<Categorie> getListCategories() {
	return categories;
    }
/**{@inheritDoc}*/
    @Override
	public IHierarchieSchema getChild(int index) {
	return categories.get(index);
    }
/**{@inheritDoc}*/
    @Override
	public int getChildCount() {
	if (getRootCategorie().size()>0){ 
	return categories.size();
	}
	return categories.size()-1; 
	
    }
/**{@inheritDoc}*/
    @Override
	public int getNiveau() {
	return 0;
    }
/**{@inheritDoc}*/
    @Override
	public String getNomHierarchie() {
	return "ROOT";
    }
/**{@inheritDoc}*/
    @Override
	public IHierarchieSchema getParent() {
	return this;
    }
/**{@inheritDoc}*/
    @Override
	public int getPosition() {
	return 0;
    }
/**{@inheritDoc}*/
    @Override
	public boolean isCategorie() {
	return false;
    }
/**{@inheritDoc}*/
    @Override
	public boolean isColonne() {
	return false;
    }
/**{@inheritDoc}*/
    @Override
	public boolean isContrainte() {
	return false;
    }
/**{@inheritDoc}*/
    @Override
	public boolean isFichier() {
	return false;
    }

}
