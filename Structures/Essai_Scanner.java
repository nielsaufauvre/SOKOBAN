package Structures;

import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.InputMismatchException;

class Essai_Scanner {
    public static void main(String [] args) {
        Scanner my_scanner;
        int ligne;

        my_scanner = new Scanner(System.in);
        System.out.println("Saisissez une ligne");


        boolean saisieValide = false;
        while (!saisieValide) {
            try {
                ligne = my_scanner.nextInt();
                saisieValide = true;

            }
            catch (InputMismatchException e) {
                System.out.println("Erreur2");
                my_scanner.next();
            }


            catch (NoSuchElementException e) {
                System.out.println("Erreur1");
                break;
            }



        }


    }
}
