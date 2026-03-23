package Modele;

import java.util.PriorityQueue;

import java.util.*;

class Couple{
    int i;
    int j;

    public Couple(int i, int j){
        this.i = i;
        this.j = j;
    }

    public int getI(){
        return i;

    }
    public int getJ(){
        return j;
    }
}

class CouplePriorite{
    Couple c;
    int priorite;

    public CouplePriorite(Couple c, int priorite){
        this.c = c;
        this.priorite = priorite;
    }
    public CouplePriorite(){
    }

    public int getPriorite(){
        return this.priorite;
    }

    public Couple getCouple(){
        return  this.c;
    }

}

public class Solveur {
    
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

    //utilisation du plus court chemin ici
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

    public List<Couple> getVoisins(int i , int j) {
        List<Couple> listVoisins = new ArrayList<Couple>();
        Couple c1 = new Couple(i - 1, j);
        Couple c1 = new Couple(i + 1, j);
        Couple c1 = new Couple(i, j - 1);

        Couple c1 = new Couple(i, j + 1);
        listVoisins.add(c1)
    }
    public void heuristique(Niveau depart){
        int [][] tab = new int [depart.nbLignes][depart.nbColonnes];

        List<CouplePriorite> actifs = new ArrayList<CouplePriorite>();

        for (int i = 0; i <depart.nbLignes; i++ ){
            for (int j = 0; j<depart.nbColonnes; j++){
                if (depart.aBut(i,j)){
                    tab[i][j] = 0;
                    Couple couple = new Couple(i,j);
                    actifs.add(new CouplePriorite(couple,0));

                }
                else if (depart.aMur(i,j)){
                    tab[i][j] = -1;
                }
                else {
                    tab[i][j] = 1000000;
                }
            }
        }

        while(!actifs.isEmpty()){
            //TODO: recherche de la priorité minimale
            int max = 1000000;
            int indice = -1;
            for (int i=0; i<actifs.size(); i++){
               if(actifs.get(i).priorite < max){
                   max = actifs.get(i).priorite;
                   indice = i;
               }
            }
            //extraction
            Couple element = actifs.get(indice).getCouple();
             int ligne = element.getI();
             int colonne = element.getJ();

             int ligneVoisin;
             int colonneVoisin;
             for(int i=0; i<4;i++){
                 //vsin HAUT
                 lig

             }




            for (){
                //TODO: rajouter voisine avec distance infinie à actifs



            }
        }

    }
        


   

   


}