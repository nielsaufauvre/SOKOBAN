package Modele;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Lit les niveaux Sokoban depuis un flux au format standard XSB.
 *
 * Chaque instance maintient sa propre position dans le flux.
 * Pour lire le niveau N, appeler lisProchainNiveau() N fois
 * (ou utiliser Jeu.niveau() qui gère l'itération).
 */
public class LecteurNiveaux {

    private Scanner scanner;

    /**
     * Lit le prochain niveau du flux. Le Scanner est initialisé à la première
     * invocation et réutilisé pour les appels suivants sur la même instance.
     *
     * @return le niveau lu, ou null si le flux est épuisé.
     */
    public Niveau lisProchainNiveau(InputStream input) {
        if (scanner == null) {
            scanner = new Scanner(input);
        }

        ArrayList<String> lignesNiveau = new ArrayList<>();
        String nom = "";

        while (scanner.hasNextLine()) {
            String ligne = scanner.nextLine();

            if (ligne.startsWith(";")) {
                nom = ligne.substring(1).trim();
                continue;
            }
            if (ligne.isEmpty()) {
                break; // Séparateur de niveaux
            }
            lignesNiveau.add(ligne);
        }

        if (lignesNiveau.isEmpty()) {
            return null; // Flux épuisé
        }

        int nbLignes   = lignesNiveau.size();
        int nbColonnes = lignesNiveau.stream().mapToInt(String::length).max().orElse(0);

        Niveau n = new Niveau(nbLignes, nbColonnes);
        n.fixeNom(nom);

        for (int i = 0; i < nbLignes; i++) {
            String ligne = lignesNiveau.get(i);
            for (int j = 0; j < ligne.length(); j++) {
                switch (ligne.charAt(j)) {
                    case '#': n.ajouteMur(i, j);                                          break;
                    case '@': n.ajoutePousseur(i, j); n.setPositionPousseur(i, j);        break;
                    case '+': n.ajoutePousseur(i, j); n.ajouteBut(i, j);
                        n.setPositionPousseur(i, j);                                break;
                    case '$': n.ajouteCaisse(i, j);                                       break;
                    case '*': n.ajouteCaisse(i, j);   n.ajouteBut(i, j);                  break;
                    case '.': n.ajouteBut(i, j);                                           break;
                    case ' ': n.videCase(i, j);                                            break;
                    // Les autres caractères sont ignorés silencieusement
                }
            }
        }

        return n;
    }
}