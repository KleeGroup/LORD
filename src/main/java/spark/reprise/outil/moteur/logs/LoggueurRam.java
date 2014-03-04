
package spark.reprise.outil.moteur.logs;

import java.util.ArrayList;
import java.util.List;

import spark.reprise.outil.moteur.ErreurConstante;
import spark.reprise.outil.moteur.IErreur;

/**
 * Un loggueur qui affiche les messages d'erreur sur la console.
 * @author maazreibi
 */
public class LoggueurRam extends AbstractLoggueur {
	protected List<IErreur> listeErreurs=new ArrayList<IErreur>();

	/**
	 * Construit un loggeur qui garde les erreurs dans la memoire.
	 */
	public LoggueurRam(){
	    super();
	}
	/**{@inheritDoc}*/
	@Override
	public void log(final IErreur err){
		listeErreurs.add(new ErreurConstante(err));
	}
	/**{@inheritDoc}*/
	@Override
	public void flushAndClose(){
		//ne fais rien
	}

	/**renvoie la liste des erreurs.
	 * @return la liste des erreurs
	 * */
	public List<IErreur> getErreurs(){
		return listeErreurs;
	}
}

