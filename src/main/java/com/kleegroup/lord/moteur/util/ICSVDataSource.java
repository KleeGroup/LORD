package com.kleegroup.lord.moteur.util;

import java.io.IOException;

import com.kleegroup.lord.moteur.exceptions.CaractereInterdit;

/**
 * Cette interface doit être implémentée par les sources les sources du moteur.
 * Elle définit les fonctions nécessaire au moteur pour lire les données. 
 */
public interface ICSVDataSource  {

	/**
	 * Normalement, la taille totale est le taille du fichier.
	 * @return le nombre de caractère que contient la source. 
	 */
	long getTotalSize();
	/**
	 * Sert à calculer le progrès de la vérification.
	 * @return le nombre de caractères lues par la source.
	 */
	long getNbCharactersRead();
	/**
	 * Définit le séparateur de champ de la source.
	 * @param separator le séparateur de champ de la source.
	 */
	void setFieldSeparator(char separator);
	/**
	 * @return true si la source contient encore des données , false sinon.
	 * @throws IOException si la source est illisible.
	 * @throws CaractereInterdit si un caractère interdit est rencontré
	 */
	boolean hasNext() throws IOException,CaractereInterdit;
	/**
	 * @return la position dans la source (la ligne actuelle)
	 */
	int getPosition();
	/**
	 * @return les données de la source 
	 * @throws IOException si la source est illisible.
	 * @throws CaractereInterdit si la source contient un caractère interdit
	 */
	String[] next() throws IOException, CaractereInterdit;
}
