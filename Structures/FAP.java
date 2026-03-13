package Structures;

class FAP<E extends Comparable<E>> {
    private SequenceListe<E> seq;

    public FAP() {
        this.seq = new SequenceListe<>();
    }
    void enfile(E e) {
        seq.insereQueue(e);
    }
    public E defile() {
        if (seq.estVide()) {
            throw new RuntimeException("FAP vide");
        }
        // C'est ici qu'il faut utiliser l'itérateur pour :
        Iterateur<E> it = seq.iterateur();
        E maximum = it.prochain();
        while (it.aProchain()){
            E courant = it.prochain();
            if (courant.compareTo(maximum) > 0) {
                maximum = courant;
            }
        }

        Iterateur<E> it2 = seq.iterateur();
        while (it2.aProchain()) {
            E element = it2.prochain();
            if (element.compareTo(maximum) == 0) {
                it2.supprime();
                break;
            }
        }

        return maximum;
        // 1. Parcourir toute la séquence s.
        // 2. Trouver le plus grand élément via compareTo.
        // 3. Le supprimer avec it.supprime() et le retourner.
    }
}