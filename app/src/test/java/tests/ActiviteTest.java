package tests;

import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;

import modele.Activite;
import modele.Sport;

import static org.junit.Assert.*;

public class ActiviteTest
{

    Activite activite1,activite2,activite3,activite4,
            activite5,activite6,activite7,activite8,
            activite9,activite10,activite11,activite12,
            activite13,activite14,activite15,activite16;

    @Before
    public void ActiviteTest()
    {
        activite1 = new Activite("Activité 1", Instant.now(), Sport.RANDONNEE, Duration.ofMinutes(60), 6.0);
        activite2 = new Activite("Activité 2",Instant.now(),Sport.RANDONNEE,Duration.ofMinutes(80),6.40);
        activite3 = new Activite("Activité 3",Instant.now(),Sport.COURSE,Duration.ofMinutes(10),2.21);
        activite4 = new Activite("Activité 4",Instant.now(),Sport.COURSE,Duration.ofMinutes(30),4.60);
        activite5 = new Activite("Activité 5",Instant.now(),Sport.VELO,Duration.ofMinutes(90),20.73);
        activite6 = new Activite("Activité 6",Instant.now(),Sport.VELO,Duration.ofMinutes(20),10.20);
        activite7 = new Activite("Activité 7",Instant.now(),Sport.RAQUETTE,Duration.ofMinutes(50),4.75);
        activite8 = new Activite("Activité 8",Instant.now(),Sport.RAQUETTE,Duration.ofMinutes(60),5.20);
        activite9 = new Activite("Activité 9",Instant.now(),Sport.SKI_RANDONNEE,Duration.ofMinutes(120),12.30);
        activite10 = new Activite("Activité 10",Instant.now(),Sport.SKI_RANDONNEE,Duration.ofMinutes(100),4.90);
        activite11 = new Activite("Activité 11",Instant.now(),Sport.SKI,Duration.ofMinutes(70),7.60);
        activite12 = new Activite("Activité 12",Instant.now(),Sport.SKI,Duration.ofMinutes(90),9.42);
        activite13 = new Activite("Activité 13",Instant.now(),Sport.PATIN,Duration.ofMinutes(45),7.3);
        activite14 = new Activite("Activité 14",Instant.now(),Sport.PATIN,Duration.ofMinutes(25),3.4);
        activite15 = new Activite("Activité 15",Instant.now(),Sport.SKI_FOND,Duration.ofMinutes(35),8.4);
        activite16 = new Activite("Activité 16",Instant.now(),Sport.SKI_FOND,Duration.ofMinutes(12),1.9);
    }

    @Before
    public void ActiviteFichierTest()
    {

    }

    @Test
    public void ActiviteInvalide()
    {
try
{
   new Activite("", Instant.now(), Sport.RANDONNEE, Duration.ofMinutes(60), 6.0);

}
catch (Exception e)
{
}
        try
        {
            new Activite(null, Instant.now(), Sport.RANDONNEE, Duration.ofMinutes(60), 6.0);

        }
        catch (Exception e)
        {
        }
        try
        {
            new Activite("Allo", Instant.now(), Sport.RANDONNEE, Duration.ofMinutes(0), 6.0);

        }
        catch (Exception e)
        {
        }
        try
        {
            new Activite("Allo", Instant.now(), Sport.RANDONNEE, Duration.ofMinutes(-5), 6.0);

        }
        catch (Exception e)
        {
        }
        try
        {
            new Activite("Allo", Instant.now(), Sport.RANDONNEE, Duration.ofMinutes(60), -1);
        }
        catch (Exception e)
        {
        }
    }

    @Test
    public void getNom()
    {
       assertTrue(activite1.getNom().equals("Activité 1"));
       assertTrue(activite2.getNom().equals("Activité 2"));
       assertTrue(activite3.getNom().equals("Activité 3"));
       assertTrue(activite4.getNom().equals("Activité 4"));
        assertTrue(activite5.getNom().equals("Activité 5"));
        assertTrue(activite6.getNom().equals("Activité 6"));
        assertTrue(activite7.getNom().equals("Activité 7"));
        assertTrue(activite8.getNom().equals("Activité 8"));
        assertTrue(activite9.getNom().equals("Activité 9"));
        assertTrue(activite10.getNom().equals("Activité 10"));
        assertTrue(activite11.getNom().equals("Activité 11"));
        assertTrue(activite12.getNom().equals("Activité 12"));
        assertTrue(activite13.getNom().equals("Activité 13"));
        assertTrue(activite14.getNom().equals("Activité 14"));
        assertTrue(activite15.getNom().equals("Activité 15"));
        assertTrue(activite16.getNom().equals("Activité 16"));

    }

    @Test
    public void setNom()
    {
        activite1.setNom("Nouveau nom");
        assertTrue(activite1.getNom().equals("Nouveau nom"));

        activite1.setNom(null);
        assertTrue(activite1.getNom().equals("Nouveau nom"));

        activite1.setNom("");
        assertTrue(activite1.getNom().equals("Nouveau nom"));

    }

    @Test
    public void getDate()
    {

    }

    @Test
    public void setDate()
    {
    }

    @Test
    public void getSport()
    {
        assertTrue(activite1.getSport().equals(Sport.RANDONNEE));
        assertTrue(activite2.getSport().equals(Sport.RANDONNEE));
        assertTrue(activite3.getSport().equals(Sport.COURSE));
        assertTrue(activite4.getSport().equals(Sport.COURSE));
        assertTrue(activite5.getSport().equals(Sport.VELO));
        assertTrue(activite6.getSport().equals(Sport.VELO));
        assertTrue(activite7.getSport().equals(Sport.RAQUETTE));
        assertTrue(activite8.getSport().equals(Sport.RAQUETTE));
        assertTrue(activite9.getSport().equals(Sport.SKI_RANDONNEE));
        assertTrue(activite10.getSport().equals(Sport.SKI_RANDONNEE));
        assertTrue(activite11.getSport().equals(Sport.SKI));
        assertTrue(activite12.getSport().equals(Sport.SKI));
        assertTrue(activite13.getSport().equals(Sport.PATIN));
        assertTrue(activite14.getSport().equals(Sport.PATIN));
        assertTrue(activite15.getSport().equals(Sport.SKI_FOND));
        assertTrue(activite16.getSport().equals(Sport.SKI_FOND));
    }

    @Test
    public void setSport()
    {
    }

    @Test
    public void getDuree()
    {
    }

    @Test
    public void setDuree()
    {
    }

    @Test
    public void getHeureDebut()
    {
    }

    @Test
    public void setHeureDebut()
    {
    }

    @Test
    public void getHeureFin()
    {
    }

    @Test
    public void setHeureFin()
    {
    }

    @Test
    public void getDistanceMetrique()
    {
    }

    @Test
    public void setDistanceMetrique()
    {
    }

    @Test
    public void getDenivelePositifMetrique()
    {
    }

    @Test
    public void getDistanceImperiale()
    {
    }

    @Test
    public void setDistanceImperiale()
    {
    }

    @Test
    public void setDenivelePositifMetrique()
    {
    }

    @Test
    public void getDeniveleNegatifMetrique()
    {
    }

    @Test
    public void setDeniveleNegatifMetrique()
    {
    }

    @Test
    public void getDenivelePositifImperiale()
    {
    }

    @Test
    public void setDenivelePositifImperiale()
    {
    }

    @Test
    public void getDeniveleNegatifImperiale()
    {
    }

    @Test
    public void setDeniveleNegatifImperiale()
    {
    }

    @Test
    public void getVitesseActuelleMetrique()
    {
    }

    @Test
    public void setVitesseActuelleMetrique()
    {
    }

    @Test
    public void getVitesseActuelleImperiale()
    {
    }

    @Test
    public void setVitesseActuelleImperiale()
    {
    }

    @Test
    public void getVitesseMetrique()
    {
    }

    @Test
    public void setVitesseMetrique()
    {
    }

    @Test
    public void getVitesseImperiale()
    {
    }

    @Test
    public void setVitesseImperiale()
    {
    }

    @Test
    public void getTabLatitude()
    {
    }

    @Test
    public void setTabLatitude()
    {
    }

    @Test
    public void getTabLongitude()
    {
    }

    @Test
    public void setTabLongitude()
    {
    }

    @Test
    public void getTabDistanceMetrique()
    {
    }

    @Test
    public void setTabDistanceMetrique()
    {
    }

    @Test
    public void getTabElevationMetrique()
    {
    }

    @Test
    public void setTabElevationMetrique()
    {
    }

    @Test
    public void getTabVitesseMetrique()
    {
    }

    @Test
    public void setTabVitesseMetrique()
    {
    }

    @Test
    public void getTabTemps()
    {
    }

    @Test
    public void setTabTemps()
    {
    }

    @Test
    public void getGraphique()
    {
    }

    @Test
    public void setGraphique()
    {
    }

    @Test
    public void getStatistique()
    {
    }

    @Test
    public void setStatistique()
    {
    }

    @Test
    public void calculerDistance()
    {
    }

    @Test
    public void calculerDenivele()
    {
    }

    @Test
    public void calculerVitesseMoyenne()
    {
    }

    @Test
    public void getAltitudeMaxMetrique()
    {
    }

    @Test
    public void setAltitudeMaxMetrique()
    {
    }

    @Test
    public void getAltitudeMaxImperiale()
    {
    }

    @Test
    public void setAltitudeMaxImperiale()
    {
    }

    @Test
    public void getAltitudeMinMetrique()
    {
    }

    @Test
    public void setAltitudeMinMetrique()
    {
    }

    @Test
    public void getAltitudeMinImperiale()
    {
    }

    @Test
    public void setAltitudeMinImperiale()
    {
    }

    @Test
    public void getAltitudeActuelleMetrique()
    {
    }

    @Test
    public void setAltitudeActuelleMetrique()
    {
    }

    @Test
    public void getAltitudeActuelleImperiale()
    {
    }

    @Test
    public void setAltitudeActuelleImperiale()
    {
    }
}