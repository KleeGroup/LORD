package com.kleegroup.lord.moteur.contraintes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.kleegroup.lord.moteur.ContrainteUniCol;

/**
 * v�rifie que la valeur repr�sente une date, et qu'elle est au format d�fini.
 * <br><br>
 * Le format par d�faut est "dd/mm/yyyy"
 * @author maazreibi
 *
 */
public class ContrainteTypeDate extends ContrainteUniCol {
	protected SimpleDateFormat formatteur;
	protected String format;

	/**
	 * Construit la contraite avec le format par d�faut "dd/mm/yyyy". 
	 */
	public ContrainteTypeDate() {
		this("dd/MM/yyyy");
	}

	/**
	 * Construit la contrainte avec un format determin�
	 * 
	 *  le format doit �tre conforme aux formats accept�s par <code>
	 *  java.text.SimpleDateFormat</code>,par exemple ("yyyy", "mm/dd/yy"). 
	 * @param format le format de la date
	 */
	public ContrainteTypeDate(String format) {
		super();
		this.format = format;
		formatteur = new SimpleDateFormat(format, Locale.FRANCE);
		formatteur.setLenient(false);
	}

	/** {@inheritDoc} */
	@Override
	public boolean estConforme(final String valeur) {
		try {
			formatteur.parse(valeur);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	/**{@inheritDoc}*/
	@Override
	public List<String> getListeParam() {
		List<String> params = new ArrayList<>();
		params.add(format);
		return params;
	}

	/**
	 * @return le format de la date. 
	 * */
	public String getFormat() {
		return format;
	}

	/**{@inheritDoc}*/
	@Override
	public boolean isContrainteType() {
		return true;
	}

	/**{@inheritDoc}*/
	@Override
	public ContrainteTypeDate copy() {
		return new ContrainteTypeDate(format);
	}
}
