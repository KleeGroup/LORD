package com.kleegroup.lord.moteur.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * C'est une classe qui h�rite de {@link BufferedReader} et qui sert � compter pr�cisement les
 * caract�res qui sont lus par un {@link Reader}. <br><br>
 * A chaque appel � read, skip , cette classe calcule le nombre de caract�re lues et le stock.
 * On peut acc�der � ce nombre en appelant la fontion {@link #getNbCharactersRead()}.
 * <br><br>
 * Cette classe est utilis�e pour calculer le progr�s de v�rification d'un fichier.
 */
public class CountingReader extends BufferedReader  {
	protected long nbCharRead = 0;

	/**
	 * @param in le Reader dont on doit compter les caract�res lues.
	 */
	public CountingReader(Reader in) {
		super(in);
	}
	/**{@inheritDoc}*/
	@Override
	public int read() throws IOException {
		final int a = super.read();
		if (a != -1) {
			nbCharRead++;
		}
		return a;

	}
	/**{@inheritDoc}*/
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		final int res= super.read(cbuf, off, len);
		if(res>0){
			nbCharRead+=res;
		}
		return res;
	}

	/**{@inheritDoc}*/
	@Override
	public String readLine() throws IOException {
		
		final String res= super.readLine();
		if(res!=null){
		    nbCharRead+=res.length();
		}
		return res;
	}
	/**{@inheritDoc}*/
	@Override
	public long skip(long n) throws IOException {
		final long res= super.skip(n);
		nbCharRead+=res;
		return res;
		

	}

	
	 /**
	 * @return le nombre de caract�res lus par ce reader.
	  * 
	 */
	public long getNbCharactersRead() {
		return nbCharRead;
	}

}
