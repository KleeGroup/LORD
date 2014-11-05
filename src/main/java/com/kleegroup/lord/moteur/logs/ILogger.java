package com.kleegroup.lord.moteur.logs;

import java.util.List;

import com.kleegroup.lord.moteur.IErreur;



/**
 * @author maazreibi
 * L'interface Loggueur permet d'utiler diff�rentes sources pour signaler les erreurs rencontr�e
 * 
 */
public interface ILogger {


	
	/**
	 * Inscrit dans le log la collection des messages d'erreur.
	 * @param listeErreurs  - La collection d'erreurs � logger
	 */
	void log(List<IErreur> listeErreurs);
	/**
	 * Inscrit l'erreur dans le log.
	 * @param err l'erreur � inscrire
	 */
	void log(IErreur err);
	/**
	 * Instruit le loggueur � vider son buffer s'il en a.
	 */
	void flushAndClose();
	
	/**
	* D�finit le nom du fichier source des erreurs de ce loggeur.
	* @param nomFichier le nom du fichier source des erreurs de ce loggeur
	*/
	void setNomFichier(String nomFichier);
	
	 /**
	 * @return le nom du fcihier source des erreurs.
	 */
	String getNomFichier();
	
	/**
	 * @return true si les erreurs contiennent le champ "Reference".
	 */
	boolean hasReferenceColonne();
	/**
	 * @param val true si les erreurs contiendront le champ reference, false sinon.
	 */
	void setReferenceColonne(boolean val);

}
