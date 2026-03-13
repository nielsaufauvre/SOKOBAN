package Structures;

public class IterateurSequenceListe<E> implements Iterateur<E> {

    private SequenceListe<E> sequence;
    private Maillon<E> precedent;
    private Maillon<E> courant;
    private boolean peutSupprimer;

    IterateurSequenceListe(SequenceListe<E> sequence) {
        this.sequence = sequence;
        this.precedent = null;
        this.courant = null;
        this.peutSupprimer = false;
    }

    public boolean aProchain() {
        if (courant == null) {
            return sequence.tete != null;
        }
        return courant.suivant != null;
    }

    public E prochain() {
        if (!aProchain()) {
            throw new RuntimeException("Pas d'élément suivant");
        }
        precedent = courant;
        if (courant == null) {
            courant = sequence.tete;
        } else {
            courant = courant.suivant;
        }
        peutSupprimer = true;
        return courant.element;
    }

    public void supprime() {
        if (!peutSupprimer) {
            throw new IllegalStateException("Appel invalide à supprime()");
        }

        if (precedent == null) {
            sequence.tete = courant.suivant;
        } else {
            precedent.suivant = courant.suivant;
        }

        if (courant.suivant == null) {
            sequence.queue = precedent;
        }

        courant = precedent;
        sequence.taille--;
        peutSupprimer = false;
    }
}