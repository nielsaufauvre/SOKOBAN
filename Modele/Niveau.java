package Modele;

import java.util.ArrayList;
import java.util.List;

public class Niveau implements Cloneable {

    public static final int TAILLE_CASE = 28; // Constante centralisée

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
    



    
    public boolean estResolu() {
        for (int i = 0; i < nbLignes; i++)
            for (int j = 0; j < nbColonnes; j++)
                if (aBut(i, j) && !aCaisse(i, j)) return false;
        return true;
    }

    @Override
    public Object clone() {
        try {
            Niveau n = (Niveau) super.clone();
            // Copie profonde du tableau 2D
            n.tableau = new int[nbLignes][nbColonnes];
            for (int i = 0; i < nbLignes; i++)
                n.tableau[i] = tableau[i].clone();
            return n;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Niveau doit être Cloneable", e);
        }
    }

    /**********************************************************************************************/
    /*fonctions ajoutées qui va nous aider pour la partie IA*/

    //renvoie  la liste des cases accessibles à partir de la case en arguments
    //chaque ligne de la liste(qui est une matrice) correspond à une case
    public int [] [] casesLibresVoisines(int x, int y){

        int [] [] listeCases = new int[4][2]; //max 4 cases intialisées à -1
        for (int i=0;i<listeCases.length;i++){
            for(int j=0;j<listeCases[i].length;j++) listeCases[i][j]=-1;
        }

        //case du haut
        if (estVide(x,y+1)){
            listeCases[0][0]=x ;
            listeCases[0][1]=y+1;
        }
        //bas
        if (estVide(x,y-1)){
            listeCases[1][0]=x;
            listeCases[1][1]=y-1;
        }
        //gauche
        if (estVide(x-1,y)){
            listeCases[2][0]=x-1 ;
            listeCases[2][1]=y;
        }
        //droite
        if (estVide(x+1,y)){
            listeCases[3][0]=x+1 ;
            listeCases[3][1]=y;
        }

        return listeCases;

    }
    //fonction qui prend le personnage et le met sur toutes les cases accessibles à sa position
    public void clonePersonnage(){
        int [][] accessibles = casesLibresVoisines(getPousseurI(),getPousseurJ());
        for(int i=0;i<accessibles.length;i++){
            int abscisse = accessibles[i][0];
            int ordonnee = accessibles[i][1];
            if(abscisse !=-1 && ordonnee !=-1) ajoutePousseur(abscisse,ordonnee); //ajout du joueur
        }

    }
    //supprime les personnages sur la carte
    public void supprimePersonnage(){
        for(int i=0;i<nbLignes;i++){
            for(int j=0;j<nbColonnes;j++){
                if(aPousseur(i,j)) videCase(i,j);
            }
        }
    }

    //fonction qui renvoie la liste des caisses
    public List<int[]> coordonneesCaisses(){
        List<int[]> listeCaisses = new ArrayList<>();
        for(int i=0;i<nbLignes;i++){
            for(int j=0;j<nbColonnes;j++){
                if(aCaisse(i,j)) listeCaisses.add(new int [] {i , j});
            }
        }

        return listeCaisses;

    }

    //elle renvoie aussi la direction où on peut pousser 
    //dans la variable direction
    public boolean poussable(int ci, int cj,int direction){
       int pi = getPousseurI();
       int pj = getPousseurJ();

       if (ci == pi-1){
            d = GAUCHE;
           return estVide(ci+1,cj)|| aBut(ci+1,cj);
       }
        if (ci == pi+1){
            d = DROITE;
            return estVide(ci-1,cj)|| aBut(ci-1,cj);
        }
        if (cj == pj-1){
            d = HAUT;
            return estVide(ci,cj+1)|| aBut(ci,cj+1);
        }
        if (cj == pj+1){
            d = BAS;
            return estVide(ci,cj-1)|| aBut(ci,cj-1);
        }

        d = -1; //cas où on ne peut pas pousser
        return false;


    }
    //on suppoose que la caisse passer en argument est poussable
    public int[][] pousser(int[][] carte, int i, int j, int direction) {
        if (direction < 0 || direction > 3) {
            return carte;
        }

        int di = 0;
        int dj = 0;

        switch (direction) {
            case HAUT:
                di = -1;
                break;
            case BAS:
                di = 1;
                break;
            case GAUCHE:
                dj = -1;
                break;
            case DROITE:
                dj = 1;
                break;
        }

        int ni = i + di;
        int nj = j + dj;
        int ci = i + 2 * di;
        int cj = j + 2 * dj;

    
            videCase(i, j);
            //ajoutePousseur(ni, nj);
            ajouteCaisse(ci, cj);
            //attention ici ajouteCaisse modifie uniquement la carte de niveau 
            //est il necessaire de passer la carte en argument????
            //celà dependra des algo d'après, car si c'est pas le tableau initiale 
            //de la carte qu'on utilise, il faudra alors modifier la carte passée
            //en argument et la fonction ajouterCaisse de même
        
        

        return carte;
    }
    
    //d'après le schema le personnage ne bouge pas(correction faite dejà dans pousser)
    //idee: trouver toutes les caisses poussables , et les pousser dans 
    //la direction poussable
    public List<int [][]>cartesAccessibles(int [][] carte){
        // on suppose que le personne a été cloné sur la carte
        List <int [][]> listeCartes = new ArrayList<int[][]>();

        List<int[]> listeCaisses = coordonneesCaisses();
        for(int i=0;i<listeCaisses.size();i++){
            int ci = listeCaisses[i][0];
            int cj = listeCaisses[i][1];
            int direction = -1;
            
            if(poussable(ci,cj,direction)){
                listeCartes.add(pousser(carte,ci,cj,d))
            }
            
        }
        return listeCaisses;


    }

    //passe à l'implementation du parcours 
    

    //renvoie une nouvelle carte en poussant c(i,j) dans la direction indiquée
    //public nouvelleCarte()

}