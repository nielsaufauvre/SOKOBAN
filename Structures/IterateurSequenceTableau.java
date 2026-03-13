package Structures;

public class IterateurSequenceTableau <E> implements Iterateur<E> {

    SequenceTableau <E>  t;
    int curseur = 0;
    boolean peutSupprimer = false;

    public IterateurSequenceTableau(SequenceTableau<E>  t) {
        this.t = t;
    }

    public boolean aProchain() {
        return curseur < t.taille;
    }

    public E prochain() {
        if (!aProchain()) {
            throw new RuntimeException("Fin de séquence");
        }
        E element = t.tableau[(t.tete + curseur) % t.tableau.length];
        curseur++;
        peutSupprimer = true;
        return element;
    }

    public void supprime() {
        if (!peutSupprimer) {
            throw new IllegalStateException("Supprime impossible sans prochain()");
        }

        int positionAbsolue = (t.tete + curseur - 1) % t.tableau.length;

        for (int i = curseur - 1; i < t.taille - 1; i++) {
            int posCourante = (t.tete + i) % t.tableau.length;
            int posSuivante = (t.tete + i + 1) % t.tableau.length;
            t.tableau[posCourante] = t.tableau[posSuivante];
        }

        t.taille--;
        t.queue = (t.queue - 1 + t.tableau.length) % t.tableau.length;
        curseur--;
        peutSupprimer = false;
    }
}