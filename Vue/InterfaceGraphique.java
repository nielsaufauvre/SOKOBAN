package Vue;

import Controleur.EcouteurDeClavier;
import Controleur.EcouteurDeSouris;
import Controleur.GameController;
import javax.swing.*;
import java.awt.*;

public class InterfaceGraphique implements Runnable {

    private final int numeroNiveau;

    public InterfaceGraphique(int numeroNiveau) {
        this.numeroNiveau = numeroNiveau;
    }

    @Override
    public void run() {
        JFrame frame = new JFrame("Sokoban — Niveau " + numeroNiveau);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Mode plein écran sans bordures
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        NiveauGraphique niveauGraphique = new NiveauGraphique(numeroNiveau);
        frame.add(niveauGraphique);

        GameController controller = new GameController(frame, niveauGraphique);
        EcouteurDeClavier clavier = new EcouteurDeClavier(frame, niveauGraphique, controller);
        EcouteurDeSouris  souris  = new EcouteurDeSouris(controller);

        niveauGraphique.addKeyListener(clavier);
        niveauGraphique.addMouseListener(souris);
        niveauGraphique.setFocusable(true);

        frame.setVisible(true);
        SwingUtilities.invokeLater(() -> niveauGraphique.requestFocusInWindow());
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage : java Vue.InterfaceGraphique <numéro_niveau>");
            System.exit(1);
        }
        try {
            int niveau = Integer.parseInt(args[0]);
            SwingUtilities.invokeLater(new InterfaceGraphique(niveau));
        } catch (NumberFormatException e) {
            System.err.println("Erreur : niveau invalide.");
        }
    }
}