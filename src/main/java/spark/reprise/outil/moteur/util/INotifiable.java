package spark.reprise.outil.moteur.util;

import spark.reprise.outil.moteur.Fichier;

/**
 * Cette interface sert à créer des éléments qui seront notifiés de certains
 * évènements qui ont lieu lors de la vérification. Cette notification est
 * nécessaire pour mettre à jour les données de l'interface graphique.
 */
public interface INotifiable {
    
    /**
     * Indique le nombre de caractères traités jusqu'à présent.
     * @param l le nombre de caractères traités jusqu'à présent.
     */
    void caractereTraites(long l);

    /**
     * indique que la vérification de ce fichier a commencée.
     * @param nomFichier le nom du fichier
     */
    void debutFichier(String nomFichier);

    /**
     * indique que la vérification de ce fichier est finie, et l'état de la verification.
     * voir aussi {@link Fichier.ETAT}
     * @param nomFichier le nom fichier dont la verif est terminee
     * @param etat l'etat du fichier (verifié sans erreur, avec erreurs, verif abandonnée...)
     */
    void finFichier(String nomFichier , Fichier.ETAT etat);
}
