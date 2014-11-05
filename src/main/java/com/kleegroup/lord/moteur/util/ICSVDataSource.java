package com.kleegroup.lord.moteur.util;

import java.io.IOException;

import com.kleegroup.lord.moteur.exceptions.CaractereInterdit;

/**
 * Cette interface doit ï¿½tre implï¿½mentï¿½e par les sources les sources du moteur.
 * Elle dï¿½finit les fonctions nï¿½cessaire au moteur pour lire les donnï¿½es. 
 */
public interface ICSVDataSource  {

	/**
	 * Normalement, la taille totale est le taille du fichier.
	 * @return le nombre de caractï¿½re que contient la source. 
	 */
	long getTotalSize();
	/**
	 * Sert ï¿½ calculer le progrï¿½s de la vï¿½rification.
	 * @return le nombre de caractï¿½res lues par la source.
	 */
	long getNbCharactersRead();
	/**
	 * dï¿½finit le séparateur de champ de la source.
	 * @param separator le séparateur de champ de la source.
	 */
	void setFieldSeparator(char separator);
	/**
	 * @return true si la source conteint encore des donnï¿½es , false sinon.
	 * @throws IOException si la source est illisible.
	 * @throws CaractereInterdit si un caractere interdit est rencontre
	 */
	boolean hasNext() throws IOException,CaractereInterdit;
	/**
	 * @return la position dans la source (la ligne actuelle)
	 */
	int getPosition();
	/**
	 * @return les donnï¿½es de la source 
	 * @throws IOException si la source est illisible.
	 * @throws CaractereInterdit si la source contient un caractere interdit
	 */
	String[] next() throws IOException, CaractereInterdit;
}
