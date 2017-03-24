package com.kleegroup.lord.moteur.util;

/**
 * Enumération simple pour indiquer quel est le séparateur de champs.
 */
public enum SeparateurChamps {
	/**
	 * Séparateur point-virgule (par défaut).
	 */
    SEPARATEUR_POINT_VIRGULE(';', "; (point-virgule)"),
	/**
	 * Séparateur virgule.
	 */
    SEPARATEUR_VIRGULE(',', ", (virgule)"),
	/**
	 * Séparateur barre verticale.
	 */
    SEPARATEUR_PIPE('|', "| (pipe)"),
    ;
	
	private char separateur = ';';
	private String label;
    
	SeparateurChamps(char separateur, String label) {
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
