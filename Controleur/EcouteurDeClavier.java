package Controleur;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import Vue.NiveauGraphique;

public class EcouteurDeClavier implements KeyListener {

    private JFrame frame;
    private NiveauGraphique niveauGraphique;
    private GameController controller;
    private AnimationJeuAutomatique animationJeuAutomatique;

    public EcouteurDeClavier(JFrame frame, NiveauGraphique niveauGraphique, GameController controller) {
        this.frame = frame;
        this.niveauGraphique = niveauGraphique;
        this.controller = controller;
        this.animationJeuAutomatique = new AnimationJeuAutomatique(niveauGraphique, this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int di = 0, dj = 0;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:  dj = -1; break;
            case KeyEvent.VK_RIGHT: dj =  1; break;
            case KeyEvent.VK_UP:    di = -1; break;
            case KeyEvent.VK_DOWN:  di =  1; break;
            case KeyEvent.VK_I:
                animationJeuAutomatique.resoudreNiveau();
                return;
            case KeyEvent.VK_Z:
                if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) {
                    controller.annuler();
                }
                return;
            default:
                return;
        }

        controller.tenterDeplacement(di, dj);
    }

    /*appeler par la fonction de resolution de niveau pour faire un deplacement*/
    public void resolution() {
        
        controller.resolutionAutomatique();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}