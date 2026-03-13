package Structures;

public class SequenceTableau <E> implements Sequence <E>{

    @SuppressWarnings("unchecked")
    E [] tableau = (E[]) new Object[10];
    int taille = 0;
    int tete = 0;
    int queue = 0;

    @SuppressWarnings("unchecked")
    public Iterateur<E> iterateur() {
        return new IterateurSequenceTableau(this);
    }

    public void agrandirTableau() {
        @SuppressWarnings("unchecked")
        E [] tableau2 = (E[]) new Object[tableau.length * 2];

        for (int i = 0; i < taille; i++) {
            tableau2[i] = tableau[(tete + i) % tableau.length];
        }
        
        tete = 0;
        queue = taille;
        tableau = tableau2;
    }

    public void insereTete(E element){
        if (taille >= tableau.length) {
            agrandirTableau();
        }
        tete = (tete - 1 + tableau.length) % tableau.length;
        this.tableau[tete] = element;
        taille++;
    }

    public void insereQueue(E element){
        if (taille >= tableau.length) {
            agrandirTableau();
        }
        this.tableau[queue] = element;
        queue = (queue + 1) % tableau.length;
        taille++;
    }

    public E extraitTete() {
        if (estVide()) {
            throw new RuntimeException("Séquence vide");
        }
        E element = this.tableau[tete];
        tete = (tete + 1) % tableau.length;
        taille--;
        return element;
    }

    public boolean estVide(){
        return this.taille == 0;
    }

    public String toString() {
        if (this.estVide()) {
            return "[]";
        }
        String resultat = "[";
        for (int i = 0; i < taille; i++) {
            resultat += tableau[(tete + i) % tableau.length];
            if (i < taille - 1) {
                resultat += ", ";
            }
        }
        resultat += "]";
        return resultat;
    }

}