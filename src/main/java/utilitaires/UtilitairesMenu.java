package utilitaires;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public final class UtilitairesMenu {
    private static final DateTimeFormatter DATE_HEURE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private UtilitairesMenu() {
    }

    public static int lireChoixMenu(Scanner scanner, String titre, List<String> options) {
        afficherTitre(titre);
        for (int index = 0; index < options.size(); index++) {
            System.out.println((index + 1) + ". " + options.get(index));
        }
        System.out.println("0. Retour");
        return lireEntier(scanner, "Votre choix : ", 0, options.size());
    }

    public static String lireChaineNonVide(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String saisie = scanner.nextLine().trim();
            if (!saisie.isEmpty()) {
                return saisie;
            }
            afficherErreur("La saisie ne peut pas être vide.");
        }
    }

    public static String lireChaineOptionnelle(Scanner scanner, String message, String valeurActuelle) {
        System.out.print(message + " [Entrée pour conserver \"" + valeurActuelle + "\"] : ");
        String saisie = scanner.nextLine().trim();
        return saisie.isEmpty() ? valeurActuelle : saisie;
    }

    public static int lireEntier(Scanner scanner, String message, int min, int max) {
        while (true) {
            System.out.print(message);
            String saisie = scanner.nextLine().trim();
            try {
                int valeur = Integer.parseInt(saisie);
                if (valeur < min || valeur > max) {
                    afficherErreur("Veuillez saisir un nombre compris entre " + min + " et " + max + ".");
                } else {
                    return valeur;
                }
            } catch (NumberFormatException exception) {
                afficherErreur("Veuillez saisir un nombre entier valide.");
            }
        }
    }

    public static int lireEntierMin(Scanner scanner, String message, int min) {
        while (true) {
            System.out.print(message);
            String saisie = scanner.nextLine().trim();
            try {
                int valeur = Integer.parseInt(saisie);
                if (valeur < min) {
                    afficherErreur("Veuillez saisir un nombre supérieur ou égal à " + min + ".");
                } else {
                    return valeur;
                }
            } catch (NumberFormatException exception) {
                afficherErreur("Veuillez saisir un nombre entier valide.");
            }
        }
    }

    public static LocalDateTime lireDateHeure(Scanner scanner, String message) {
        while (true) {
            System.out.print(message + " (format dd/MM/yyyy HH:mm) : ");
            String saisie = scanner.nextLine().trim();
            try {
                return LocalDateTime.parse(saisie, DATE_HEURE_FORMAT);
            } catch (DateTimeParseException exception) {
                afficherErreur("Format invalide. Exemple attendu : 25/12/2026 14:30");
            }
        }
    }

    public static LocalDate lireDate(Scanner scanner, String message) {
        while (true) {
            System.out.print(message + " (format dd/MM/yyyy) : ");
            String saisie = scanner.nextLine().trim();
            try {
                return LocalDate.parse(saisie, DATE_FORMAT);
            } catch (DateTimeParseException exception) {
                afficherErreur("Format invalide. Exemple attendu : 25/12/2026");
            }
        }
    }

    public static boolean demanderConfirmation(Scanner scanner, String message) {
        while (true) {
            System.out.print(message + " (O/N) : ");
            String saisie = scanner.nextLine().trim().toUpperCase();
            if ("O".equals(saisie) || "OUI".equals(saisie)) {
                return true;
            }
            if ("N".equals(saisie) || "NON".equals(saisie)) {
                return false;
            }
            afficherErreur("Réponse attendue : O ou N.");
        }
    }

    public static void afficherTitre(String titre) {
        System.out.println();
        System.out.println("=".repeat(70));
        System.out.println(titre);
        System.out.println("=".repeat(70));
    }

    public static void afficherSucces(String message) {
        System.out.println("[SUCCÈS] " + message);
    }

    public static void afficherErreur(String message) {
        System.out.println("[ERREUR] " + message);
    }

    public static void afficherInformation(String message) {
        System.out.println("[INFO] " + message);
    }

    public static void afficherListe(List<?> elements, String messageVide) {
        if (elements == null || elements.isEmpty()) {
            afficherInformation(messageVide);
            return;
        }
        for (Object element : elements) {
            System.out.println("- " + element);
        }
    }

    public static void pause(Scanner scanner) {
        System.out.print("Appuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }
}

