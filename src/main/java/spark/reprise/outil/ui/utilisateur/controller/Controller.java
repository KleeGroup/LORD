package spark.reprise.outil.ui.utilisateur.controller;

import java.awt.Component;

import spark.reprise.outil.ui.utilisateur.model.Model;

/**
 * Classe mere des controlleur de l'interface utilisateur.
 * @param <C> le view du controlleur
 * @param <M> le modele
 */
public abstract class Controller<C extends Component, M extends Model> {
	protected C view;
	protected M model;
}
