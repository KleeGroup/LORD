package com.kleegroup.lord.moteur.contraintes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kleegroup.lord.moteur.ContrainteUniCol;

/**
 * Cette contrainte est une contrainte auxiliaire à la contrainte de référence. <br><br>
 * Elle sert 
 * à stocker les valeurs de la colonne référencée dans un HashSet, qui sera utilisé par la 
 * contrainte de référence principale(ContrainteReference) pour déterminer si la 
 * valeur référencée existe ou pas. 
 * @author maazreibi
 *
 */
public class ContrainteReferenceLookup extends ContrainteUniCol {

	protected Set<String> dict = new HashSet<>();

	/**
	 * {@inheritDoc}	  
	 */
	@Override
	public boolean estConforme(final String valeur) {
		dict.add(valeur);
		return true;
	}

	/**
	 * 	Cette méthode permet de d�terminer si une valeur a déjà été rencontrée dans la colonne
	 * référencée.
	 * @param valeur la valeur à tester
	 * @return <code>true</code> si la valeur a déjà été rencontrée, <code>false</code> sinon  
	 */
	public boolean lookup(final String valeur) {
		return dict.contains(valeur);
	}

	/**
	 * {@inheritDoc}	  
	 */
	//	@Override
	//	public String getMessageErreur(){
	//		return "Pas d'erreur";		
	//	}
	/**{@inheritDoc}*/
	@Override
	public List<String> getListeParam() {
		return new ArrayList<>();
	}

	/**{@inheritDoc}*/
	@Override
	public void clean() {
		dict.clear();
	}

	/**Il est inutile de copier cette contrainte. <br>Elle sera automatiquement crée lors 
	 * de l'ajout de la référence en utilisant la fonction 
	 * {@link com.kleegroup.lord.moteur.Fichier#addReference(String, com.kleegroup.lord.moteur.Colonne)}
	 * @return une contrainte de type {@link ContrainteTRUE}
	 * */
	@Override
	public ContrainteTRUE copy() {
		return new ContrainteTRUE();
	}

}
