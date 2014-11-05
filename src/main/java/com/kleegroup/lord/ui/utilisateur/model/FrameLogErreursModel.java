package com.kleegroup.lord.ui.utilisateur.model;

import java.util.List;
import java.util.Map;

import com.kleegroup.lord.moteur.Fichier;
import com.kleegroup.lord.moteur.IErreur;
import com.kleegroup.lord.moteur.Schema;
import com.kleegroup.lord.moteur.logs.LoggueurRam;
import com.kleegroup.lord.ui.common.model.FileTreeModel;
import com.kleegroup.lord.ui.utilisateur.model.listeErreurs.ListeErreurs;
import com.kleegroup.lord.ui.utilisateur.model.listeErreurs.ListeErreursNoRef;
import com.kleegroup.lord.ui.utilisateur.model.listeErreurs.ListeErreursWithRef;

/**
 * Modele du FrameLogErreurs.
 */
public class FrameLogErreursModel extends Model{

	private Map<String,LoggueurRam> loggueurs;
	
	 /**
	 * @return la liste des erreurs associ�e � chaque fichier.
	 */
	public Map<String, LoggueurRam> getLoggueurs() {
		return loggueurs;
	}

	/**
	 * @param loggueurs la liste des erreurs associ�e � chaque fichier.
	 */
	public void setLoggueurs(Map<String, LoggueurRam> loggueurs) {
		this.loggueurs = loggueurs;
	}

	/**
	 * @param f le fichier concern�
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
	 * @param s le schema en utilis�.
	 * @return une liste des fichiers du schema.
	 */
	public FileTreeModel getListFichier(Schema s){
	    return new FileTreeModel(s,true);
	}

}
