package com.kleegroup.lord.moteur.util;

/**
 * Enumération simple pour indiquer quel est le séparateur de décimale (point ou virgule).
 */
public enum SeparateurDecimales {
    /**
     * Séparateur virgule (valeur par défaut).
     */
    SEPARATEUR_VIRGULE(',', ", (virgule)"),
    /**
     * Séparateur point.
     */
    SEPARATEUR_POINT('.', ". (point)"),
    ;
	
	private char separateur = ',';
	private String label;
    
	SeparateurDecimales(char separateur, String label) {
		this.separateur = separateur;
		this.label = label;
	}
	
	public char value() {
		return separateur;
	}
	
	@Override
	public String toString() {
		if (label != null) {
			return label;
		}
		return super.toString();
	}
	
}
