package Vue;

import Modele.Jeu;
import Modele.Niveau;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class NiveauGraphique extends JComponent {

    // -----------------------------------------------------------------------
    // Constante partagée : plus de "28" codé en dur partout
    // -----------------------------------------------------------------------
    public static final int TAILLE_CASE = Niveau.TAILLE_CASE;

    // Chemin du fichier de niveaux — chargé depuis le classpath (fonctionne dans un JAR)
    private static final String CHEMIN_NIVEAUX = "/Structures/test.txt";

    // Images
    private Image But_img, Caisse_img, Caisse_sur_but_img, Mur_img, Pousseur_img, Sol_img;

    public Niveau niveau;
    public int pousseur_i, pousseur_j;
    public int numNiveau;

    // Animation de déplacement
    private Timer timer;
    private double progression = 1.0;
    private int dirI, dirJ;
    private boolean caisseDeplacee = false;

    // -----------------------------------------------------------------------
    // Constructeur
    // -----------------------------------------------------------------------

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

            But_img          = chargerImage("/Images/But.png");
            Caisse_img       = chargerImage("/Images/Caisse.png");
            Caisse_sur_but_img = chargerImage("/Images/Caisse_sur_but.png");
            Mur_img          = chargerImage("/Images/Mur.png");
            Pousseur_img     = chargerImage("/Images/Pousseur.png");
            Sol_img          = chargerImage("/Images/Sol.png");

            this.pousseur_i = niveau.getPousseurI();
            this.pousseur_j = niveau.getPousseurJ();

        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement du niveau " + numero_niveau + " : " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // Méthodes utilitaires statiques
    // -----------------------------------------------------------------------

    /**
     * Vérifie si un numéro de niveau existe dans le fichier de niveaux.
     * Utilisé par GameController pour détecter la fin du jeu.
     */
    public static boolean niveauExiste(int numero) {
        try {
            InputStream in = NiveauGraphique.class.getResourceAsStream(CHEMIN_NIVEAUX);
            if (in == null) return false;
            Jeu jeu = new Jeu(numero);
            Niveau n = jeu.niveau(in, numero);
            return n != null;
        } catch (Exception e) {
            return false;
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

    // -----------------------------------------------------------------------
    // Animation
    // -----------------------------------------------------------------------

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

    // -----------------------------------------------------------------------
    // Rendu
    // -----------------------------------------------------------------------

    @Override
    public Dimension getPreferredSize() {
        if (niveau == null) return new Dimension(400, 300);
        return new Dimension(
                niveau.colonnes() * TAILLE_CASE,
                niveau.lignes()   * TAILLE_CASE
        );
    }

    @Override
    public void paintComponent(Graphics g) {
        if (niveau == null) return;

        Graphics2D drawable = (Graphics2D) g;
        drawable.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawable.clearRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < niveau.lignes(); i++) {
            for (int j = 0; j < niveau.colonnes(); j++) {
                int x = j * TAILLE_CASE;
                int y = i * TAILLE_CASE;

                // Sol / But
                if (niveau.aBut(i, j)) {
                    drawable.drawImage(But_img, x, y, TAILLE_CASE, TAILLE_CASE, null);
                } else if (!niveau.aMur(i, j)) {
                    drawable.drawImage(Sol_img, x, y, TAILLE_CASE, TAILLE_CASE, null);
                }

                // Mur
                if (niveau.aMur(i, j)) {
                    drawable.drawImage(Mur_img, x, y, TAILLE_CASE, TAILLE_CASE, null);
                }
                // Caisse (avec animation si elle vient d'être poussée)
                else if (niveau.aCaisse(i, j)) {
                    int drawX = x, drawY = y;
                    if (caisseDeplacee && i == pousseur_i + dirI && j == pousseur_j + dirJ) {
                        drawX -= (int) ((1.0 - progression) * dirJ * TAILLE_CASE);
                        drawY -= (int) ((1.0 - progression) * dirI * TAILLE_CASE);
                    }
                    Image img = niveau.aBut(i, j) ? Caisse_sur_but_img : Caisse_img;
                    drawable.drawImage(img, drawX, drawY, TAILLE_CASE, TAILLE_CASE, null);
                }
                // Pousseur (avec animation)
                else if (niveau.aPousseur(i, j)) {
                    int animX = x - (int) ((1.0 - progression) * dirJ * TAILLE_CASE);
                    int animY = y - (int) ((1.0 - progression) * dirI * TAILLE_CASE);
                    drawable.drawImage(Pousseur_img, animX, animY, TAILLE_CASE, TAILLE_CASE, null);
                }

                // Marque (debug / IA)
                if (niveau.aMarque(i, j)) {
                    drawable.setColor(Color.RED);
                    drawable.setStroke(new BasicStroke(3));
                    drawable.drawLine(x + 5, y + 5, x + TAILLE_CASE - 5, y + TAILLE_CASE - 5);
                    drawable.drawLine(x + TAILLE_CASE - 5, y + 5, x + 5, y + TAILLE_CASE - 5);
                }
            }
        }
    }

    public Niveau getNiveau() { return niveau; }
}