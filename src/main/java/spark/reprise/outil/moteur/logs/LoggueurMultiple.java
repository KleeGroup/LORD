package spark.reprise.outil.moteur.logs;

import java.util.ArrayList;
import java.util.List;

import spark.reprise.outil.moteur.IErreur;

/**
 * Un loggueur qui affiche les messages d'erreur sur la console.
 * @author maazreibi
 */
public class LoggueurMultiple extends AbstractLoggueur {

	protected List<ILogger> loggers = new ArrayList<>();

	/**
	 * Construit un logger qui distribue les erreurs à plusieurs loggeurs.
	 */
	public LoggueurMultiple() {
		super();
	}

	/**{@inheritDoc}*/
	@Override
	public void log(final IErreur err) {
		for (ILogger logger : loggers) {
			logger.log(err);
		}

	}

	/**{@inheritDoc}*/
	@Override
	public void flushAndClose() {
		for (ILogger logger : loggers) {
			logger.flushAndClose();
		}
	}

	/**
	 * Rajoute un loggueur à la liste des loggueurs à notifier.
	 * @param logger le loggueur à rajouter.
	*/
	public void addLogger(ILogger logger) {
		loggers.add(logger);
	}

	/**{@inheritDoc}*/
	@Override
	public void setReferenceColonne(boolean val) {
		for (ILogger l : loggers) {
			l.setReferenceColonne(val);
		}
	}

}
