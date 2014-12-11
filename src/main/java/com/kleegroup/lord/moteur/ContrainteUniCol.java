﻿package com.kleegroup.lord.moteur;

import java.util.List;
import java.util.ResourceBundle;

import com.kleegroup.lord.moteur.exceptions.ExceptionMoteur;
import com.kleegroup.lord.moteur.util.IHierarchieSchema;



/**
 * La classe ContrainteUniCol est la classe mère de toutes les contraintes 
 * appliquées à une colonne. Elle se charge d'effectuer les vérifications sur 
 * les valeurs de la colonne, à l'aide de ses classe 
 * dérivées. La vérification se fait, ligne par ligne, une valeur à la fois.
 * <br><br>
 * Cette classe fournit les fonctionalités communes à toutes les classes Contraintes.
 * En particulier, elle se charger d'appeler la fonction de vérification 
 * <code>({@link #estConforme})</code>, de créer une instance d'<code>Erreur</code>
 *  et de renseigner les informations d'erreur dans l'objet <code>Erreur</code> 
 *  crée si nécessaire.
 * <br><br>
 * Les classes dérivées doivent implémenter les deux méthodes abstraites 
 * {@link #estConforme} et {@link #getMessageErreur()}.La vérification se 
 * fait dans la méthode estConforme. Si la valeur est correcte, la valeur de retour
 * doit être <code>true</code>. Dans le cas contraire, un objet Erreur est crée, et 
 * l'erreur est remontée vers l'objet appelant (normalement un instance de 
 * {@link com.kleegroup.lord.moteur.Colonne}).   
 *   
 * @author maazreibi
 *
 */
public abstract class ContrainteUniCol  implements IContrainte,IHierarchieSchema {

    protected final ResourceBundle resourceMap = ResourceBundle.getBundle("resources.ContraintesMessagesErreur");
    	
    private Colonne colonneParent;

	private Interprete interpreteMsg=null;
	
	private String msgErr=resourceMap.getString(getClass().getSimpleName());

	private String id=this.getClass().getSimpleName();
	/**
	 * Cette fonction effectue les vérifications nï¿½cessaires sur la valeur d'entrée.
	 * Si une erreur est detectée, l'appel suivant à {@link #getMessageErreur()}
	 * doit renvoyer le message d'erreur correspondant.
	 * @param valeur la valeur à vérifier
	 * @return <code>true</code> si aucune erreur n'est detectée.
	 * 			 <false> si une erreur est detectée.
	 * @throws ExceptionMoteur si la contrainte ne peut etre verifiee
	 *  
	 */
	public abstract boolean estConforme(String valeur) throws ExceptionMoteur;
	/**
	 * Le message d'erreur renvoyï¿½ en cas d'erreur. Le mï¿½canisme de vï¿½rifaction 
	 * fait en sorte que, si une erreur est detectï¿½e, cette fonction est appelï¿½e 
	 * pour rï¿½cupï¿½rer le message d'erreur correspondant, avant de vï¿½rifier
	 * les valeurs suivante.  
	 * @return le message d'erreur correspondant ï¿½ la situation.
	 */
	public String getMessageErreur(){
		return msgErr;		
	}



	/**
	 * {@inheritDoc}
	 * @throws ExceptionMoteur En cas d'echec de verification
	 * 	 * 
	 */
	@Override
	public ErreurUniCol verifie(long numLigne,String[]valeurs) throws ExceptionMoteur{
		
		String valeur="";
		if (colonneParent.getPosition()<valeurs.length){
			valeur=valeurs[colonneParent.getPosition()];
		}

		if (estConforme(valeur) ){
			if (abandonneVerifColonne()){
				return ErreurUniCol.ERR_VIDE;
			}

			return Erreur.pasDErreur();

		}
		final ErreurUniCol err=new ErreurUniCol(this,numLigne,valeurs);


		if (interpreteMsg==null){
			interpreteMsg=Interprete.fromTemplate(getMessageErreur());
			interpreteMsg.setIdVerif(id);
			interpreteMsg.setNomColonne(colonneParent.getNom());
			interpreteMsg.setNomFichier(colonneParent.getFichierParent().getNom());
			interpreteMsg.setContrainteOrigine(this);

			String noms_colonnes[]=new String [colonneParent.getFichierParent().getNbColonnes()];
			for(int i=0;i<noms_colonnes.length;i++){
				noms_colonnes[i]=colonneParent.getFichierParent().getColonne(i).getNom();
			}
			interpreteMsg.setNomColonne(noms_colonnes);			
		}


		return err;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getID(){
		return id;
	}

	/**
	 * indique si une erreur detectï¿½e nous force ï¿½ abandonner le reste des 
	 * vï¿½rifications de la colonne.Par exemple, un champ obligatoire 
	 * ne contient pas de valeur.
	 * <br><br>
	 * Cette mï¿½thode est principalement utilisï¿½e pour abandonner les vï¿½rifications 
	 * quand un champ est vide.  
	 * @return <code>true</code> s'il faut abandonner la vï¿½rification de la colonne,
	 * <code>false</code> sinon
	 */
	public boolean abandonneVerifColonne(){
		return false;
	}
	/**
	 * indique s'il faut crï¿½er un objet erreur.
	 * <br><br>
	 * Actuellement utilisï¿½e uniquement dans le cas de <code>
	 * ContrainteFacultatif</code>
	 * @return <code>true</code> s'il faut crï¿½er un objet erreur,
	 * <code>false</code> sinon
	 */
	public boolean isErreurLoggable(){
		return true;
	}
	/**
	 * Renvoie la colonne parent.
	 * @return la colonne parent
	 * 
	 */
	public Colonne getColonneParent() {
		return colonneParent;
	}
	/**
	 * Definit la colonne parent.
	 * @param colonneParent la colonne parent
	 * 
	 */
	public void setColonneParent(Colonne colonneParent) {
		this.colonneParent = colonneParent;
	}

	/**
	 * @return l'interprete qui evluera les templates d'erreur pour produire le message d'erreur
	 */
	public Interprete getInterprete(){
		return interpreteMsg;
	}
	/**{@inheritDoc} */
	@Override
	public String getNomFichier(){
		return colonneParent.getFichierParent().getNom();
	}
	
	/**
	 * @return la liste des parametres de la contrainte
	 */
	public abstract List<String> getListeParam();
	
	/**{@inheritDoc}
	 */
	@Override
	public Fichier getFichier(){
		return colonneParent.getFichierParent();
	}
	
	/**{@inheritDoc}*/
	@Override
	public String interprete(String balise, int indice){
	    return balise;
	}
	
	/**nettoie l'objet.
	 * Remet ï¿½ zero les donnï¿½es spï¿½cifique utilisï¿½e lors de la derniï¿½re vï¿½rification
	 * pour pouvoir  rï¿½utiliser cet objet pour une nouvelle vï¿½rification 
	 * */
	@Override
	public void clean(){
	    /**/
	}
	
	/**
	 * Les contrainte type indique le type de la colonne. Il en existe 4:
	 * ContrainteTypeEntier,ContrainteTypeDecimal, ContrainteTypeDate et
	 * ContrainteTypeChaineDeCaractere. 
	 * 
	 * 
	 * @return true si la contrainte est une contrainte de type, false sinon.
	 */
	public boolean isContrainteType() {
	    return false;
	}
	
	/**
	 * @return une copie de la contrainte
	 */
	public abstract ContrainteUniCol copy();
/**{@inheritDoc}*/	
	@Override
	public IHierarchieSchema getChild(int index) {
	    return null;
	}
/**{@inheritDoc}*/
	@Override
	public int getChildCount() {
	    return 0;
	}
/**{@inheritDoc}*/
	@Override
	public int getNiveau() {
	    return 3;
	}
/**{@inheritDoc}*/
	@Override
	public String getNomHierarchie() {
	    return getID();
	}
/**{@inheritDoc}*/
	@Override
	public IHierarchieSchema getParent() {
	    return getColonneParent();
	}
/**{@inheritDoc}*/
	@Override
	public boolean isCategorie() {
	    return false;
	}
/**{@inheritDoc}*/
	@Override
	public boolean isColonne() {
	    return true;
	}
/**{@inheritDoc}*/
	@Override
	public boolean isContrainte() {
	    return true;
	}
/**{@inheritDoc}*/
	@Override
	public boolean isFichier() {
	    return false;
	}/**{@inheritDoc}*/
	 @Override
	public int getPosition() {
		return getColonneParent().getContraintes().indexOf(this);
	    }

}

