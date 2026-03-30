package Modele;

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

   

    public int getPriorite(){
        return this.priorite;
    }

    public Couple getCouple(){
        return  this.c;
    }
}

class NiveauPriorite {
    Niveau niveau;
    int priorite;

    public NiveauPriorite(Niveau niveau, int priorite) {
        this.niveau = niveau;
        this.priorite = priorite;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public int getPriorite() {
        return priorite;
    }
}

public class Solveur {

    int INFINI = 1000000;
    int MUR = -1;

    //le constructeur
    public Solveur(){

    }

    public int nombrePositions(Niveau depart) {
        Niveau debut = (Niveau)depart.clone();
        debut.clonePersonnage();
        Deque<Niveau> actifs = new ArrayDeque<>();
        Map<String, Niveau> dejavu = new HashMap<String, Niveau>();

        String codeInitial = debut.code();
        actifs.push(debut);
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
    //on a implemente ça avant l'heuristique
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

    /*fonction pour avoir la liste des voisisn de (i , j)*/
    public List<Couple> getVoisins(int i , int j) {
        List<Couple> listVoisins = new ArrayList<Couple>();
        Couple c1 = new Couple(i - 1, j);
        Couple c2 = new Couple(i + 1, j);
        Couple c3 = new Couple(i, j - 1);
        Couple c4 = new Couple(i, j + 1);

        listVoisins.add(c1);
        listVoisins.add(c2);
        listVoisins.add(c3);
        listVoisins.add(c4);

        return listVoisins;
    }

    //Verifier toujours qu'on est dans la grille parce que pour les cases en extrémité
    // on risque de sortir
    public boolean estDansGrille(Niveau depart, int i, int j) {
        return i >= 0 && i < depart.nbLignes && j >= 0 && j < depart.nbColonnes;
    }

    public int[][] heuristique(Niveau depart){
        //le tableau à retourner 
        int [][] dist = new int [depart.nbLignes][depart.nbColonnes];

        Deque<Couple> actifs = new ArrayDeque<Couple>();

        for (int i = 0; i < depart.nbLignes; i++ ){
            for (int j = 0; j < depart.nbColonnes; j++){
                if (depart.aBut(i,j)){
                    dist[i][j] = 0;
                    Couple couple = new Couple(i,j);
                    actifs.add(couple);//initialisation à 0
                }
                else if (depart.aMur(i,j)){
                    dist[i][j] = MUR; //-1 pour les murs
                }
                else {
                    dist[i][j] = INFINI; //valeur qu'on suppose à infini
                }
            }
        }

        while(!actifs.isEmpty()){
            Couple element = actifs.remove();
            int ligne = element.getI();
            int colonne = element.getJ();

            //recuperer la liste des voisisn 
            List<Couple> voisins = getVoisins(ligne , colonne);

            for(int i = 0; i < voisins.size(); i++){
                int ligneVoisin = voisins.get(i).getI();
                int colonneVoisin = voisins.get(i).getJ();

                if (estDansGrille(depart, ligneVoisin, colonneVoisin)
                        && dist[ligneVoisin][colonneVoisin] == INFINI) {
                    dist[ligneVoisin][colonneVoisin] = dist[ligne][colonne] + 1;
                    actifs.add(voisins.get(i));
                }
            }
        }

        return dist;
    }

   public int coutHeuristique(Niveau courant, int[][] distBut) {
    int cout = 0;

    List<int[]> caisses = courant.coordonneesCaisses();

    for (int[] caisse : caisses) {
        int i = caisse[0];
        int j = caisse[1];

        if (distBut[i][j] == INFINI) {
            return INFINI;
        }

        cout += distBut[i][j];
    }

    return cout;
    }

    public List<Niveau> aStar(Niveau depart) {
        Niveau debut = (Niveau) depart.clone();
        debut.clonePersonnage();

        int[][] distBut = heuristique(debut);

        PriorityQueue<NiveauPriorite> actifs = new PriorityQueue<>(
                Comparator.comparingInt(NiveauPriorite::getPriorite)
        );

        Map<String, Niveau> dejavu = new HashMap<>(); //on associe à chaque état son predecesseur
        Map<String, Integer> distance = new HashMap<>(); //à chaque état son coût

       //initialisation
        String codeInitial = debut.code();
        distance.put(codeInitial, 0);
        dejavu.put(codeInitial, null);

        int hInitial = coutHeuristique(debut, distBut);
        actifs.add(new NiveauPriorite(debut, hInitial));

        while (!actifs.isEmpty()) {
            Niveau courant = actifs.poll().getNiveau();
            String codeCourant = courant.code();
            int gCourant = distance.get(codeCourant); //c'est le cout parcouru depuis le départ(la fonction g)

            if (courant.estResolu()) {
                System.out.print("Nombre itérations: " + dejavu.size());
                return chemin(dejavu, courant);
            }

            for (Niveau suivant : courant.cartesAccessibles()) {
                String codeSuivant = suivant.code();
                int nouveauG = gCourant + 1; //la nouvelle distance parcouruue depuis le debut

                if (!distance.containsKey(codeSuivant) || nouveauG < distance.get(codeSuivant)) {
                    //mise à jour si jamais vu ou je trouve un chemin meilleur
                    distance.put(codeSuivant, nouveauG);
                    dejavu.put(codeSuivant, courant);

                    int h = coutHeuristique(suivant, distBut);
                    int f = nouveauG + h; //ça revient a  la formule f=g+h (f etant la prioritz dans la file aussi )

                    actifs.add(new NiveauPriorite(suivant, f));
                }
            }
        }

        System.out.print("Impossible, Nombre itérations: " + dejavu.size());
        return null;
    }
}