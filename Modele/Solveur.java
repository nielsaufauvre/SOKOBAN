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

    public CouplePriorite(){
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

    public List<Niveau> cheminPousseur(Niveau etat, int cibleI, int cibleJ) {
        Niveau debut = (Niveau) etat.clone();
        debut.supprimePersonnage();
        debut.ajoutePousseur(etat.pousseurI, etat.pousseurJ);
        debut.setPositionPousseur(etat.pousseurI, etat.pousseurJ);

        Deque<Niveau> actifs = new ArrayDeque<>();
        Map<String, Niveau> dejavu = new HashMap<>();

        String codeInitial = debut.pousseurI + "," + debut.pousseurJ;
        actifs.add(debut);
        dejavu.put(codeInitial, null);

        while (!actifs.isEmpty()) {
            Niveau courant = actifs.remove();

            if (courant.pousseurI == cibleI && courant.pousseurJ == cibleJ) {
                List<Niveau> chemin = new ArrayList<>();
                Niveau c = courant;
                while (c != null) {
                    chemin.add(0, c);
                    String code = c.pousseurI + "," + c.pousseurJ;
                    c = dejavu.get(code);
                }
                return chemin;
            }

            int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
            for (int[] d : dirs) {
                int ni = courant.pousseurI + d[0];
                int nj = courant.pousseurJ + d[1];

                if (ni >= 0 && ni < courant.nbLignes && nj >= 0 && nj < courant.nbColonnes
                        && !courant.aMur(ni, nj) && !courant.aCaisse(ni, nj)) {
                    String code = ni + "," + nj;
                    if (!dejavu.containsKey(code)) {
                        Niveau suivant = courant.deplacerPousseur(ni, nj);
                        dejavu.put(code, courant);
                        actifs.add(suivant);
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    public List<Niveau> chemin(Map<String, Niveau> dejaVu, Niveau carteGagnante) {
        List<Niveau> coupsDePousse = new ArrayList<>();
        Niveau courant = carteGagnante;

        while (courant != null) {
            coupsDePousse.add(0, courant);
            courant = dejaVu.get(courant.code());
        }

        List<Niveau> cheminComplet = new ArrayList<>();

        for (int k = 0; k < coupsDePousse.size() - 1; k++) {
            Niveau avant = coupsDePousse.get(k);
            Niveau apres = coupsDePousse.get(k + 1);

            List<int[]> caissesAvant = avant.coordonneesCaisses();
            List<int[]> caissesApres = apres.coordonneesCaisses();

            int caisseBougeeI = -1, caisseBougeeJ = -1;
            int pousseurCibleI = -1, pousseurCibleJ = -1;

            for (int[] ca : caissesAvant) {
                boolean presenteApres = false;
                for (int[] cb : caissesApres) {
                    if (ca[0] == cb[0] && ca[1] == cb[1]) { presenteApres = true; break; }
                }
                if (!presenteApres) {
                    caisseBougeeI = ca[0];
                    caisseBougeeJ = ca[1];
                }
            }

            for (int[] cb : caissesApres) {
                boolean presenteAvant = false;
                for (int[] ca : caissesAvant) {
                    if (cb[0] == ca[0] && cb[1] == ca[1]) { presenteAvant = true; break; }
                }
                if (!presenteAvant) {
                    pousseurCibleI = caisseBougeeI + (caisseBougeeI - cb[0]);
                    pousseurCibleJ = caisseBougeeJ + (caisseBougeeJ - cb[1]);
                }
            }

            Niveau etatPourBFS = (Niveau) avant.clone();
            etatPourBFS.supprimePersonnage();
            etatPourBFS.ajoutePousseur(avant.pousseurI, avant.pousseurJ);
            etatPourBFS.setPositionPousseur(avant.pousseurI, avant.pousseurJ);

            List<Niveau> deplacement = cheminPousseur(etatPourBFS, pousseurCibleI, pousseurCibleJ);

            for (int m = 0; m < deplacement.size() - 1; m++) {
                Niveau etape = (Niveau) avant.clone();
                etape.supprimePersonnage();
                int pi = deplacement.get(m).pousseurI;
                int pj = deplacement.get(m).pousseurJ;
                etape.ajoutePousseur(pi, pj);
                etape.setPositionPousseur(pi, pj);
                cheminComplet.add(etape);
            }

            cheminComplet.add(apres);
        }

        return cheminComplet;
    }

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

        System.out.print("Impossible, Nombre itérations: " + dejavu.size());
        return null;
    }

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

    public boolean estDansGrille(Niveau depart, int i, int j) {
        return i >= 0 && i < depart.nbLignes && j >= 0 && j < depart.nbColonnes;
    }

    public int[][] heuristique(Niveau depart){
        int [][] dist = new int [depart.nbLignes][depart.nbColonnes];

        Deque<Couple> actifs = new ArrayDeque<Couple>();

        for (int i = 0; i < depart.nbLignes; i++ ){
            for (int j = 0; j < depart.nbColonnes; j++){
                if (depart.aBut(i,j)){
                    dist[i][j] = 0;
                    Couple couple = new Couple(i,j);
                    actifs.add(couple);
                }
                else if (depart.aMur(i,j)){
                    dist[i][j] = MUR;
                }
                else {
                    dist[i][j] = INFINI;
                }
            }
        }

        while(!actifs.isEmpty()){
            Couple element = actifs.remove();
            int ligne = element.getI();
            int colonne = element.getJ();

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

        Map<String, Niveau> dejavu = new HashMap<>();
        Map<String, Integer> distance = new HashMap<>();

        String codeInitial = debut.code();
        distance.put(codeInitial, 0);
        dejavu.put(codeInitial, null);

        int hInitial = coutHeuristique(debut, distBut);
        actifs.add(new NiveauPriorite(debut, hInitial));

        while (!actifs.isEmpty()) {
            Niveau courant = actifs.poll().getNiveau();
            String codeCourant = courant.code();
            int gCourant = distance.get(codeCourant);

            if (courant.estResolu()) {
                return chemin(dejavu, courant);
            }

            for (Niveau suivant : courant.cartesAccessibles()) {
                String codeSuivant = suivant.code();
                int nouveauG = gCourant + 1;

                if (!distance.containsKey(codeSuivant) || nouveauG < distance.get(codeSuivant)) {
                    distance.put(codeSuivant, nouveauG);
                    dejavu.put(codeSuivant, courant);

                    int h = coutHeuristique(suivant, distBut);
                    int f = nouveauG + h;

                    actifs.add(new NiveauPriorite(suivant, f));
                }
            }
        }

        System.out.print("Impossible, Nombre itérations: " + dejavu.size());
        return null;
    }
}