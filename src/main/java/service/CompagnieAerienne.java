package service;

import modele.Avion;
import modele.Passager;
import modele.Reservation;
import modele.StatutReservation;
import modele.Vol;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CompagnieAerienne {
    private final ArrayList<Avion> listeAvions;
    private final ArrayList<Vol> listeVols;
    private final ArrayList<Passager> listePassagers;
    private final ArrayList<Reservation> listeReservations;
    private int sequenceReservation;

    public CompagnieAerienne() {
        this.listeAvions = new ArrayList<>();
        this.listeVols = new ArrayList<>();
        this.listePassagers = new ArrayList<>();
        this.listeReservations = new ArrayList<>();
        this.sequenceReservation = 1;
    }

    public List<Avion> getListeAvions() {
        return new ArrayList<>(listeAvions);
    }

    public List<Vol> getListeVols() {
        return new ArrayList<>(listeVols);
    }

    public List<Passager> getListePassagers() {
        return new ArrayList<>(listePassagers);
    }

    public List<Reservation> getListeReservations() {
        return new ArrayList<>(listeReservations);
    }

    public void ajouterAvion(Avion avion) {
        Objects.requireNonNull(avion, "L'avion est obligatoire.");
        if (rechercherAvionParIdentifiant(avion.getId()).isPresent()) {
            throw new IllegalArgumentException("Un avion avec cet identifiant existe déjà.");
        }
        listeAvions.add(avion);
    }

    public void modifierAvion(String idActuel, String nouvelId, String modele, int capacite, String compagnie, int anneeService) {
        Avion avion = obtenirAvionParId(idActuel);
        String idNormalise = normaliserTexte(nouvelId).toUpperCase();
        if (!avion.getId().equalsIgnoreCase(idNormalise) && rechercherAvionParIdentifiant(idNormalise).isPresent()) {
            throw new IllegalArgumentException("Le nouvel identifiant d'avion existe déjà.");
        }
        verifierCapacitePourVolsAssocies(avion, capacite);
        avion.setId(idNormalise);
        avion.setModele(modele);
        avion.setCapacite(capacite);
        avion.setCompagnie(compagnie);
        avion.setAnneeService(anneeService);
        listeVols.stream()
                .filter(vol -> vol.getAvion().getId().equalsIgnoreCase(avion.getId()))
                .forEach(Vol::recalculerPlacesDisponibles);
    }

    public void supprimerAvion(String idAvion) {
        Avion avion = obtenirAvionParId(idAvion);
        boolean avionUtilise = listeVols.stream()
                .anyMatch(vol -> vol.getAvion().getId().equalsIgnoreCase(avion.getId()));
        if (avionUtilise) {
            throw new IllegalStateException("Impossible de supprimer un avion affecté à un vol.");
        }
        listeAvions.remove(avion);
    }

    public Optional<Avion> rechercherAvionParIdentifiant(String idAvion) {
        String critere = normaliserTexte(idAvion).toUpperCase();
        return listeAvions.stream()
                .filter(avion -> avion.getId().equalsIgnoreCase(critere))
                .findFirst();
    }

    public List<Avion> rechercherAvionParModele(String modele) {
        String critere = normaliserTexte(modele).toLowerCase();
        return listeAvions.stream()
                .filter(avion -> avion.getModele().toLowerCase().contains(critere))
                .sorted(Comparator.comparing(Avion::getModele).thenComparing(Avion::getId))
                .collect(Collectors.toList());
    }

    public void ajouterVol(String numeroVol, String villeDepart, String villeDestination, LocalDateTime dateDepart, String idAvion) {
        String numeroNormalise = normaliserTexte(numeroVol).toUpperCase();
        if (rechercherVolParNumero(numeroNormalise).isPresent()) {
            throw new IllegalArgumentException("Un vol avec ce numéro existe déjà.");
        }
        verifierDateVol(dateDepart);
        Avion avion = obtenirAvionParId(idAvion);
        Vol vol = new Vol(numeroNormalise, villeDepart, villeDestination, dateDepart, avion);
        listeVols.add(vol);
    }

    public void modifierVol(String numeroActuel, String nouveauNumero, String villeDepart, String villeDestination, LocalDateTime dateDepart, String idAvion) {
        Vol vol = obtenirVolParNumero(numeroActuel);
        String numeroNormalise = normaliserTexte(nouveauNumero).toUpperCase();
        if (!vol.getNumeroVol().equalsIgnoreCase(numeroNormalise) && rechercherVolParNumero(numeroNormalise).isPresent()) {
            throw new IllegalArgumentException("Le nouveau numéro de vol existe déjà.");
        }
        verifierDateVol(dateDepart);
        Avion nouvelAvion = obtenirAvionParId(idAvion);
        if (vol.getSiegesOccupes().size() > nouvelAvion.getCapacite()) {
            throw new IllegalArgumentException("Le nouvel avion n'a pas assez de places pour les réservations existantes.");
        }
        vol.setNumeroVol(numeroNormalise);
        vol.setVilleDepart(villeDepart);
        vol.setVilleDestination(villeDestination);
        vol.setDateDepart(dateDepart);
        vol.setAvion(nouvelAvion);
    }

    public void supprimerVol(String numeroVol) {
        Vol vol = obtenirVolParNumero(numeroVol);
        boolean existeReservationActive = listeReservations.stream()
                .anyMatch(reservation -> reservation.estActive() && reservation.getVol().getNumeroVol().equalsIgnoreCase(vol.getNumeroVol()));
        if (existeReservationActive) {
            throw new IllegalStateException("Impossible de supprimer un vol contenant des réservations actives.");
        }
        listeVols.remove(vol);
    }

    public Optional<Vol> rechercherVolParNumero(String numeroVol) {
        String critere = normaliserTexte(numeroVol).toUpperCase();
        return listeVols.stream()
                .filter(vol -> vol.getNumeroVol().equalsIgnoreCase(critere))
                .findFirst();
    }

    public List<Vol> rechercherVolParDestination(String destination) {
        String critere = normaliserTexte(destination).toLowerCase();
        return listeVols.stream()
                .filter(vol -> vol.getVilleDestination().toLowerCase().contains(critere))
                .sorted(Comparator.comparing(Vol::getVilleDestination).thenComparing(Vol::getDateDepart))
                .collect(Collectors.toList());
    }

    public List<Vol> rechercherVolsParDestinationEtDate(String destination, LocalDate date) {
        String critere = normaliserTexte(destination).toLowerCase();
        Objects.requireNonNull(date, "La date de recherche est obligatoire.");
        return listeVols.stream()
                .filter(vol -> vol.getVilleDestination().toLowerCase().contains(critere)
                        && vol.getDateDepart().toLocalDate().equals(date))
                .sorted(Comparator.comparing(Vol::getDateDepart).thenComparing(Vol::getNumeroVol))
                .collect(Collectors.toList());
    }

    public List<Vol> trierVolsParDestination() {
        return listeVols.stream()
                .sorted(Comparator.comparing(Vol::getVilleDestination)
                        .thenComparing(Vol::getDateDepart)
                        .thenComparing(Vol::getNumeroVol))
                .collect(Collectors.toList());
    }

    public List<Vol> trierVolsParDate() {
        return listeVols.stream()
                .sorted(Comparator.comparing(Vol::getDateDepart).thenComparing(Vol::getNumeroVol))
                .collect(Collectors.toList());
    }

    public void ajouterPassager(Passager passager) {
        Objects.requireNonNull(passager, "Le passager est obligatoire.");
        if (rechercherPassagerParIdentifiant(passager.getId()).isPresent()) {
            throw new IllegalArgumentException("Un passager avec cet identifiant existe déjà.");
        }
        boolean passeportExistant = listePassagers.stream()
                .anyMatch(existant -> existant.getNumeroPasseport().equalsIgnoreCase(passager.getNumeroPasseport()));
        if (passeportExistant) {
            throw new IllegalArgumentException("Un passager avec ce numéro de passeport existe déjà.");
        }
        listePassagers.add(passager);
    }

    public void modifierPassager(String idActuel, String nouvelId, String nom, String prenom, String numeroPasseport, String nationalite) {
        Passager passager = obtenirPassagerParId(idActuel);
        String idNormalise = normaliserTexte(nouvelId).toUpperCase();
        String passeportNormalise = normaliserTexte(numeroPasseport).toUpperCase();
        if (!passager.getId().equalsIgnoreCase(idNormalise) && rechercherPassagerParIdentifiant(idNormalise).isPresent()) {
            throw new IllegalArgumentException("Le nouvel identifiant du passager existe déjà.");
        }
        boolean passeportExistant = listePassagers.stream()
                .filter(existant -> !existant.getId().equalsIgnoreCase(passager.getId()))
                .anyMatch(existant -> existant.getNumeroPasseport().equalsIgnoreCase(passeportNormalise));
        if (passeportExistant) {
            throw new IllegalArgumentException("Le numéro de passeport est déjà utilisé.");
        }
        passager.setId(idNormalise);
        passager.setNom(nom);
        passager.setPrenom(prenom);
        passager.setNumeroPasseport(passeportNormalise);
        passager.setNationalite(nationalite);
    }

    public void supprimerPassager(String idPassager) {
        Passager passager = obtenirPassagerParId(idPassager);
        boolean aReservationActive = listeReservations.stream()
                .anyMatch(reservation -> reservation.estActive() && reservation.getPassager().getId().equalsIgnoreCase(passager.getId()));
        if (aReservationActive) {
            throw new IllegalStateException("Impossible de supprimer un passager ayant des réservations actives.");
        }
        listePassagers.remove(passager);
    }

    public Optional<Passager> rechercherPassagerParIdentifiant(String idPassager) {
        String critere = normaliserTexte(idPassager).toUpperCase();
        return listePassagers.stream()
                .filter(passager -> passager.getId().equalsIgnoreCase(critere))
                .findFirst();
    }

    public List<Passager> rechercherPassagerParNom(String nom) {
        String critere = normaliserTexte(nom).toLowerCase();
        return listePassagers.stream()
                .filter(passager -> passager.getNom().toLowerCase().contains(critere)
                        || passager.getPrenom().toLowerCase().contains(critere))
                .sorted(Comparator.comparing(Passager::getNom).thenComparing(Passager::getPrenom))
                .collect(Collectors.toList());
    }

    public Reservation reserverVol(String idPassager, String numeroVol, String numeroSiege) {
        Passager passager = obtenirPassagerParId(idPassager);
        Vol vol = obtenirVolParNumero(numeroVol);
        if (vol.getDateDepart().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Impossible de réserver un vol dont la date de départ est déjà passée.");
        }
        if (vol.estComplet()) {
            throw new IllegalStateException("Le vol est complet.");
        }
        boolean reservationExistante = listeReservations.stream()
                .anyMatch(reservation -> reservation.estActive()
                        && reservation.getPassager().getId().equalsIgnoreCase(passager.getId())
                        && reservation.getVol().getNumeroVol().equalsIgnoreCase(vol.getNumeroVol()));
        if (reservationExistante) {
            throw new IllegalStateException("Le passager possède déjà une réservation active pour ce vol.");
        }
        String siegeFinal = numeroSiege == null || numeroSiege.trim().isEmpty()
                ? vol.genererNumeroSiegeAutomatique()
                : numeroSiege.trim().toUpperCase();
        vol.reserverPlace(siegeFinal);
        try {
            Reservation reservation = new Reservation(genererIdReservation(), passager, vol, LocalDateTime.now(), siegeFinal);
            listeReservations.add(reservation);
            return reservation;
        } catch (RuntimeException exception) {
            vol.libererPlace(siegeFinal);
            throw exception;
        }
    }

    public void annulerReservation(String idReservation) {
        Reservation reservation = obtenirReservationParId(idReservation);
        if (!reservation.estActive()) {
            throw new IllegalStateException("La réservation est déjà annulée.");
        }
        reservation.getVol().libererPlace(reservation.getNumeroSiege());
        reservation.annulerReservation();
    }

    public List<Passager> afficherPassagersDunVol(String numeroVol) {
        Vol vol = obtenirVolParNumero(numeroVol);
        return listeReservations.stream()
                .filter(Reservation::estActive)
                .filter(reservation -> reservation.getVol().getNumeroVol().equalsIgnoreCase(vol.getNumeroVol()))
                .map(Reservation::getPassager)
                .distinct()
                .sorted(Comparator.comparing(Passager::getNom).thenComparing(Passager::getPrenom))
                .collect(Collectors.toList());
    }

    public List<Vol> afficherVolsReservesParPassager(String idPassager) {
        Passager passager = obtenirPassagerParId(idPassager);
        return listeReservations.stream()
                .filter(Reservation::estActive)
                .filter(reservation -> reservation.getPassager().getId().equalsIgnoreCase(passager.getId()))
                .map(Reservation::getVol)
                .distinct()
                .sorted(Comparator.comparing(Vol::getDateDepart).thenComparing(Vol::getNumeroVol))
                .collect(Collectors.toList());
    }

    public List<Vol> afficherVolsDisponibles() {
        return listeVols.stream()
                .filter(vol -> !vol.estComplet() && vol.getDateDepart().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Vol::getDateDepart).thenComparing(Vol::getNumeroVol))
                .collect(Collectors.toList());
    }

    public List<Reservation> afficherReservationsEnCours() {
        return listeReservations.stream()
                .filter(Reservation::estActive)
                .sorted(Comparator.comparing(Reservation::getDateReservation).reversed())
                .collect(Collectors.toList());
    }

    public double calculerTauxRemplissage(String numeroVol) {
        return obtenirVolParNumero(numeroVol).getTauxRemplissage();
    }

    public long compterReservationsActivesPourVol(String numeroVol) {
        Vol vol = obtenirVolParNumero(numeroVol);
        return listeReservations.stream()
                .filter(Reservation::estActive)
                .filter(reservation -> reservation.getVol().getNumeroVol().equalsIgnoreCase(vol.getNumeroVol()))
                .count();
    }

    public Reservation obtenirReservationParId(String idReservation) {
        String idNormalise = normaliserTexte(idReservation).toUpperCase();
        return listeReservations.stream()
                .filter(reservation -> reservation.getIdReservation().equalsIgnoreCase(idNormalise))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Aucune réservation trouvée avec cet identifiant."));
    }

    public Avion obtenirAvionParId(String idAvion) {
        return rechercherAvionParIdentifiant(idAvion)
                .orElseThrow(() -> new IllegalArgumentException("Aucun avion trouvé avec cet identifiant."));
    }

    public Vol obtenirVolParNumero(String numeroVol) {
        return rechercherVolParNumero(numeroVol)
                .orElseThrow(() -> new IllegalArgumentException("Aucun vol trouvé avec ce numéro."));
    }

    public Passager obtenirPassagerParId(String idPassager) {
        return rechercherPassagerParIdentifiant(idPassager)
                .orElseThrow(() -> new IllegalArgumentException("Aucun passager trouvé avec cet identifiant."));
    }

    public boolean contientAvions() {
        return !listeAvions.isEmpty();
    }

    public boolean contientVols() {
        return !listeVols.isEmpty();
    }

    public boolean contientPassagers() {
        return !listePassagers.isEmpty();
    }

    public boolean contientReservations() {
        return !listeReservations.isEmpty();
    }

    public long compterReservationsActives() {
        return listeReservations.stream()
                .filter(reservation -> reservation.getStatut() == StatutReservation.ACTIVE)
                .count();
    }

    private void verifierDateVol(LocalDateTime dateDepart) {
        Objects.requireNonNull(dateDepart, "La date de départ est obligatoire.");
        if (!dateDepart.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La date de départ doit être ultérieure à la date actuelle.");
        }
    }

    private void verifierCapacitePourVolsAssocies(Avion avion, int nouvelleCapacite) {
        long reservationsMax = listeVols.stream()
                .filter(vol -> vol.getAvion().getId().equalsIgnoreCase(avion.getId()))
                .mapToLong(vol -> vol.getSiegesOccupes().size())
                .max()
                .orElse(0L);
        if (nouvelleCapacite < reservationsMax) {
            throw new IllegalArgumentException("La nouvelle capacité est inférieure au nombre de sièges déjà réservés sur un vol utilisant cet avion.");
        }
    }

    private String genererIdReservation() {
        return String.format("RES-%04d", sequenceReservation++);
    }

    private String normaliserTexte(String valeur) {
        Objects.requireNonNull(valeur, "La valeur est obligatoire.");
        String resultat = valeur.trim();
        if (resultat.isEmpty()) {
            throw new IllegalArgumentException("La valeur ne peut pas être vide.");
        }
        return resultat;
    }
}

