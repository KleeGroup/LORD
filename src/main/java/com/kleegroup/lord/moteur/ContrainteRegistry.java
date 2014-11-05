package com.kleegroup.lord.moteur;

import java.util.HashMap;
import java.util.Map;

import com.kleegroup.lord.moteur.contraintes.ContrainteMultiColFonctionsSpecifiques;
import com.kleegroup.lord.moteur.contraintes.ContrainteMultiColUnique;

public class ContrainteRegistry {

	public enum ContrainteMulticolEnum {
		UNIQUE("Unique", ContrainteMultiColUnique.class.getName()),
		EQUALTY("test", ContrainteMultiColFonctionsSpecifiques.class.getName()),
		EMPTY_STRING("notnull", ContrainteMultiColFonctionsSpecifiques.class.getName()),
		SHORTER_THAN("tailleInf", ContrainteMultiColFonctionsSpecifiques.class.getName());

		private final String implementation;
		private final String fonction;
		private final static Map<String, String> implementationByFunction;
		private final static Class<?>[] paramTypes = {String.class, String.class, String.class, String[].class};

		ContrainteMulticolEnum(String fonction, String implementation) {
			this.fonction = fonction;
			this.implementation = implementation;
		}

		public String getFonction() {
			return fonction;
		}

		private String getImplementation() {
			return implementation;
		}

		public static ContrainteMultiCol getInstance(String id, String errTemplate, String nomFonction, String... cols) {
			Object[] params = { id, errTemplate, nomFonction, cols };
			ContrainteMultiCol instance = null;
			try {
				instance = (ContrainteMultiCol) Class.forName(implementationByFunction.get(nomFonction)).getConstructor(paramTypes).newInstance(params);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return instance;
		}

		static {
			implementationByFunction = new HashMap<String, String>();
			for (ContrainteMulticolEnum contrainte : ContrainteMulticolEnum.values()) {
				implementationByFunction.put(contrainte.getFonction(), contrainte.getImplementation());
			}
		}
	}
}
