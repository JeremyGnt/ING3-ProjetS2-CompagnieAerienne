package modele;

import java.time.Year;
import java.util.Objects;

public class Avion {
    private String id;
    private String modele;
    private int capacite;
    private String compagnie;
    private int anneeService;

    public Avion(String id, String modele, int capacite, String compagnie, int anneeService) {
        setId(id);
        setModele(modele);
        setCapacite(capacite);
        setCompagnie(compagnie);
        setAnneeService(anneeService);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = normaliserTexte(id, "L'identifiant de l'avion est obligatoire.");
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = normaliserTexte(modele, "Le modèle de l'avion est obligatoire.");
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        if (capacite <= 0) {
            throw new IllegalArgumentException("La capacité doit être strictement positive.");
        }
        this.capacite = capacite;
    }

    public String getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(String compagnie) {
        this.compagnie = normaliserTexte(compagnie, "La compagnie est obligatoire.");
    }

    public int getAnneeService() {
        return anneeService;
    }

    public void setAnneeService(int anneeService) {
        int anneeCourante = Year.now().getValue();
        if (anneeService < 1950 || anneeService > anneeCourante) {
            throw new IllegalArgumentException("L'année de mise en service doit être comprise entre 1950 et l'année courante.");
        }
        this.anneeService = anneeService;
    }

    public String afficherDetails() {
        return String.format(
                "Avion[id=%s, modèle=%s, capacité=%d, compagnie=%s, année=%d]",
                id,
                modele,
                capacite,
                compagnie,
                anneeService
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
        return afficherDetails();
    }
}

