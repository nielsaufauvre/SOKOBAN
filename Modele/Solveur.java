package Modele;

import java.util.*;

public class Solveur {
    //ATTENTION:Supprime personnage n'a pas ete encore 
    //utilisé , faut savoir où l'utiliser

    //retourne le nombre de carte à accessibles
    //à partir de la carte de depart (cette derniere est incluse)
    //je pense que je viens de me rendre compte que cette fonction 
    //ne nous sera pas utile devant
    
    //le constructeur
    public Solveur(){

    }
    
    public int nombrePositions(Niveau depart) {
        Niveau debut = (Niveau)depart.clone();
        debut.clonePersonnage();
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

    //reconstruction du chemin à partir de la carteGagnante
    //elle nous permet de remonter les predecesseurs
    //la liste de retour contient les chemin à partir du debut.    
    //jusqu'à la carte gagnante
    public List<Niveau> chemin(Map<String,Niveau> dejaVu, Niveau carteGagnante){
        List<Niveau> chemin = new ArrayList<>();
        Niveau courant = carteGagnante;

        while (courant != null) {
            chemin.add(0,courant);
            courant = dejaVu.get(courant.code());
        }

        return chemin;
    }

    //utilisation du plus court chemin ici(je pense)
    //utilisation du plus court chemin ici(je pense)
    public List<Niveau> resoluble(Niveau depart) {
        Niveau debut = (Niveau) depart.clone();
        debut.clonePersonnage();

        Deque<Niveau> actifs = new ArrayDeque<>();
        Map<String, Niveau> dejavu = new HashMap<String, Niveau>();

        String codeInitial = debut.code();
        actifs.add(debut);
        dejavu.put(codeInitial, null);

        while (!actifs.isEmpty()) {
            Niveau courant = actifs.remove();

            if (courant.estResolu()){
                System.out.print("Nombre itérations: " + dejavu.size());
                return chemin(dejavu,courant);
            }
            for (Niveau suivant : courant.cartesAccessibles()) {
                String codeSuivant = suivant.code();

                if (!dejavu.containsKey(codeSuivant)) {
                    dejavu.put(codeSuivant, courant);
                    actifs.add(suivant);
                }
            }
        }

        //le niveau ne peut pas être resolu donc
        System.out.print("Impossible, Nombre itérations: " + dejavu.size());
        return null;
    }
        


   

   


}