/*
 * Created on 13 avr. 2004
 * by jmainaud
 */
package com.kleegroup.lord.utils.csv;

/**
 * Exception de lecture du fichier CSV.
 * 
 * @author jmainaud, $Author: maalzreibi $
 * @version $Revision: 1.1 $
 * 
 * @since 13 avr. 2004
 */
public class CsvReaderException extends CsvException {
    private static final long serialVersionUID = -7823795381901899780L;

    /**
     * Construit une nouvelle instance de CsvReaderException
     * 
     * @param message
     * @param position
     */
    CsvReaderException(String message, CsvPosition position) {
        super(message, position);
    }
}
