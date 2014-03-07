package com.kleegroup.lord.moteur;

import java.util.ArrayList;
import java.util.List;

/**
 * Repr�sentre une erreur rencntr�e. Contient toutes les informations n�cessaires sous 
 * forme de string.
 * 
 * Cet objet est cr�e par une des classes contraintes, quand une erreur est detect�e.
 */
public class ErreurMultiCol extends Erreur {
	/**
	 * @param contrainteParent la contrainte qui a g�n�r�e l'erreur
	 * @param numLigne num�ro de ligne de l'erreur
	 * @param valeurs les valeurs de tous les champs de la ligne de l'erreurs
	 */
	public ErreurMultiCol(ContrainteMultiCol contrainteParent, long numLigne, String[] valeurs) {
		super(contrainteParent, numLigne, valeurs);
	}

	//@Override
	/**{@inheritDoc}*/
	@Override
	public String getErrColonne() {
		StringBuilder msg = new StringBuilder(getContrainteParent().getFichierParent().getColonne(getContrainteParent().getIndiceParam()[0]).getDescOuNom());
		for (int i = 1; i < getContrainteParent().getIndiceParam().length; i++) {
			msg.append(", " + getContrainteParent().getFichierParent().getColonne(getContrainteParent().getIndiceParam()[i]).getDescOuNom());
		}
		return msg.toString();
	}

	/**{@inheritDoc}*/
	@Override
	public String getErrMessage() {
		return getContrainteParent().getMessageErreur();
	}

	/**{@inheritDoc}*/
	@Override
	public String getErrValeur() {
		StringBuilder msg = new StringBuilder("'" + errValeurs[getContrainteParent().getIndiceParam()[0]] + "'");
		for (int i = 1; i < getContrainteParent().getIndiceParam().length; i++) {
			msg.append(", >" + errValeurs[getContrainteParent().getIndiceParam()[i]] + "<");
		}
		return msg.toString();
	}

	/**{@inheritDoc}*/
	@Override
	public ContrainteMultiCol getContrainteParent() {
		return (ContrainteMultiCol) errOrigine;
	}

	/**{@inheritDoc} 	 */
	@Override
	public List<Colonne> getColonnes() {
		List<Colonne> listeCol = new ArrayList<>();
		for (int i = 0; i < getContrainteParent().getIndiceParam().length; i++) {
			listeCol.add(getFichier().getColonne(getContrainteParent().getIndiceParam()[i]));
		}

		return listeCol;
	}

}
