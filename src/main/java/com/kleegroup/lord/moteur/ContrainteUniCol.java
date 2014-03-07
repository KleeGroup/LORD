package com.kleegroup.lord.moteur;

import java.util.List;
import java.util.ResourceBundle;

import com.kleegroup.lord.moteur.exceptions.ExceptionMoteur;
import com.kleegroup.lord.moteur.util.IHierarchieSchema;



/**
 * La classe ContrainteUniCol est la classe m�re de toutes les contraintes 
 * appliqu�es � une colonne. Elle se charge d'effectuer les v�rifications sur 
 * les valeurs de la colonne, � l'aide de ses classe 
 * d�riv�es. La v�rification se fait, ligne par ligne, une valeur � la fois.
 * <br><br>
 * Cette classe fournit les fonctionalit�s communes � toutes les classes Contraintes.
 * En particulier, elle se charger d'appeler la fonction de v�rification 
 * <code>({@link #estConforme})</code>, de cr�er une instance d'<code>Erreur</code>
 *  et de renseigner les informations d'erreur dans l'objet <code>Erreur</code> 
 *  cr�e si n�cessaire.
 * <br><br>
 * Les classes d�riv�es doivent impl�menter les deux m�thodes abstraites 
 * {@link #estConforme} et {@link #getMessageErreur()}.La v�rification se 
 * fait dans la m�thode estConforme. Si la valeur est correcte, la valeur de retour
 * doit �tre <code>true</code>. Dans le cas contraire, un objet Erreur est cr�e, et 
 * l'erreur est remont�e vers l'objet appelant (normalement un instance de 
 * {@link com.kleegroup.lord.moteur.Colonne}).   
 *   
 * @author maazreibi
 *
 */
public abstract class ContrainteUniCol  implements IContrainte,IHierarchieSchema {

    	protected final ResourceBundle resourceMap = ResourceBundle
    		.getBundle("resources.ContraintesMessagesErreur");
    	
    	private Colonne colonneParent;

	private Interprete interpreteMsg=null;
	
	private String msgErr=resourceMap.getString(getClass().getSimpleName());

	private String id=this.getClass().getSimpleName();
	/**
	 * Cette fonction effectue les v�rifications n�cessaires sur la valeur d'entr�e.
	 * Si une erreur est detect�e, l'appel suivant � {@link #getMessageErreur()}
	 * doit renvoyer le message d'erreur correspondant.
	 * @param valeur la valeur � v�rifier
	 * @return <code>true</code> si aucune erreur n'est detect�e.
	 * 			 <false> si une erreur est detect�e.
	 * @throws ExceptionMoteur si la contrainte ne peut etre verifiee
	 *  
	 */
	public abstract boolean estConforme(String valeur) throws ExceptionMoteur;
	/**
	 * Le message d'erreur renvoy� en cas d'erreur. Le m�canisme de v�rifaction 
	 * fait en sorte que, si une erreur est detect�e, cette fonction est appel�e 
	 * pour r�cup�rer le message d'erreur correspondant, avant de v�rifier
	 * les valeurs suivante.  
	 * @return le message d'erreur correspondant � la situation.
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
	 * indique si une erreur detect�e nous force � abandonner le reste des 
	 * v�rifications de la colonne.Par exemple, un champ obligatoire 
	 * ne contient pas de valeur.
	 * <br><br>
	 * Cette m�thode est principalement utilis�e pour abandonner les v�rifications 
	 * quand un champ est vide.  
	 * @return <code>true</code> s'il faut abandonner la v�rification de la colonne,
	 * <code>false</code> sinon
	 */
	public boolean abandonneVerifColonne(){
		return false;
	}
	/**
	 * indique s'il faut cr�er un objet erreur.
	 * <br><br>
	 * Actuellement utilis�e uniquement dans le cas de <code>
	 * ContrainteFacultatif</code>
	 * @return <code>true</code> s'il faut cr�er un objet erreur,
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
	 * Remet � zero les donn�es sp�cifique utilis�e lors de la derni�re v�rification
	 * pour pouvoir  r�utiliser cet objet pour une nouvelle v�rification 
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

