package com.kleegroup.lord.moteur.util;

import com.kleegroup.lord.moteur.Fichier;

/**
 * Cette interface sert � cr�er des �l�ments qui seront notifi�s de certains
 * �v�nements qui ont lieu lors de la v�rification. Cette notification est
 * n�cessaire pour mettre � jour les donn�es de l'interface graphique.
 */
public interface INotifiable {
    
    /**
     * Indique le nombre de caract�res trait�s jusqu'� pr�sent.
     * @param l le nombre de caract�res trait�s jusqu'� pr�sent.
     */
    void caractereTraites(long l);

    /**
     * indique que la v�rification de ce fichier a commenc�e.
     * @param nomFichier le nom du fichier
     */
    void debutFichier(String nomFichier);

    /**
     * indique que la v�rification de ce fichier est finie, et l'�tat de la verification.
     * voir aussi {@link Fichier.ETAT}
     * @param nomFichier le nom fichier dont la verif est terminee
     * @param etat l'etat du fichier (verifi� sans erreur, avec erreurs, verif abandonn�e...)
     */
    void finFichier(String nomFichier , Fichier.ETAT etat);
}
