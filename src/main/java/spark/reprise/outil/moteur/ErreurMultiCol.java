package spark.reprise.outil.moteur;

import java.util.ArrayList;
import java.util.List;



/**
 * Représentre une erreur rencntrée. Contient toutes les informations nécessaires sous 
 * forme de string.
 * 
 * Cet objet est crée par une des classes contraintes, quand une erreur est detectée.
 */
public class ErreurMultiCol extends Erreur {
	/**
	 * @param contrainteParent la contrainte qui a générée l'erreur
	 * @param numLigne numéro de ligne de l'erreur
	 * @param valeurs les valeurs de tous les champs de la ligne de l'erreurs
	 */
	public ErreurMultiCol(ContrainteMultiCol contrainteParent,long numLigne,String[]valeurs){
		super(contrainteParent,numLigne,valeurs);			
	}

	//@Override
	/**{@inheritDoc}*/
	@Override
	public String getErrColonne() {
		StringBuilder msg=new StringBuilder(
			getContrainteParent().getFichierParent().getColonne( getContrainteParent().getIndiceParam()[0]).getDescOuNom());
		for(int i=1;i<getContrainteParent().getIndiceParam().length;i++ ){
			msg.append(", "+getContrainteParent().getFichierParent().getColonne( getContrainteParent().getIndiceParam()[i]).getDescOuNom());
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
		StringBuilder msg= new StringBuilder("'"+errValeurs[ getContrainteParent().getIndiceParam()[0]]+"'");
		for(int i=1;i<getContrainteParent().getIndiceParam().length;i++ ){
			msg.append(", >"+errValeurs [getContrainteParent().getIndiceParam()[i]]+"<");
		}
		return msg.toString();
	}

	/**{@inheritDoc}*/
	@Override
	public ContrainteMultiCol getContrainteParent() {
		return (ContrainteMultiCol)errOrigine;
	}

	/**{@inheritDoc} 	 */
	@Override
	public List<Colonne> getColonnes() {
		List<Colonne> listeCol=new ArrayList<Colonne>();
		for(int i=0;i<getContrainteParent().getIndiceParam().length;i++ ){
			listeCol.add(
					getFichier().getColonne(
					getContrainteParent().getIndiceParam()[i]));
		}
		
		return listeCol;
	}
	

}
