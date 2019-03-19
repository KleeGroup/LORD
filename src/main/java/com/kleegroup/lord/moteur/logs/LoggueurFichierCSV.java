/**
 * 
 */
package com.kleegroup.lord.moteur.logs;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ResourceBundle;

import com.kleegroup.lord.moteur.IErreur;

/**
 * Cette classe s'occupe de sauvegarder les fichiers de logs sous forme CSV.
 */
public class LoggueurFichierCSV extends AbstractLoggueur {
    protected static final ResourceBundle RESOURCEMAP = ResourceBundle
	    .getBundle("resources.GeneralUIMessages");

    protected String
    nomFichier = RESOURCEMAP.getString("TableModel.Fichier"),
    position = RESOURCEMAP.getString("TableModel.Ligne"),
	    reference = RESOURCEMAP.getString("TableModel.Reference"),
	    nomColonne = RESOURCEMAP.getString("TableModel.Colonne"),
	    valeur = RESOURCEMAP.getString("TableModel.Valeur"),
	    erreur = RESOURCEMAP.getString("TableModel.MessageErreur");

    protected PrintStream out = null;

    protected String chemin;

    protected boolean headerPublie = false;

    protected long nbErr = 0;

    protected String formatSansRef = "\"%1s\";\"%2s\";\"%3s\";\"%s\";\"%5s\"\n",
    formatAvecRef = "\"%1s\";\"%2s\";\"%3s\";\"%s\";\"%5s\";\"%6s\"\n";

    /**
     * @param cheminFichier
     *                le chemin du fichier ou il faut ecrire
     */
    public LoggueurFichierCSV(String cheminFichier) {
	super();
	this.chemin = cheminFichier;
    }

    /** {@inheritDoc} */
    @Override
	public void flushAndClose() {
	if (out != null) {
	    out.flush();
	    out.close();
	}
    }

    /** {@inheritDoc} */
    @Override
	public void log(IErreur err) {
	if (!headerPublie) {
	    creeFichierSurDisque();
	}
	if (out != null) {
		if (hasReferenceColonne()) {
		out.format(formatAvecRef, echapper(err.getNomFichier()), err
			.getErrLigne(),
			echapper(err.getReference().toString()), echapper(err
				.getErrColonne()),
			echapper(err.getErrValeur()), echapper(err
				.getErrMessage()));
	    } else {
		out.format(formatSansRef, echapper(err.getNomFichier()), err
			.getErrLigne(), echapper(err.getErrColonne()),
			echapper(err.getErrValeur()), echapper(err
				.getErrMessage()));
	    }
	}

    }

    private void creeFichierSurDisque() {
	FileOutputStream fos = null;
	try {
	    fos = new FileOutputStream(chemin);

	} catch (final FileNotFoundException e) {
	    e.printStackTrace();
	    headerPublie = true;
	    out = null;
	    return;
	}

	this.out = new PrintStream(new BufferedOutputStream(fos));
	//FIXME
	if (hasReferenceColonne()) {
	    out.format(formatAvecRef, nomFichier, position, reference,
		    nomColonne, valeur, erreur);
	} else {
	    out.format(formatSansRef, nomFichier, position,
		    nomColonne, valeur, erreur);
	}
	headerPublie = true;
    }

    private String echapper(String val) {
	return val.replaceAll("\"", "\"\"");
    }

}
