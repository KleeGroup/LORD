package com.kleegroup.lord.moteur.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.kleegroup.lord.moteur.exceptions.CaractereInterdit;
import com.kleegroup.lord.moteur.util.CountingReader;
import com.kleegroup.lord.moteur.util.ICSVDataSource;

/**
 * Cette classe sert comme adaptateur pour la classe CsvReader.
 * Elle permet au moteur de l'utiliser comme source de donnée.
 */
public class CsvReaderAdapter implements ICSVDataSource {
	private static org.apache.log4j.Logger logAppli = Logger.getLogger(CsvReaderAdapter.class);

	protected CsvReader reader;
	protected CountingReader counter;
	protected long size=0;
	
	/**
	 * @param path le chemin d'accès du fichier à lire
	 */
	public CsvReaderAdapter(String path){
	    this(path,"ISO-8859-15");
	}
	
	/**
	 * @param path path le chemin d'acces du fichier a lire
	 * @param encoding l'encodage du fichier à lire
	 */
	public CsvReaderAdapter(String path, String encoding){
		try{
			size=(new File(path)).length();
			counter=new CountingReader(new InputStreamReader(new FileInputStream(path), encoding));
			reader=new CsvReader( counter);
		}catch(final UnsupportedEncodingException e){
			reader=null;
			logAppli.error(e);
		}catch(final FileNotFoundException e){
			reader=null;
			logAppli.error(e);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean hasNext() throws IOException {
	    if (reader==null){
		return false;
	    }
		final boolean val=reader.hasNext();
		if(!val){
		    reader.close();
		}
		return val;
	}
	
	/** {@inheritDoc}
	 * @throws CaractereInterdit
	 */
	@Override
	public String[] next() throws IOException, CaractereInterdit {
		if (reader==null) {
		    return new String[0];
		}
		try {
		  	return reader.next();
		} catch (final CsvException e) {
			throw new CaractereInterdit(e.getEnregistrement(), e.getColonne(), "");
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public int getPosition() {

		return (int)reader.getPosition().getLigne();
	}
	
	/** {@inheritDoc} */
	@Override
	public long getTotalSize() {
		return size;
	}
	
	/** {@inheritDoc} */
	@Override
	public long getNbCharactersRead(){
		return counter.getNbCharactersRead();
	}
	
	/** {@inheritDoc} */
	@Override
	public void setFieldSeparator(char separator) {
	    reader.setFinDeChamp(separator);
	}
}
