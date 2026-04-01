package Controleur;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import Vue.NiveauGraphique;

public class EcouteurDeSouris implements MouseListener {

	private final GameController controller;

	public EcouteurDeSouris(GameController controller) {
		this.controller = controller;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		NiveauGraphique vue = controller.getNiveauGraphique();

		int tailleCase = vue.getTailleCaseActuelle();
		int dx = vue.getDecalageX();
		int dy = vue.getDecalageY();

		// Conversion des coordonnées écran en coordonnées grille (i, j)
		int nj = (e.getX() - dx) / tailleCase;
		int ni = (e.getY() - dy) / tailleCase;

		int pi = vue.niveau.getPousseurI();
		int pj = vue.niveau.getPousseurJ();

		int di = ni - pi;
		int dj = nj - pj;

		// Autoriser uniquement les déplacements adjacents d'une case
		if (Math.abs(di) + Math.abs(dj) != 1) return;

		controller.tenterDeplacement(di, dj);
	}

	@Override public void mouseClicked(MouseEvent e)  {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e)  {}
	@Override public void mouseExited(MouseEvent e)   {}
}