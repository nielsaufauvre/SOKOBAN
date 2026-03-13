package Structures;

interface Sequence <E> {

    public void insereTete(E element);
    public void insereQueue(E element);
    public E extraitTete();
    public boolean estVide();
    public String toString();
    public Iterateur<E> iterateur();

}


