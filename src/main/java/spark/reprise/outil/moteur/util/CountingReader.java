package spark.reprise.outil.moteur.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * C'est une classe qui hérite de {@link BufferedReader} et qui sert à compter précisement les
 * caractères qui sont lus par un {@link Reader}. <br><br>
 * A chaque appel à read, skip , cette classe calcule le nombre de caractère lues et le stock.
 * On peut accéder à ce nombre en appelant la fontion {@link #getNbCharactersRead()}.
 * <br><br>
 * Cette classe est utilisée pour calculer le progrès de vérification d'un fichier.
 */
public class CountingReader extends BufferedReader  {
	protected long nbCharRead = 0;

	/**
	 * @param in le Reader dont on doit compter les caractères lues.
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
	 * @return le nombre de caractères lus par ce reader.
	  * 
	 */
	public long getNbCharactersRead() {
		return nbCharRead;
	}

}
