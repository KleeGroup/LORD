package com.kleegroup.lord.moteur.logs;

import java.io.PrintStream;

import com.kleegroup.lord.moteur.IErreur;

/**
 * Un loggueur qui affiche les messages d'erreur sur la console.
 * @author maazreibi
 */
public class LoggueurConsole extends AbstractLoggueur {

	protected String format = "|%1$-40s|%2$-4s|%3$-28s|%4$-20s|%5$-34s|\n";
	protected boolean headerPublie=false;
	protected long nbErr=0;
	protected PrintStream out=System.out;

	/**
	 * Cree un loggueur console, sortie sur la console.
	 */
	public LoggueurConsole(){
		this(System.out);
	}
	/**
	 * Cree un loggueur console, sortie sur le PrintStream sp�cifi�.
	 * @param out le printStream utilis� pour logger les erreurs
	 */
	public LoggueurConsole(PrintStream out){
		super();
	    this.out=out;

	}


	/**{@inheritDoc}*/
	@Override
	public void log(final IErreur err){
		if(!headerPublie){
			out.println("");
			out.format(format,"nom","pos","nom_col","     valeur","     message_erreur");
			headerPublie=true;
		}
		out.format(format,err.getNomFichier(),err.getErrLigne(),err.getErrColonne()
				,err.getErrValeur(),err.getErrMessage());

	}

	/**{@inheritDoc}*/
	@Override
	public void flushAndClose() {
		headerPublie=false;
	}



}
