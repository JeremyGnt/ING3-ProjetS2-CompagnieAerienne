package modele;

import java.util.Objects;

public class Passager {
    private String id;
    private String nom;
    private String prenom;
    private String numeroPasseport;
    private String nationalite;

    public Passager(String id, String nom, String prenom, String numeroPasseport, String nationalite) {
        setId(id);
        setNom(nom);
        setPrenom(prenom);
        setNumeroPasseport(numeroPasseport);
        setNationalite(nationalite);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = normaliserTexte(id, "L'identifiant du passager est obligatoire.");
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = normaliserTexte(nom, "Le nom du passager est obligatoire.").toUpperCase();
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = capitaliser(normaliserTexte(prenom, "Le prénom du passager est obligatoire."));
    }

    public String getNumeroPasseport() {
        return numeroPasseport;
    }

    public void setNumeroPasseport(String numeroPasseport) {
        this.numeroPasseport = normaliserTexte(numeroPasseport, "Le numéro de passeport est obligatoire.").toUpperCase();
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = capitaliser(normaliserTexte(nationalite, "La nationalité est obligatoire."));
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public String afficherDetails() {
        return String.format(
                "Passager[id=%s, nom=%s, prénom=%s, passeport=%s, nationalité=%s]",
                id,
                nom,
                prenom,
                numeroPasseport,
                nationalite
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

    private String capitaliser(String texte) {
        String minuscule = texte.toLowerCase();
        return Character.toUpperCase(minuscule.charAt(0)) + minuscule.substring(1);
    }

    @Override
    public String toString() {
        return afficherDetails();
    }
}

