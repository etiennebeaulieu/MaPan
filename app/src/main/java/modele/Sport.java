package modele;


import android.media.Image;
import android.net.Uri;

import com.example.mapan.R;

public enum Sport {
    RANDONNEE("Randonnée pédestre", R.drawable.rando), COURSE("Course à pied", R.drawable.course), VELO("Vélo", R.drawable.velo), RAQUETTE("Raquette", R.drawable.raquette), SKI_RANDONNEE("Ski de randonnée", R.drawable.ski_rando), SKI("Ski alpin", R.drawable.ski_alpin), PATIN("Patin à glace", R.drawable.patin), SKI_FOND("Ski de fond", R.drawable.ski_fond);

    private final String nom;
    private final int image;

    Sport(String nom, int image) {
        this.nom = nom;
        this.image = image;
    }

    public String getNom() {
        return nom;
    }

    public int getImage() {
        return image;
    }
}
