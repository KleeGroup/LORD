package com.kleegroup.lord.moteur.util;

import java.io.IOException;

import com.kleegroup.lord.moteur.exceptions.CaractereInterdit;

/**
 * Cette interface doit �tre impl�ment�e par les sources les sources du moteur.
 * Elle d�finit les fonctions n�cessaire au moteur pour lire les donn�es. 
 */
public interface ICSVDataSource  {

	/**
	 * Normalement, la taille totale est le taille du fichier.
	 * @return le nombre de caract�re que contient la source. 
	 */
	long getTotalSize();
	/**
	 * Sert � calculer le progr�s de la v�rification.
	 * @return le nombre de caractères lues par la source.
	 */
	long getNbCharactersRead();
	/**
	 * D�finit le s�parateur de champ de la source.
	 * @param separator le s�parateur de champ de la source.
	 */
	void setFieldSeparator(char separator);
	/**
	 * @return true si la source contient encore des donn�es , false sinon.
	 * @throws IOException si la source est illisible.
	 * @throws CaractereInterdit si un caract�re interdit est rencontr�
	 */
	boolean hasNext() throws IOException,CaractereInterdit;
	/**
	 * @return la position dans la source (la ligne actuelle)
	 */
	int getPosition();
	/**
	 * @return les donn�es de la source 
	 * @throws IOException si la source est illisible.
	 * @throws CaractereInterdit si la source contient un caract�re interdit
	 */
	String[] next() throws IOException, CaractereInterdit;
	
	/**
	 * Ferme le lecteur de Fichiers CSV.
	 */
	public void close();
}
