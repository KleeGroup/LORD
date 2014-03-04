package spark.reprise.outil.ui.utilisateur.model;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import spark.reprise.outil.moteur.Categories.Categorie;
import spark.reprise.outil.moteur.Fichier;
import spark.reprise.outil.moteur.Schema;


/**
 * Modele pour la classe FrameSelectionCheminsFichiers.
 */
public class FrameSelectionCheminsFichiersModel extends Model {

    protected Schema schema;
    

    /**
     * Construit un modele a partir d'un schema s.
     * @param s le schema du modele.
     */
    public FrameSelectionCheminsFichiersModel(Schema s){
	this.schema=s;
    }

    /**
     * Active/desactive le fichier.
     * @param nom lle nom du fichier à désactiver.
     * @param etat true s'il faut l'activer.
     */
    public void setEnabled(String nom, boolean etat) {
	if (etat) {
	    schema.getFichier(nom).setEtatFichier(
		    Fichier.ETAT.EN_ATTENTE);
	} else {
	    schema.getFichier(nom).setEtatFichier(
		    Fichier.ETAT.DESACTIVE_UTILISATEUR);
	}
	
    }
    
 
    /**
     * @return true si au moins un fichier est actif.
     */
    public boolean isAuMoinsUnfichierActif(){
	return schema.getNbFichiersActifs()>0;
    }
    /**
     * @return null si les chemins donnés à chaque fichiers sont valide,
     * sinon renvoie le nom du premier fichier invalide.
     */
    public String isValide() {
	for(final Fichier f:schema.getFichiers()){
	    if (f.isEnabled() &&
		    !(isCheminValide(f.getChemin()) &&
			    ("".equals(f.getExtension()) ||
			f.getChemin().endsWith(f.getExtension())))){
		return f.getNom();
	    }
	}
	return null;
    }
    
    /**
     * @return true si on peut ecrire dans le repertoire de log.
     */
    public boolean isValideDirLog(){
	final File logDir=new File(schema.getEmplacementFichiersLogs());
	if (!(logDir.exists() && logDir.isDirectory())){
	    return false;
	}
	File test;
	try {
	    test= File.createTempFile("/delete-me", "tst",new File(logDir.getAbsolutePath()));
	} catch (final IOException e) {
	    return false;
	}
	return test.delete();

    }

    /**
     * @param nom le nom du fichier
     * @param chemin le chemin d'acces de ce fichier
     */
    public void setChemin(String nom, String chemin)  {
	final Fichier f=schema.getFichier(nom);
	if(f!=null){
	    f.setChemin(chemin);
	}
    }

    /**
     * @param nom le nom du fichier.
     * @return le chemin d'accès de ce fichier.
     */
    public String getChemin(String nom){
	return schema.getFichier(nom).getChemin();
    }

    /**
     * @return le nombre de fichiers dans le modele.
     */
    public int size() {
	return schema.getNbFichiers();
    }

    /**
     * nettoie le modele pour pouvoire refaire une autre verification.
     */
    public void clean(){
	schema.clean();
    }

    /**
     * Cherche dans un repertoire les fichiers pour remplir automqtiquement l'emplacement
     * prévu.
     * @param dir le répertoire où il faut chercher
     */
    public void searchDir(File dir) {
	if(dir==null || ! dir.isDirectory()){
	    return;
	}
	for(final Fichier f:schema.getFichiers()){
	    final String prefix=f.getPrefixNom();
	    final String extension=f.getExtension();
	    final File[] fichiersCandidats=dir.listFiles(new MyFileFilter(prefix, extension));
	    if (fichiersCandidats.length>0){
		try {
		    f.setChemin(fichiersCandidats[0].getCanonicalPath());
		    setChemin(f.getNom(),f.getChemin());
		} catch (final IOException e) {/***/
		    f.setChemin("");
		    setChemin(f.getNom(), "");
		}
	    }
	}
    }

    /**
     * @param nom le nom du fichier.
     * @return l'extension du fichier.
     */
    public String getExtension(String nom){
	return schema.getFichier(nom).getExtension();
    }

    /**
     * @return une liste qui contient les noms des fichiers.
     */
    public List<String> getNomsFichiers(){
	final List<String> res=new ArrayList<String>();
	for (final Fichier f:schema.getFichiers()){
	    res.add(f.getNom());
	}
	return res;
    }
    /**
     * Renvoie l'état d'activation du fichier.
     * @param nom le nom du fichier.
     * @return l'état du fichier.
     */
    public boolean getEnabled (String nom){
	return schema.getFichier(nom).isEnabled();
    }
    private boolean isCheminValide(String chemin){
	
	return chemin!=null && (new File(chemin)).exists();
    }

    /**
     * @return une liste des noms de catégorie du schéma.
     */
    public List<String> getNomsCategories() {
	final List<String> nomCategories=new ArrayList<String>();
	for(int i=0;i<schema.getCategories().getChildCount();i++){
		final Categorie c=schema.getCategories().getCategorie(i);
	    nomCategories.add(c.getNom());
	}
	return nomCategories;
    }
    /**
     * Renvoie la liste des fichiers d'une catégorie.
     * @param nomCategorie le nom de la catégorie.
     * @return la liste des fichiers.
     */
    public List<Fichier> getFichiers(String nomCategorie){
	return schema.getCategories().getFileList(nomCategorie);
    }
    /**
     * @return la liste des fichiers non attribués à une catégorie.
     */
    public List<Fichier> getFichiersSansCategorie(){
	return schema.getCategories().getRootCategorie().getFiles();
    }

    /**
     * @return le nom de la catégorie racine
     */
    public String getRootCategorie() {
	return schema.getCategories().getRootCategorie().getNom();
    }
    
    static class MyFileFilter implements FileFilter {
    	private final String prefix;
		private final String extension;
    	
		 /**
	     * Constructeur MyFileFilter
	     * 
	     * @param prefix paramètre à définir
	     * @param extension paramètre à définir
	     */
    	public MyFileFilter(String prefix, String extension) {
    		super();
    		this.prefix = prefix;
    		this.extension = extension;
    	}
    	
		@Override
		public boolean accept(File pathname) {
		    if(prefix==null){
			return true;
		    }
		    final String chemin=pathname.getName();
		    return chemin!=null && chemin.toLowerCase().startsWith(prefix.toLowerCase())
		     &&(extension==null|| chemin.endsWith(extension));
		}
	}
}
