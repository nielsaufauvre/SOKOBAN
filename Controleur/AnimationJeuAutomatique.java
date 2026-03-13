package Controleur;

import Vue.NiveauGraphique;
import javax.swing.Timer;
import java.util.Random;

/**
 * Lance des coups aléatoires à intervalle régulier (mode démo).
 * Appuyer sur 'I' pour basculer l'animation on/off.
 */
public class AnimationJeuAutomatique {

    private final NiveauGraphique vue;
    private final EcouteurDeClavier controleur;
    private final Timer timer;
    private final Random random = new Random();

    private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    public AnimationJeuAutomatique(NiveauGraphique vue, EcouteurDeClavier controleur) {
        this.vue = vue;
        this.controleur = controleur;
        this.timer = new Timer(500, e -> jouerCoupAleatoire());
    }

    public void basculer() {
        if (timer.isRunning()) timer.stop(); else timer.start();
    }

    public boolean estActif() {
        return timer.isRunning();
    }

    private void jouerCoupAleatoire() {
        int[] d = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        // On délègue au contrôleur : il vérifiera si le coup est valide
        controleur.deplacer(d[0], d[1]);
    }
}