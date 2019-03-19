/*
 * Created on 13 avr. 2004
 * by jmainaud
 */
package com.kleegroup.lord.moteur.reader;

/**
 * @author jmainaud, $Author: maalzreibi $
 * @version $Revision: 1.1 $
 * @since 13 avr. 2004
 */
public class CsvException extends Exception {
    private static final long serialVersionUID = 3940084923077140652L;

    private CsvPosition position;

    /**
     * Construit une nouvelle instance de CsvReaderException
     * 
     * @param position
     */
    protected CsvException(String message, CsvPosition position) {
        super(message);
        this.position = position;
    }

    /**
     * Indique la ligne de l'erreur.
     * 
     * @return la ligne de l'erreur.
     */
    public long getLigne() {
        return position.getLigne();
    }

    /**
     * Indique la colonne de l'erreur.
     * 
     * @return la colonne de l'erreur.
     */
    public long getColonne() {
        return position.getColonne();
    }

    /**
     * Indique l'enregistrement de l'erreur.
     * L'enregistrement est différent d'une ligne. L'enregistrement est l'ensemble complet 
     * des valeurs, une pour chaque colonne.Il peut donc s'étendre sur plusieurs lignes.
     * @return l'enregistrement de l'erreur.
     */
    public long getEnregistrement() {
        return position.getEnregistrement();
    }

}
