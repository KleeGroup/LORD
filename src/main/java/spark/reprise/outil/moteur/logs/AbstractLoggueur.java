package spark.reprise.outil.moteur.logs;

import java.util.List;

import spark.reprise.outil.moteur.IErreur;

/**
 * classe abstraite qui implemente l'interface <code>ILogger</code> . Elle
 * fournit une implémentation d'une méthode qui permet de logguer une liste
 * d'erreurs
 * 
 * @author maazreibi
 * 
 */
public abstract class AbstractLoggueur implements ILogger {
    private String nomFichier;
    private boolean refColonne=false;

    /** {@inheritDoc} */
    @Override
	public void log(final List<IErreur> listeErreurs) {
	for (IErreur s : listeErreurs) {
	    log(s);
	}
    }

   /**{@inheritDoc}*/
    @Override
	public String getNomFichier() {
	return nomFichier;
    }

    /**{@inheritDoc}*/
    @Override
	public void setNomFichier(final String nomFichier) {
	this.nomFichier = nomFichier;
    }
    /**{@inheritDoc}*/
    @Override
	public  boolean hasReferenceColonne(){
	return refColonne;
    }
    /**{@inheritDoc}*/
    @Override
	public void setReferenceColonne(boolean val){
	refColonne=val;
    }
}
