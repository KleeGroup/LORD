package spark.reprise.outil.ui.utilisateur.model;

import java.util.List;
import java.util.Map;

import spark.reprise.outil.moteur.Fichier;
import spark.reprise.outil.moteur.IErreur;
import spark.reprise.outil.moteur.Schema;
import spark.reprise.outil.moteur.logs.LoggueurRam;
import spark.reprise.outil.ui.common.model.FileTreeModel;
import spark.reprise.outil.ui.utilisateur.model.listeErreurs.ListeErreurs;
import spark.reprise.outil.ui.utilisateur.model.listeErreurs.ListeErreursNoRef;
import spark.reprise.outil.ui.utilisateur.model.listeErreurs.ListeErreursWithRef;

/**
 * Modele du FrameLogErreurs.
 */
public class FrameLogErreursModel extends Model{

	private Map<String,LoggueurRam> loggueurs;
	
	 /**
	 * @return la liste des erreurs associée à chaque fichier.
	 */
	public Map<String, LoggueurRam> getLoggueurs() {
		return loggueurs;
	}

	/**
	 * @param loggueurs la liste des erreurs associée à chaque fichier.
	 */
	public void setLoggueurs(Map<String, LoggueurRam> loggueurs) {
		this.loggueurs = loggueurs;
	}

	/**
	 * @param f le fichier concerné
	 * @return la liste des erreurs du fichier f
	 */
	public ListeErreurs getErreurs(Fichier f){
	    List<IErreur> listeErrs=loggueurs.get(f.getNom()).getErreurs();
	    if (f.hasColonneReference()){
		return new ListeErreursWithRef(listeErrs);
	    }
	    return new ListeErreursNoRef(listeErrs);
	}
	/**
	 * @param s le schema en utilisé.
	 * @return une liste des fichiers du schema.
	 */
	public FileTreeModel getListFichier(Schema s){
	    return new FileTreeModel(s,true);
	}

}
