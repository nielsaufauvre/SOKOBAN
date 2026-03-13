package Structures;

public class SequenceListe<E> implements Sequence<E> {

    Maillon<E> tete;
    Maillon<E> queue;
    int taille;


    public SequenceListe() {
        this.tete = null;
        this.queue = null;
        this.taille = 0;
    }

    public void insereTete(E element) {
        Maillon<E> nouveau = new Maillon<>(element, tete);
        tete = nouveau;
        if (queue == null) {
            queue = nouveau;
        }
        taille++;
    }

    public void insereQueue(E element) {
        Maillon<E> nouveau = new Maillon<>(element, null);
        if (queue == null) {
            tete = nouveau;
        } else {
            queue.suivant = nouveau;
        }
        queue = nouveau;
        taille++;
    }

    public E extraitTete() {
        if (estVide()) {
            throw new RuntimeException("Séquence vide");
        }
        E valeur = tete.element;
        tete = tete.suivant;
        if (tete == null) {
            queue = null;
        }
        taille--;
        return valeur;
    }

    public boolean estVide() {
        return tete == null;
    }

    public Iterateur<E> iterateur() {
        return new IterateurSequenceListe<>(this);
    }

    @Override
    public String toString() {
        if (estVide()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        Maillon<E> courant = tete;
        while (courant != null) {
            sb.append(courant.element);
            if (courant.suivant != null) {
                sb.append(", ");
            }
            courant = courant.suivant;
        }
        sb.append("]");
        return sb.toString();
    }
}