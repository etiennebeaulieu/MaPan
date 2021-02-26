package tests;

import com.example.mapan.R;

import org.junit.Before;
import org.junit.Test;
import modele.Sport;

import static org.junit.Assert.*;

public class SportTest
{
    String[] tabNom = {"Randonnée pédestre","Course à pied", "Vélo","Raquette","Ski de randonnée","Ski alpin","Patin à glace","Ski de fond"};
int[] tabImage =    { R.drawable.rando, R.drawable.course, R.drawable.velo, R.drawable.raquette, R.drawable.ski_rando, R.drawable.ski_alpin, R.drawable.patin, R.drawable.ski_fond};


    @Test
    public void getNom()
    {
        int i = 0;
        for (Sport s:Sport.values())
        {
            assertTrue(s.getNom().equals(tabNom[i]));
            i++;
        }
    }

    @Test
    public void getImage()
    {
        int i = 0;
        for (Sport s:Sport.values())
        {
            assertTrue(s.getImage()==(tabImage[i]));
            i++;
        }
    }
}