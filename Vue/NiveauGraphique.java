package Vue;

import Modele.Jeu;
import Modele.Niveau;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class NiveauGraphique extends JComponent {

    private static final String CHEMIN_NIVEAUX = "../sokoban-tests/Tests.txt";

    // Images
    private Image But_img, Caisse_img, Caisse_sur_but_img, Mur_img, Pousseur_img, Sol_img;

    public Niveau niveau;
    public int pousseur_i, pousseur_j;
    public int numNiveau;

    // Variables pour l'adaptation graphique
    private int tailleCase;
    private int decalageX;
    private int decalageY;

    // Animation
    private Timer timer;
    private double progression = 1.0;
    private int dirI, dirJ;
    private boolean caisseDeplacee = false;

    public NiveauGraphique(int numero_niveau) {
        numNiveau = numero_niveau;
        try {
            Jeu jeu = new Jeu(numero_niveau);
            InputStream in = NiveauGraphique.class.getResourceAsStream(CHEMIN_NIVEAUX);
            if (in == null) {
                afficherErreur("Fichier de niveaux introuvable : " + CHEMIN_NIVEAUX);
                return;
            }
            niveau = jeu.niveau(in, numero_niveau);
            if (niveau == null) {
                afficherErreur("Le niveau " + numero_niveau + " n'existe pas.");
                return;
            }

            But_img            = chargerImage("/Images/But.png");
            Caisse_img         = chargerImage("/Images/Caisse.png");
            Caisse_sur_but_img = chargerImage("/Images/Caisse_sur_but.png");
            Mur_img            = chargerImage("/Images/Mur.png");
            Pousseur_img       = chargerImage("/Images/Pousseur.png");
            Sol_img            = chargerImage("/Images/Sol.png");

            this.pousseur_i = niveau.getPousseurI();
            this.pousseur_j = niveau.getPousseurJ();

        } catch (Exception e) {
            afficherErreur("Erreur chargement niveau " + numero_niveau + " : " + e.getMessage());
        }
    }

    private void calculerDimensions() {
        if (niveau == null) return;

        // Calcul de la taille de case maximale possible
        int tailleH = getWidth() / niveau.colonnes();
        int tailleV = getHeight() / niveau.lignes();
        this.tailleCase = Math.min(tailleH, tailleV);

        // Calcul des décalages pour centrer le dessin
        this.decalageX = (getWidth() - (niveau.colonnes() * tailleCase)) / 2;
        this.decalageY = (getHeight() - (niveau.lignes() * tailleCase)) / 2;
    }

    public int getTailleCaseActuelle() { return tailleCase; }
    public int getDecalageX() { return decalageX; }
    public int getDecalageY() { return decalageY; }

    public void demarrerAnimation(int di, int dj, boolean pousseCaisse) {
        this.dirI = di;
        this.dirJ = dj;
        this.caisseDeplacee = pousseCaisse;
        this.progression = 0.0;
        if (timer != null && timer.isRunning()) timer.stop();
        timer = new Timer(15, e -> {
            progression += 0.2;
            if (progression >= 1.0) {
                progression = 1.0;
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        if (niveau == null) return;

        calculerDimensions(); // Mise à jour dynamique à chaque redessin

        Graphics2D drawable = (Graphics2D) g;
        drawable.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fond d'écran
        drawable.setColor(Color.DARK_GRAY);
        drawable.fillRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < niveau.lignes(); i++) {
            for (int j = 0; j < niveau.colonnes(); j++) {
                int x = decalageX + (j * tailleCase);
                int y = decalageY + (i * tailleCase);

                if (niveau.aBut(i, j)) {
                    drawable.drawImage(But_img, x, y, tailleCase, tailleCase, null);
                } else if (!niveau.aMur(i, j)) {
                    drawable.drawImage(Sol_img, x, y, tailleCase, tailleCase, null);
                }

                if (niveau.aMur(i, j)) {
                    drawable.drawImage(Mur_img, x, y, tailleCase, tailleCase, null);
                } else if (niveau.aCaisse(i, j)) {
                    int drawX = x, drawY = y;
                    if (caisseDeplacee && i == pousseur_i + dirI && j == pousseur_j + dirJ) {
                        drawX -= (int) ((1.0 - progression) * dirJ * tailleCase);
                        drawY -= (int) ((1.0 - progression) * dirI * tailleCase);
                    }
                    Image img = niveau.aBut(i, j) ? Caisse_sur_but_img : Caisse_img;
                    drawable.drawImage(img, drawX, drawY, tailleCase, tailleCase, null);
                } else if (niveau.aPousseur(i, j)) {
                    int animX = x - (int) ((1.0 - progression) * dirJ * tailleCase);
                    int animY = y - (int) ((1.0 - progression) * dirI * tailleCase);
                    drawable.drawImage(Pousseur_img, animX, animY, tailleCase, tailleCase, null);
                }
            }
        }
    }

    private Image chargerImage(String chemin) throws Exception {
        InputStream is = NiveauGraphique.class.getResourceAsStream(chemin);
        if (is == null) throw new Exception("Image introuvable : " + chemin);
        return ImageIO.read(is);
    }

    private void afficherErreur(String message) {
        JOptionPane.showMessageDialog(null, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean niveauExiste(int numero) {
        try {
            InputStream in = NiveauGraphique.class.getResourceAsStream(CHEMIN_NIVEAUX);
            if (in == null) return false;
            return new Jeu(numero).niveau(in, numero) != null;
        } catch (Exception e) { return false; }
    }
}