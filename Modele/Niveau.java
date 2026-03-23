package Modele;

import java.util.ArrayList;
import java.util.List;

public class Niveau implements Cloneable {

    public static final int TAILLE_CASE = 28; 

    static final int VIDE    = 0;
    static final int MUR     = 1;
    static final int POUSSEUR = 2;
    static final int CAISSE  = 4;
    static final int BUT     = 8;
    static final int MARQUE  = 16;

    static final int HAUT = 0;
    static final int BAS = 1;
    static final int GAUCHE = 2;
    static final int DROITE = 3;

    int[][] tableau;
    int nbLignes;
    int nbColonnes;

    int pousseurI;
    int pousseurJ;

    String nom;

    public Niveau(int l, int c) {
        this.nbLignes  = l;
        this.nbColonnes = c;
        this.tableau   = new int[l][c];
    }

    public void setPositionPousseur(int i, int j) { this.pousseurI = i; this.pousseurJ = j; }
    public int  getPousseurI() { return pousseurI; }
    public int  getPousseurJ() { return pousseurJ; }

    public void fixeNom(String nom) { this.nom = nom; }

    public void videCase(int i, int j)      { tableau[i][j] = VIDE; }
    public void ajouteMur(int i, int j)     { tableau[i][j] |= MUR; }
    public void ajoutePousseur(int i, int j){ tableau[i][j] |= POUSSEUR; }
    public void retirePousseur(int i, int j){ tableau[i][j] &= ~POUSSEUR; }
    public void ajouteCaisse(int i, int j)  { tableau[i][j] |= CAISSE; }
    public void retireCaisse(int i, int j)  { tableau[i][j] &= ~CAISSE; }
    public void ajouteBut(int i, int j)     { tableau[i][j] |= BUT; }

    public int     lignes()   { return nbLignes; }
    public int     colonnes() { return nbColonnes; }
    public String  nom()      { return nom; }

    public boolean estVide(int i, int j)   { return tableau[i][j] == VIDE; }
    public boolean aMur(int i, int j)      { return (tableau[i][j] & MUR)      != 0; }
    public boolean aBut(int i, int j)      { return (tableau[i][j] & BUT)      != 0; }
    public boolean aPousseur(int i, int j) { return (tableau[i][j] & POUSSEUR) != 0; }
    public boolean aCaisse(int i, int j)   { return (tableau[i][j] & CAISSE)   != 0; }

    public void    placerMarque(int i, int j)  { tableau[i][j] |=  MARQUE; }
    public void    enleverMarque(int i, int j) { tableau[i][j] &= ~MARQUE; }
    public boolean aMarque(int i, int j)       { return (tableau[i][j] & MARQUE) != 0; }
    

  @Override
    public Object clone() {
        try {
            Niveau n = (Niveau) super.clone();
            n.tableau = new int[nbLignes][nbColonnes];
            for (int i = 0; i < nbLignes; i++)
                n.tableau[i] = tableau[i].clone();
            return n;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Niveau doit être Cloneable", e);
        }
    }

    /**********************************************************************************************/
    //FONCTIONS POUR L'IA
   
   //renvoie les coordonnées des case Libres qui sont voisines de (x,y)
    public List<int[]> casesLibresVoisines(int x, int y) {
        List<int[]> liste = new ArrayList<>();
        int[][] deplacements = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};

        for (int[] d : deplacements) {
            int nx = x + d[0];
            int ny = y + d[1];

            if (nx >= 0 && nx < nbLignes && ny >= 0 && ny < nbColonnes
                    && !aMur(nx, ny) && !aCaisse(nx, ny)) {
                liste.add(new int[]{nx, ny});
            }
        }
        return liste;
    }

    //clone le personnage
    public void clonePersonnage() {

        List<int[]> pile = new ArrayList<>();
        int[] coordonnees = new int[2];

        coordonnees[0] = getPousseurI();
        coordonnees[1] = getPousseurJ();

        pile.add(coordonnees);

        while (!pile.isEmpty()) {
            int[] pos = pile.remove(pile.size() - 1);
            int pi = pos[0];
            int pj = pos[1];

            for (int[] voisine : casesLibresVoisines(pi, pj)) {
                int vi = voisine[0];
                int vj = voisine[1];
                if (!aPousseur(vi, vj)) {
                    ajoutePousseur(vi, vj);
                    pile.add(new int[]{vi, vj});
                }
            }
        }
    }

    //supprime un personnage
    public void supprimePersonnage(){
        for(int i=0;i<nbLignes;i++){
            for(int j=0;j<nbColonnes;j++){
                if(aPousseur(i,j)) retirePousseur(i,j);
            }
        }
    }
    
    //renvoie la liste des coordonnees de toutes les caisses
    public List<int[]> coordonneesCaisses(){
        List<int[]> listeCaisses = new ArrayList<>();
        for(int i=0;i<nbLignes;i++){
            for(int j=0;j<nbColonnes;j++){
                if(aCaisse(i,j)) listeCaisses.add(new int [] {i , j});
            }
        }

        return listeCaisses;

    }

    //determine si une caisse est poussable
    public boolean poussable(int ci, int cj, int direction) {
        int di = 0, dj = 0;

        switch (direction) {
            case HAUT:   di = -1; break;
            case BAS:    di =  1; break;
            case GAUCHE: dj = -1; break;
            case DROITE: dj =  1; break;
        }
        int pi = ci - di;
        int pj = cj - dj;
        int ni = ci + di;
        int nj = cj + dj;

        return pi >= 0 && pi < nbLignes && pj >= 0 && pj < nbColonnes && ni >= 0 && ni < nbLignes && nj >= 0 && nj < nbColonnes
                && aPousseur(pi, pj) && !aMur(ni, nj) && !aCaisse(ni, nj);
    }
   
   //pousse une caisse dans la direction donnée
   
    public Niveau pousser(int i, int j, int direction) {
        Niveau nouveauNiveau = (Niveau) this.clone();

        int di = 0, dj = 0;
        switch (direction) {
            case HAUT:   di = -1; break;
            case BAS:    di =  1; break;
            case GAUCHE: dj = -1; break;
            case DROITE: dj =  1; break;
        }

        int ni = i + di;
        int nj = j + dj;


        nouveauNiveau.supprimePersonnage();
        nouveauNiveau.retireCaisse(i, j);
        nouveauNiveau.ajouteCaisse(ni, nj);
        nouveauNiveau.ajoutePousseur(i, j);
        nouveauNiveau.setPositionPousseur(i, j);

        return nouveauNiveau;
    }

    
    //renvoie les cartesAccessibles
    public List<Niveau> cartesAccessibles() {
        List<Niveau> accessibles = new ArrayList<>();
        List<int[]> caisses = coordonneesCaisses(); 

        for (int[] c : caisses) {
            for (int direction : new int[]{HAUT, BAS, GAUCHE, DROITE}) {
                if (poussable(c[0], c[1], direction)) {
                    Niveau niveau = pousser(c[0], c[1], direction);
                    niveau.clonePersonnage();
                    accessibles.add(niveau);
                }
            }
        }
        return accessibles;
    }

    //le code pour la hashmap utilisée dans la classe solveur
    public String code() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                sb.append(tableau[i][j]).append(",");
            }
        }
        return sb.toString();
    }


    private int nbButs(){
        int compteur=0;
        for (int i = 0; i < nbLignes; i++){
            for (int j = 0; j < nbColonnes; j++){
                if(aBut(i,j)) compteur++;
            }
        }
            
        return compteur;       
    }

    private int nbCaisses(){
        int compteur=0;
        for (int i = 0; i < nbLignes; i++){
            for (int j = 0; j < nbColonnes; j++){
                if(aCaisse(i,j)) compteur++;
            }
        }
            
        return compteur;   

    }
    
    //Resolution
    public boolean estResolu() {
        //verifier d'abord que le nombre de caisses==nombre de buts
        int nombreCaisses = nbCaisses();
        int nombreButs = nbButs();
        if(nombreCaisses != nombreButs ) return false;
        for (int i = 0; i < nbLignes; i++)
            for (int j = 0; j < nbColonnes; j++)
                if (aBut(i, j) && !aCaisse(i, j)){
                    return false;
                }
        return true;
    }

    //pour garder un seul personnage visible sur la carte
    //car en réalite on a un clonage de personnage à chaque fois 
    //lors de l'exploration 
    public void unPersonnage() {
        supprimePersonnage();
        ajoutePousseur(pousseurI, pousseurJ);
    }





}