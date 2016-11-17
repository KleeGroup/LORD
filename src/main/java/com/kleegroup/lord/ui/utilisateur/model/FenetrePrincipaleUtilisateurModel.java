package com.kleegroup.lord.ui.utilisateur.model;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import com.kleegroup.lord.moteur.Schema;

/**
 * Mod�le de la fenetre principale de l'interface utilisateur.
 */
public class FenetrePrincipaleUtilisateurModel extends Model{
	Schema schema;
	/**
	 * Essaie de charger le fichier "config.xml" dans le repertoire du programme.
	 * @param execDir le repertoire du programme
	 * @throws FileNotFoundException si le fichier n'est pas trouvé.
	 * @throws JAXBException Exception JAXB
	 */
	public FenetrePrincipaleUtilisateurModel (File execDir) throws FileNotFoundException, JAXBException{
			
	    
	    schema= Schema.fromXML(new java.io.FileInputStream(
					getFichierConf(execDir)));
	}
	
	private File getFichierConf(File execDir) throws FileNotFoundException{
	    File fichierDeConf=new File(".","config.xml");
	    if (!fichierDeConf.exists()){
		fichierDeConf=new File(execDir,"config.xml");
	    }
	    if (!fichierDeConf.exists()){
		throw new FileNotFoundException(fichierDeConf.getAbsolutePath());
	    }
		
	    return fichierDeConf;
	}

	/**
	 * @return le schema en cours d'utilisation. voir {@link Schema}
	 */
	public Schema getSchema() {
		return schema;
	}

	/**
	 * @param path le r�pertoire o� seront sauvegard�s les fichiers de logs
	 */
	public void setEmplacementLogs(String path){
	    schema.setEmplacementFichiersLogs(path);
	}
	/**
	 * @return le r�pertoire o� seront sauvegard�s les fichiers de logs
	 */
	public String getEmplacementLogs(){
	    return schema.getEmplacementFichiersLogs();
	}
}
