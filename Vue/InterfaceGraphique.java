package Vue;

import Controleur.EcouteurDeClavier;
import Controleur.EcouteurDeSouris;
import Controleur.GameController;
import javax.swing.*;

/**
 INTERFACE GRAPHIQUE
 */
public class InterfaceGraphique implements Runnable {

    private final int numeroNiveau;

    public InterfaceGraphique(int numeroNiveau) {
        this.numeroNiveau = numeroNiveau;
    }

    @Override
    public void run() {
        JFrame frame = new JFrame("Sokoban — Niveau " + numeroNiveau);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        NiveauGraphique niveauGraphique = new NiveauGraphique(numeroNiveau);
        frame.add(niveauGraphique);

        // Un seul GameController partagé par les deux écouteurs
        GameController controller = new GameController(frame, niveauGraphique);

        EcouteurDeClavier clavier = new EcouteurDeClavier(frame, niveauGraphique, controller);
        EcouteurDeSouris  souris  = new EcouteurDeSouris(controller);

        niveauGraphique.addKeyListener(clavier);
        niveauGraphique.addMouseListener(souris);
        niveauGraphique.setFocusable(true);

        // pack() calcule la taille optimale d'après getPreferredSize() au lieu d'un setSize() approximatif
        frame.pack();
        frame.setLocationRelativeTo(null); // Centrer à l'écran
        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> niveauGraphique.requestFocusInWindow());
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage : java Vue.InterfaceGraphique <numéro_niveau>");
            System.err.println("Exemple : java Vue.InterfaceGraphique 1");
            System.exit(1);
        }

        int niveau;
        try {
            niveau = Integer.parseInt(args[0]);
            if (niveau < 1) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.err.println("Erreur : le numéro de niveau doit être un entier positif.");
            System.exit(1);
            return;
        }

        SwingUtilities.invokeLater(new InterfaceGraphique(niveau));
    }
}