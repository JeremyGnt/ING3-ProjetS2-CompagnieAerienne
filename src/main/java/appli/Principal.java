package appli;

import modele.Avion;
import modele.Passager;
import modele.Reservation;
import modele.Vol;
import service.CompagnieAerienne;
import utilitaires.UtilitairesMenu;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private static final DateTimeFormatter FORMAT_DATE_DEMO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        CompagnieAerienne compagnieAerienne = new CompagnieAerienne();
        List<String> guideDemonstration = initialiserDonneesDemo(compagnieAerienne);
        afficherGuideDemonstration(guideDemonstration);

        try (Scanner scanner = new Scanner(System.in)) {
            lancerApplication(scanner, compagnieAerienne);
        }
    }

    private static void lancerApplication(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        boolean quitter = false;
        while (!quitter) {
            int choix = UtilitairesMenu.lireChoixMenu(scanner, "MENU PRINCIPAL - GESTION DE LA COMPAGNIE AÉRIENNE", List.of(
                    "Gestion des avions",
                    "Gestion des vols",
                    "Gestion des passagers",
                    "Gestion des réservations",
                    "Affichage et consultation",
                    "Fonctionnalités avancées"
            ));

            switch (choix) {
                case 1 -> menuAvions(scanner, compagnieAerienne);
                case 2 -> menuVols(scanner, compagnieAerienne);
                case 3 -> menuPassagers(scanner, compagnieAerienne);
                case 4 -> menuReservations(scanner, compagnieAerienne);
                case 5 -> menuAffichage(scanner, compagnieAerienne);
                case 6 -> menuFonctionnalitesAvancees(scanner, compagnieAerienne);
                case 0 -> quitter = UtilitairesMenu.demanderConfirmation(scanner, "Voulez-vous vraiment quitter l'application ?");
                default -> UtilitairesMenu.afficherErreur("Choix invalide.");
            }
        }
        UtilitairesMenu.afficherInformation("Au revoir.");
    }

    private static void menuAvions(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        boolean retour = false;
        while (!retour) {
            int choix = UtilitairesMenu.lireChoixMenu(scanner, "GESTION DES AVIONS", List.of(
                    "Ajouter un avion",
                    "Modifier un avion",
                    "Supprimer un avion",
                    "Afficher la liste des avions",
                    "Rechercher un avion"
            ));
            switch (choix) {
                case 1 -> executerAction(scanner, () -> ajouterAvion(scanner, compagnieAerienne));
                case 2 -> executerAction(scanner, () -> modifierAvion(scanner, compagnieAerienne));
                case 3 -> executerAction(scanner, () -> supprimerAvion(scanner, compagnieAerienne));
                case 4 -> executerAction(scanner, () -> afficherAvions(compagnieAerienne));
                case 5 -> executerAction(scanner, () -> rechercherAvion(scanner, compagnieAerienne));
                case 0 -> retour = true;
                default -> UtilitairesMenu.afficherErreur("Choix invalide.");
            }
        }
    }

    private static void menuVols(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        boolean retour = false;
        while (!retour) {
            int choix = UtilitairesMenu.lireChoixMenu(scanner, "GESTION DES VOLS", List.of(
                    "Ajouter un vol",
                    "Modifier un vol",
                    "Supprimer un vol",
                    "Afficher la liste des vols",
                    "Rechercher un vol",
                    "Afficher les vols disponibles"
            ));
            switch (choix) {
                case 1 -> executerAction(scanner, () -> ajouterVol(scanner, compagnieAerienne));
                case 2 -> executerAction(scanner, () -> modifierVol(scanner, compagnieAerienne));
                case 3 -> executerAction(scanner, () -> supprimerVol(scanner, compagnieAerienne));
                case 4 -> executerAction(scanner, () -> afficherVols(compagnieAerienne.getListeVols(), "Aucun vol enregistré."));
                case 5 -> executerAction(scanner, () -> rechercherVol(scanner, compagnieAerienne));
                case 6 -> executerAction(scanner, () -> afficherVols(compagnieAerienne.afficherVolsDisponibles(), "Aucun vol disponible."));
                case 0 -> retour = true;
                default -> UtilitairesMenu.afficherErreur("Choix invalide.");
            }
        }
    }

    private static void menuPassagers(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        boolean retour = false;
        while (!retour) {
            int choix = UtilitairesMenu.lireChoixMenu(scanner, "GESTION DES PASSAGERS", List.of(
                    "Ajouter un passager",
                    "Modifier un passager",
                    "Supprimer un passager",
                    "Afficher la liste des passagers",
                    "Rechercher un passager"
            ));
            switch (choix) {
                case 1 -> executerAction(scanner, () -> ajouterPassager(scanner, compagnieAerienne));
                case 2 -> executerAction(scanner, () -> modifierPassager(scanner, compagnieAerienne));
                case 3 -> executerAction(scanner, () -> supprimerPassager(scanner, compagnieAerienne));
                case 4 -> executerAction(scanner, () -> afficherPassagers(compagnieAerienne.getListePassagers(), "Aucun passager enregistré."));
                case 5 -> executerAction(scanner, () -> rechercherPassager(scanner, compagnieAerienne));
                case 0 -> retour = true;
                default -> UtilitairesMenu.afficherErreur("Choix invalide.");
            }
        }
    }

    private static void menuReservations(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        boolean retour = false;
        while (!retour) {
            int choix = UtilitairesMenu.lireChoixMenu(scanner, "GESTION DES RÉSERVATIONS", List.of(
                    "Réserver un billet",
                    "Annuler une réservation",
                    "Afficher les passagers d'un vol",
                    "Afficher les vols réservés par un passager",
                    "Afficher les réservations en cours"
            ));
            switch (choix) {
                case 1 -> executerAction(scanner, () -> reserverVol(scanner, compagnieAerienne));
                case 2 -> executerAction(scanner, () -> annulerReservation(scanner, compagnieAerienne));
                case 3 -> executerAction(scanner, () -> afficherPassagersDunVol(scanner, compagnieAerienne));
                case 4 -> executerAction(scanner, () -> afficherVolsDunPassager(scanner, compagnieAerienne));
                case 5 -> executerAction(scanner, () -> afficherReservations(compagnieAerienne.afficherReservationsEnCours(), "Aucune réservation active."));
                case 0 -> retour = true;
                default -> UtilitairesMenu.afficherErreur("Choix invalide.");
            }
        }
    }

    private static void menuAffichage(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        boolean retour = false;
        while (!retour) {
            int choix = UtilitairesMenu.lireChoixMenu(scanner, "AFFICHAGE ET CONSULTATION", List.of(
                    "Afficher tous les avions",
                    "Afficher tous les vols",
                    "Afficher tous les passagers",
                    "Afficher les vols disponibles",
                    "Afficher les réservations en cours"
            ));
            switch (choix) {
                case 1 -> executerAction(scanner, () -> afficherAvions(compagnieAerienne));
                case 2 -> executerAction(scanner, () -> afficherVols(compagnieAerienne.getListeVols(), "Aucun vol enregistré."));
                case 3 -> executerAction(scanner, () -> afficherPassagers(compagnieAerienne.getListePassagers(), "Aucun passager enregistré."));
                case 4 -> executerAction(scanner, () -> afficherVols(compagnieAerienne.afficherVolsDisponibles(), "Aucun vol disponible."));
                case 5 -> executerAction(scanner, () -> afficherReservations(compagnieAerienne.afficherReservationsEnCours(), "Aucune réservation active."));
                case 0 -> retour = true;
                default -> UtilitairesMenu.afficherErreur("Choix invalide.");
            }
        }
    }

    private static void menuFonctionnalitesAvancees(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        boolean retour = false;
        while (!retour) {
            int choix = UtilitairesMenu.lireChoixMenu(scanner, "FONCTIONNALITÉS AVANCÉES", List.of(
                    "Recherche multicritère des vols (destination + date)",
                    "Trier les vols par destination",
                    "Trier les vols par date",
                    "Calculer le taux de remplissage d'un vol"
            ));
            switch (choix) {
                case 1 -> executerAction(scanner, () -> rechercheMulticritereVol(scanner, compagnieAerienne));
                case 2 -> executerAction(scanner, () -> afficherVols(compagnieAerienne.trierVolsParDestination(), "Aucun vol enregistré."));
                case 3 -> executerAction(scanner, () -> afficherVols(compagnieAerienne.trierVolsParDate(), "Aucun vol enregistré."));
                case 4 -> executerAction(scanner, () -> afficherTauxRemplissage(scanner, compagnieAerienne));
                case 0 -> retour = true;
                default -> UtilitairesMenu.afficherErreur("Choix invalide.");
            }
        }
    }

    private static void ajouterAvion(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        UtilitairesMenu.afficherTitre("AJOUT D'UN AVION");
        String id = UtilitairesMenu.lireChaineNonVide(scanner, "Identifiant : ");
        String modele = UtilitairesMenu.lireChaineNonVide(scanner, "Modèle : ");
        int capacite = UtilitairesMenu.lireEntierMin(scanner, "Capacité : ", 1);
        String compagnie = UtilitairesMenu.lireChaineNonVide(scanner, "Compagnie : ");
        int anneeService = UtilitairesMenu.lireEntierMin(scanner, "Année de mise en service : ", 1950);

        compagnieAerienne.ajouterAvion(new Avion(id, modele, capacite, compagnie, anneeService));
        UtilitairesMenu.afficherSucces("Avion ajouté avec succès.");
    }

    private static void modifierAvion(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        verifierPresence(compagnieAerienne.contientAvions(), "Aucun avion à modifier.");
        afficherAvions(compagnieAerienne);
        String idActuel = UtilitairesMenu.lireChaineNonVide(scanner, "Identifiant de l'avion à modifier : ");
        Avion avion = compagnieAerienne.obtenirAvionParId(idActuel);
        System.out.println("Avion sélectionné : " + avion.afficherDetails());

        String nouvelId = UtilitairesMenu.lireChaineNonVide(scanner, "Nouvel identifiant : ");
        String modele = UtilitairesMenu.lireChaineNonVide(scanner, "Nouveau modèle : ");
        int capacite = UtilitairesMenu.lireEntierMin(scanner, "Nouvelle capacité : ", 1);
        String compagnie = UtilitairesMenu.lireChaineNonVide(scanner, "Nouvelle compagnie : ");
        int anneeService = UtilitairesMenu.lireEntierMin(scanner, "Nouvelle année de mise en service : ", 1950);

        compagnieAerienne.modifierAvion(avion.getId(), nouvelId, modele, capacite, compagnie, anneeService);
        UtilitairesMenu.afficherSucces("Avion modifié avec succès.");
    }

    private static void supprimerAvion(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        verifierPresence(compagnieAerienne.contientAvions(), "Aucun avion à supprimer.");
        afficherAvions(compagnieAerienne);
        String id = UtilitairesMenu.lireChaineNonVide(scanner, "Identifiant de l'avion à supprimer : ");
        if (UtilitairesMenu.demanderConfirmation(scanner, "Confirmer la suppression ?")) {
            compagnieAerienne.supprimerAvion(id);
            UtilitairesMenu.afficherSucces("Avion supprimé avec succès.");
        }
    }

    private static void afficherAvions(CompagnieAerienne compagnieAerienne) {
        UtilitairesMenu.afficherTitre("LISTE DES AVIONS");
        UtilitairesMenu.afficherListe(compagnieAerienne.getListeAvions(), "Aucun avion enregistré.");
    }

    private static void rechercherAvion(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        verifierPresence(compagnieAerienne.contientAvions(), "Aucun avion enregistré.");
        int choix = UtilitairesMenu.lireChoixMenu(scanner, "RECHERCHE D'UN AVION", List.of(
                "Par identifiant",
                "Par modèle"
        ));
        switch (choix) {
            case 1 -> {
                String id = UtilitairesMenu.lireChaineNonVide(scanner, "Identifiant : ");
                System.out.println(compagnieAerienne.obtenirAvionParId(id).afficherDetails());
            }
            case 2 -> {
                String modele = UtilitairesMenu.lireChaineNonVide(scanner, "Modèle : ");
                UtilitairesMenu.afficherListe(compagnieAerienne.rechercherAvionParModele(modele), "Aucun avion trouvé pour ce modèle.");
            }
            case 0 -> UtilitairesMenu.afficherInformation("Retour au menu précédent.");
            default -> UtilitairesMenu.afficherErreur("Choix invalide.");
        }
    }

    private static void ajouterVol(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        verifierPresence(compagnieAerienne.contientAvions(), "Ajoutez d'abord un avion avant de créer un vol.");
        UtilitairesMenu.afficherTitre("AJOUT D'UN VOL");
        afficherAvions(compagnieAerienne);
        String numeroVol = UtilitairesMenu.lireChaineNonVide(scanner, "Numéro du vol : ");
        String villeDepart = UtilitairesMenu.lireChaineNonVide(scanner, "Ville de départ : ");
        String villeDestination = UtilitairesMenu.lireChaineNonVide(scanner, "Ville de destination : ");
        LocalDateTime dateDepart = UtilitairesMenu.lireDateHeure(scanner, "Date et heure de départ");
        String idAvion = UtilitairesMenu.lireChaineNonVide(scanner, "Identifiant de l'avion utilisé : ");

        compagnieAerienne.ajouterVol(numeroVol, villeDepart, villeDestination, dateDepart, idAvion);
        UtilitairesMenu.afficherSucces("Vol ajouté avec succès.");
    }

    private static void modifierVol(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        verifierPresence(compagnieAerienne.contientVols(), "Aucun vol à modifier.");
        afficherVols(compagnieAerienne.getListeVols(), "Aucun vol enregistré.");
        String numeroActuel = UtilitairesMenu.lireChaineNonVide(scanner, "Numéro du vol à modifier : ");
        Vol vol = compagnieAerienne.obtenirVolParNumero(numeroActuel);
        System.out.println("Vol sélectionné : " + vol.afficherDetails());
        afficherAvions(compagnieAerienne);

        String nouveauNumero = UtilitairesMenu.lireChaineNonVide(scanner, "Nouveau numéro du vol : ");
        String villeDepart = UtilitairesMenu.lireChaineNonVide(scanner, "Nouvelle ville de départ : ");
        String villeDestination = UtilitairesMenu.lireChaineNonVide(scanner, "Nouvelle ville de destination : ");
        LocalDateTime dateDepart = UtilitairesMenu.lireDateHeure(scanner, "Nouvelle date et heure de départ");
        String idAvion = UtilitairesMenu.lireChaineNonVide(scanner, "Identifiant du nouvel avion : ");

        compagnieAerienne.modifierVol(vol.getNumeroVol(), nouveauNumero, villeDepart, villeDestination, dateDepart, idAvion);
        UtilitairesMenu.afficherSucces("Vol modifié avec succès.");
    }

    private static void supprimerVol(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        verifierPresence(compagnieAerienne.contientVols(), "Aucun vol à supprimer.");
        afficherVols(compagnieAerienne.getListeVols(), "Aucun vol enregistré.");
        String numeroVol = UtilitairesMenu.lireChaineNonVide(scanner, "Numéro du vol à supprimer : ");
        if (UtilitairesMenu.demanderConfirmation(scanner, "Confirmer la suppression du vol ?")) {
            compagnieAerienne.supprimerVol(numeroVol);
            UtilitairesMenu.afficherSucces("Vol supprimé avec succès.");
        }
    }

    private static void rechercherVol(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        verifierPresence(compagnieAerienne.contientVols(), "Aucun vol enregistré.");
        int choix = UtilitairesMenu.lireChoixMenu(scanner, "RECHERCHE D'UN VOL", List.of(
                "Par numéro",
                "Par destination"
        ));
        switch (choix) {
            case 1 -> {
                String numero = UtilitairesMenu.lireChaineNonVide(scanner, "Numéro du vol : ");
                System.out.println(compagnieAerienne.obtenirVolParNumero(numero).afficherDetails());
            }
            case 2 -> {
                String destination = UtilitairesMenu.lireChaineNonVide(scanner, "Destination : ");
                afficherVols(compagnieAerienne.rechercherVolParDestination(destination), "Aucun vol trouvé pour cette destination.");
            }
            case 0 -> UtilitairesMenu.afficherInformation("Retour au menu précédent.");
            default -> UtilitairesMenu.afficherErreur("Choix invalide.");
        }
    }

    private static void ajouterPassager(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        UtilitairesMenu.afficherTitre("AJOUT D'UN PASSAGER");
        String id = UtilitairesMenu.lireChaineNonVide(scanner, "Identifiant : ");
        String nom = UtilitairesMenu.lireChaineNonVide(scanner, "Nom : ");
        String prenom = UtilitairesMenu.lireChaineNonVide(scanner, "Prénom : ");
        String numeroPasseport = UtilitairesMenu.lireChaineNonVide(scanner, "Numéro de passeport : ");
        String nationalite = UtilitairesMenu.lireChaineNonVide(scanner, "Nationalité : ");

        compagnieAerienne.ajouterPassager(new Passager(id, nom, prenom, numeroPasseport, nationalite));
        UtilitairesMenu.afficherSucces("Passager ajouté avec succès.");
    }

    private static void modifierPassager(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        verifierPresence(compagnieAerienne.contientPassagers(), "Aucun passager à modifier.");
        afficherPassagers(compagnieAerienne.getListePassagers(), "Aucun passager enregistré.");
        String idActuel = UtilitairesMenu.lireChaineNonVide(scanner, "Identifiant du passager à modifier : ");
        Passager passager = compagnieAerienne.obtenirPassagerParId(idActuel);
        System.out.println("Passager sélectionné : " + passager.afficherDetails());

        String nouvelId = UtilitairesMenu.lireChaineNonVide(scanner, "Nouvel identifiant : ");
        String nom = UtilitairesMenu.lireChaineNonVide(scanner, "Nouveau nom : ");
        String prenom = UtilitairesMenu.lireChaineNonVide(scanner, "Nouveau prénom : ");
        String numeroPasseport = UtilitairesMenu.lireChaineNonVide(scanner, "Nouveau numéro de passeport : ");
        String nationalite = UtilitairesMenu.lireChaineNonVide(scanner, "Nouvelle nationalité : ");

        compagnieAerienne.modifierPassager(passager.getId(), nouvelId, nom, prenom, numeroPasseport, nationalite);
        UtilitairesMenu.afficherSucces("Passager modifié avec succès.");
    }

    private static void supprimerPassager(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        verifierPresence(compagnieAerienne.contientPassagers(), "Aucun passager à supprimer.");
        afficherPassagers(compagnieAerienne.getListePassagers(), "Aucun passager enregistré.");
        String id = UtilitairesMenu.lireChaineNonVide(scanner, "Identifiant du passager à supprimer : ");
        if (UtilitairesMenu.demanderConfirmation(scanner, "Confirmer la suppression du passager ?")) {
            compagnieAerienne.supprimerPassager(id);
            UtilitairesMenu.afficherSucces("Passager supprimé avec succès.");
        }
    }

    private static void rechercherPassager(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        verifierPresence(compagnieAerienne.contientPassagers(), "Aucun passager enregistré.");
        int choix = UtilitairesMenu.lireChoixMenu(scanner, "RECHERCHE D'UN PASSAGER", List.of(
                "Par identifiant",
                "Par nom ou prénom"
        ));
        switch (choix) {
            case 1 -> {
                String id = UtilitairesMenu.lireChaineNonVide(scanner, "Identifiant : ");
                System.out.println(compagnieAerienne.obtenirPassagerParId(id).afficherDetails());
            }
            case 2 -> {
                String nom = UtilitairesMenu.lireChaineNonVide(scanner, "Nom ou prénom : ");
                afficherPassagers(compagnieAerienne.rechercherPassagerParNom(nom), "Aucun passager trouvé.");
            }
            case 0 -> UtilitairesMenu.afficherInformation("Retour au menu précédent.");
            default -> UtilitairesMenu.afficherErreur("Choix invalide.");
        }
    }

    private static void reserverVol(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        verifierPresence(compagnieAerienne.contientPassagers(), "Ajoutez d'abord un passager.");
        verifierPresence(compagnieAerienne.contientVols(), "Ajoutez d'abord un vol.");

        UtilitairesMenu.afficherTitre("RÉSERVER UN BILLET");
        afficherPassagers(compagnieAerienne.getListePassagers(), "Aucun passager enregistré.");
        String idPassager = UtilitairesMenu.lireChaineNonVide(scanner, "Identifiant du passager : ");
        afficherVols(compagnieAerienne.afficherVolsDisponibles(), "Aucun vol disponible.");
        String numeroVol = UtilitairesMenu.lireChaineNonVide(scanner, "Numéro du vol : ");

        String numeroSiege = "";
        boolean choixManuel = UtilitairesMenu.demanderConfirmation(scanner, "Voulez-vous choisir manuellement un siège ?");
        if (!choixManuel) {
            UtilitairesMenu.afficherInformation("Le prochain siège disponible sera attribué automatiquement.");
        } else {
            numeroSiege = UtilitairesMenu.lireChaineNonVide(scanner, "Numéro du siège (exemple 1A) : ");
        }

        Reservation reservation = compagnieAerienne.reserverVol(idPassager, numeroVol, numeroSiege);
        UtilitairesMenu.afficherSucces("Réservation effectuée avec succès : " + reservation.afficherReservation());
    }

    private static void annulerReservation(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        verifierPresence(compagnieAerienne.contientReservations(), "Aucune réservation enregistrée.");
        afficherReservations(compagnieAerienne.getListeReservations(), "Aucune réservation enregistrée.");
        String idReservation = UtilitairesMenu.lireChaineNonVide(scanner, "Identifiant de la réservation à annuler : ");
        if (UtilitairesMenu.demanderConfirmation(scanner, "Confirmer l'annulation ?")) {
            compagnieAerienne.annulerReservation(idReservation);
            UtilitairesMenu.afficherSucces("Réservation annulée avec succès.");
        }
    }

    private static void afficherPassagersDunVol(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        verifierPresence(compagnieAerienne.contientVols(), "Aucun vol enregistré.");
        String numeroVol = UtilitairesMenu.lireChaineNonVide(scanner, "Numéro du vol : ");
        List<Passager> passagers = compagnieAerienne.afficherPassagersDunVol(numeroVol);
        UtilitairesMenu.afficherTitre("PASSAGERS DU VOL " + numeroVol.toUpperCase());
        afficherPassagers(passagers, "Aucun passager pour ce vol.");
    }

    private static void afficherVolsDunPassager(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        verifierPresence(compagnieAerienne.contientPassagers(), "Aucun passager enregistré.");
        String idPassager = UtilitairesMenu.lireChaineNonVide(scanner, "Identifiant du passager : ");
        List<Vol> vols = compagnieAerienne.afficherVolsReservesParPassager(idPassager);
        UtilitairesMenu.afficherTitre("VOLS RÉSERVÉS PAR LE PASSAGER " + idPassager.toUpperCase());
        afficherVols(vols, "Aucun vol réservé pour ce passager.");
    }

    private static void rechercheMulticritereVol(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        verifierPresence(compagnieAerienne.contientVols(), "Aucun vol enregistré.");
        String destination = UtilitairesMenu.lireChaineNonVide(scanner, "Destination recherchée : ");
        LocalDate date = UtilitairesMenu.lireDate(scanner, "Date recherchée");
        List<Vol> vols = compagnieAerienne.rechercherVolsParDestinationEtDate(destination, date);
        UtilitairesMenu.afficherTitre("RÉSULTAT DE LA RECHERCHE MULTICRITÈRE");
        afficherVols(vols, "Aucun vol trouvé pour cette destination et cette date.");
    }

    private static void afficherTauxRemplissage(Scanner scanner, CompagnieAerienne compagnieAerienne) {
        verifierPresence(compagnieAerienne.contientVols(), "Aucun vol enregistré.");
        String numeroVol = UtilitairesMenu.lireChaineNonVide(scanner, "Numéro du vol : ");
        double taux = compagnieAerienne.calculerTauxRemplissage(numeroVol);
        UtilitairesMenu.afficherInformation(String.format("Taux de remplissage du vol %s : %.2f%%", numeroVol.toUpperCase(), taux));
    }

    private static void afficherVols(List<Vol> vols, String messageVide) {
        UtilitairesMenu.afficherTitre("LISTE DES VOLS");
        UtilitairesMenu.afficherListe(vols, messageVide);
    }

    private static void afficherPassagers(List<Passager> passagers, String messageVide) {
        UtilitairesMenu.afficherTitre("LISTE DES PASSAGERS");
        UtilitairesMenu.afficherListe(passagers, messageVide);
    }

    private static void afficherReservations(List<Reservation> reservations, String messageVide) {
        UtilitairesMenu.afficherTitre("LISTE DES RÉSERVATIONS");
        UtilitairesMenu.afficherListe(reservations, messageVide);
    }

    private static void verifierPresence(boolean condition, String messageErreur) {
        if (!condition) {
            throw new IllegalStateException(messageErreur);
        }
    }

    private static void executerAction(Scanner scanner, ActionMenu actionMenu) {
        try {
            actionMenu.executer();
        } catch (RuntimeException exception) {
            UtilitairesMenu.afficherErreur(exception.getMessage());
        }
        UtilitairesMenu.pause(scanner);
    }

    private static List<String> initialiserDonneesDemo(CompagnieAerienne compagnieAerienne) {
        try {
            compagnieAerienne.ajouterAvion(new Avion("AV-100", "Airbus A320", 180, "Air France", 2018));
            compagnieAerienne.ajouterAvion(new Avion("AV-200", "Boeing 737", 160, "Transavia", 2019));
            compagnieAerienne.ajouterAvion(new Avion("AV-300", "Embraer E190", 100, "Regional Wings", 2020));
            compagnieAerienne.ajouterAvion(new Avion("AV-400", "Airbus A321", 220, "Air France", 2021));

            compagnieAerienne.ajouterPassager(new Passager("P-001", "Dupont", "Alice", "FR123456", "Française"));
            compagnieAerienne.ajouterPassager(new Passager("P-002", "Martin", "Louis", "FR654321", "Française"));
            compagnieAerienne.ajouterPassager(new Passager("P-003", "Garcia", "Elena", "ES112233", "Espagnole"));
            compagnieAerienne.ajouterPassager(new Passager("P-004", "Diallo", "Amadou", "SN778899", "Sénégalaise"));
            compagnieAerienne.ajouterPassager(new Passager("P-005", "Benali", "Sara", "MA445566", "Marocaine"));
            compagnieAerienne.ajouterPassager(new Passager("P-006", "Rossi", "Marco", "IT778811", "Italienne"));

            LocalDateTime dateRome = LocalDateTime.now().plusDays(2).withHour(9).withMinute(30).withSecond(0).withNano(0);
            LocalDateTime dateMadrid = LocalDateTime.now().plusDays(3).withHour(14).withMinute(15).withSecond(0).withNano(0);
            LocalDateTime dateBerlin = LocalDateTime.now().plusDays(4).withHour(18).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime dateNice = LocalDateTime.now().plusDays(5).withHour(11).withMinute(10).withSecond(0).withNano(0);
            LocalDateTime dateDublin = LocalDateTime.now().plusDays(6).withHour(7).withMinute(45).withSecond(0).withNano(0);

            compagnieAerienne.ajouterVol("AF101", "Paris", "Rome", dateRome, "AV-100");
            compagnieAerienne.ajouterVol("AF145", "Paris", "Rome", dateRome.withHour(16).withMinute(45), "AV-400");
            compagnieAerienne.ajouterVol("TV220", "Lyon", "Madrid", dateMadrid, "AV-200");
            compagnieAerienne.ajouterVol("RW330", "Marseille", "Berlin", dateBerlin, "AV-300");
            compagnieAerienne.ajouterVol("AP404", "Bordeaux", "Nice", dateNice, "AV-300");
            compagnieAerienne.ajouterVol("ML510", "Lille", "Dublin", dateDublin, "AV-100");

            Reservation reservationAliceRome = compagnieAerienne.reserverVol("P-001", "AF101", "");
            Reservation reservationAliceMadrid = compagnieAerienne.reserverVol("P-001", "TV220", "3C");
            Reservation reservationLouisRome = compagnieAerienne.reserverVol("P-002", "AF101", "2A");
            Reservation reservationElenaMadrid = compagnieAerienne.reserverVol("P-003", "TV220", "2B");
            Reservation reservationAmadouRome = compagnieAerienne.reserverVol("P-004", "AF145", "4D");
            Reservation reservationSaraBerlin = compagnieAerienne.reserverVol("P-005", "RW330", "1C");

            return List.of(
                    "Passager P-001 (Alice Dupont) : 2 réservations actives sur AF101 et TV220.",
                    "Vol AF101 : déjà réservé par P-001 et P-002.",
                    "Vol TV220 : déjà réservé par P-001 et P-003.",
                    "Recherche multicritère prête : destination Rome, date " + dateRome.toLocalDate().format(FORMAT_DATE_DEMO) + ".",
                    "Réserver en direct : utiliser P-006 (Marco Rossi) sur AP404 ou ML510.",
                    "Annuler en direct : utiliser " + reservationAliceMadrid.getIdReservation() + ", "
                            + reservationLouisRome.getIdReservation() + ", "
                            + reservationElenaMadrid.getIdReservation() + ", "
                            + reservationAmadouRome.getIdReservation() + " ou "
                            + reservationSaraBerlin.getIdReservation() + ".",
                    "Siège auto déjà attribué : " + reservationAliceRome.getIdReservation() + " pour P-001 sur AF101."
            );
        } catch (RuntimeException exception) {
            throw new IllegalStateException("Erreur lors de l'initialisation des données de démonstration : " + exception.getMessage(), exception);
        }
    }

    private static void afficherGuideDemonstration(List<String> guideDemonstration) {
        UtilitairesMenu.afficherTitre("DONNÉES DE DÉMONSTRATION CHARGÉES");
        UtilitairesMenu.afficherInformation("Le projet démarre avec un scénario prêt pour la soutenance :");
        UtilitairesMenu.afficherListe(guideDemonstration, "Aucune donnée de démonstration chargée.");
        System.out.println();
    }

    @FunctionalInterface
    private interface ActionMenu {
        void executer();
    }
}

