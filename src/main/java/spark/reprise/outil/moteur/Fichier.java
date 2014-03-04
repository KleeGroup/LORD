package spark.reprise.outil.moteur;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import spark.reprise.outil.moteur.Categories.Categorie;
import spark.reprise.outil.moteur.contraintes.ContrainteMultiColFonctionsSpecifiques;
import spark.reprise.outil.moteur.contraintes.ContrainteMultiColUnique;
import spark.reprise.outil.moteur.contraintes.ContrainteReference;
import spark.reprise.outil.moteur.contraintes.ContrainteReferenceLookup;
import spark.reprise.outil.moteur.exceptions.AbandonUtilisateur;
import spark.reprise.outil.moteur.exceptions.CaractereInterdit;
import spark.reprise.outil.moteur.exceptions.ExceptionMoteur;
import spark.reprise.outil.moteur.exceptions.NbreColonnesInsuffisant;
import spark.reprise.outil.moteur.exceptions.TropDErreurs;
import spark.reprise.outil.moteur.logs.ILogger;
import spark.reprise.outil.moteur.util.ICSVDataSource;
import spark.reprise.outil.moteur.util.IHierarchieSchema;
import spark.reprise.outil.moteur.util.INotifiable;

/**
 * Represente un fichier � verifier.
 */
public class Fichier implements IHierarchieSchema {

    static final ResourceBundle RESOURCEMAP = ResourceBundle
	    .getBundle("resources.GeneralUIMessages");

    private static String nomRefLookup = ContrainteReferenceLookup.class
	    .getSimpleName().substring("Contrainte".length());

    private static org.apache.log4j.Logger logAppli = Logger
	    .getLogger(Fichier.class);

    protected INotifiable eltAnotifier = null;

    protected List<ContrainteMultiCol> contraintesMultiCol = new ArrayList<ContrainteMultiCol>();

    private ICSVDataSource source;

    private final List<Colonne> colonnes = new ArrayList<Colonne>();

    private ILogger logger;

    private List<IErreur> erreurs = new ArrayList<IErreur>();

    private final Collection<Fichier> fichiersDontOnDepend = new ArrayList<Fichier>();

    private final Collection<Fichier> fichiersQuiDependentDeNous = new ArrayList<Fichier>();

    private volatile boolean abandonneVerifFichier = false;

    private boolean erreurSiTropDeColonnes = true;

    private int nbLignesEntete = 0;

    private int seuilAbandon = -1;

    private long nbErreurs = 0;

    private int groupe = 0;

    private int niveauTopo = -1;

    private boolean pause;

    private boolean cancel;

    private String nom;

    private String chemin;

    private String nomCategorie;

    private String prefixNom;

    private String extension = "";

    private Categorie categorie;

    private int posColRef[]=new int[0];

    /*
     * determine le mode d'affichage de toString 0 = le nom simplement
     * autre= nom +(nbErreurs) si nbErreurs!=0
     */
    private int modeAffichage = 0;

    /** Indique l'�tat du fichier. */
    public enum ETAT {

	/**
	 * Le fichier n'a pas �t� encore v�rifi�.
	 */
	EN_ATTENTE {
	    /**{@inheritDoc}*/
	    @Override
	    /**{@inheritDoc}*/
	    public String toString() {
		return RESOURCEMAP.getString("etatFichier.EN_ATTENTE");
	    }
	},
	/** en cours de verification. */
	EN_COURS_DE_VERIFICATION {
	    /**{@inheritDoc}*/
	    @Override
	    /**{@inheritDoc}*/
	    public String toString() {
		return RESOURCEMAP
			.getString("etatFichier.EN_COURS_DE_VERIFICATION");
	    }
	},
	/**
	 * l'utilisateur a demand� l'abandon de la v�rification.
	 */
	ABANDONNE_UTILISATEUR {
	    /**{@inheritDoc}*/
	    @Override
	    /**{@inheritDoc}*/
	    public String toString() {
		return RESOURCEMAP
			.getString("etatFichier.ABANDONNE_UTILISATEUR");
	    }
	},
	/**
	 * l'utilisateur a demand� l'abandon de la v�rification.
	 */

	ABANDONNE_ERREUR_LECTURE {
	    /**{@inheritDoc}*/
	    @Override
	    /**{@inheritDoc}*/
	    public String toString() {
		return RESOURCEMAP
			.getString("etatFichier.ABANDONNE_ERREUR_LECTURE");
	    }
	},

	/**
	 * L'utilisateur a d�sactiv� la v�irifcation de ce fichier.
	 * */
	DESACTIVE_UTILISATEUR {
	    /**{@inheritDoc}*/
	    @Override
	    public String toString() {
		return RESOURCEMAP
			.getString("etatFichier.DESACTIVE_UTILISATEUR");
	    }
	},
	/**
	 * Le fichier a �t� d�sactiv� car il d�pend sur un fichier d�sactiv� par
	 * l'utilisateur.
	 */
	DESACTIVE_DEPENDANCE {
	    /**{@inheritDoc}*/
	    @Override
	    public String toString() {
		return RESOURCEMAP
			.getString("etatFichier.DESACTIVE_DEPENDANCE");
	    }
	},

	/**
	 * le fichier a �t� v�rifi�.Il contient des erreurs.
	 */
	VERIFIE_SANS_ERREUR {
	    /**{@inheritDoc}*/
	    @Override
	    /**{@inheritDoc}*/
	    public String toString() {
		return RESOURCEMAP.getString("etatFichier.VERIFIE_SANS_ERREUR");
	    }
	},
	/**
	 * le fichier a �t� v�rifi�.Il ne contient pas d'erreur.
	 */
	VERIFIE_AVEC_ERREUR {
	    /**{@inheritDoc}*/
	    @Override
	    /**{@inheritDoc}*/
	    public String toString() {
		return RESOURCEMAP.getString("etatFichier.VERIFIE_AVEC_ERREUR");
	    }
	},

	/**
	 * La v�rificationde ce fichier a �t� abandonn�. des fichiers dont il
	 * depend ou bien de groupe inf�rieurs n'ont pas �t� v�rifi�s.
	 */

	ABANDONNE_DEPENDANCE {
	    /**{@inheritDoc}*/
	    @Override
	    public String toString() {
		return RESOURCEMAP
			.getString("etatFichier.ABANDONNE_DEPENDANCE");
	    }
	},
	/**
	 * D�passemnt du seuil tol�r� d'erreurs dans le fichier.
	 */
	ABANDON_TROP_D_ERREUR {
	    /**{@inheritDoc}*/
	    @Override
	    /**{@inheritDoc}*/
	    public String toString() {
		return RESOURCEMAP
			.getString("etatFichier.ABANDON_TROP_D_ERREUR");
	    }
	},
	/**Une exception non g�r�e a eu lieu dans le moteur.V�rification abandonn�e.*/
	ABANDON_ERREUR_MOTEUR {
	    /**{@inheritDoc}*/
	    @Override
	    /**{@inheritDoc}*/
	    public String toString() {
		return RESOURCEMAP
			.getString("etatFichier.ABANDON_ERREUR_MOTEUR");
	    }
	},


    }

    private ETAT etatFichier = ETAT.EN_ATTENTE;

    /**
     * Contruit un fichier .
     *
     * @param nom
     *                nom du fichier
     * @param chemin
     *                le chemin d'acces du fichier
     * @param source
     *                source des lignes du fichier
     * @param log
     *                Objet responsable de loggue rles erreurs
     */
    public Fichier(String nom, String chemin, ICSVDataSource source, ILogger log) {
	this(nom, chemin);
	this.logger = log;
	this.source = source;
	logAppli.trace(">" + nom + "<.loggueur!=null ? ="
		+ Boolean.toString(log != null) + "\n" + ">" + nom
		+ "<.source!=null ? =" + Boolean.toString(source != null));

    }

    /**
     * Construit un fichier.<br>
     *
     * @param nom
     *                nom du fichier
     * @param prefixNom
     *                le d�but du nom du fichier (pour le retrouver
     *                automatiquement)
     */
    public Fichier(String nom, String prefixNom) {
	logAppli.trace("Cr�tion d'un fichier. nom = " + nom);
	this.nom = nom;
	this.prefixNom = prefixNom;
	cancel = false;
	synchronized (this) {
	    pause = false;

	}
    }

    /**
     * Rajoute une contrainte multicolonne au fichier.
     *
     * @param id
     *                l'identifiant de l'erreur multicolonne
     * @param errTemplate
     *                le template du message d'erreur
     * @param nomFonction
     *                le nom de la fonction de v�rification de la contrainte
     * @param cols
     *                les colonnes � v�rifier
     */
    public void addContrainteMultiCol(String id, String errTemplate,
	    String nomFonction, String... cols) {
	logAppli.trace("Ajout d'une contrainte MultiColonne \n" + "id=>" + id
		+ "< . " + "\n" + "errTemplate=>" + errTemplate + "<");
	String msg = cols[0];
	for (int i = 1; i < cols.length; i++) {
	    msg += "," + cols[i];
	}
	logAppli.trace(nomFonction + "(" + msg + ")");
	if ("Unique".equals(nomFonction)) {
	    addContrainteMultiCol(new ContrainteMultiColUnique(id, errTemplate,
		    cols));
	} else {
	    addContrainteMultiCol(new ContrainteMultiColFonctionsSpecifiques(
		    id, errTemplate, nomFonction, cols));
	}

    }

    /**
     * Rajoute une contrainte multicolonne au fichier.
     *
     * @param contrainte
     *                la contrainte multicol � rajouter
     *
     */
    public void addContrainteMultiCol(ContrainteMultiCol contrainte) {
	contrainte.setFichierParent(this);
	contraintesMultiCol.add(contrainte);
    }

    /**
     * Modifie la source des lignes du fichier.
     *
     * @param source
     *                source des ligne du fichier
     */
    public void setSource(ICSVDataSource source) {
	this.source = source;
    }

    /**
     * Ajoute une colonne au fichier. La colonne est rajout�e � la fin.
     *
     * @param c
     *                la colonne � rajouter
     */
    public void addColonne(Colonne c) {
	addColonne(c, colonnes.size());

    }

    /**
     * Ajoute une colonne au fichier. La colonne est rajout�e � la position
     * pos.
     *
     * @param c
     *                la colonne � rajouter.
     * @param pos
     *                la position de la colonne.
     */
    public void addColonne(Colonne c, int pos) {
	if (getColonne(c.getNom()) == null) {
	    logAppli.trace("Ajout de colonne. nom = >" + c.getNom()
		    + "<. position = " + colonnes.size());

	    c.setFichierParent(this);
	    c.setPosition(pos);
	    colonnes.add(pos, c);
	    for (int i = pos; i < colonnes.size(); i++) {
		colonnes.get(i).setPosition(i);
	    }
	} else {
	    logAppli
		    .error("Le fichier >"
			    + nom
			    + "< poss�de d�j� une "
			    + "colonne poss�dant le m�me nom que la colonne � ajouter.\n"
			    + "La colonne n'a pas �t� ajout�e");
	}
    }

    protected void pause() {
	synchronized (this) {
	    pause = true;
	}

    }

    protected void resume() {
	synchronized (this) {
	    pause = false;
	    this.notifyAll();
	}
    }

    /**
     * Lance la v�rification du fichier.
     *
     */
    protected void verifie() {
	logAppli.info(">" + nom + "<.Lancement de la v�rification");
	logAppli.debug("Nombre de lignes d'ent�te = " + nbLignesEntete);
	etatFichier = ETAT.EN_COURS_DE_VERIFICATION;

	logArbo();
	if (eltAnotifier != null) {
	    eltAnotifier.debutFichier(getNom());
	}

	try {
	    sauterLignesEntete();

	    verifieLigne();

	} catch (final CaractereInterdit err) {
	    final long posErrColonne =err.getErrColonne();
	    String nomCol="";
	    if (posErrColonne>=0 && posErrColonne < colonnes.size()){
		nomCol=colonnes.get((int)posErrColonne).getDescOuNom();
	    }
	    erreurs.add(ErreurConstante.errCaractereInterdit(err.getNumLigne(), nomCol
		    , err.getErrValeur()));
	    etatFichier=ETAT.ABANDONNE_ERREUR_LECTURE;
	} catch (final AbandonUtilisateur err) {
	    return;
	} catch (final TropDErreurs err) {
	    etatFichier = ETAT.ABANDON_TROP_D_ERREUR;
	    erreurs.add(ErreurConstante.errTropDErreurs(nom, err
		    .getLigneErreur()));
	    logAppli.info("Abandon de la v�rification du fichier " + nom
		    + "cause : d�passement du seuil d'erreur tol�r�es"
		    + "seuil d'erreur = " + seuilAbandon
		    + " .nbre d'erreurs = " + nbErreurs);

	} catch (final IOException e) {
	    erreurs.add(ErreurConstante.errLectureFicher(nom));
	    logAppli.info("Erreur de lecture du fichier " + nom
		    + " .Chemin d'acc�s " + getChemin());
	} catch (final ExceptionMoteur e) {
	    logAppli.info(e);
	    erreurs.add(ErreurConstante.errExceptionMoteur(source.getPosition(), e.getMessage(), nom ));
	    return;
	}catch(final Throwable t){//exception non prevue, generalement erreur dans le moteur
	    etatFichier=ETAT.ABANDON_ERREUR_MOTEUR;
	    logAppli.error(t);
	    erreurs.add(ErreurConstante.errExceptionMoteur(source.getPosition(), t.getMessage(), nom ));
	    return;
	} finally {
	    nbErreurs+=erreurs.size();
	    logger.log(erreurs);
	    logger.flushAndClose();
	    finFichier();
	}

	return;
    }

    private void finFichier() {
	logAppli.info(">" + nom
		+ "<.fin de v�rification. Nombre d'erreurs d�t�ct�es="
		+ nbErreurs);

	 if (
		etatFichier==ETAT.EN_COURS_DE_VERIFICATION
	) {
	     if (nbErreurs == 0) {
	    etatFichier = ETAT.VERIFIE_SANS_ERREUR;
	} else {
	    etatFichier = ETAT.VERIFIE_AVEC_ERREUR;
	}
	 }
	if (eltAnotifier != null) {
	    eltAnotifier.finFichier(getNom(), etatFichier);
	}
    }

    private void verifieLigne() throws CaractereInterdit, TropDErreurs,
	    AbandonUtilisateur, ExceptionMoteur, IOException {
	long num_ligne = 0;
	String[] input;
	int res = 0;
	while (!abandonneVerifFichier && source.hasNext()) {
	    verifieSiUtilisateurVeutAbandonner();
	    input = source.next();
	    num_ligne = source.getPosition();
	    res = 0;
	    for (int i = 0; i < input.length; i++) {
		res += input[i].length();
	    }

	    try {
		if (erreurSiTropDeColonnes && input.length > colonnes.size()) {
		    throw new NbreColonnesInsuffisant();
		}
		erreurs = verifie(num_ligne, input);

	    } catch (final NbreColonnesInsuffisant err) {
		erreurs.add(ErreurConstante.errNbreColonnesIncorrect(num_ligne,
			input.length, colonnes.size()));
		logAppli
			.debug(nom
				+ "Le nombre de colonnes lu est sup�rieur � celui attendu"
				+ "lu=" + input.length + ".attendu="
				+ colonnes.size());
		return;
	    }
	    nbErreurs += erreurs.size();
	    if (seuilAbandon > 0 && nbErreurs >= seuilAbandon) {
		throw new TropDErreurs(num_ligne);
	    }
	    notify(source.getNbCharactersRead());

	    logger.log(erreurs);
	    erreurs.clear();
	}
    }

    private void verifieSiUtilisateurVeutAbandonner() throws AbandonUtilisateur {
	try {
	    synchronized (this) {
		while (pause) {
		    this.wait();
		}
	    }
	} catch (final InterruptedException e) {
	    etatFichier = ETAT.ABANDONNE_UTILISATEUR;
	} finally {
	    if (cancel) {
		etatFichier = ETAT.ABANDONNE_UTILISATEUR;
		logger.flushAndClose();
		throw new AbandonUtilisateur();
	    }
	}
    }

    private void notify(long l) {
	if (eltAnotifier != null) {
	    eltAnotifier.caractereTraites(l);
	}

    }

    protected List<IErreur> verifie(long numLigne, String[] valeurs)
	    throws ExceptionMoteur {
	final List<IErreur> errs = new ArrayList<IErreur>();
	for (final Colonne c : colonnes) {
	    errs.addAll(c.verifie(numLigne, valeurs));
	}

	for (final ContrainteMultiCol c : getContraintesMultiCol()) {
	    final IErreur e = c.verifie(numLigne, valeurs);
	    if (e != Erreur.pasDErreur()) {
		errs.add(e);
	    }
	}
	return errs;
    }

    private void logArbo() {
	logAppli.debug("    Nombre de colonnes " + colonnes.size());
	int i = 0;
	for (final Colonne c : colonnes) {
	    if (i != c.getPosition()) {
		logAppli.error("La colonne >" + c.getNom()
			+ "< n'est pas � la bonne position");
		logAppli.error("position dans le fichier=" + i
			+ ". Colonne.position=" + c.getPosition());
	    }
	    if (c.getFichierParent() != this) {
		logAppli.error("La colonne >" + c.getNom()
			+ "< n'a pas le bon fichierParent");

	    }
	    i++;
	    logAppli.debug("        " + c.getPosition() + " - >" + c.getNom()
		    + "<");
	    for (final ContrainteUniCol cuc : c.getContraintes()) {
		logAppli.debug("                " + cuc.getID());
	    }
	}
    }

    private void sauterLignesEntete() throws IOException,CaractereInterdit {
	int temp = nbLignesEntete;
	while (source.hasNext() && temp > 0) {
	    source.next();
	    temp--;
	}
    }

    /**
     * Renvoie la liste des colonnes du fichier.
     *
     * @return la liste des colonnes du fichier
     */
    public List<Colonne> getColonnes() {
	return colonnes;
    }

    /**
     * Renvoie une contrainte de type {@linkplain ContrainteRef_Lookup}.Si
     * une contrainte de Lookup n'existe pas, elle est rajout�e � la colonne .
     * <br>
     * <br>
     * Cela permet d'avoir une syntaxe plus facile pour rajouter une
     * contrainte de r�f�rence entre deux colonnes.<br>
     *
     * @param nom_colonne
     *                le nom de la colonne r�f�renc�e
     * @return la contrainte de Lookup
     */
    private ContrainteReferenceLookup ref(String nomColonne) {
	if (getColonne(nomColonne) == null) {
	    logAppli
		    .error("Une r�f�rence a �t� demand�e � une colonne non d�finie");
	    logAppli.error("colonne demand�e = " + nomColonne);
	    return null;
	}

	if (getColonne(nomColonne).getContrainte(nomRefLookup) == null) {
	    getColonne(nomColonne).addContrainte(
		    new ContrainteReferenceLookup());

	    logAppli.trace(">" + nom + "<:une ContrainteReferenceLookup"
		    + " a �t� cr�e pour la colonne >" + nomColonne + "<");
	}

	return (ContrainteReferenceLookup) getColonne(nomColonne)
		.getContrainte(nomRefLookup);
    }

    /**
     * Renvoie une contrainte de type ContrianteReference.Si une contrainte
     * de Lookup n'existe pas sur mla colonne r�f�renc�e, elle est rajout�e
     * � la colonne . <br>
     * <br>
     * Cela permet d'avoir une syntaxe plus facile pour rajouter une
     * contrainte de r�f�rence entre deux colonnes.<br>
     *
     * @param nomColonne
     *                le nom de la colonne r�f�renc�e
     * @return la contrainte de Lookup
     */
    private ContrainteUniCol reference(String nomColonne) {
	return new ContrainteReference(ref(nomColonne));
    }

    /**
     * @param nomColonne
     *                le nom de la colonne du fichier
     * @param c2
     *                la colonne r�f�renc�e
     */
    public void addReference(String nomColonne, Colonne c2) {

	if (c2.getFichierParent() != this) {
	    fichiersDontOnDepend.add(c2.getFichierParent());
	    c2.getFichierParent().fichiersQuiDependentDeNous.add(this);
	}
	getColonne(nomColonne).addContrainte(
		c2.getFichierParent().reference(c2.getNom()));

    }

    /**
     * Renvoie le loggueur du fichier.
     *
     * @return le loggueur du fichier
     */
    public ILogger getLogger() {
	return logger;
    }

    /**
     * REmplace le logguer du fichier par un nouveau loggueur.
     *
     * @param logger
     *                le nouveau loggueur du fichier
     */
    public void setLogger(ILogger logger) {
	this.logger = logger;
	logger.setReferenceColonne(hasColonneReference());
    }

    /**
     * Renvoie la ieme colonne du fichier.
     *
     * @param pos
     *                position de la colonne demand�e
     * @return la colonne � la position pos
     */
    public Colonne getColonne(int pos) {
	return colonnes.get(pos);
    }
    /**
     * @return le nombre de colonnes du fichier.
     */
    public int getNbColonnes(){
	return colonnes.size();
    }

    /**
     * Remplace la collonne � la position pos , par la colonne c.
     *
     * @param pos
     *                la position de la colonne � remplacer
     * @param c
     *                la nouvelle colonne
     */
    public void setColonne(int pos, Colonne c) {
	colonnes.set(pos, c);
    }

    /**
     * Rajoute une colonne au fichier. LA colonne sera rajout�e � la fin.<br>
     * Remarque: Le nom de chaque colonne doit etre unique
     *
     * @param nomColonne
     *                le nom de la colonne � rajouter
     */
    public void addColonne(String nomColonne) {
	addColonne(new Colonne(nomColonne));
    }

    /**
     * Rajoute une liste de colonne � la fin du fichier.
     *
     * @param cols
     *                les noms des colonnes � rajouter
     */
    public void addColonne(String... cols) {
	for (final String s : cols) {
	    addColonne(s);
	}
    }

    /**
     * . Renvoie la colonne dont le nom est nom_colonne
     *
     * @param nomColonne
     *                le nom de la colonne recherch�e
     * @return la colonne recherch�e
     */
    public Colonne getColonne(String nomColonne) {
	for (final Colonne col : colonnes) {
	    if (nomColonne.equals(col.getNom())) {
		return col;
	    }
	}
	return null;
    }


    /**
     * @param seuil
     *                le nombre d'erreur tol�r� avant l'abandon de la v�rif (<=0
     *                si pas d'abandon)
     */
    public void setSeuilAbandon(int seuil) {
	logAppli.debug("fichier " + nom + ".seuilAbandon=" + seuil);
	seuilAbandon = seuil;
    }

    /**
     * @return le nombre d'erreur tol�r� avant l'abandon de la v�rif (<=0
     *         si pas d'abandon)
     */
    public int getSeuilAbandon() {
	return seuilAbandon;
    }

    /**
     * @return le nombre de ligne d'entetes (ignor�es lors de la
     *         v�rification)
     */
    public int getNbLignesEntete() {
	return nbLignesEntete;
    }

    /**
     * @param nbLignesEntete
     *                le nombre de ligne d'entetes (ignor�es lors de la
     *                v�rification)
     */
    public void setNbLignesEntete(int nbLignesEntete) {
	this.nbLignesEntete = nbLignesEntete;
	logAppli.debug("fichier " + nom + ".nbLignesEntete=" + nbLignesEntete);
    }

    /**
     * @return si la v�rification l�ve une erreur si le nombre de colonnes
     *         lues est sup�rieur � celui attendu
     */
    public boolean isErreurSiTropDeColonnes() {
	return erreurSiTropDeColonnes;
    }

    /**
     * @param erreur
     *                true si la v�rification doit lever une erreur si le
     *                nombre de colonnes lues est sup�rieur � celui attendu
     */
    public void setErreurSiTropDeColonnes(boolean erreur) {
	this.erreurSiTropDeColonnes = erreur;
    }

    /**
     * @return Le nom du fichier.
     */
    public String getNom() {
	return nom;
    }

    /**
     * @return le groupe de verification du fichier
     */
    public int getGroupe() {
	return groupe;
    }

    /**
     * definit le groupe de verification du fichier.
     *
     * @param grp
     *                le groupe du fichier
     */
    public void setNiveau(int grp) {
	this.groupe = grp;
    }

    /**
     * Renvoie le "niveau" du fichier. <br>
     * l'ordre est definit formellement de la maniere suivante:<br>
     * un fichier qui ne depend sur aucun autre fichier, est au niveau 0 un
     * fichier qui depend sur d'autre fichier a son niveau egal au maximum
     * de l'ordre de ses fils +1<br>
     * niveau= max (fils.niveau)+1
     *
     * @return le niveau topologique du fichier
     */
    public int getNiveauTopo() {
	// if (fichiersDontOnDepend.isEmpty()) {
	// return 0;
	// }
	//
	// niveauTopo = Collections.max(fichiersDontOnDepend,
	// new FichierComparteurOrdreTopo()).niveauTopo + 1;
	return niveauTopo;
    }

    private int calculeNiveauTopo() {
	if (fichiersDontOnDepend.isEmpty()) {
	    return 0;
	}
	int max = 0;



	for (final Fichier f : fichiersDontOnDepend) {
		if (fichiersQuiDependentDeNous.contains(f)){
			logAppli.error("dependence circulaire entre " + this.nom + " et " + f.nom);
			return Integer.MIN_VALUE;
		}
	    final int tmp = f.calculeNiveauTopo();
	    max = max > tmp ? max : tmp;
	}
	return max + 1;
    }

    /**
     * Indique si le fichier est pret pour lancer la verification. Un
     * fichier est pret si tous les fichiers dont il depend ont ete verifies
     *
     * @return true s'il est pret pour la verification
     */
    public boolean isPretVerif() {
	if (etatFichier == ETAT.DESACTIVE_UTILISATEUR) {
	    return false;
	}
	for (final Fichier f : fichiersDontOnDepend) {
	    if (!(f.etatFichier == ETAT.VERIFIE_AVEC_ERREUR || f.etatFichier == ETAT.VERIFIE_SANS_ERREUR)) {
		return false;

	    }
	}
	return true;
    }

    /**
     * renvoie la liste des contraintes multicolonnes.
     *
     * @return la liste des contraintes multicolonnes
     */
    public List<ContrainteMultiCol> getContraintesMultiCol() {
	return contraintesMultiCol;
    }

    /**
     * renvoie le chemin d'acces du fichier.
     *
     * @return le chemin d'acces du fichier
     */
    public String getChemin() {
	return chemin;
    }

    /**
     * @return un objet ETAT representant l'etat de l'objet
     */
    public ETAT getEtatFichier() {
	return etatFichier;
    }

    /**
     * modifie l'etat du fichier.
     *
     * @param etatFichier
     *                le nouvel etat du fichier
     */
    public void setEtatFichier(ETAT etatFichier) {
	this.etatFichier = etatFichier;
    }

    /**
     * @return me nombre de caracteres dans la source de ce fichier
     */
    public long getNbCaracteresTotal() {
	return source.getTotalSize();
    }

    /**
     * Designe un element a notifier dans le cas de certains evenement(
     * debut de verification, fin de verification, avacncement,...).
     *
     * @param eltAnotifier
     *                l'element a notifier
     */
    public void setEltAnotifier(INotifiable eltAnotifier) {
	this.eltAnotifier = eltAnotifier;
    }

    /**
     * @return le nombre d'erreurs detect�es jusqu'� pr�sent dans ce fichier
     */
    public long getNbErreurs() {
	return nbErreurs;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
	if (modeAffichage == 0 || getNbErreurs() == 0) {
	    return getNom();
	}
	return getNom() + " (" + getNbErreurs() + ")";
    }

    /**
     * abandonne la verification de ce ficier, si une verification est en
     * cours
     */
    protected void cancel() {
	cancel = true;
	etatFichier = ETAT.ABANDONNE_UTILISATEUR;

    }

    /**
     * @param chemin
     *                designe le chemin du fichier source
     */
    public void setChemin(String chemin) {
	if (chemin == null) {
	    return;
	}

	this.chemin = chemin;
    }

    /**
     * @return une liste des fichiers qu'on reference
     */
    public Collection<Fichier> getFichiersDontOnDepend() {
	return fichiersDontOnDepend;
    }

    /**
     * @return une liste des fichiers qui nous referencent
     */
    public Collection<Fichier> getFichiersQuiDependentDeNous() {
	return fichiersQuiDependentDeNous;
    }

    /**
     * @return le fichier est il activ�?
     */
    public boolean isEnabled() {
	return etatFichier == ETAT.EN_ATTENTE;

    }

    /**
     * nettoie l'objet. Remet � zero les donn�es sp�cifique utilis�e lors de
     * la derni�re v�rification pour pouvoir r�utiliser cet objet pour une
     * nouvelle v�rification
     */
    protected void clean() {
	nbErreurs = 0;
	synchronized(this){
	    pause = false;
	}
	cancel = false;
	int nbColRef=0;
	for (final Colonne c : colonnes) {
	    c.clean();
	    if(c.isColonneReference()){
		nbColRef++;
	    }
	}
	for (final IContrainte cmc : contraintesMultiCol) {
	    cmc.clean();
	}
	niveauTopo = calculeNiveauTopo();

	posColRef=new int[nbColRef];
	int j=0;
	for(int i=0;i<colonnes.size();i++){
	    if (colonnes.get(i).isColonneReference()){
		posColRef[j]=i;
		System.out.println(j+"#"+posColRef[j]);
		j++;

	    }
	}

    }

    /**
     * le PrefixNom sert a retrouver le fichier parmi d'autres dans un
     * repertoire de donn�es. Il consiste d'une partie du nom du fichier
     *
     * @return le Prefix du nom du fichier
     */
    public String getPrefixNom() {
	return prefixNom;
    }

    /**
     * le PrefixNom sert a retrouver le fichier parmi d'autres dans un
     * repertoire de donn�es. Il consiste d'une partie du nom du fichier
     *
     * @param prefixNom
     *                le Prefix du nom du fichier
     */
    public void setPrefixNom(String prefixNom) {
	this.prefixNom = prefixNom;
    }

    /**
     * @return l'extension du fichier
     */
    public String getExtension() {
	return extension;
    }

    /**
     * d�signe l'extension du fichier.
     *
     * @param extension
     *                l'extension du fichier
     */
    public void setExtension(String extension) {
	if (extension!=null){
	    this.extension = extension;
	}
    }

    /**
     * @param nom
     *                le nom du fichier
     */
    public void setNom(String nom) {
	this.nom = nom;
    }

    /**
     * le groupe sert a d�finir une hi�rarchie entre les fichiers. Tant que
     * les fichiers du groupe inf�rieur ne sont pas correctes, on ne v�rifie
     * pas les fichiers des groupes sup�rieurs
     *
     * @param groupe
     *                le groupe du fichier
     */
    public void setGroupe(int groupe) {
	this.groupe = groupe;
    }

    /**
     * renvoit une copie du fichier.
     *
     * @return la copie du fichier
     */
    public Fichier copy() {
	final Fichier fichierEquiv = new Fichier("", "");
	fichierEquiv.nom = "Clone";
	fichierEquiv.prefixNom = prefixNom;
	fichierEquiv.extension = extension;
	fichierEquiv.groupe = groupe;
	fichierEquiv.nbLignesEntete = nbLignesEntete;
	fichierEquiv.seuilAbandon = seuilAbandon;
	for (final Colonne col : colonnes) {
	    fichierEquiv.addColonne(col.copy());
	}

	for (final ContrainteMultiCol cmc : getContraintesMultiCol()) {
	    fichierEquiv.addContrainteMultiCol(cmc.getID(), cmc
		    .getErrTemplate(), cmc.getNomFonction(), cmc
		    .getNomColonnes().toArray(
			    new String[cmc.getNomColonnes().size()]));
	}

	return fichierEquiv;
    }

    /**
     * determine le comportoment de toString().<br>
     * =0 toString() = nom du fichier <br>
     * !=0 toString() = nom du fichier (NbErreurs) <br>
     *
     * @return le mode d'affichage
     */
    public int getModeAffichage() {
	return modeAffichage;
    }

    /**
     * determine le comportoment de toString().<br>
     * =0 toString() = nom du fichier <br>
     * !=0 toString() = nom du fichier (NbErreurs) <br>
     *
     * @param modeAffichage
     *                le mode d'affichage
     */
    public void setModeAffichage(int modeAffichage) {
	this.modeAffichage = modeAffichage;
    }

    /**
     * notifie que le fichier a �t� abandonn�.
     * @param etatFinal l'etat final du fichier.
     */
    public void abandonFichier(ETAT etatFinal){
	etatFichier=etatFinal;
	eltAnotifier.finFichier(nom, etatFichier);
    }

    /**
     * @return la cat�gorie du fichier.
     */
    public Categorie getCategorie() {
        return categorie;
    }

    /**
     * Rattache le fichier � cat�gorie.
     * @param categorie la nouvelle cat�gorie du fichier.
     */
    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
        setNomCategorie(categorie.getNom());
    }

    /**
     * @return le nom de la cat�gorie du fichier.
     */
    public String getNomCategorie(){
	return nomCategorie;
    }

    /**
     * Modifie le nom de la cat�gorie du fichier.
     * @param nomCategorie le nom de la cat�gorie du fichier.
     */
    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    /**
     * Supprime une colonne du fichier.
     * @param pos la position de la colonne � supprimer.
     */
    public void deleteColonne(int pos) {
	colonnes.remove(pos);

    }

    /**
     * D�place une colonne vers le bas.
     * @param selectedRow la position de la colonne.
     */
    public void moveColonneDn(int selectedRow) {
	if(selectedRow<0 || !(selectedRow<colonnes.size()-1)){
		return;
	    }
	    final Colonne c1=getColonne(selectedRow),
	    	c2=getColonne(selectedRow+1);
	    colonnes.set(selectedRow, c2);
	    colonnes.set(selectedRow+1, c1);
    }
    /**
     * D�place une colonne vers le haut.
     * @param selectedRow la position de la colonne.
     */
    public void moveColonneUp(int selectedRow) {
	if(selectedRow<=0 || !(selectedRow<colonnes.size())){
		return;
	    }
	    final Colonne c1=getColonne(selectedRow),
	    	c2=getColonne(selectedRow-1);
	    colonnes.set(selectedRow, c2);
	    colonnes.set(selectedRow-1, c1);
    }

    /**
     * @return une suggestion de nom de colonne non utilis� par les autres colonnes du fichier.
     */
    public String getNomColonneLibre() {
	String nomCol="Nouvelle Colonne ";
	    int colNumber=1;
	    while(getColonne(nomCol+colNumber)!=null){
		colNumber++;
	    }
	    nomCol+=colNumber;
	return nomCol;
    }

    /**
     * @return true s'il contient au moins une colonne de reference
     * */
    public boolean hasColonneReference(){
	return posColRef.length>0;
    }
    /**
     * @param valeurs les valeurs de la ligne
     * @return la liste des valeurs de reference de la colonnes
     */
    public List<String> getValeursColRef(String[] valeurs){
	final String [] resTmp=new String[posColRef.length];
	for(int i=0;i<posColRef.length;i++){
	    resTmp[i]=posColRef[i]<valeurs.length?
		    		valeurs[posColRef[i]].intern():
	    		    "";

	}
//	List<String> res=new ArrayList<String>(posColRef.length);
//	for(int i=0;i<posColRef.length;i++){
//	    if(posColRef[i]<valeurs.length){
//		String tmp=valeurs[posColRef[i]].intern();
//		res.add( tmp);
//		System.out.println(tmp);
//	    }else{
//		res.add("");
//	    }
//	}

	return java.util.Arrays.asList(resTmp);
    }
/**{@inheritDoc}*/
    @Override
	public IHierarchieSchema getChild(int index) {
	return colonnes.get(index);
    }
/**{@inheritDoc}*/
    @Override
	public int getChildCount() {
	return colonnes.size();
    }
/**{@inheritDoc}*/
    @Override
	public int getNiveau() {
	return 1;
    }
/**{@inheritDoc}*/
    @Override
	public String getNomHierarchie() {
	return getNom();
    }
/**{@inheritDoc}*/
    @Override
	public IHierarchieSchema getParent() {
	return categorie;
    }
/**{@inheritDoc}*/
    @Override
	public int getPosition() {
	return categorie.getPosFichier(nom);
    }
/**{@inheritDoc}*/
    @Override
	public boolean isCategorie() {
	return false;
    }
/**{@inheritDoc}*/
    @Override
	public boolean isColonne() {
	return false;
    }
/**{@inheritDoc}*/
    @Override
	public boolean isContrainte() {
	return false;
    }
/**{@inheritDoc}*/
    @Override
	public boolean isFichier() {
	return true;
    }

}
