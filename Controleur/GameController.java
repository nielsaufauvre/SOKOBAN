package Controleur;

import javax.swing.*;
import Vue.NiveauGraphique;
import Modele.Niveau;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Classe centrale qui contient toute la logique de déplacement et de
 * changement de niveau. EcouteurDeClavier et EcouteurDeSouris délèguent
 * ici pour éviter la duplication de code.
 */
public class GameController {

    private static final int TAILLE_CASE = 28;

    private JFrame frame;
    private NiveauGraphique niveauGraphique;

    /** Historique des états pour le undo (Ctrl+Z). */
    private final Deque<Niveau> historique = new ArrayDeque<>();

    public GameController(JFrame frame, NiveauGraphique niveauGraphique) {
        this.frame = frame;
        this.niveauGraphique = niveauGraphique;
    }

    public NiveauGraphique getNiveauGraphique() {
        return niveauGraphique;
    }

    public int getTailleCase() {
        return TAILLE_CASE;
    }

    // -----------------------------------------------------------------------
    // Helpers partagés (étaient dupliqués dans les deux écouteurs)
    // -----------------------------------------------------------------------

    public boolean estAccessible(int i, int j) {
        return niveauGraphique.niveau.estVide(i, j) ||
                (niveauGraphique.niveau.aBut(i, j) && !niveauGraphique.niveau.aCaisse(i, j));
    }

    public void libereAncienneCase(int pi, int pj) {
        if (niveauGraphique.niveau.aBut(pi, pj)) {
            niveauGraphique.niveau.retirePousseur(pi, pj);
        } else {
            niveauGraphique.niveau.videCase(pi, pj);
        }
    }

    // -----------------------------------------------------------------------
    // Tentative de déplacement (appelée par les deux écouteurs)
    // -----------------------------------------------------------------------

    public void tenterDeplacement(int di, int dj) {
        int pi = niveauGraphique.niveau.getPousseurI();
        int pj = niveauGraphique.niveau.getPousseurJ();
        int ni = pi + di;
        int nj = pj + dj;

        if (estAccessible(ni, nj)) {
            sauvegarderEtat();
            appliquerMouvement(pi, pj, ni, nj, di, dj, false);
        } else if (niveauGraphique.niveau.aCaisse(ni, nj)) {
            int ci = ni + di;
            int cj = nj + dj;
            if (estAccessible(ci, cj)) {
                sauvegarderEtat();
                niveauGraphique.niveau.retireCaisse(ni, nj);
                niveauGraphique.niveau.ajouteCaisse(ci, cj);
                appliquerMouvement(pi, pj, ni, nj, di, dj, true);
            }
        }
    }

    private void appliquerMouvement(int pi, int pj, int ni, int nj, int di, int dj, boolean pousse) {
        libereAncienneCase(pi, pj);
        niveauGraphique.niveau.ajoutePousseur(ni, nj);
        niveauGraphique.niveau.setPositionPousseur(ni, nj);
        niveauGraphique.pousseur_i = ni;
        niveauGraphique.pousseur_j = nj;

        niveauGraphique.demarrerAnimation(di, dj, pousse);

        if (niveauGraphique.niveau.estResolu()) {
            Timer t = new Timer(250, e -> passerAuNiveauSuivant());
            t.setRepeats(false);
            t.start();
        }
    }

    // -----------------------------------------------------------------------
    // Undo
    // -----------------------------------------------------------------------

    private void sauvegarderEtat() {
        historique.push((Niveau) niveauGraphique.niveau.clone());
    }

    public void annuler() {
        if (historique.isEmpty()) return;
        Niveau ancien = historique.pop();
        niveauGraphique.niveau = ancien;
        niveauGraphique.pousseur_i = ancien.getPousseurI();
        niveauGraphique.pousseur_j = ancien.getPousseurJ();
        niveauGraphique.repaint();
    }

    // -----------------------------------------------------------------------
    // Passage au niveau suivant (était dupliqué 3 fois)
    // -----------------------------------------------------------------------

    private void passerAuNiveauSuivant() {
        int prochain = niveauGraphique.numNiveau + 1;

        // Vérifier s'il existe un niveau suivant
        if (!NiveauGraphique.niveauExiste(prochain)) {
            afficherEcranFin();
            return;
        }

        historique.clear();
        frame.remove(niveauGraphique);
        niveauGraphique = new NiveauGraphique(prochain);
        frame.add(niveauGraphique);

        // Recréer les écouteurs sur le nouveau composant
        EcouteurDeClavier clavier = new EcouteurDeClavier(frame, niveauGraphique, this);
        EcouteurDeSouris souris  = new EcouteurDeSouris(this);

        niveauGraphique.addKeyListener(clavier);
        niveauGraphique.addMouseListener(souris);
        niveauGraphique.setFocusable(true);
        SwingUtilities.invokeLater(() -> niveauGraphique.requestFocusInWindow());

        // Redimensionner la fenêtre au nouveau niveau
        frame.revalidate();
        frame.repaint();
        frame.pack();
    }

    private void afficherEcranFin() {
        JOptionPane.showMessageDialog(
                frame,
                "Félicitations ! Vous avez terminé tous les niveaux !",
                "Jeu terminé",
                JOptionPane.INFORMATION_MESSAGE
        );
        System.exit(0);
    }
}