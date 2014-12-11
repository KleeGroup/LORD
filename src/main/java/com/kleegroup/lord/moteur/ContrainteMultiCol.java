package com.kleegroup.lord.moteur;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.kleegroup.lord.moteur.contraintes.ContrainteMultiColFonctionsSpecifiques;

/**
 * Défini une contrainte sur plusieurs colonnes.
 * 
 */
public abstract class ContrainteMultiCol implements IContrainte {

	protected static final org.apache.log4j.Logger LOGAPPLI = Logger
			.getLogger(ContrainteMultiColFonctionsSpecifiques.class);

	protected Fichier fichierParent;
	protected Interprete interpreteMsg = null;

	protected String id = "MultiCol";
	protected String cols[] = null;
	protected String errTemplate = "";
	protected String errMessage = "";

	protected int[] indiceParam = null;

	protected ContrainteMultiCol(String id, String errTemplate, String... cols) {
		super();
		this.id = id;
		setErrTemplate(errTemplate);
		this.cols = cols;
	}

	protected abstract boolean estConforme(String[] valeur);

	/**
	 * @return le nom de la fonction de verification
	 */
	public String getNomFonction() {
		return this.getClass().getSimpleName();
	}

	protected void construitIndiceColonnes(String... colonnes) {
		int nbreArgs = colonnes.length;
		indiceParam = new int[nbreArgs];
		for (int i = 0; i < nbreArgs; i++) {
			indiceParam[i] = fichierParent.getColonne(colonnes[i])
					.getPosition();
		}
	}

	protected boolean verifie(String[] valeurs) {
		if (indiceParam == null) {
			construitIndiceColonnes(cols);
		}

		// Contruction du tableau de paramètres
		String[] param = new String[indiceParam.length];
		for (int i = 0; i < indiceParam.length; i++) {
			// FIXED : cas des dernières colonnes facultatives.
			// La valeur "null" est transmise (valeur non définie).
			// Il est de la responsabilité de la fonction de conformité de
			// définir son comportement vis-à-vis des valeurs nulles.
			if (indiceParam[i] >= valeurs.length) {
				param[i] = null;
			} else {
				param[i] = valeurs[indiceParam[i]];
			}
		}
		return estConforme(param);
	}

	/**
	 * 
	 * @param numLigne	le numéro de la ligne à vérifier
	 * @param valeurs	les valeurs à vérifier
	 * @return 			un objet Erreur contenant les informations nécessaires
	 */
	@Override
	public IErreur verifie(long numLigne, String[] valeurs) {
		if (verifie(valeurs)) {
			return Erreur.pasDErreur();
		}

		if (interpreteMsg == null) {
			construitInterprete();
		}
		errMessage = interpreteMsg.bind(numLigne, valeurs);

		ErreurMultiCol e = new ErreurMultiCol(this, numLigne, valeurs);
		return e;
	}

	protected void construitInterprete() {
		interpreteMsg = Interprete.fromTemplate(errTemplate);
		interpreteMsg.setContrainteOrigine(this);
		interpreteMsg.setIdVerif(id);
		interpreteMsg.setNomColonne("");
		interpreteMsg.setNomFichier(fichierParent.getNom());
		String nomsColonnes[] = new String[fichierParent.getNbColonnes()];
		for (int i = 0; i < nomsColonnes.length; i++) {
			nomsColonnes[i] = fichierParent.getColonne(i).getNom();
		}
		interpreteMsg.setNomColonne(nomsColonnes);
	}

	/**
	 * @return renvoie le message d'erreur
	 */
	public String getMessageErreur() {

		return errMessage;
	}

	/**
	 * renvoit l'identifiant de la contrainte. L'identifiant est une chaîne de
	 * caractère determinée par l'utilisateur.
	 * 
	 * @return l'identifiant de la contrainte
	 */
	@Override
	public String getID() {
		return id;
	}

	/**
	 * @return renvoie le fichier auquel est attaché la contrainte
	 */
	public Fichier getFichierParent() {
		return fichierParent;
	}

	/**
	 * @param fichierParent
	 *            désigne le fichier parent de la contrainte
	 */
	public void setFichierParent(Fichier fichierParent) {
		this.fichierParent = fichierParent;
	}

	/** {@inheritDoc} */
	@Override
	public String getNomFichier() {
		return fichierParent.getNom();
	}

	/**
	 * renvoie la liste des nom des colonnes de la contrainte.
	 * 
	 * @return la liste des nom des colonnes de la contrainte
	 */
	public List<String> getNomColonnes() {
		List<String> nom = new ArrayList<>();
		if (indiceParam != null) {
			for (int i = 0; i < indiceParam.length; i++) {
				nom.add(fichierParent.getColonne(indiceParam[i]).getNom());
			}
			return nom;
		}
		return Arrays.asList(cols);
	}

	/**
	 * renvoie le template d'erreur utilisé pour générer le message d'erreur.
	 * 
	 * @return le template d'erreur utilisé pour générer le message d'erreur
	 */
	public String getErrTemplate() {
		return errTemplate;
	}

	/**
	 * @return un tableau des indices des parametres de la contrainte
	 */
	public int[] getIndiceParam() {
		return indiceParam.clone();
	}

	/**
	 * @param errTemplate
	 *            le template du message d'erreur
	 */
	public void setErrTemplate(String errTemplate) {
		this.errTemplate = errTemplate;
	}

	/** {@inheritDoc} */
	@Override
	public Fichier getFichier() {
		return fichierParent;
	}

	/** {@inheritDoc} */
	@Override
	public String interprete(String balise, int indice) {
		return balise;
	}

	/**
	 * nettoie l'objet. Remet à zero les données spécifiques utilisées
	 * lors de la dernière vérification pour pouvoir réutiliser cet objet
	 * pour une nouvelle vérification
	 * */
	@Override
	public void clean() {
		/**/
	}

}