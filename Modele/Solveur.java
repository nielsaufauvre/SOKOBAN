package Modele;

import java.util.*;

public class Solveur {

    public int nombre_positions(Niveau depart) {
        depart.clonePersonnage();

        Deque<Niveau> actifs = new ArrayDeque<>();
        Map<String, Niveau> dejavu = new HashMap<String, Niveau>();

        String codeInitial = depart.code();
        actifs.push(depart);
        dejavu.put(codeInitial, null);

        while (!actifs.isEmpty()) {
            Niveau courant = actifs.pop();

            for (Niveau suivant : courant.cartesAccessibles()) {
                String codeSuivant = suivant.code();
                if (!dejavu.containsKey(codeSuivant)) {
                    dejavu.put(codeSuivant, courant);
                    actifs.push(suivant);
                }
            }
        }

        return dejavu.size();
    }


    public List<Niveau> resoluble(Niveau depart) {
        depart.clonePersonnage();

        Deque<Niveau> actifs = new ArrayDeque<>();
        Map<String, Niveau> dejavu = new HashMap<String, Niveau>();

        String codeInitial = depart.code();
        actifs.push(depart);
        dejavu.put(codeInitial, null);

        while (!actifs.isEmpty()) {
            Niveau courant = actifs.pop();

            if (courant.estResolu()){
                System.out.print("Nombre itérations : " + dejavu.size());
                return chemin(dejavu,courant);
            }
            for (Niveau suivant : courant.cartesAccessibles()) {
                String codeSuivant = suivant.code();

                if (!dejavu.containsKey(codeSuivant)) {
                    dejavu.put(codeSuivant, courant);
                    actifs.push(suivant);
                }
            }
        }

        System.out.print("Impossible, Nombre itérations : " + dejavu.size());
        return null;
    }

    public List<Niveau> chemin(Map<String,Niveau> dejaVu, Niveau carteGagnante){
        List<Niveau> chemin = new ArrayList<>();
        Niveau courant = carteGagnante;

        while (courant != null) {
            chemin.add(0,courant);
            courant = dejaVu.get(courant.code());
        }

        return chemin;
    }


}