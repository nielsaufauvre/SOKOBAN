package Structures;

interface Iterateur<E>  {
    public boolean aProchain();
    public E prochain();
    public void supprime();
}