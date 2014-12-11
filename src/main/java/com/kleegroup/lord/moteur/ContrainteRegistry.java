package com.kleegroup.lord.moteur;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.kleegroup.lord.moteur.contraintes.ContrainteMultiColFonctionsSpecifiques;
import com.kleegroup.lord.moteur.contraintes.ContrainteMultiColUnique;
import com.kleegroup.lord.moteur.contraintes.ContrainteMultiColUniqueNullEqAny;

public class ContrainteRegistry {

	public enum ContrainteMulticolEnum {
		UNIQUE("Unique", ContrainteMultiColUnique.class.getName()),
		UNIQUE_NULL_EQ_ANY("UniqueNullEqAny", ContrainteMultiColUniqueNullEqAny.class.getName()),
		EQUALTY("test", ContrainteMultiColFonctionsSpecifiques.class.getName()),
		EMPTY_STRING("notnull", ContrainteMultiColFonctionsSpecifiques.class.getName()),
		SHORTER_THAN("tailleInf", ContrainteMultiColFonctionsSpecifiques.class.getName());

		private final String implementation;
		private final String fonction;
		private final static Map<String, String> implementationByFunction;
		private final static Class<?>[] constructorParamTypes = {String.class, String.class, String.class, String[].class};
		private final static Class<?>[] validatorParamTypes = {String.class, String[].class};

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

		public static ContrainteMultiCol createConstraint(String id, String errTemplate, String nomFonction, String... cols) {
			Object[] params = { id, errTemplate, nomFonction, cols };
			ContrainteMultiCol instance = null;
			try {
				instance = (ContrainteMultiCol) Class.forName(implementationByFunction.get(nomFonction)).getConstructor(constructorParamTypes).newInstance(params);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return instance;
		}

		public static boolean isValide(String nomFonction, String... cols) {
			Object[] params = { nomFonction, cols };
			Boolean o = Boolean.FALSE;
			try {
				Method method = Class.forName(implementationByFunction.get(nomFonction)).getMethod("isValide", validatorParamTypes);
				o = (Boolean)method.invoke(null, params);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return o.booleanValue();
		}

		static {
			implementationByFunction = new HashMap<String, String>();
			for (ContrainteMulticolEnum contrainte : ContrainteMulticolEnum.values()) {
				implementationByFunction.put(contrainte.getFonction(), contrainte.getImplementation());
			}
		}
	}
}
