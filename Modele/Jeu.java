package Modele;

import java.io.InputStream;

/**
 * Point d'entrée du modèle. Charge un niveau à partir d'un fichier de niveaux XSB.
 *
 * Correction : on réutilise un seul LecteurNiveaux pour parcourir le flux
 * efficacement au lieu d'en créer un nouveau (et de recommencer depuis le début)
 * à chaque appel.
 */
public class Jeu {

    private final int niveauActuel;

    public Jeu(int numero_niveau) {
        this.niveauActuel = numero_niveau;
    }

    /**
     * Charge le niveau numéro {@code numero_niveau} depuis le flux donné.
     * Le flux doit être positionné au début (pas encore lu).
     */
    public Niveau niveau(InputStream input, int numero_niveau) {
        LecteurNiveaux lecteur = new LecteurNiveaux();

        // Sauter les (numero_niveau - 1) premiers niveaux
        for (int i = 0; i < numero_niveau - 1; i++) {
            Niveau saut = lecteur.lisProchainNiveau(input);
            if (saut == null) {
                return null; // Moins de niveaux que demandé
            }
        }

        return lecteur.lisProchainNiveau(input);
    }

    public int getNiveauActuel() {
        return niveauActuel;
    }
}