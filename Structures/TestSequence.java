package Structures;

public class TestSequence {
    public static void main(String[] args) {

        // --- Test avec SequenceListe ---
        System.out.println("=== Test SequenceListe ===");
        // On précise le type Integer entre chevrons
        Sequence<Integer> sl = new SequenceListe<>();

        sl.insereTete(12);
        sl.insereQueue(20);
        sl.insereQueue(30);

        System.out.println("Liste après insertions : " + sl.toString());
        System.out.println("Extraction tête : " + sl.extraitTete()); // Doit afficher 12
        System.out.println("Liste après extraction : " + sl.toString());

        // Test de l'itérateur sur la liste
        System.out.print("Parcours via itérateur : ");
        Iterateur<Integer> itL = sl.iterateur();
        while (itL.aProchain()) {
            System.out.print(itL.prochain() + " ");
        }
        System.out.println("\n");


        // --- Test avec SequenceTableau ---
        System.out.println("=== Test SequenceTableau ===");
        Sequence<Integer> st = new SequenceTableau<>();

        st.insereTete(50);
        st.insereQueue(60);
        st.insereTete(40);

        System.out.println("Tableau après insertions : " + st.toString());

        // Test de suppression via l'itérateur
        Iterateur<Integer> itT = st.iterateur();
        while (itT.aProchain()) {
            Integer val = itT.prochain();
            if (val == 60) {
                itT.supprime();
                System.out.println("Suppression de 60 via l'itérateur.");
            }
        }
        System.out.println("Tableau final : " + st.toString());

        // Preuve de la généricité avec un autre type (String
    }
}