package Modele;

public class Niveau implements Cloneable {

    public static final int TAILLE_CASE = 28; // Constante centralisée

    static final int VIDE    = 0;
    static final int MUR     = 1;
    static final int POUSSEUR = 2;
    static final int CAISSE  = 4;
    static final int BUT     = 8;
    static final int MARQUE  = 16;

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
}