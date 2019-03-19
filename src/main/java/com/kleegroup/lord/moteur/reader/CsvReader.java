/*
 * Created on 7 avr. 2004
 * by jmainaud
 */
package com.kleegroup.lord.moteur.reader;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de lecture d'un fichier CSV.
 *
 * @author jmainaud, $Author: maalzreibi $
 * @version $Revision: 1.1 $
 *
 * @since 7 avr. 2004
 */
public class CsvReader {
	/** Etat Début de l'enregistrement. */
	private static final int ETAT_ENREGISTREMENT = 0;

	/** Etat Champ. */
	private static final int ETAT_CHAMP = 1;

	/** Etat Valeur. */
	private static final int ETAT_VALEUR = 2;

	/** Etat Valeur avec guillemets. */
	private static final int ETAT_VALEUR_GUILLEMETS = 3;

	/** Etat Guillemets. */
	private static final int ETAT_GUILLEMETS = 4;

	/** Etat Erreur. */
	private static final int ETAT_ERREUR = 6;

	/** Fin de fichier. */
	private static final int CHAR_FDF = -1;

	/** Fin de ligne. */
	private static final char CHAR_FDL = '\n';

	/** Caractère ignoré. */
	private static final char CHAR_IGNORE = '\r';

	/** Fin de champ. */
	private static final char CHAR_FDC_DEFAUT = ';';

	/** Guillemets. */
	private static final char CHAR_GUILLEMET = '"';

	/** Caractère marquant la fin d'un champ. */
	private char finDeChamp = CHAR_FDC_DEFAUT;

	/** CsvPosition du curseur. */
	private final CsvPosition posCurseur;

	/** CsvPosition de l'enregistrement. */
	private CsvPosition posEnregistrement;

	/** CsvPosition du prochain enregistrement. */
	private CsvPosition posProchainEnregistrement;

	/** Erreur lattente. */
	private CsvException erreur;

	/** Fin de fichier atteinte. */
	private boolean finDeFichier;

	/** Indique s'il y a un autre. */
	private Boolean enregistrementLu;

	/** Nouveau champ */
	private final StringBuffer champ;

	/** Liste des valeurs lues */
	private final List<CharSequence> enregistrement;

	/** Ligne courante. */
	private final StringBuffer enregistrementTexte;

	/** Flux d'entrée. */
	private final Reader in;

	//	/** Compteur de caracteres lus*/
	//	private long nbCaracteresLus=0;

	/**
	 * Construit une nouvelle instance de CsvReader.
	 *
	 * @param in un objet {@link Reader} qui permet de lire le fichier csv
	 */
	public CsvReader(Reader in) {
		this.in = in;
		posCurseur = new CsvPosition(0, 1, 0, 0);
		finDeFichier = false;
		champ = new StringBuffer();
		enregistrement = new ArrayList<>();
		enregistrementTexte = new StringBuffer();
	}

	private void nouveauCharactere(char c) {
		champ.append(c);
	}

	private void nouveauChamp() {
		enregistrement.add(champ.toString());
		champ.setLength(0);
	}

	private void nouvelleLigne() {
		posCurseur.nouvelleLigne();
	}

	private void nouvelEnregistrement() {
		if (!isLigneVide()) {
			posCurseur.nouvelEnregistrement();
		}

		posProchainEnregistrement = posCurseur.copy();
		erreur = null;
		enregistrementTexte.setLength(0);
		enregistrement.clear();
		champ.setLength(0);
	}

	private void nouvelleErreur(String message) {
		if (erreur == null) {
			erreur = new CsvException(message, posCurseur.copy());
		}
	}

	private boolean isLigneVide() {
		return enregistrementTexte.toString().trim().length() == 0;
	}

	/**
	 * Donne le nom du dernier enregistrement lu. Ou du prochain si <code>hasNext()</code> a été
	 * appelé.
	 *
	 * @return le nom du dernier enregistrement lu.
	 */
	public long getNumeroEnregistrement() {
		return (posEnregistrement != null) ? posEnregistrement.getEnregistrement() : (-1);
	}

	/**
	 * Donne la position courante dans le flux.
	 *
	 * @return la position courante dans le flux.
	 */
	public CsvPosition getPosition() {
		return posCurseur.copy();
	}

	/**
	 * Lit la propriété <code>fde</code>.
	 *
	 * @return la valeur de <code>fde</code>.
	 */
	public char getFinDeChamp() {
		return finDeChamp;
	}

	/**
	 * Change la propriété <code>fde</code>.
	 *
	 * @param fde la nouvelle valeur de <code>fde</code>.
	 */
	public void setFinDeChamp(char fde) {
		this.finDeChamp = fde;
	}

	/**
	 * Indique s'il y a un autre enregistrement.
	 *
	 * @return <code>true</code> s'il y a un autre enregsitrement.
	 *
	 * @throws IOException En cas d'erreur d'entré-sortie.
	 */
	public boolean hasNext() throws IOException {
		// Si la valeur a déjà été calculée, on passe.
		if (enregistrementLu == null) {
			// Si la fin du fichier a été atteinte, on renvoie null.
			if (finDeFichier) {
				enregistrementLu = Boolean.FALSE;
			} else {
				int etat = ETAT_ENREGISTREMENT;
				nouvelEnregistrement();

				do {
					// Lecture du caractère suivant
					final int iChar = in.read();
					posCurseur.nouvelleColonne();

					// Traitement de la fin de fichier
					// On enregistre l'état et on sort de la boucle de lecture.
					if (iChar == CHAR_FDF) {
						if (champ.length() > 0) {
							nouveauChamp();
						}
						finDeFichier = true;
						break;
					}

					final char cChar = (char) iChar;
					enregistrementTexte.append(cChar);

					// on ignore les caractères à ignorer.
					if (cChar == CHAR_IGNORE) {
						continue;
					}

					switch (etat) {
						case ETAT_ENREGISTREMENT:
						case ETAT_CHAMP:

							if (cChar == CHAR_FDL) {
								etat = ETAT_ENREGISTREMENT;
							} else if (cChar == getFinDeChamp()) {
								nouveauChamp();
								etat = ETAT_CHAMP;
							} else if (cChar == CHAR_GUILLEMET) {
								etat = ETAT_VALEUR_GUILLEMETS;
							} else {
								nouveauCharactere(cChar);
								etat = ETAT_VALEUR;
							}

							break;

						case ETAT_VALEUR:

							if (cChar == CHAR_FDL) {
								nouveauChamp();
								etat = ETAT_ENREGISTREMENT;
							} else if (cChar == getFinDeChamp()) {
								nouveauChamp();
								etat = ETAT_CHAMP;
							} else if (cChar == CHAR_GUILLEMET) {
								nouvelleErreur("Guillemet dans un champ non encadré de guillemets.");
								etat = ETAT_ERREUR;
							} else {
								nouveauCharactere(cChar);
								etat = ETAT_VALEUR;
							}

							break;

						case ETAT_VALEUR_GUILLEMETS:

							if (cChar == CHAR_GUILLEMET) {
								etat = ETAT_GUILLEMETS;
							} else {
								nouveauCharactere(cChar);
								etat = ETAT_VALEUR_GUILLEMETS;
							}
							break;

						case ETAT_GUILLEMETS:

							if (cChar == CHAR_GUILLEMET) {
								nouveauCharactere(CHAR_GUILLEMET);
								etat = ETAT_VALEUR_GUILLEMETS;
							} else if (cChar == CHAR_FDL) {
								nouveauChamp();
								etat = ETAT_ENREGISTREMENT;
							} else if (cChar == getFinDeChamp()) {
								nouveauChamp();
								etat = ETAT_CHAMP;
							} else {
								nouvelleErreur("Guillemet simple dans un champ.");
								etat = ETAT_ERREUR;
							}

							break;

						case ETAT_ERREUR:

							if (cChar == CHAR_FDL) {
								etat = ETAT_ENREGISTREMENT;
							}

							break;
						default:
					}

					// On enregistre la nouvelle ligne éventuelle.
					if (cChar == CHAR_FDL) {
						nouvelleLigne();
					}
				} while ((etat != ETAT_ENREGISTREMENT) || isLigneVide());

				enregistrementLu = Boolean.valueOf(!isLigneVide());
			}
		}

		return enregistrementLu.booleanValue();
	}

	/**
	 * Donne l'enregistement suivant. Lors qu'il n'y a plus d'enregistrement possible, renvoie
	 * <code>null</code>.
	 *
	 * @return Un tableau de String représentant l'enregistrement suivant.
	 *
	 * @throws CsvReaderException En cas d'erreur dans le fichier.
	 * @throws IOException En cas d'erreur d'entré-sortie.
	 */
	public String[] next() throws CsvException, IOException {
		if (!hasNext()) {
			return new String[0];
		}

		// On avance
		enregistrementLu = null;
		posEnregistrement = posProchainEnregistrement;

		if (erreur != null) {
			throw erreur;
		}

		// Fin de l'enregistrement
		return enregistrement.toArray(new String[enregistrement.size()]);
	}

	/**
	 * Ferme le lecteur de Fichiers CSV.
	 *
	 * @throws IOException En cas d'erreur d'entré-sortie.
	 */
	public void close() throws IOException {
		in.close();
	}

}
