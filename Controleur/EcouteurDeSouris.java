package Controleur;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EcouteurDeSouris implements MouseListener {

	private final GameController controller;

	public EcouteurDeSouris(GameController controller) {
		this.controller = controller;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int tailleCase = controller.getTailleCase();
		int ni = e.getY() / tailleCase;
		int nj = e.getX() / tailleCase;
		int pi = controller.getNiveauGraphique().niveau.getPousseurI();
		int pj = controller.getNiveauGraphique().niveau.getPousseurJ();

		int di = ni - pi;
		int dj = nj - pj;

		// Autoriser uniquement les déplacements d'une case (4 directions)
		if (Math.abs(di) + Math.abs(dj) != 1) return;

		controller.tenterDeplacement(di, dj);
	}

	@Override public void mouseClicked(MouseEvent e)  {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e)  {}
	@Override public void mouseExited(MouseEvent e)   {}
}