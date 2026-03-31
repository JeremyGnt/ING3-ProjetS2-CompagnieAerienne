package modele;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Vol {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final Pattern PATTERN_SIEGE = Pattern.compile("^(\\d+)([A-F])$");

    private String numeroVol;
    private String villeDepart;
    private String villeDestination;
    private LocalDateTime dateDepart;
    private Avion avion;
    private int placesDisponibles;
    private final Set<String> siegesOccupes;

    public Vol(String numeroVol, String villeDepart, String villeDestination, LocalDateTime dateDepart, Avion avion) {
        this.siegesOccupes = new LinkedHashSet<>();
        setNumeroVol(numeroVol);
        setVilleDepart(villeDepart);
        setVilleDestination(villeDestination);
        setDateDepart(dateDepart);
        setAvion(avion);
        this.placesDisponibles = avion.getCapacite();
    }

    public String getNumeroVol() {
        return numeroVol;
    }

    public void setNumeroVol(String numeroVol) {
        this.numeroVol = normaliserTexte(numeroVol, "Le numéro du vol est obligatoire.").toUpperCase();
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        String valeur = capitaliser(normaliserTexte(villeDepart, "La ville de départ est obligatoire."));
        verifierVillesDifferentes(valeur, this.villeDestination);
        this.villeDepart = valeur;
    }

    public String getVilleDestination() {
        return villeDestination;
    }

    public void setVilleDestination(String villeDestination) {
        String valeur = capitaliser(normaliserTexte(villeDestination, "La ville de destination est obligatoire."));
        verifierVillesDifferentes(this.villeDepart, valeur);
        this.villeDestination = valeur;
    }

    public LocalDateTime getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(LocalDateTime dateDepart) {
        Objects.requireNonNull(dateDepart, "La date de départ est obligatoire.");
        this.dateDepart = dateDepart;
    }

    public Avion getAvion() {
        return avion;
    }

    public void setAvion(Avion avion) {
        Objects.requireNonNull(avion, "L'avion du vol est obligatoire.");
        if (siegesOccupes.size() > avion.getCapacite()) {
            throw new IllegalArgumentException("Le nouvel avion ne dispose pas d'une capacité suffisante pour les réservations existantes.");
        }
        this.avion = avion;
        recalculerPlacesDisponibles();
    }

    public int getPlacesDisponibles() {
        return placesDisponibles;
    }

    public Set<String> getSiegesOccupes() {
        return new LinkedHashSet<>(siegesOccupes);
    }

    public void reserverPlace(String numeroSiege) {
        String siegeNormalise = normaliserSiege(numeroSiege);
        if (!numeroSiegeEstValide(siegeNormalise)) {
            throw new IllegalArgumentException("Le numéro de siège est invalide pour la capacité de l'avion.");
        }
        if (siegesOccupes.contains(siegeNormalise)) {
            throw new IllegalStateException("Le siège " + siegeNormalise + " est déjà occupé.");
        }
        if (placesDisponibles <= 0) {
            throw new IllegalStateException("Le vol est complet.");
        }
        siegesOccupes.add(siegeNormalise);
        placesDisponibles--;
    }

    public void libererPlace(String numeroSiege) {
        String siegeNormalise = normaliserSiege(numeroSiege);
        if (siegesOccupes.remove(siegeNormalise)) {
            placesDisponibles++;
        }
    }

    public boolean estComplet() {
        return placesDisponibles <= 0;
    }

    public boolean siegeEstDisponible(String numeroSiege) {
        String siegeNormalise = normaliserSiege(numeroSiege);
        return numeroSiegeEstValide(siegeNormalise) && !siegesOccupes.contains(siegeNormalise);
    }

    public String genererNumeroSiegeAutomatique() {
        for (int index = 0; index < avion.getCapacite(); index++) {
            String siege = convertirIndexEnSiege(index);
            if (!siegesOccupes.contains(siege)) {
                return siege;
            }
        }
        throw new IllegalStateException("Aucun siège disponible sur ce vol.");
    }

    public double getTauxRemplissage() {
        if (avion.getCapacite() == 0) {
            return 0.0;
        }
        return (double) siegesOccupes.size() / avion.getCapacite() * 100.0;
    }

    public void recalculerPlacesDisponibles() {
        placesDisponibles = avion.getCapacite() - siegesOccupes.size();
    }

    public String afficherDetails() {
        return String.format(
                "Vol[numéro=%s, départ=%s, destination=%s, date=%s, avion=%s, places disponibles=%d/%d, remplissage=%.2f%%]",
                numeroVol,
                villeDepart,
                villeDestination,
                dateDepart.format(FORMATTER),
                avion.getModele() + " (" + avion.getId() + ")",
                placesDisponibles,
                avion.getCapacite(),
                getTauxRemplissage()
        );
    }

    private void verifierVillesDifferentes(String depart, String destination) {
        if (depart != null && destination != null && depart.equalsIgnoreCase(destination)) {
            throw new IllegalArgumentException("La ville de départ doit être différente de la ville de destination.");
        }
    }

    private String normaliserTexte(String valeur, String messageErreur) {
        Objects.requireNonNull(valeur, messageErreur);
        String resultat = valeur.trim();
        if (resultat.isEmpty()) {
            throw new IllegalArgumentException(messageErreur);
        }
        return resultat;
    }

    private String capitaliser(String texte) {
        String minuscule = texte.toLowerCase();
        return Character.toUpperCase(minuscule.charAt(0)) + minuscule.substring(1);
    }

    private String normaliserSiege(String numeroSiege) {
        Objects.requireNonNull(numeroSiege, "Le numéro de siège est obligatoire.");
        String resultat = numeroSiege.trim().toUpperCase();
        if (resultat.isEmpty()) {
            throw new IllegalArgumentException("Le numéro de siège est obligatoire.");
        }
        return resultat;
    }

    private boolean numeroSiegeEstValide(String numeroSiege) {
        Matcher matcher = PATTERN_SIEGE.matcher(numeroSiege);
        if (!matcher.matches()) {
            return false;
        }
        int rangee = Integer.parseInt(matcher.group(1));
        int colonne = matcher.group(2).charAt(0) - 'A';
        int index = (rangee - 1) * 6 + colonne;
        return index >= 0 && index < avion.getCapacite();
    }

    private String convertirIndexEnSiege(int index) {
        int rangee = (index / 6) + 1;
        char lettre = (char) ('A' + (index % 6));
        return rangee + String.valueOf(lettre);
    }

    @Override
    public String toString() {
        return afficherDetails();
    }
}

