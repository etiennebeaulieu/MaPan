package modele;


import android.net.Uri;

public enum Sport {
    RANDONNEE("Randonnée pédestre", "android.resource://main/drawable/rando.png"), COURSE("Course à pied", "android.resource://main/drawable/course"), VELO("Vélo", "android.resource://main/drawable/velo"), RAQUETTE("Raquette", "android.resource://main/drawable/raquette"), SKI_RANDONNEE("Ski de randonnée", "android.resource://main/drawable/ski_rando"), SKI("Ski alpin", "android.resource://main/drawable/ski_alpin"), PATIN("Patin à glace", "android.resource://main/drawable/patin"), SKI_FOND("Ski de fond", "android.resource://main/drawable/ski_fond");

    private final String nom;
    private final Uri uriImage;

    Sport(String nom, String uri) {
        this.nom = nom;
        this.uriImage = Uri.parse(uri);
    }

    public String getNom() {
        return nom;
    }

    public Uri getUriImage() {
        return uriImage;
    }
}
