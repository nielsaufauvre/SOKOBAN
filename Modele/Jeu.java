package Modele;

import java.io.InputStream;

/*classe jeu qu'on utilise pour lire les niveaux et connaitre le niveau actuel*/

public class Jeu {

    private final int niveauActuel;

    public Jeu(int numero_niveau) {
        this.niveauActuel = numero_niveau;
    }

    
    public Niveau niveau(InputStream input, int numero_niveau) {
        LecteurNiveaux lecteur = new LecteurNiveaux();

        // Sauter les (numero_niveau - 1) premiers niveaux
        for (int i = 0; i < numero_niveau - 1; i++) {
            Niveau saut = lecteur.lisProchainNiveau(input);
            if (saut == null) {
                return null; 
            }
        }

        return lecteur.lisProchainNiveau(input);
    }

    public int getNiveauActuel() {
        return niveauActuel;
    }
}