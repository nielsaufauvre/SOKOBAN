package Structures;

public class TestGeneral {
    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("       TESTS SÉQUENCE LISTE");
        System.out.println("========================================\n");
        testSequence(new SequenceListe<>());

        System.out.println("\n========================================");
        System.out.println("       TESTS SÉQUENCE TABLEAU");
        System.out.println("========================================\n");
        testSequence(new SequenceTableau<>());

        System.out.println("\n========================================");
        System.out.println("       TESTS FAP (Integer)");
        System.out.println("========================================\n");
        testFAPInteger();

        System.out.println("\n========================================");
        System.out.println("       TESTS FAP (CoupleFAPInt)");
        System.out.println("========================================\n");
        testFAPCouple();

        System.out.println("\n========================================");
        System.out.println("       TOUS LES TESTS PASSÉS ✓");
        System.out.println("========================================");
    }

    static void testSequence(Sequence<Integer> seq) {
        // Test insertion
        seq.insereTete(2);
        seq.insereTete(1);
        seq.insereQueue(3);
        seq.insereQueue(4);
        System.out.println("Après insertions (1,2 tête / 3,4 queue): " + seq);
        assert seq.toString().equals("[1, 2, 3, 4]") : "Erreur insertions";

        // Test extraction
        int val = seq.extraitTete();
        System.out.println("extraitTete() = " + val);
        assert val == 1 : "Erreur extraitTete";
        System.out.println("Après extraction: " + seq);

        // Test estVide
        assert !seq.estVide() : "Erreur estVide";
        System.out.println("estVide() = " + seq.estVide());

        // Test itérateur parcours
        System.out.print("Parcours itérateur: ");
        Iterateur<Integer> it = seq.iterateur();
        while (it.aProchain()) {
            System.out.print(it.prochain() + " ");
        }
        System.out.println();

        // Test itérateur suppression
        seq.insereQueue(0);
        seq.insereQueue(5);
        seq.insereQueue(0);
        System.out.println("Avant suppression des 0: " + seq);

        Iterateur<Integer> it2 = seq.iterateur();
        while (it2.aProchain()) {
            if (it2.prochain() == 0) {
                it2.supprime();
            }
        }
        System.out.println("Après suppression des 0: " + seq);

        // Test vidage complet
        while (!seq.estVide()) {
            seq.extraitTete();
        }
        assert seq.estVide() : "Erreur vidage";
        System.out.println("Après vidage complet: " + seq);
        System.out.println("estVide() = " + seq.estVide());

        // Test exception séquence vide
        try {
            seq.extraitTete();
            System.out.println("ERREUR: exception non levée");
        } catch (RuntimeException e) {
            System.out.println("Exception correctement levée: " + e.getMessage());
        }
    }

    static void testFAPInteger() {
        FAP<Integer> fap = new FAP<>();

        fap.enfile(5);
        fap.enfile(2);
        fap.enfile(8);
        fap.enfile(1);
        fap.enfile(9);
        fap.enfile(3);
        System.out.println("Enfilé: 5, 2, 8, 1, 9, 3");

        System.out.print("Défilé (ordre décroissant attendu): ");
        int prev = Integer.MAX_VALUE;
        while (true) {
            try {
                int val = fap.defile();
                System.out.print(val + " ");
                assert val <= prev : "Erreur ordre FAP";
                prev = val;
            } catch (RuntimeException e) {
                break;
            }
        }
        System.out.println("\nFAP vidée correctement");
    }

    static void testFAPCouple() {
        FAP<CoupleFAPInt<String>> fap = new FAP<>();

        fap.enfile(new CoupleFAPInt<>("Tâche C", 3));
        fap.enfile(new CoupleFAPInt<>("Tâche A", 10));
        fap.enfile(new CoupleFAPInt<>("Tâche B", 5));
        fap.enfile(new CoupleFAPInt<>("Tâche D", 1));
        System.out.println("Enfilé: C(3), A(10), B(5), D(1)");

        System.out.println("Défilé par priorité décroissante:");
        int prev = Integer.MAX_VALUE;
        while (true) {
            try {
                CoupleFAPInt<String> c = fap.defile();
                System.out.println("  " + c.element + " (priorité " + c.priorite + ")");
                assert c.priorite <= prev : "Erreur ordre FAP Couple";
                prev = c.priorite;
            } catch (RuntimeException e) {
                break;
            }
        }
        System.out.println("FAP Couple vidée correctement");
    }
}