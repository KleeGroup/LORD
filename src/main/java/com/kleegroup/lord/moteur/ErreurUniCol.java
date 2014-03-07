package com.kleegroup.lord.moteur;

import java.util.ArrayList;
import java.util.List;

/**
 * Repr�sentre une erreur rencntr�e. Contient toutes les informations n�cessaires sous 
 * forme de string.
 * 
 * Cet objet est cr�e par une des classes contraintes, quand une erreur est detect�e.
 */
public class ErreurUniCol extends Erreur {

	/**
	 * Indique que la valeur est vide, et qu'il est inutile de continuer les v�rifications.
	 */
	public static final ErreurUniCol ERR_VIDE = new ErreurUniCol();

	private ErreurUniCol() {/**/

	}

	/**
	 * @param contrainteParent La contrainte unicol qui g�n�re l'erreur
	 * @param numLigne le num�ro de la ligne de l'erreur
	 * @param valeurs les valeurs de l'erreur
	 */
	public ErreurUniCol(ContrainteUniCol contrainteParent, long numLigne, String valeurs[]) {
		super(contrainteParent, numLigne, valeurs);
	}

	/**{@inheritDoc}*/
	@Override
	public ContrainteUniCol getContrainteParent() {
		return (ContrainteUniCol) errOrigine;
	}

	/**{@inheritDoc}*/
	@Override
	public String getErrColonne() {
		//renvoie la description de la colonne parent 
		//ou le nom de la colonne si cette derniere est nulle 
		return getContrainteParent().getColonneParent().getDescOuNom();
	}

	/**{@inheritDoc}*/
	@Override
	public String getErrValeur() {
		return errValeurs[getContrainteParent().getColonneParent().getPosition()];
	}

	/**{@inheritDoc}*/
	@Override
	public String getErrMessage() {
		final String msg = getContrainteParent().getInterprete().bind(errLigne, errValeurs);
		//errValeurs=null;
		return msg;
	}

	/**{@inheritDoc}*/
	@Override
	public List<Colonne> getColonnes() {
		List<Colonne> listeCol = new ArrayList<>();
		listeCol.add(getContrainteParent().getColonneParent());
		return listeCol;
	}

}
