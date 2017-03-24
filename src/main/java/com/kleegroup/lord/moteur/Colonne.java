package com.kleegroup.lord.moteur;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.kleegroup.lord.moteur.contraintes.ContrainteListeValeursPermises;
import com.kleegroup.lord.moteur.contraintes.ContrainteReference;
import com.kleegroup.lord.moteur.contraintes.ContrainteRegex;
import com.kleegroup.lord.moteur.contraintes.ContrainteTRUE;
import com.kleegroup.lord.moteur.contraintes.ContrainteTaille;
import com.kleegroup.lord.moteur.contraintes.ContrainteTypeChaineDeCaractere;
import com.kleegroup.lord.moteur.contraintes.ContrainteTypeDate;
import com.kleegroup.lord.moteur.contraintes.ContrainteTypeDecimal;
import com.kleegroup.lord.moteur.contraintes.ContrainteTypeEntier;
import com.kleegroup.lord.moteur.contraintes.ContrainteUnique;
import com.kleegroup.lord.moteur.exceptions.ExceptionMoteur;
import com.kleegroup.lord.moteur.util.IHierarchieSchema;
import com.kleegroup.lord.moteur.util.SeparateurDecimales;

/**
 * Represente une colonne du fichier.<br>
 * Une colonne possède une liste de contraintes. Ces contraintes seront
 * vérifiées en série sur chaque valeur de la colonne. <br>
 * <br>
 * Certaines erreurs peuvent déclencher l'abandon de la vérification d'une
 * valeur. Ces contraintes "existentielle" déclenchent l'abandon si la valeur
 * est vide. Les contraintes existentielles sont:<br>
 * ContrainteVide, ContrainteObligatoire et ContrainteFacultatif.<br>
 * La contrainte ContrainteTypeChaineDeCaractere peut aussi déclencher l'abondon
 * de la vérification du fichier si une chaîne contient une valeur interdite
 * <code>'"'</code> et <code> '\n'</code>.
 * 
 */
public class Colonne implements IHierarchieSchema {
	protected final ResourceBundle resourceMap = ResourceBundle.getBundle("resources.ContraintesMessagesErreur");

	private String nom = "";
	private String description = "";

	private Fichier fichierParent;

	private int position;

	private final List<ContrainteUniCol> contraintes = new LinkedList<>();

	private PRESENCE presenceValeur = PRESENCE.FACULTATIVE;

	private final Map<String, IContrainte> refContraintes = new HashMap<>();

	private final List<Erreur> erreurs = new ArrayList<>();
	private final List<Erreur> listeUneSeuleErreur = new ArrayList<>();
	private final Erreur errVide = ErreurUniCol.ERR_VIDE, noErr = Erreur.pasDErreur();
	private boolean colonneReference = false;

	/**
	 * Un type qui sert � indiquer si la valeur d'un champ est obligatoire. *
	 */
	public enum PRESENCE {
		/**
		 * Indique que la colonne doit contenir une valeur.
		 */
		OBLIGATOIRE,
		/**
		 * Indique que la valeur de la colonne peut etre renseign�e ou pas.
		 */
		FACULTATIVE,
		/**
		 * Indique que la colonne doit etre vide.
		 */
		INTERDITE,
	}

	/**
	 * COnstructeur qui permet de pr�ciser le nom de la colonne.
	 * 
	 * @param nom :
	 *            nom de la colonne
	 */

	public Colonne(String nom) {
		this.nom = nom;
		listeUneSeuleErreur.add(noErr);
	}

	/**
	 * V�rifie la valeur selon les contraintes d�finies sur la colonne, et
	 * renvoie une liste d'erreur.
	 * 
	 * @param numLigne
	 *            le numero de ligne a verifier
	 * @param valeur
	 *            la ligne � v�rifier
	 * @return renvoie une liste des erreurs retrouv�es
	 * @throws ExceptionMoteur
	 *             En cas d'echec de verification
	 */
	public List<Erreur> verifie(long numLigne, String valeur[]) throws ExceptionMoteur {
		clearErreurs();

		final Erreur err = detectePresenceValeur(numLigne, valeur);
		if (err == errVide) {
			return erreurs;
		}
		if (err == noErr) {
			return verifierContraintes(numLigne, valeur);
		}

		return listWrapError(err);

	}

	private List<Erreur> listWrapError(Erreur err) {
		listeUneSeuleErreur.set(0, err);
		return listeUneSeuleErreur;
	}

	private void clearErreurs() {
		erreurs.clear();
	}

	private List<Erreur> verifierContraintes(long numLigne, String[] valeur) throws ExceptionMoteur {
		for (final ContrainteUniCol c : contraintes) {

			final ErreurUniCol e = c.verifie(numLigne, valeur);
			if (e == Erreur.pasDErreur()) {
				continue;
			}
			erreurs.add(e);
		}
		return erreurs;
	}

	private Erreur detectePresenceValeur(long numLigne, String valeur[]) {
		final Boolean champEstVide = position >= valeur.length || "".equals(valeur[position]);
		ErreurConstante err = null;

		if (presenceValeur == PRESENCE.OBLIGATOIRE && champEstVide) {

			err = new ErreurConstante(numLigne, resourceMap.getString("colonne.ErreurVide"), getNom(), "", fichierParent.getNom(), fichierParent.getValeursColRef(valeur));

			return err;
		}

		if (presenceValeur == PRESENCE.INTERDITE && !champEstVide) {
			err = new ErreurConstante(numLigne, resourceMap.getString("colonne.ErreurNonVide"), getNom(), valeur[position], fichierParent.getNom(), fichierParent.getValeursColRef(valeur));

			return err;

		}
		if (champEstVide) {
			return ErreurUniCol.ERR_VIDE;
		}
		return Erreur.pasDErreur();
	}

	/**
	 * Ajoute une contrainte � la colonne.<br>
	 * Toutes les contraintes doivent poss�der des noms diff�rents.Les noms des
	 * contraintes d�riv�es de ContrainteUniCol est le nom de la classe. Ce qui
	 * implique qu'une colonne doit avoir au plus une seul contrainte par classe
	 * d�riv�es.<br>
	 * Pour les contraintes de type ContrainteMultiCol, le nom est determin� par
	 * l'utilisateur.
	 * 
	 * @param c
	 *            la contrainte � rajouter
	 */
	public void addContrainte(ContrainteUniCol c) {
		String id = c.getClass().getSimpleName();
		id = id.substring("Contrainte".length());
		refContraintes.put(id, c);
		c.setColonneParent(this);
		contraintes.add(c);
	}

	/**
	 * Renvoie le nom de la colonne.
	 * 
	 * @return le nom de la colonne
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @return la description de la colonne si est elle pas nulle, sinon le nom.
	 */
	public String getDescOuNom() {

		return "".equals(description) ? nom : description;
	}

	/**
	 * renvoie la liste des contraintes de la colonne.
	 * 
	 * @return la listes des contraintes de la colonne
	 */
	public List<ContrainteUniCol> getContraintes() {
		return contraintes;
	}

	/**
	 * Renvoie la ieme contrainte de la colonne.
	 * 
	 * @param i
	 *            ordre de v�rification de la contrainte (d�but � 0)
	 * @return la contrainte � la position i
	 */
	public IContrainte getContrainte(int i) {
		return contraintes.get(i);
	}

	/**
	 * Renvoie la contrainte qui poss�de le nom nom_contrainte.<br>
	 * Les noms des contraintes doivent �tre uniques pour chaque colonne.
	 * 
	 * @param nomContrainte
	 *            le nom de la contrainte recherch�e
	 * @return la contrainte qui poss�de le nom nom_contrainte
	 */
	public IContrainte getContrainte(String nomContrainte) {
		return refContraintes.get(nomContrainte);
	}

	/**
	 * Rajoute la contrainte c � la position i. Si une contrainte existe d�j� �
	 * cette position , elle est remplac�e.
	 * 
	 * @param i
	 *            la position de la contrainte
	 * @param c
	 *            la contrainte � rajouter
	 */
	public void setContrainte(int i, ContrainteUniCol c) {
		contraintes.set(i, c);
	}

	/**
	 * Renvoie le fichier auquel est rattach�e la contrainte.
	 * 
	 * @return le fichier auquel est rattach�e la contrainte
	 */
	public Fichier getFichierParent() {
		return fichierParent;
	}

	/**
	 * D�finit le fichier auquel est rattach�e la contrainte.
	 * 
	 * @param fichierParent
	 *            fichier auquel est rattach�e la contrainte
	 */
	public void setFichierParent(Fichier fichierParent) {
		this.fichierParent = fichierParent;
	}

	/**
	 * @return la position de la colonne dans le fichier parent
	 */
	@Override
	public int getPosition() {
		return position;
	}

	/**
	 * d�finit la position de la colonne.
	 * 
	 * @param postion
	 *            la position du fichier parmi les colonnes du fichier parent
	 */
	public void setPosition(int postion) {
		this.position = postion;
	}

	/**
	 * @return l'etat de la colonne
	 */
	public PRESENCE getPresenceValeur() {
		return presenceValeur;
	}

	/**
	 * d�finit l'�tat de la colonne.
	 * 
	 * @param etat
	 *            l'etat de la colonne
	 */
	public void setPresenceValeur(PRESENCE etat) {
		this.presenceValeur = etat;
	}

	/**nettoie l'objet.
	 * Remet � zero les donn�es sp�cifique utilis�e lors de la derni�re v�rification
	 * pour pouvoir  r�utiliser cet objet pour une nouvelle v�rification
	 * */
	public void clean() {
		for (final IContrainte c : contraintes) {
			c.clean();
		}

	}

	/**
	 * @return la description de la colonne.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * d�finit la description de la colonne.
	 * @param description description de la colonne
	 */
	public void setDescription(String description) {
		if (description != null) {
			this.description = description;
		}
	}

	/**
	 * d�finit le nom de colonne.
	 * @param nom le nom de colonne.
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**{@inheritDoc}*/
	@Override
	public String toString() {
		String desc = description;
		desc = "".equals(desc) ? "" : " : " + desc;
		return nom + desc;
	}

	/**
	 * Renvoie une contrainte type qui repr�sente le type de la colonne, null si auccune
	 * n'a �t� trouv�e.
	 * @return une contrainte type qui repr�sente le type de la colonne, null si auccune
	 * n'a �t� trouv�
	 */
	public ContrainteUniCol getContrainteType() {
		for (final ContrainteUniCol cuc : contraintes) {
			if (cuc.isContrainteType()) {
				return cuc;
			}
		}
		return null;

	}

	/**
	 * 
	 * @return si une contrainte de taille a �t� d�fini,
	 * renvoie la taille max, Integer.MAX_VALUE sinon
	 */
	public int getTaille() {
		for (final ContrainteUniCol cuc : contraintes) {
			if (cuc instanceof ContrainteTaille) {
				return ((ContrainteTaille) cuc).getTailleMax();
			}
		}
		return Integer.MAX_VALUE;
	}

	/**
	 * @return une copie de la colonne.
	 */
	public Colonne copy() {
		final Colonne colonneEquiv = new Colonne(nom);
		colonneEquiv.description = description;
		colonneEquiv.presenceValeur = presenceValeur;
		for (final ContrainteUniCol contrainteOriginale : contraintes) {
			colonneEquiv.addContrainte(contrainteOriginale.copy());
		}
		return colonneEquiv;
	}

	/**
	 * @return le format de la colonne.
	 */
	public String getFormat() {
		for (final ContrainteUniCol cuc : contraintes) {
			if (cuc instanceof ContrainteRegex) {
				return ((ContrainteRegex) cuc).getRegex();
			}
			if (cuc instanceof ContrainteTypeDate) {
				return ((ContrainteTypeDate) cuc).getFormat();
			}
			if (cuc instanceof ContrainteTypeDecimal) {
				return ((ContrainteTypeDecimal) cuc).getFormat();
			}
		}
		return "";
	}

	/**
	 * Renvoie la contrainte de r�f�rence d�finie sur cette colonne. null si auccune n'est d�finie.
	 * @return la contrainte de r�frence.
	 */
	public ContrainteReference getContrainteReference() {
		for (final ContrainteUniCol cont : contraintes) {
			if (cont instanceof ContrainteReference) {
				return (ContrainteReference) cont;
			}
		}
		return null;
	}

	/**
	 * @return la liste des valeurs permise pour cette colonne.
	 */
	public String getListeValeursPermise() {
		for (final ContrainteUniCol cont : contraintes) {
			if (cont instanceof ContrainteListeValeursPermises) {
				final ContrainteListeValeursPermises ncont = (ContrainteListeValeursPermises) cont;
				return ncont.getSimpleListeValeurs();
			}
		}
		return "";
	}

	/**
	 * @return true si les valeurs de cette colonne doivent �tre uniques.
	 */
	public boolean isValeursUniques() {
		for (final ContrainteUniCol cont : contraintes) {
			if (cont instanceof ContrainteUnique) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return le type de la colonne.
	 */
	public String getType() {
		for (final ContrainteUniCol cont : contraintes) {
			if (isContrainteType(cont)) {
				return cont.getClass().getSimpleName().substring("ContrainteType".length());
			}
		}
		return "";
	}

	private boolean isContrainteType(IContrainte cont) {
		return cont instanceof ContrainteTypeDate || cont instanceof ContrainteTypeEntier || cont instanceof ContrainteTypeDecimal || cont instanceof ContrainteTypeChaineDeCaractere;
	}

	/**
	 * Cr�e une contrainte de format sur la colonne.<br>
	 * Le format doit �tre compatible avec le type de la colonne.
	 * 
	 * @param format le format de la colonne.
	 */
	public void createContrainteFormat(String format) {
		final String type = getType();
		if ("ChaineDeCaractere".equals(type)) {
			replaceContrainteRegex(format);
		}
		if ("Date".equals(type) && checkFormatDateValide(format)) {
			replaceContrainteDate(format);
		}
		if ("Decimal".equals(type) && checkFormatDecimalValide(format)) {
			replaceContrainteDecimal(format);
		}
	}

	private boolean checkFormatDecimalValide(String string) {
		return string.matches("\\d+(,\\d+)?");
	}

	private boolean checkFormatDateValide(String string) {
		try {
			//On v�rifie la validit� du pattern de date
			new SimpleDateFormat().applyPattern(string);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * Supprimer les contraintes de type typeContrainte du fichier.
	 * @param typeContrainte le type des contrainte � supprimer.
	 */
	public void removeContraintes(Class<?> typeContrainte) {
		for (int i = 0; i < getContraintes().size(); i++) {
			if (getContrainte(i).getClass().equals(typeContrainte)) {
				getContraintes().remove(i);
				i--;
			}
		}
	}

	private void replaceContrainteDecimal(String string) {
		removeContraintes(ContrainteTypeDecimal.class);
		addContrainte(ContrainteTypeDecimal.fromString(string));

	}

	private void replaceContrainteDate(String format) {
		removeContraintes(ContrainteTypeDate.class);
		addContrainte(new ContrainteTypeDate(format));
	}

	private void replaceContrainteRegex(String regex) {
		removeContraintes(ContrainteRegex.class);
		addContrainte(new ContrainteRegex(regex));
	}

	/**
	 * 
	 * @param value la liste des valeurs permise, s�par�es par une virgule.
	 */
	public void setListeValeursPermises(String value) {
		removeContraintes(ContrainteListeValeursPermises.class);
		if (value != null && !"".equals(value.trim())) {
			final String vals[] = value.split(",");
			for (int i = 0; i < vals.length; i++) {
				vals[i] = vals[i].trim();
			}

			addContrainte(new ContrainteListeValeursPermises(vals));
		}
	}

	/**
	 * Si la taille est n�gative,la contrainte de taille sera supprim�e.
	 * @param val la taille maximale de la colonne.
	 */
	public void setTaille(String val) {
		if ("".equals(val)) {
			removeContraintes(ContrainteTaille.class);
			return;
		}
		try {
			final Integer taille = Integer.valueOf(val);
			if (taille >= 0) {
				removeContraintes(ContrainteTaille.class);
				addContrainte(new ContrainteTaille(taille));
			}
		} catch (final NumberFormatException ex) {
			//do nothing
		}
	}

	/**
	 * @param value d�finit le type de la colonne.
	 */
	public void setType(String value) {
		removeContraintes(ContrainteTypeChaineDeCaractere.class);
		removeContraintes(ContrainteTypeDate.class);
		removeContraintes(ContrainteTypeEntier.class);
		removeContraintes(ContrainteTypeDecimal.class);
		addContrainte(createContrainteType(value));
	}

	private ContrainteUniCol createContrainteType(String contrainteNom) {
		if ("Date".equals(contrainteNom)) {
			return new ContrainteTypeDate();
		}
		if ("Entier".equals(contrainteNom)) {
			return new ContrainteTypeEntier();
		}
		if ("Decimal".equals(contrainteNom)) {
			return new ContrainteTypeDecimal(10, SeparateurDecimales.SEPARATEUR_VIRGULE, 3);
		}
		if ("ChaineDeCaractere".equals(contrainteNom)) {
			return new ContrainteTypeChaineDeCaractere();
		}

		return new ContrainteTRUE();
	}

	/**
	 * D�termine si les valeurs de cette colonne doivent �tre unqiues.
	 * @param value true s'il doivent l'�tre.
	 */
	public void setUnique(boolean value) {
		removeContraintes(ContrainteUnique.class);
		if (value) {//rendre la colonne unique
			addContrainte(new ContrainteUnique());
		}
	}

	/**
	 * @return les types possibles de la colonne
	 */
	public static String[] getTypesPossibles() {
		return new String[] { ContrainteTypeEntier.class.getSimpleName().substring("ContrainteType".length()), ContrainteTypeDecimal.class.getSimpleName().substring("ContrainteType".length()), ContrainteTypeDate.class.getSimpleName().substring("ContrainteType".length()), ContrainteTypeChaineDeCaractere.class.getSimpleName().substring("ContrainteType".length()), };

	}

	/**
	 * Definit cette colonne comme faisant partie des colonne de reference.
	 * @param b true pour rajouter � l'ensemble, false sinon.
	 */
	public void setColonneReference(boolean b) {
		colonneReference = b;
	}

	/**
	 * @return true si cette colonne comme faisant partie
	 *  des colonne de reference, false sinon.
	 */
	public boolean isColonneReference() {
		return colonneReference;
	}

	/**{@inheritDoc}*/
	@Override
	public IHierarchieSchema getChild(int index) {
		return contraintes.get(index);
	}

	/**{@inheritDoc}*/
	@Override
	public int getChildCount() {
		return contraintes.size();
	}

	/**{@inheritDoc}*/
	@Override
	public int getNiveau() {
		return 2;
	}

	/**{@inheritDoc}*/
	@Override
	public String getNomHierarchie() {
		return getDescOuNom();
	}

	/**{@inheritDoc}*/
	@Override
	public IHierarchieSchema getParent() {
		return getFichierParent();
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
		return false;
	}

	/**{@inheritDoc}*/
	@Override
	public boolean isFichier() {
		return false;
	}

}
