package Controleur;

import javax.swing.*;
import Vue.NiveauGraphique;
import Modele.Niveau;
import java.util.ArrayDeque;
import java.util.Deque;
import Modele.Solveur;
import java.util.List;

/**
 * Classe centrale qui contient toute la logique de déplacement et de
 * changement de niveau. EcouteurDeClavier et EcouteurDeSouris délèguent
 */


public class GameController {

    private static final int TAILLE_CASE = 28;

    private JFrame frame;
    private NiveauGraphique niveauGraphique;
    private Solveur solveur;

    /** Historique des états pour le <refaire> ctrl+Z*/

    private final Deque<Niveau> historique = new ArrayDeque<>();

    public GameController(JFrame frame, NiveauGraphique niveauGraphique) {
        this.frame = frame;
        this.niveauGraphique = niveauGraphique;
        this.solveur = new Solveur(); //creation d'une classe solveur
    }

    public NiveauGraphique getNiveauGraphique() {
        return niveauGraphique;
    }

    public int getTailleCase() {
        return TAILLE_CASE;
    }

    
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
        frame.setTitle("Sokoban — Niveau " + prochain);

        EcouteurDeClavier clavier = new EcouteurDeClavier(frame, niveauGraphique, this);
        EcouteurDeSouris souris  = new EcouteurDeSouris(this);

        niveauGraphique.addKeyListener(clavier);
        niveauGraphique.addMouseListener(souris);
        niveauGraphique.setFocusable(true);
        SwingUtilities.invokeLater(() -> niveauGraphique.requestFocusInWindow());


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

     private void afficherEcranFinNiveau() {
        JOptionPane.showMessageDialog(
                frame,
                "Félicitations ! Vous avez terminé ce Niveau!",
                "Niveau terminé 🎉 ",
                JOptionPane.INFORMATION_MESSAGE
        );
       
    }

     private void afficherEcranImpossible() {
        JOptionPane.showMessageDialog(
                frame,
                "IMPOSSIBLE DE RESOUDRE CE NIVEAU",
                "OUUUUPS😬 ",
                JOptionPane.INFORMATION_MESSAGE
        );
       
    }


    //la resolution automatique
    public void resolutionAutomatique(){
        List<Niveau> niveaux = solveur.resoluble(niveauGraphique.niveau);
        if(niveaux==null){
            if (niveaux == null) {
            SwingUtilities.invokeLater(() -> afficherEcranImpossible());
            passerAuNiveauSuivant();
            return;
            }
        }

        final int[] index = {0};

        Timer timer = new javax.swing.Timer(300, null);
        timer.addActionListener(e -> {
            if (index[0] >= niveaux.size()) {
                timer.stop();
                afficherEcranFinNiveau();
                passerAuNiveauSuivant();
                return;
            }

            Niveau affichage = (Niveau) niveaux.get(index[0]).clone();
            affichage.unPersonnage();

            niveauGraphique.niveau = affichage;
            niveauGraphique.pousseur_i = affichage.getPousseurI();
            niveauGraphique.pousseur_j = affichage.getPousseurJ();
            niveauGraphique.repaint();
            
            index[0]++;
        });

        timer.start();
    }
}