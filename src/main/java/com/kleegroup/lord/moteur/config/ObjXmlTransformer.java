package com.kleegroup.lord.moteur.config;

import javax.xml.bind.JAXBElement;

import com.kleegroup.lord.config.xml.ObjectFactory;
import com.kleegroup.lord.config.xml.TypeColonne;
import com.kleegroup.lord.config.xml.TypeContrainte;
import com.kleegroup.lord.config.xml.TypeContrainteMultiColonne;
import com.kleegroup.lord.config.xml.TypeFichier;
import com.kleegroup.lord.config.xml.TypeSchema;
import com.kleegroup.lord.moteur.Categories;
import com.kleegroup.lord.moteur.Colonne;
import com.kleegroup.lord.moteur.ContrainteMultiCol;
import com.kleegroup.lord.moteur.ContrainteUniCol;
import com.kleegroup.lord.moteur.Fichier;
import com.kleegroup.lord.moteur.Schema;

/**
 * Sert à convertir un schéma de moteur en document XML.
 * 
 */
public class ObjXmlTransformer {
	private final ObjectFactory localOF = new ObjectFactory();

	/**
	 * effectue la transformation Objet<-> ObjetsJAXB.
	 * 
	 * @param schemaObjOriginal
	 *            le shema a transformer
	 * @return le schema JAXB equivalent
	 */
	public JAXBElement<TypeSchema> transform(final Schema schemaObjOriginal) {
		final TypeSchema schemaEquiv = localOF.createTypeSchema();

		schemaEquiv.setAfficherExportLogs(schemaObjOriginal.isAfficherExportLogs());
		schemaEquiv.setSeparateurChamps(schemaObjOriginal.getSeparateurChamp().name());
		schemaEquiv.setSeparateurDecimal(schemaObjOriginal.getSeparateurDecimales().name());
		schemaEquiv.setEncodage(schemaObjOriginal.getEncoding());
		for (Categories.Categorie c : schemaObjOriginal.getCategories().getListCategories()) {
			for (Fichier f : c.getFiles()) {
				schemaEquiv.getFichier().add(transform(f));
			}
		}
		return localOF.createSchema(schemaEquiv);
	}

	private TypeFichier transform(final Fichier fichierOriginal) {
		final TypeFichier fichierEquiv = localOF.createTypeFichier();
		fichierEquiv.setNom(fichierOriginal.getNom());
		fichierEquiv.setPrefixNom(fichierOriginal.getPrefixNom());
		fichierEquiv.setExtension(fichierOriginal.getExtension());
		fichierEquiv.setGroupe(fichierOriginal.getGroupe());
		fichierEquiv.setNbLignesEntete(fichierOriginal.getNbLignesEntete());
		fichierEquiv.setSeuilErreurs(fichierOriginal.getSeuilAbandon());
		fichierEquiv.setCategorie(fichierOriginal.getNomCategorie());

		fichierEquiv.setColonnes(localOF.createTypeFichierColonnes());
		for (Colonne col : fichierOriginal.getColonnes()) {
			fichierEquiv.getColonnes().getColonne().add(transform(col));
		}

		for (ContrainteMultiCol cmc : fichierOriginal.getContraintesMultiCol()) {
			fichierEquiv.getContrainte().add(transform(cmc));
		}

		return fichierEquiv;
	}

	private TypeColonne transform(final Colonne colonneOriginale) {
		final TypeColonne colonneEquiv = localOF.createTypeColonne();
		colonneEquiv.setNom(colonneOriginale.getNom());
		colonneEquiv.setDescription(colonneOriginale.getDescription());
		colonneEquiv.setPresenceValeur(colonneOriginale.getPresenceValeur().toString());
		colonneEquiv.setColonneDeReference(colonneOriginale.isColonneReference());

		for (ContrainteUniCol contrainteOriginale : colonneOriginale.getContraintes()) {
			colonneEquiv.getContrainte().add(transform(contrainteOriginale));
		}
		return colonneEquiv;
	}

	private TypeContrainte transform(final ContrainteUniCol contrainteOrigin) {
		final TypeContrainte contrainteEquiv = localOF.createTypeContrainte();
		contrainteEquiv.setType(contrainteOrigin.getID());
		for (String param : contrainteOrigin.getListeParam()) {
			contrainteEquiv.getParam().add(param);
		}
		return contrainteEquiv;
	}

	private TypeContrainteMultiColonne transform(final ContrainteMultiCol contrainteOrigin) {
		final TypeContrainteMultiColonne cmcEquiv = localOF.createTypeContrainteMultiColonne();
		cmcEquiv.setId(contrainteOrigin.getID());
		cmcEquiv.setMessageErreur(contrainteOrigin.getErrTemplate());
		cmcEquiv.setNomFonction(contrainteOrigin.getNomFonction());
		for (String nomColonne : contrainteOrigin.getNomColonnes()) {
			final TypeContrainteMultiColonne.Colonne param = localOF.createTypeContrainteMultiColonneColonne();
			param.setNom(nomColonne);
			cmcEquiv.getColonne().add(param);
		}
		return cmcEquiv;
	}

}
