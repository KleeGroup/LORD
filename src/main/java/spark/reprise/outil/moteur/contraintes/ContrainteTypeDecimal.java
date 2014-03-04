package spark.reprise.outil.moteur.contraintes;

import java.util.ArrayList;
import java.util.List;

import spark.reprise.outil.moteur.ContrainteUniCol;
import spark.reprise.outil.moteur.util.SeparateurDecimales;



/**
 * Vérifie que la valeur représente un nombre décimal.
 * @author maazreibi
 *
 */
public class ContrainteTypeDecimal extends ContrainteUniCol {

	
	protected String regex;//expression reguliere utilisee pour tester le decimal
	protected String separateurFraction=",";
	protected int avantVirgule,apresVirgule;
	private int etat=0;
	
	private String 
	PAS_D_ERREUR="PAS_D_ERREUR",
	TROP_DE_CHIFFRES_AVANT_VIRG="TROP_DE_CHIFFRES_AVANT_VIRG",
	TROP_DE_CHIFFRES_APRES_VIRG="TROP_DE_CHIFFRES_APRES_VIRG",
	PAS_UN_CHIFFRE="PAS_UN_CHIFFRE";
	
	
	
	/**
	 * Construit la contrainte avec les paramètres fournies.
	 * <br><br>
	 * Le paramèter séparateur de décimal peut prendre 2 valeurs. Ces valeurs sont 
	 * des constantes définie dans la classe:
	 * <code>SEPARATEUR_VIRGULE </code> et <code>SEPARATEUR_POINT</code>
	 * @param avantVirgule nombre de chiffres avant la virgule
	 * @param separateur constante indiquent le séprateur de décimale désiré
	 * @param apresVirgule nombre de chiffres après la virgule
	 */
	public ContrainteTypeDecimal(int avantVirgule,SeparateurDecimales separateur,int apresVirgule){
		super();
		this.apresVirgule=apresVirgule;
		this.avantVirgule=avantVirgule;
		initMsgErreurs();

		if (separateur==SeparateurDecimales.SEPARATEUR_POINT){
			separateurFraction=".";			
		}
		regex="([\\+|-])?"+ //plus ou moins au debut
		"(\\d){1,"+avantVirgule+"}" //pattern avant la virgule %d=nbre de chiffre max
		 +
		"(" +separateurFraction+ // separateur de virgule (, ou .) 
		"(\\d){1," +apresVirgule+ // pattern apres la virgule %d=nbre de chiffre max
		"})?";
	}

	private void initMsgErreurs() {
	    PAS_D_ERREUR=resourceMap.getString("ContrainteTypeDecimal.PAS_D_ERREUR");
	    TROP_DE_CHIFFRES_AVANT_VIRG=resourceMap.getString("ContrainteTypeDecimal.TROP_DE_CHIFFRES_AVANT_VIRG");
	    TROP_DE_CHIFFRES_APRES_VIRG=resourceMap.getString("ContrainteTypeDecimal.TROP_DE_CHIFFRES_APRES_VIRG");
	    PAS_UN_CHIFFRE=resourceMap.getString("ContrainteTypeDecimal.PAS_UN_CHIFFRE");
	}

	/** {@inheritDoc} */
	@Override
	public  boolean estConforme(final String valeur) {
		etat=0;
	    String nVal=valeur;
	    if(valeur.startsWith("+")||valeur.startsWith("-")){
		nVal=valeur.substring(1);
	    }
	    String vals[]=nVal.split(separateurFraction,2);
	    if(vals.length==0||vals.length>2){
		return false;
	    }
	    etat=isInLimit(vals[0], avantVirgule);
	    if(etat==0){//partie avant virgule est Ok , test de celle d'apres
		if(vals.length==2){
		    etat=isInLimit(vals[1], apresVirgule);
		    if(etat==2){
			etat=3;//trop de chiffres apres la virgule 
		    }
		    if(etat!=0){//erreur dans la partie apres virgule
			return false;
		    }
		}
		
		return true;//pas de chiffre avant la virgule, ou chiffres corrects
	    }
	    
	    return false;//erreur dans le nombre

	}
	private int isInLimit(String val,int limit){
	    /* 0 = OK
	     * 1 = pas un nombre
	     * 2 = trop de chiffres
	     * */
	    if (isAllDigits(val)){
		if (val.length()>limit){
		    return 2;
		}
		return 0;
	    }
	    return 1;
	}
	private boolean isAllDigits(String string) {
	    return string.matches("(\\d)+");
	}

	/**{@inheritDoc}*/
	@Override
	public List<String> getListeParam() {
		List<String> params= new ArrayList<String>();
		params.add(Integer.toString(avantVirgule));
		params.add(Integer.toString(apresVirgule));
		return params;
	}

	/**
	 * Le format d'un nombre décimal est le nombre qui représente les valeurs acceptées par cette 
	 * contrainte. Le format set lors de l'affichage à l'écran d'une réprésentation de cette contrainte.
	 * Le format est le suivant avantVirgule,apresVirgule  où avantVirgule est
	 * un nombre représentant le nombre de chiffre acceptés avant la virgule, et
	 * apresVirgule le nombre de chiffres après.
	 * 
	 * @return le format de vérification
	 * */
	public String getFormat() {
	    return avantVirgule+","+apresVirgule;
	}
	/**
	 * Construit une ContrainteTypeDecimal à partir d'une chaine de caractères représentant 
	 * le format. voir aussi {@link #getFormat()}.
	 * @param format le format de la contrainte
	 * @return une ContrainteTypeDecimal qui suit le format défini.
	 */
	public static ContrainteTypeDecimal fromString(String format){
	    String [] arr=format.split(",");
	    int avVirg=0,apVirg=0;
	    if (arr.length>0){
		avVirg=Integer.valueOf(arr[0]);
		if (arr.length>1){
			apVirg=Integer.valueOf(arr[1]);
		}
	    }
	    return new ContrainteTypeDecimal(avVirg,SeparateurDecimales.SEPARATEUR_VIRGULE,apVirg);
	}
	/**{@inheritDoc}*/
	@Override
	public boolean isContrainteType(){
	    return true;
	}
	
	/**{@inheritDoc}*/
	@Override
	public ContrainteTypeDecimal copy() {
	    ContrainteTypeDecimal nc= new ContrainteTypeDecimal(avantVirgule,
		    SeparateurDecimales.SEPARATEUR_VIRGULE,
		    apresVirgule);
	    nc.separateurFraction=separateurFraction;
	    return nc;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String getMessageErreur(){
	    switch(etat){
	    case 0:
		return PAS_D_ERREUR;
	    case 2:
		return TROP_DE_CHIFFRES_AVANT_VIRG;
	    case 3:
		return TROP_DE_CHIFFRES_APRES_VIRG;
	    default:
		return PAS_UN_CHIFFRE;
	    }
	}



}

