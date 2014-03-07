package com.kleegroup.lord.ui.utilisateur.controller;

import java.io.FileNotFoundException;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;

import com.kleegroup.lord.ui.utilisateur.model.Model;
import com.kleegroup.lord.ui.utilisateur.view.FrameBienvenue;

/**
 *	Controlleur pour FrameBienvenue.
 *
 */
public class FrameBienvenueController extends FrameController<FrameBienvenue,Model > {

    /**
     * Construit un controlleur pour FrameBienvenue.
     * @param fpc le controlleur de la fenetre principale.
     */
    public FrameBienvenueController(FenetrePrincipaleUtilisateurController fpc) {
	super(fpc);
    }

    /** {@inheritDoc} */
    @Override
    public boolean canGoBack() {
	return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean canGoNext(){
	try {
		rechargerFichierConf();
		
		}catch (FileNotFoundException e) {
		    
		    JOptionPane.showMessageDialog(null, resourceMap.getString("ErreurChargementConf")+" " +e.getMessage());
		    java.awt.EventQueue.invokeLater(new Runnable() {
			    @Override
				public void run() {
		    fenetrePrincipaleController.quitter();
			    }});
		} catch (JAXBException e) {
		    JOptionPane.showMessageDialog(null, resourceMap.getString("ErreurConfInvalide") +e.getMessage());
		}
		return true;
    }

    /** {@inheritDoc} */
    @Override
    String getName() {
	return "FrameBienvenue";
    }

    /** {@inheritDoc} */
    @Override
    protected FrameBienvenue createView() {
	return new FrameBienvenue();
    }

    /** {@inheritDoc} */
    @Override
    public void activate() {
	    fenetrePrincipale.setEnabledPrecedent(false);
		fenetrePrincipale.setEtape(0);
    }

    private void rechargerFichierConf() throws FileNotFoundException, JAXBException {
	    fenetrePrincipaleController.chargerFichierConf();
	    fenetrePrincipale.clear();
	    fenetrePrincipale.addFrame(getFrame(), getName());
	    setNext(new FrameSelectionCheminsFichiersController(fenetrePrincipaleController));
    }

    /** {@inheritDoc} */
    @Override
    public void deactivate() {
	fenetrePrincipale.setEnabledPrecedent(true);
    }

    @Override
    protected Model createModel() {
	return null;
    }

}
