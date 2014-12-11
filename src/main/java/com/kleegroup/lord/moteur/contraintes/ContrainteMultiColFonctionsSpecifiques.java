package com.kleegroup.lord.moteur.contraintes;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;

import com.kleegroup.lord.moteur.ContrainteMultiCol;

/**
 * Cette classe est responsable d'implémenter les vérifications spécifiques.
 */
public class ContrainteMultiColFonctionsSpecifiques extends ContrainteMultiCol {
	//la méthode qu'appelera la contrainte pour effectuer la verification
	private Method method = null;

	/**
	 * @param id l'identifiant de la contrainte
	 * @param errTemplate le template du message d'erreur
	 * @param nomFonction le nom de fonction utilisée
	 * @param cols les noms des colonnes a utiliser parmi la liste col
	 */
	public ContrainteMultiColFonctionsSpecifiques(String id, String errTemplate, String nomFonction, String... cols) {
		super(id, errTemplate, cols);
		Class<?>[] paramTypes = new Class[cols.length];
		for (int i = 0; i < paramTypes.length; i++) {
			paramTypes[i] = String.class;
		}

		try {
			method = FonctionsSpecifiques.class.getMethod(nomFonction, paramTypes);
		} catch (SecurityException e) {
			LOGAPPLI.error(e);
		} catch (NoSuchMethodException e) {
			LOGAPPLI.error("Erreur : Fonction non trouvée " + "id=" + id + ".");
		}

	}

	
	/**
	 * Teste si la fonction est valide. La fonction est valide, si elle existe, si tous
	 * ses paramètres sont de type String et si le nombre de colonnes désignés paramètres
	 * est égale au nombre des paramètres accepté par la fonction.
	 * @param nomFonction le nom de la fonction
	 * @param cols les colonnes désignées paramètres de la fonction
	 * @return True si la fonction est valide, false sinon.
	 */
	public static boolean isValide(String nomFonction, String... cols) {
		for (Method m : FonctionsSpecifiques.class.getMethods()) {
			if (m.getName().equals(nomFonction) && isAllParmsString(m) && m.getParameterTypes().length == cols.length) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean estConforme(String[] valeurs) {
		try {
			//appel de la fonction
			//true=pas d'erreur , false = erreur
			if (method != null) {
				return (Boolean) method.invoke(null, (Object[]) valeurs);
			}

			return true;

		} catch (Exception e) {
			/* echec de l'appel de la fonction
			 * causes probables: nbre de parametre incorrecte, param == null, droits d'accÃ¨s(private,protected method)
			 */
			String msg = "";
			for (int i = 0; i < valeurs.length; i++) {
				msg += i + " : " + valeurs[i] + "\n";
			}
			LOGAPPLI.error("Erreur lors de l'invocation d'une methode multicolonne " + "id=" + id + ".");
			if (method != null) {
				LOGAPPLI.error("nom de la fonction=" + method.getName());
			}
			LOGAPPLI.error(msg);

			return true;
		}
	}

	/**
	 * renvoie les noms des fonctions utilisables pour construire une ContrainteMultiCol.
	 * @return la liste des noms des fontions disponibles 
	 */
	public static Collection<String> getMethods() {
		Collection<String> f = new ArrayList<>();
		for (Method m : FonctionsSpecifiques.class.getMethods()) {
			if (Modifier.isStatic(m.getModifiers()) && Modifier.isPublic(m.getModifiers()) && isAllParmsString(m)) {
				//les fonctions doivent ête statiques et publiques
				f.add(m.getName());
			}
		}
		return f;
	}

	private static boolean isAllParmsString(Method m) {
		for (Class<?> c : m.getParameterTypes()) {
			if (c != String.class) {
				return false;
			}
		}
		return true;
	}

	/**
	 * renvoie le nom de la fonction de verification.
	 * @return le nom de la fonction de verification
	 */
	@Override
	public String getNomFonction() {
		if (method == null) {
			return "";
		}
		return method.getName();
	}
}
