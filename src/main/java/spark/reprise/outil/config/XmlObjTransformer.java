package spark.reprise.outil.config;

import java.util.ArrayList;
import java.util.List;

import spark.reprise.outil.config.xml.TypeColonne;
import spark.reprise.outil.config.xml.TypeContrainte;
import spark.reprise.outil.config.xml.TypeContrainteMultiColonne;
import spark.reprise.outil.config.xml.TypeFichier;
import spark.reprise.outil.config.xml.TypeSchema;
import spark.reprise.outil.moteur.Colonne;
import spark.reprise.outil.moteur.Colonne.PRESENCE;
import spark.reprise.outil.moteur.ContrainteMultiCol;
import spark.reprise.outil.moteur.ContrainteUniCol;
import spark.reprise.outil.moteur.Fichier;
import spark.reprise.outil.moteur.Schema;
import spark.reprise.outil.moteur.contraintes.ContrainteListeValeursPermises;
import spark.reprise.outil.moteur.contraintes.ContrainteMultiColFonctionsSpecifiques;
import spark.reprise.outil.moteur.contraintes.ContrainteMultiColUnique;
import spark.reprise.outil.moteur.contraintes.ContrainteRegex;
import spark.reprise.outil.moteur.contraintes.ContrainteTRUE;
import spark.reprise.outil.moteur.contraintes.ContrainteTaille;
import spark.reprise.outil.moteur.contraintes.ContrainteTypeChaineDeCaractere;
import spark.reprise.outil.moteur.contraintes.ContrainteTypeDate;
import spark.reprise.outil.moteur.contraintes.ContrainteTypeDecimal;
import spark.reprise.outil.moteur.contraintes.ContrainteTypeEntier;
import spark.reprise.outil.moteur.contraintes.ContrainteUnique;
import spark.reprise.outil.moteur.util.SeparateurDecimales;

/**
 * Sert a tranformer un schema XML (JAXB) en schema de moteur.
 */
public class XmlObjTransformer {
	Schema schemaEquiv = new Schema();

	List<Colonne> colonnesPrinc = new ArrayList<Colonne>();

	List<String> fichierRef = new ArrayList<String>();

	List<String> colonneRef = new ArrayList<String>();

	SeparateurDecimales separateur = SeparateurDecimales.SEPARATEUR_VIRGULE;

	/**
	 * @param schemaOriginal
	 *            le schema extrait de l'XML
	 * @return le schema de moteur
	 */
	public Schema transform(TypeSchema schemaOriginal) {
		if (schemaOriginal == null) {
			return new Schema();
		}
		try {
			separateur = SeparateurDecimales.valueOf(schemaOriginal
					.getSeparateurDecimal());
		} catch (Exception e) {
			separateur = SeparateurDecimales.SEPARATEUR_VIRGULE;
		}

		schemaEquiv.setEncoding(schemaOriginal.getEncodage());
		schemaEquiv.setAfficherExportLogs(schemaOriginal.getAfficherExportLogs());
		char sep = schemaOriginal.getSeparateurChamps().charAt(0);
		schemaEquiv.setSeparateurChamp(sep);

		schemaEquiv.setSeparateurDecimales(separateur.toString());

		for (TypeFichier fichierOriginal : schemaOriginal.getFichier()) {
			schemaEquiv.addFichier(transform(fichierOriginal));
		}
		traiterRefrence();
		return schemaEquiv;
	}

	private void traiterRefrence() {
		for (int i = 0; i < colonnesPrinc.size(); i++) {
			Fichier fRef = schemaEquiv.getFichier(fichierRef.get(i));
			if (fRef != null) {
				Colonne cRef = fRef.getColonne(colonneRef.get(i));
				if (cRef != null) {// la colonne a été trouvee
					colonnesPrinc.get(i).getFichierParent()
							.addReference(colonnesPrinc.get(i).getNom(), cRef);
				}
			}
		}
	}

	private Fichier transform(TypeFichier fichierOriginal) {
		Fichier fichierEquiv = new Fichier(fichierOriginal.getNom(),
				fichierOriginal.getPrefixNom());
		fichierEquiv.setExtension(fichierOriginal.getExtension());
		for (TypeColonne colonneOriginale : fichierOriginal.getColonnes()
				.getColonne()) {
			fichierEquiv.addColonne(transform(colonneOriginale));
		}
		for (TypeContrainteMultiColonne contrainteOriginale : fichierOriginal
				.getContrainte()) {
			fichierEquiv.addContrainteMultiCol(transform(contrainteOriginale));
		}
		fichierEquiv.setNbLignesEntete(fichierOriginal.getNbLignesEntete());
		fichierEquiv.setSeuilAbandon(fichierOriginal.getSeuilErreurs());
		fichierEquiv.setGroupe(fichierOriginal.getGroupe());
		fichierEquiv.setNomCategorie(fichierOriginal.getCategorie());
		return fichierEquiv;
	}

	private Colonne transform(TypeColonne colonneOriginale) {
		Colonne colonneEquiv = new Colonne(colonneOriginale.getNom());
		String desc = colonneOriginale.getDescription();
		colonneEquiv.setDescription(desc);
		String presence = colonneOriginale.getPresenceValeur();

		colonneEquiv.setColonneReference(colonneOriginale
				.getColonneDeReference());

		if ("INTERDITE".equals(presence)) {
			colonneEquiv.setPresenceValeur(PRESENCE.INTERDITE);
			return colonneEquiv;
		}
		if ("OBLIGATOIRE".equals(presence)) {
			colonneEquiv.setPresenceValeur(PRESENCE.OBLIGATOIRE);
		}

		for (TypeContrainte contrainteOriginale : colonneOriginale
				.getContrainte()) {
			if ("ContrainteReference".equals(contrainteOriginale.getType())) {
				addReference(colonneEquiv, contrainteOriginale.getParam()
						.get(0), contrainteOriginale.getParam().get(1));
			} else {
				// on ignore ContrainteTRUE parceque cette contrainte ne fais
				// rien
				if (!"ContrainteTRUE".equals(contrainteOriginale.getType())) {
					colonneEquiv.addContrainte(transform(contrainteOriginale));
				}
			}
		}
		return colonneEquiv;
	}

	private ContrainteMultiCol transform(
			TypeContrainteMultiColonne contrainteOriginale) {
		String cols[] = new String[contrainteOriginale.getColonne().size()];
		for (int i = 0; i < cols.length; i++) {
			cols[i] = contrainteOriginale.getColonne().get(i).getNom();
		}
		if ("Unique".equals(contrainteOriginale.getNomFonction())) {
			return new ContrainteMultiColUnique(contrainteOriginale.getId(),
					contrainteOriginale.getMessageErreur(), cols);

		}
		return new ContrainteMultiColFonctionsSpecifiques(
				contrainteOriginale.getId(),
				contrainteOriginale.getMessageErreur(),
				contrainteOriginale.getNomFonction(), cols);

	}

	private ContrainteUniCol transform(TypeContrainte contrainteOriginale) {
		String type = contrainteOriginale.getType();
		ContrainteUniCol res = createContrainteSimple(type);
		if (res != null) {
			return res;
		}

		if ("ContrainteTaille".equals(type)) {
			int taille = Integer
					.parseInt(contrainteOriginale.getParam().get(0));
			return new ContrainteTaille(taille);
		}
		if ("ContrainteRegex".equals(type)) {
			return new ContrainteRegex(contrainteOriginale.getParam().get(0));
		}
		if ("ContrainteTypeDate".equals(type)) {
			return new ContrainteTypeDate(contrainteOriginale.getParam().get(0));
		}
		if ("ContrainteTypeDecimal".equals(type)) {
			return new ContrainteTypeDecimal(
					Integer.parseInt(contrainteOriginale.getParam().get(0)),
					separateur, Integer.parseInt(contrainteOriginale.getParam()
							.get(1)));
		}
		if ("ContrainteListeValeursPermises".equals(type)) {
			String[] valeursPermises = new String[contrainteOriginale
					.getParam().size()];
			for (int i = 0; i < valeursPermises.length; i++) {
				valeursPermises[i] = contrainteOriginale.getParam().get(i);
			}
			return new ContrainteListeValeursPermises(valeursPermises);
		}
		return new ContrainteTRUE();
	}

	private static ContrainteUniCol createContrainteSimple(String type) {
		if ("ContrainteUnique".equals(type)) {
			return new ContrainteUnique();
		}
		if ("ContrainteTypeEntier".equals(type)) {
			return new ContrainteTypeEntier();
		}
		if ("ContrainteTypeChaineDeCaractere".equals(type)) {
			return new ContrainteTypeChaineDeCaractere();
		}
		return null;
	}

	private void addReference(Colonne c, String nomFichier, String nomColonne) {
		colonnesPrinc.add(c);
		fichierRef.add(nomFichier);
		colonneRef.add(nomColonne);
	}
}
