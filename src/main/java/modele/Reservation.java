package modele;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Reservation {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private String idReservation;
    private Passager passager;
    private Vol vol;
    private LocalDateTime dateReservation;
    private String numeroSiege;
    private StatutReservation statut;

    public Reservation(String idReservation, Passager passager, Vol vol, LocalDateTime dateReservation, String numeroSiege) {
        setIdReservation(idReservation);
        setPassager(passager);
        setVol(vol);
        setDateReservation(dateReservation);
        setNumeroSiege(numeroSiege);
        this.statut = StatutReservation.ACTIVE;
    }

    public String getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(String idReservation) {
        this.idReservation = normaliserTexte(idReservation, "L'identifiant de réservation est obligatoire.").toUpperCase();
    }

    public Passager getPassager() {
        return passager;
    }

    public void setPassager(Passager passager) {
        this.passager = Objects.requireNonNull(passager, "Le passager est obligatoire.");
    }

    public Vol getVol() {
        return vol;
    }

    public void setVol(Vol vol) {
        this.vol = Objects.requireNonNull(vol, "Le vol est obligatoire.");
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = Objects.requireNonNull(dateReservation, "La date de réservation est obligatoire.");
    }

    public String getNumeroSiege() {
        return numeroSiege;
    }

    public void setNumeroSiege(String numeroSiege) {
        this.numeroSiege = normaliserTexte(numeroSiege, "Le numéro de siège est obligatoire.").toUpperCase();
    }

    public StatutReservation getStatut() {
        return statut;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = Objects.requireNonNull(statut, "Le statut de réservation est obligatoire.");
    }

    public boolean estActive() {
        return statut == StatutReservation.ACTIVE;
    }

    public void annulerReservation() {
        if (!estActive()) {
            throw new IllegalStateException("La réservation est déjà annulée.");
        }
        this.statut = StatutReservation.ANNULEE;
    }

    public String afficherReservation() {
        return String.format(
                "Réservation[id=%s, passager=%s, vol=%s, date=%s, siège=%s, statut=%s]",
                idReservation,
                passager.getNomComplet(),
                vol.getNumeroVol(),
                dateReservation.format(FORMATTER),
                numeroSiege,
                statut
        );
    }

    private String normaliserTexte(String valeur, String messageErreur) {
        Objects.requireNonNull(valeur, messageErreur);
        String resultat = valeur.trim();
        if (resultat.isEmpty()) {
            throw new IllegalArgumentException(messageErreur);
        }
        return resultat;
    }

    @Override
    public String toString() {
        return afficherReservation();
    }
}

