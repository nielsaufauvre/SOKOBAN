package Modele;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/* utiliser lisProchainNiveau pour lire le niveau suivant*/

public class LecteurNiveaux {

    private Scanner scanner;

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
                break; 
            }
            lignesNiveau.add(ligne);
        }

        if (lignesNiveau.isEmpty()) {
            return null;
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
                    
                }
            }
        }

        return n;
    }
}