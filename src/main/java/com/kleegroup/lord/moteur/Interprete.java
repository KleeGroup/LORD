package com.kleegroup.lord.moteur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/** */
public class Interprete {
	private static org.apache.log4j.Logger logAppli = Logger.getLogger(Interprete.class);
	boolean isCompact = false;
	List<ENUM_TYPE_BALISE> type = new ArrayList<>();
	List<String> balises = new ArrayList<>();
	List<Integer> indices = new ArrayList<>();

	Map<String, String> valeur = new HashMap<>();

	IContrainte contrainteOrigine;

	final Integer pasDIndice = Integer.valueOf(-13);

	enum ENUM_TYPE_BALISE {
		/** */
		TYPE_DEMANDE_CONTRAINTE("", true),
		/** */
		TYPE_NOM_FICHIER("nom_fichier", true),
		/** */
		TYPE_ID_VERIFICATION("id_verification", true),
		/** */
		TYPE_NOM_COLONNE("nom_colonne", true),

		/** */
		TYPE_VALEUR("valeur", false),
		/** */
		TYPE_NUM_LIGNE("n_ligne", false), ;
		String id;
		boolean convertibleEnConst;

		ENUM_TYPE_BALISE(String nom, boolean convertibleEnConstante) {
			id = nom;
			convertibleEnConst = convertibleEnConstante;
		}

		static ENUM_TYPE_BALISE fromBalise(String balise) {
			for (ENUM_TYPE_BALISE a : ENUM_TYPE_BALISE.values()) {
				if (a.id.equals(balise)) {
					return a;
				}
			}
			return TYPE_DEMANDE_CONTRAINTE;
		}
	}

	/**
	 * Construit un message d'erreur correspondant � la ligne actuelle.
	 * @param numLigne le num�ro de la ligne d'erreur
	 * @param val les valeur de la ligne d'erreur
	 * @return le message d'erreur
	 */
	public String bind(long numLigne, String val[]) {
		if (!isCompact) {
			/*
			 * evalue les elt qui ne devraient pas changer pour un fichier(nom de colonne,nom de fichier ,..)
			 * les convertit en constantes pour eviter de les reevaluer a chaque appel
			 */
			compact();
			isCompact = true;
		}

		String msg = "";
		for (int i = 0; i < type.size(); i++) {
			switch (type.get(i)) {
				case TYPE_DEMANDE_CONTRAINTE:
					msg += bind(type.get(i), balises.get(i), indices.get(i));
					break;
				case TYPE_NUM_LIGNE:
					msg += numLigne;
					break;
				case TYPE_VALEUR:
					if (indices.get(i) < val.length) {
						msg += val[indices.get(i)];
					} else {
						msg += "[" + ENUM_TYPE_BALISE.TYPE_VALEUR.name() + "(" + indices.get(i) + ")" + "]";
					}
					break;
				default:
			}
		}

		return msg;
	}

	private void compact() {
		for (int i = 0; i < type.size(); i++) {
			if (type.get(i).convertibleEnConst) {
				balises.set(i, bind(type.get(i), balises.get(i), indices.get(i)));
				type.set(i, ENUM_TYPE_BALISE.TYPE_DEMANDE_CONTRAINTE);
			}
		}

		for (int i = type.size() - 2; i >= 0; i--) {
			if (type.get(i) == type.get(i + 1) && type.get(i) == ENUM_TYPE_BALISE.TYPE_DEMANDE_CONTRAINTE) {
				balises.set(i, balises.get(i) + balises.get(i + 1));
				balises.remove(i + 1);
				indices.remove(i + 1);
				type.remove(i + 1);
			}
		}
		logAppli.trace("après compact \n" + this);

	}

	private String bind(ENUM_TYPE_BALISE typeBalise, String balise, int indice) {
		switch (typeBalise) {
			case TYPE_DEMANDE_CONTRAINTE:
				return contrainteOrigine.interprete(balise, indice);
			case TYPE_ID_VERIFICATION:
				return valeur.get(ENUM_TYPE_BALISE.TYPE_ID_VERIFICATION.id);
			case TYPE_NOM_FICHIER:
				return valeur.get(ENUM_TYPE_BALISE.TYPE_NOM_FICHIER.id);
			case TYPE_NOM_COLONNE:
				return valeur.get(ENUM_TYPE_BALISE.TYPE_NOM_COLONNE.id + indice);
			default:
				return "";
		}

	}

	protected static int extraireIndice(final String valeur) {
		String[] tempIndice = valeur.split("\\(|\\)", -1);
		int indice = -1;
		if (tempIndice.length > 1) {
			indice = Integer.parseInt(tempIndice[1]);
		}
		return indice;
	}

	protected static String extraireBalise(final String valeur) {
		return valeur.split("\\(|\\)", -1)[0];
	}

	protected void separerExpression(final String template) {
		final String[] temp = template.split("[\\[\\]]", -1);

		for (int i = 0; i < temp.length; i++) {
			if (i % 2 == 0) {
				//traiter les "constantes". Elles sont tjrs dans les positions paires
				//ignorer les constantes vides
				if ("".equals(temp[i])) {
					continue;
				}
				type.add(ENUM_TYPE_BALISE.TYPE_DEMANDE_CONTRAINTE);
				balises.add(temp[i]);
				indices.add(pasDIndice); //les constantes ne doivent pas avoir d'indice , on leur donne un indice invalide
			} else {
				//traiter les variables.Elles sont tjrs dans les positions impaires
				String balise = extraireBalise(temp[i]);
				int indice = extraireIndice(temp[i]);

				type.add(ENUM_TYPE_BALISE.fromBalise(balise));
				balises.add(balise);
				indices.add(indice);
			}
		}
	}

	/**
	 * Construit un interprete a partir d'un template d'erreur.
	 * Le template contient des balises qui seront interpr�t�s pour g�n�rer les message d'erreur
	 * @param template le template de l'erreur
	 * @return un inteprete pour evaluer le template
	 */
	public static Interprete fromTemplate(final String template) {
		logAppli.trace("template interprete =" + template);
		Interprete inft = new Interprete();
		inft.separerExpression(template);
		logAppli.trace("interprète généré \n" + inft);

		return inft;
	}

	/**{@inheritDoc}*/
	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < type.size(); i++) {

			str += type.get(i) + " - >" + balises.get(i) + "< " + "(" + indices.get(i) + ")" + "\n";
		}
		return str;
	}

	/**
	 * D�finit le nom de la contrainte.
	 * @param idv le nom de la contrainte
	 */
	public void setIdVerif(final String idv) {
		valeur.put(ENUM_TYPE_BALISE.TYPE_ID_VERIFICATION.id, idv);
	}

	/**
	 * D�finit le nom de la colonne de l'erreur.
	 * @param nomCol le nom de la contrainte
	 */
	public void setNomColonne(final String nomCol) {
		valeur.put(ENUM_TYPE_BALISE.TYPE_NOM_FICHIER.id, nomCol);
	}

	/**
	 * D�finit le nom du fichier de l'erreur.
	 * @param nomFichier le nom du fichier
	 */
	public void setNomFichier(final String nomFichier) {
		valeur.put(ENUM_TYPE_BALISE.TYPE_NOM_FICHIER.id, nomFichier);
	}

	/**
	 * D�finit la valeur qui a g�n�r� l'erreur.
	 * @param val la valeur erron�e
	 */

	public void setValeur(final String val) {
		valeur.put(ENUM_TYPE_BALISE.TYPE_VALEUR.id, val);
	}

	/**
	 * D�finit les nom des colonnes du fichier.
	 * @param val les noms des colonnes du fichier
	 */
	public void setNomColonne(final String val[]) {
		for (int i = 0; i < val.length; i++) {
			valeur.put(ENUM_TYPE_BALISE.TYPE_NOM_COLONNE.id + i, val[i]);
		}
	}

	/**
	 * @return la contrainte qui utilise cet interprete.
	 */
	public IContrainte getContrainteOrigine() {
		return contrainteOrigine;
	}

	/**
	 * @param contrainteOrigine d�finit la contrainte qui utilise cet interprete.
	 */
	public void setContrainteOrigine(IContrainte contrainteOrigine) {
		this.contrainteOrigine = contrainteOrigine;
	}

}
