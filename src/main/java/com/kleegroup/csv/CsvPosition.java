/*
 * Created on 13 avr. 2004
 * by jmainaud
 */
package com.kleegroup.csv;

/**
 * CsvPosition du curseur.
 *
 * @author $Author: maalzreibi $
 * @version $Revision: 1.1 $
 */
public class CsvPosition {
	/** Num�ro du caract�re courant. */
	private long caractere;
	
	/** Num�ro de l'enregistrement. */
	private long enregistrement;

	/** Num�ro de la ligne du fichier. */
	private long ligne;

	/** Num�ro de la colonne du fichier (Nb caract�res). */
	private long colonne;

	/**
	 * Construit une nouvelle instance de CsvPosition
	 *
	 * @param enregistrement
	 */
	CsvPosition(long enregistrement) {
		this(0, enregistrement, 0, 0);
	}

	/**
	 * Construit une nouvelle instance de CsvPosition
	 *
	 * @param enregistrement
	 * @param ligne
	 * @param colonne
	 */
	CsvPosition(long caractere, long enregistrement, long ligne, long colonne) {
		this.caractere = caractere;
		this.enregistrement = enregistrement;
		this.ligne = ligne;
		this.colonne = colonne;
	}

	/**
	 * Copie la position.
	 *
	 * @return La copie de la position.
	 */
	CsvPosition copy() {
		return new CsvPosition(caractere, enregistrement, ligne, colonne);
	}

	/**
	 * Ajoute une ligne. Le nombre de colonne est repis � 1.
	 */
	void nouvelleLigne() {
		++ligne;
		colonne = 0;
	}

	/**
	 * Ajoute une colonne.
	 */
	void nouvelleColonne() {
		++colonne;
		++caractere;
	}

	/**
	 * Ajoute un enregsitrement.
	 */
	void nouvelEnregistrement() {
		++enregistrement;
	}

	/**
	 * Lit la propri�t� <code>enregistrement</code>.
	 *
	 * @return la valeur de <code>enregistrement</code>.
	 */
	public long getEnregistrement() {
		return enregistrement;
	}

	/**
	 * Lit la propri�t� <code>ligne</code>.
	 *
	 * @return la valeur de <code>ligne</code>.
	 */
	public long getLigne() {
		return ligne;
	}

	/**
	 * Lit la propri�t� <code>colonne</code>.
	 *
	 * @return la valeur de <code>colonne</code>.
	 */
	public long getColonne() {
		return colonne;
	}

	/**
	 * Lit la propri�t� <code>caractere</code>.
	 *
	 * @return la valeur de <code>caractere</code>.
	 */
	public long getCaractere() {
		return caractere;
	}
	
	
}
