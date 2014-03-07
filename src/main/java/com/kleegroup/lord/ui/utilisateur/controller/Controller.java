package com.kleegroup.lord.ui.utilisateur.controller;

import java.awt.Component;

import com.kleegroup.lord.ui.utilisateur.model.Model;

/**
 * Classe mere des controlleur de l'interface utilisateur.
 * @param <C> le view du controlleur
 * @param <M> le modele
 */
public abstract class Controller<C extends Component, M extends Model> {
	protected C view;
	protected M model;
}
