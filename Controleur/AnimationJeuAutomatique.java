package Controleur;

import Vue.NiveauGraphique;
import javax.swing.Timer;
import java.util.Random;

/**
 * RESOLUTION IA
 */
public class AnimationJeuAutomatique {

    private final NiveauGraphique niveauGraphique;
    private final EcouteurDeClavier controleur;
    private final Timer timer;
    //private final Random random = new Random();

    //private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    public AnimationJeuAutomatique(NiveauGraphique vue, EcouteurDeClavier controleur) {
        this.niveauGraphique = vue;
        this.controleur = controleur;
        this.timer = new Timer(500, e -> resoudreNiveau());
    }

    public void basculer() {
        if (timer.isRunning()) timer.stop(); else timer.start();
    }

    public boolean estActif() {
        return timer.isRunning();
    }

    public void resoudreNiveau() {

        controleur.resolution();
    }
}