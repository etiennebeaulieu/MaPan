package modele;


public enum Sport {
    RANDONNEE("Randonnée pédestre"), COURSE("Course à pied"), VELO("Vélo"), RAQUETTE("Raquette"), SKI_RANDONNEE("Ski de randonnée"), SKI("Ski alpin"), PATIN("Patin à glace"), SKI_FOND("Ski de fond");

private final String nom;

    Sport(String nom) {
        this.nom = nom;
    }
}
