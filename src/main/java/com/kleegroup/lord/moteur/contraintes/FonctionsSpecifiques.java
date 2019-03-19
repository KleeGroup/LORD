package com.kleegroup.lord.moteur.contraintes;

/**
 * Contient les fonctions statiques utilisables pour créer une contrainte multicolonne.
 * 
 * @author maazreibi
 *
 */
public abstract class FonctionsSpecifiques {
	/**
	 * compare deux strings.
	 * @param string1 le premier string
	 * @param string2 le second string
	 * @return si string1 est ï¿½gal ï¿½ string2
	 */
	public static boolean test (final String string1,final String string2){
		return string1.equals(string2);
	}
	/**
	 * vérifie si un string est vide.
	 * @param valeur le string à vérifier
	 * @return si valeur est vide
	 */
	public static boolean notnull (final String valeur){
		return "".equals(valeur);
	}
	
	/**
	 * @param s1 le premier string a comparer 
	 * @param s2 le second string a comparer 
	 * @return  si taille(s1)< taille(s2)
	 */
	public static boolean tailleInf(final String s1, final String s2){
		return s1.length()<s2.length();
	}
}
