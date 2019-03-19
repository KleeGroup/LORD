package com.kleegroup.lord.ui;

import java.awt.EventQueue;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.UIManager;

import org.apache.log4j.PropertyConfigurator;

/**
 * La classe qui démarre l'application.
 */
public abstract class UILauncher {
	protected File execDir;

	/**
	 * Démarre le programme.
	 */
	public final void run() {
		//	    URL url = getClass().getClassLoader().getResource(getClass().getPackage().getName().replace('.', '/'));
		//	    String file = url.getFile();
		//	    if ("jar".equals(url.getProtocol())) {
		//		url = new URL(file.substring(0, file.lastIndexOf('!')));
		//		execDir = new File(url.getFile()).getParentFile();
		//	    }
		//	    
		configureLog4j();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			/** Ne fait rien */
			e.getMessage();
		}

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				montrerFenetrePrincipale();
			}
		});
	}

	private void configureLog4j() {
		final File log4j = new File(getExecDir(), "log4j.properties");
		if (log4j.exists()) {
			PropertyConfigurator.configure(log4j.getAbsolutePath());
		} else {
			URL locConfDefault = ClassLoader.getSystemClassLoader().getResource("log4j.default.properties");
			if (locConfDefault != null) {
				PropertyConfigurator.configure(locConfDefault);
			}
		}
	}

	protected abstract void montrerFenetrePrincipale();

	protected File getExecDir() {
		/**
		 * http://weblogs.java.net/blog/kohsuke/archive/2007/04/how_to_convert.html  
		 * Pour corriger un bug. Le programme ne marchait pas 
		 * si le repertoire contenait un espace (%20) dans son chemin.
		 * */

		final URL location = getClass().getProtectionDomain().getCodeSource().getLocation();
		File parent;
		try {
			parent = new File(location.toURI());
		} catch (URISyntaxException e1) {
			parent = new File(location.getPath());
		}
		return parent.getParentFile();
	}
}
