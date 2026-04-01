package appli;

import modele.Avion;
import modele.Passager;
import modele.Reservation;
import modele.StatutReservation;
import modele.Vol;
import service.CompagnieAerienne;

import java.time.LocalDateTime;
import java.util.List;

public final class SmokeTest {
    private SmokeTest() {
    }

    public static void main(String[] args) {
        try {
            executerScenarioDeConformite();
            System.out.println("[OK] Verification des fonctionnalites obligatoires reussie.");
        } catch (RuntimeException exception) {
            System.err.println("[ECHEC] " + exception.getMessage());
            exception.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private static void executerScenarioDeConformite() {
        CompagnieAerienne compagnie = new CompagnieAerienne();
        LocalDateTime maintenant = LocalDateTime.now().plusDays(5).withSecond(0).withNano(0);

        testerGestionAvions(compagnie);
        testerGestionVols(compagnie, maintenant);
        testerGestionPassagers(compagnie);
        testerReservationsEtConsultation(compagnie, maintenant.plusDays(1));
    }

    private static void testerGestionAvions(CompagnieAerienne compagnie) {
        compagnie.ajouterAvion(new Avion("AV-100", "Airbus A320", 180, "Air France", 2018));
        compagnie.ajouterAvion(new Avion("AV-200", "Boeing 737", 160, "Transavia", 2019));
        compagnie.ajouterAvion(new Avion("AV-300", "Embraer E190", 100, "Hop", 2020));

        assertEquals(3, compagnie.getListeAvions().size(), "L'ajout d'un avion devrait mettre a jour la liste.");
        assertEquals("AV-100", compagnie.obtenirAvionParId("av-100").getId(), "La recherche d'un avion par identifiant a echoue.");
        assertEquals(1, compagnie.rechercherAvionParModele("airbus").size(), "La recherche d'un avion par modele a echoue.");

        compagnie.modifierAvion("AV-200", "AV-250", "Boeing 737 Max", 170, "Transavia Europe", 2021);
        Avion avionModifie = compagnie.obtenirAvionParId("AV-250");
        assertEquals("Boeing 737 Max", avionModifie.getModele(), "La modification d'un avion a echoue.");
        assertEquals(170, avionModifie.getCapacite(), "La capacite de l'avion n'a pas ete modifiee.");

        compagnie.supprimerAvion("AV-300");
        assertEquals(2, compagnie.getListeAvions().size(), "La suppression d'un avion a echoue.");
    }

    private static void testerGestionVols(CompagnieAerienne compagnie, LocalDateTime maintenant) {
        compagnie.ajouterVol("AF101", "Paris", "Rome", maintenant, "AV-100");
        compagnie.ajouterVol("TV202", "Lyon", "Madrid", maintenant.plusHours(4), "AV-250");
        compagnie.ajouterVol("RW303", "Nice", "Berlin", maintenant.plusDays(1), "AV-100");

        assertEquals(3, compagnie.getListeVols().size(), "L'ajout d'un vol devrait mettre a jour la liste.");
        assertEquals("AF101", compagnie.obtenirVolParNumero("af101").getNumeroVol(), "La recherche d'un vol par numero a echoue.");
        assertEquals(1, compagnie.rechercherVolParDestination("madrid").size(), "La recherche d'un vol par destination a echoue.");

        compagnie.modifierVol("TV202", "TV999", "Marseille", "Lisbonne", maintenant.plusDays(2), "AV-100");
        Vol volModifie = compagnie.obtenirVolParNumero("TV999");
        assertEquals("Marseille", volModifie.getVilleDepart(), "La ville de depart du vol n'a pas ete modifiee.");
        assertEquals("Lisbonne", volModifie.getVilleDestination(), "La destination du vol n'a pas ete modifiee.");
        assertEquals(1, compagnie.rechercherVolsParDestinationEtDate("Rome", maintenant.toLocalDate()).size(),
                "La recherche multicritere des vols a echoue.");

        List<Vol> volsParDestination = compagnie.trierVolsParDestination();
        List<Vol> volsParDate = compagnie.trierVolsParDate();
        assertEquals(3, volsParDestination.size(), "Le tri des vols par destination a echoue.");
        assertEquals("Berlin", volsParDestination.get(0).getVilleDestination(), "Le tri des vols par destination est incorrect.");
        assertEquals(3, volsParDate.size(), "Le tri des vols par date a echoue.");
        assertEquals("AF101", volsParDate.get(0).getNumeroVol(), "Le tri des vols par date est incorrect.");

        compagnie.supprimerVol("RW303");
        assertEquals(2, compagnie.getListeVols().size(), "La suppression d'un vol a echoue.");
    }

    private static void testerGestionPassagers(CompagnieAerienne compagnie) {
        compagnie.ajouterPassager(new Passager("P-001", "Dupont", "Alice", "FR123456", "Francaise"));
        compagnie.ajouterPassager(new Passager("P-002", "Martin", "Louis", "FR654321", "Francaise"));
        compagnie.ajouterPassager(new Passager("P-003", "Garcia", "Elena", "ES112233", "Espagnole"));

        assertEquals(3, compagnie.getListePassagers().size(), "L'ajout d'un passager devrait mettre a jour la liste.");
        assertEquals("P-001", compagnie.obtenirPassagerParId("p-001").getId(), "La recherche d'un passager par identifiant a echoue.");
        assertEquals(1, compagnie.rechercherPassagerParNom("martin").size(), "La recherche d'un passager par nom a echoue.");

        compagnie.modifierPassager("P-002", "P-020", "Bernard", "Louis", "FR654322", "Francaise");
        Passager passagerModifie = compagnie.obtenirPassagerParId("P-020");
        assertEquals("BERNARD", passagerModifie.getNom(), "La modification d'un passager a echoue.");

        compagnie.supprimerPassager("P-003");
        assertEquals(2, compagnie.getListePassagers().size(), "La suppression d'un passager a echoue.");
    }

    private static void testerReservationsEtConsultation(CompagnieAerienne compagnie, LocalDateTime datePetitVol) {
        Reservation reservationAuto = compagnie.reserverVol("P-001", "AF101", "");
        Reservation reservationManuelle = compagnie.reserverVol("P-020", "AF101", "2A");

        assertEquals(StatutReservation.ACTIVE, reservationAuto.getStatut(), "La reservation devrait etre active.");
        assertEquals("1A", reservationAuto.getNumeroSiege(), "Le numero de siege automatique est incorrect.");
        assertTrue(!reservationAuto.getNumeroSiege().isBlank(), "Le numero de siege doit etre renseigne.");
        assertTrue(reservationAuto.getDateReservation() != null, "La date de reservation doit etre renseignee.");

        List<Passager> passagersDuVol = compagnie.afficherPassagersDunVol("AF101");
        assertEquals(2, passagersDuVol.size(), "L'affichage des passagers d'un vol a echoue.");

        List<Vol> volsDuPassager = compagnie.afficherVolsReservesParPassager("P-001");
        assertEquals(1, volsDuPassager.size(), "L'affichage des vols reserves par un passager a echoue.");

        compagnie.annulerReservation(reservationManuelle.getIdReservation());
        assertEquals(StatutReservation.ANNULEE, reservationManuelle.getStatut(), "L'annulation d'une reservation a echoue.");

        compagnie.ajouterAvion(new Avion("AV-400", "ATR 72", 1, "Regional", 2022));
        compagnie.ajouterVol("AT001", "Bordeaux", "Nantes", datePetitVol, "AV-400");
        compagnie.reserverVol("P-020", "AT001", "1A");

        assertThrows(() -> compagnie.reserverVol("P-001", "AT001", ""), "vol est complet");

        assertEquals(2, compagnie.afficherReservationsEnCours().size(), "L'affichage des reservations en cours est incorrect.");
        assertTrue(!compagnie.afficherVolsDisponibles().isEmpty(), "Les vols disponibles devraient etre affichables.");
        assertTrue(compagnie.calculerTauxRemplissage("AF101") > 0.0, "Le taux de remplissage devrait etre strictement positif.");

        assertTrue(compagnie.contientAvions(), "La liste des avions devrait etre accessible.");
        assertTrue(compagnie.contientVols(), "La liste des vols devrait etre accessible.");
        assertTrue(compagnie.contientPassagers(), "La liste des passagers devrait etre accessible.");
        assertTrue(compagnie.contientReservations(), "La liste des reservations devrait etre accessible.");
    }

    private static void assertEquals(Object attendu, Object obtenu, String message) {
        if (attendu == null ? obtenu != null : !attendu.equals(obtenu)) {
            throw new IllegalStateException(message + " Attendu=" + attendu + ", obtenu=" + obtenu);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    private static void assertThrows(Action action, String extraitMessage) {
        try {
            action.executer();
            throw new IllegalStateException("Une exception etait attendue, mais aucune n'a ete levee.");
        } catch (RuntimeException exception) {
            if (exception.getMessage() == null || !exception.getMessage().toLowerCase().contains(extraitMessage.toLowerCase())) {
                throw new IllegalStateException("Le message d'erreur attendu n'a pas ete recu: " + exception.getMessage(), exception);
            }
        }
    }

    @FunctionalInterface
    private interface Action {
        void executer();
    }
}
