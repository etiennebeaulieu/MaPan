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
        activite1 = new Activite("Activité 1", Instant.now(), Sport.RANDONNEE, 60, 6.0);
        activite2 = new Activite("Activité 2",Instant.now(),Sport.RANDONNEE,80,6.40);
        activite3 = new Activite("Activité 3",Instant.now(),Sport.COURSE,10,2.21);
        activite4 = new Activite("Activité 4",Instant.now(),Sport.COURSE,30,4.60);
        activite5 = new Activite("Activité 5",Instant.now(),Sport.VELO,90,20.73);
        activite6 = new Activite("Activité 6",Instant.now(),Sport.VELO,20,10.20);
        activite7 = new Activite("Activité 7",Instant.now(),Sport.RAQUETTE,50,4.75);
        activite8 = new Activite("Activité 8",Instant.now(),Sport.RAQUETTE,60,5.20);
        activite9 = new Activite("Activité 9",Instant.now(),Sport.SKI_RANDONNEE,120,12.30);
        activite10 = new Activite("Activité 10",Instant.now(),Sport.SKI_RANDONNEE,100,4.90);
        activite11 = new Activite("Activité 11",Instant.now(),Sport.SKI,75,7.60);
        activite12 = new Activite("Activité 12",Instant.now(),Sport.SKI,90,9.42);
        activite13 = new Activite("Activité 13",Instant.now(),Sport.PATIN,45,7.35);
        activite14 = new Activite("Activité 14",Instant.now(),Sport.PATIN,25,3.4);
        activite15 = new Activite("Activité 15",Instant.now(),Sport.SKI_FOND,35,8.4);
        activite16 = new Activite("Activité 16",Instant.now(),Sport.SKI_FOND,12,1.9);
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
              new Activite("", Instant.now(), Sport.RANDONNEE, 60, 6.0);

}
        catch (Exception ignored)
        {
        }
        try
        {
            new Activite(null, Instant.now(), Sport.RANDONNEE, 60, 6.0);

        }
        catch (Exception ignored)
        {
        }
        try
        {
            new Activite("Allo", Instant.now(), Sport.RANDONNEE, 60, -1);

        }
        catch (Exception ignored)
        {
        }
        try
        {
            new Activite("Allo", Instant.now(), Sport.RANDONNEE, 0, 6.0);

        }
        catch (Exception ignored)
        {
        }
        try
        {
            new Activite("Allo", Instant.now(), Sport.RANDONNEE, -1, -1);
        }
        catch (Exception ignored)
        {
        }
        try
        {
            new Activite("Allo", Instant.now(), null, 60, 6.0);

        }
        catch (Exception ignored)
        {
        }

    }

    @Test
    public void getNom()
    {
        assertEquals("Activité 1", activite1.getNom());
        assertEquals("Activité 2", activite2.getNom());
        assertEquals("Activité 3", activite3.getNom());
        assertEquals("Activité 4", activite4.getNom());
        assertEquals("Activité 5", activite5.getNom());
        assertEquals("Activité 6", activite6.getNom());
        assertEquals("Activité 7", activite7.getNom());
        assertEquals("Activité 8", activite8.getNom());
        assertEquals("Activité 9", activite9.getNom());
        assertEquals("Activité 10", activite10.getNom());
        assertEquals("Activité 11", activite11.getNom());
        assertEquals("Activité 12", activite12.getNom());
        assertEquals("Activité 13", activite13.getNom());
        assertEquals("Activité 14", activite14.getNom());
        assertEquals("Activité 15", activite15.getNom());
        assertEquals("Activité 16", activite16.getNom());

    }

    @Test
    public void setNom()
    {
        activite1.setNom("Nouveau nom");
        assertEquals("Nouveau nom", activite1.getNom());

        activite1.setNom(null);
        assertEquals("Nouveau nom", activite1.getNom());

        activite1.setNom("");
        assertEquals("Nouveau nom", activite1.getNom());

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
        assertEquals(activite1.getSport(), Sport.RANDONNEE);
        assertEquals(activite2.getSport(), Sport.RANDONNEE);
        assertEquals(activite3.getSport(), Sport.COURSE);
        assertEquals(activite4.getSport(), Sport.COURSE);
        assertEquals(activite5.getSport(), Sport.VELO);
        assertEquals(activite6.getSport(), Sport.VELO);
        assertEquals(activite7.getSport(), Sport.RAQUETTE);
        assertEquals(activite8.getSport(), Sport.RAQUETTE);
        assertEquals(activite9.getSport(), Sport.SKI_RANDONNEE);
        assertEquals(activite10.getSport(), Sport.SKI_RANDONNEE);
        assertEquals(activite11.getSport(), Sport.SKI);
        assertEquals(activite12.getSport(), Sport.SKI);
        assertEquals(activite13.getSport(), Sport.PATIN);
        assertEquals(activite14.getSport(), Sport.PATIN);
        assertEquals(activite15.getSport(), Sport.SKI_FOND);
        assertEquals(activite16.getSport(), Sport.SKI_FOND);
    }

    @Test
    public void setSport()
    {
        activite1.setSport(Sport.COURSE);
        assertEquals(activite1.getSport(), Sport.COURSE);

        activite1.setSport(Sport.VELO);
        assertEquals(activite1.getSport(), Sport.VELO);

        activite1.setSport(Sport.RAQUETTE);
        assertEquals(activite1.getSport(), Sport.RAQUETTE);

        activite1.setSport(Sport.SKI_RANDONNEE);
        assertEquals(activite1.getSport(), Sport.SKI_RANDONNEE);

        activite1.setSport(Sport.SKI);
        assertEquals(activite1.getSport(), Sport.SKI);

        activite1.setSport(Sport.PATIN);
        assertEquals(activite1.getSport(), Sport.PATIN);

        activite1.setSport(Sport.SKI_FOND);
        assertEquals(activite1.getSport(), Sport.SKI_FOND);

        activite1.setSport(Sport.RANDONNEE);
        assertEquals(activite1.getSport(), Sport.RANDONNEE);

        activite1.setSport(null);
        assertEquals(activite1.getSport(), Sport.RANDONNEE);
    }

    @Test
    public void getDuree()
    {
        assertEquals(activite1.getDuree(), Duration.ofMinutes(60));
        assertEquals(activite2.getDuree(), Duration.ofMinutes(80));
        assertEquals(activite3.getDuree(), Duration.ofMinutes(10));
        assertEquals(activite4.getDuree(), Duration.ofMinutes(30));
        assertEquals(activite5.getDuree(), Duration.ofMinutes(90));
        assertEquals(activite6.getDuree(), Duration.ofMinutes(20));
        assertEquals(activite7.getDuree(), Duration.ofMinutes(50));
        assertEquals(activite8.getDuree(), Duration.ofMinutes(60));
        assertEquals(activite9.getDuree(), Duration.ofMinutes(120));
        assertEquals(activite10.getDuree(), Duration.ofMinutes(100));
        assertEquals(activite11.getDuree(), Duration.ofMinutes(75));
        assertEquals(activite12.getDuree(), Duration.ofMinutes(90));
        assertEquals(activite13.getDuree(), Duration.ofMinutes(45));
        assertEquals(activite14.getDuree(), Duration.ofMinutes(25));
        assertEquals(activite15.getDuree(), Duration.ofMinutes(35));
        assertEquals(activite16.getDuree(), Duration.ofMinutes(12));
    }

    @Test
    public void setDuree()
    {
        activite1.setDuree(20);
        assertEquals(activite1.getDuree(), Duration.ofMinutes(20));

        activite1.setDuree(0);
        assertEquals(activite1.getDuree(), Duration.ofMinutes(20));

        activite1.setDuree(-20);
        assertEquals(activite1.getDuree(), Duration.ofMinutes(20));
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
        assertEquals(6, activite1.getDistanceMetrique(), 0.0);
        assertEquals(6.4, activite2.getDistanceMetrique(), 0.0);
        assertEquals(2.21, activite3.getDistanceMetrique(), 0.0);
        assertEquals(4.6, activite4.getDistanceMetrique(), 0.0);
        assertEquals(20.73, activite5.getDistanceMetrique(), 0.0);
        assertEquals(10.20, activite6.getDistanceMetrique(), 0.0);
        assertEquals(4.75, activite7.getDistanceMetrique(), 0.0);
        assertEquals(5.2, activite8.getDistanceMetrique(), 0.0);
        assertEquals(12.3, activite9.getDistanceMetrique(), 0.0);
        assertEquals(4.9, activite10.getDistanceMetrique(), 0.0);
        assertEquals(7.6, activite11.getDistanceMetrique(), 0.0);
        assertEquals(9.42, activite12.getDistanceMetrique(), 0.0);
        assertEquals(7.35, activite13.getDistanceMetrique(), 0.0);
        assertEquals(3.4, activite14.getDistanceMetrique(), 0.0);
        assertEquals(8.4, activite15.getDistanceMetrique(), 0.0);
        assertEquals(1.9, activite16.getDistanceMetrique(), 0.0);
    }

    @Test
    public void setDistanceMetrique()
    {
        activite1.setDistanceMetrique(10.43);
        assertEquals(10.43, activite1.getDistanceMetrique(), 0.0);

        activite1.setDistanceMetrique(-1);
        assertEquals(10.43, activite1.getDistanceMetrique(), 0.0);
    }

    @Test
    public void getDistanceImperiale()
    {
        assertEquals(6, activite1.getDistanceImperiale(), 0.0);
        assertEquals(6.4, activite2.getDistanceImperiale(), 0.0);
        assertEquals(2.21, activite3.getDistanceImperiale(), 0.0);
        assertEquals(4.6, activite4.getDistanceImperiale(), 0.0);
        assertEquals(20.73, activite5.getDistanceImperiale(), 0.0);
        assertEquals(10.20, activite6.getDistanceImperiale(), 0.0);
        assertEquals(4.75, activite7.getDistanceImperiale(), 0.0);
        assertEquals(5.2, activite8.getDistanceImperiale(), 0.0);
        assertEquals(12.3, activite9.getDistanceImperiale(), 0.0);
        assertEquals(4.9, activite10.getDistanceImperiale(), 0.0);
        assertEquals(7.6, activite11.getDistanceImperiale(), 0.0);
        assertEquals(9.42, activite12.getDistanceImperiale(), 0.0);
        assertEquals(7.35, activite13.getDistanceImperiale(), 0.0);
        assertEquals(3.4, activite14.getDistanceImperiale(), 0.0);
        assertEquals(8.4, activite15.getDistanceImperiale(), 0.0);
        assertEquals(1.9, activite16.getDistanceImperiale(), 0.0);
    }

    @Test
    public void setDistanceImperiale()
    {
        activite1.setDistanceImperiale(10);
        //System.out.println(activite1.getDistanceImperiale());
      // assertEquals(10,activite1.getDistanceImperiale(), 0.0);

      //  activite1.setDistanceImperiale(-1);
      //  assertEquals(10, activite1.getDistanceImperiale(), 0.0);
    }

    @Test
    public void getDenivelePositifMetrique()
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
        assertEquals(6, activite1.getVitesseMetrique(), 0.0);
        //assertEquals(4.8, activite2.getVitesseMetrique(), 0.0);
        assertEquals(13.26, activite3.getVitesseMetrique(), 0.0);
        assertEquals(9.2, activite4.getVitesseMetrique(), 0.0);
        assertEquals(13.82, activite5.getVitesseMetrique(), 0.0);
      //  assertEquals(30.6, activite6.getVitesseMetrique(), 0.0);
        assertEquals(5.7, activite7.getVitesseMetrique(), 0.0);
        assertEquals(5.2, activite8.getVitesseMetrique(), 0.0);
        assertEquals(6.15, activite9.getVitesseMetrique(), 0.0);
        assertEquals(2.94, activite10.getVitesseMetrique(), 0.0);
        assertEquals(6.08, activite11.getVitesseMetrique(), 0.0);
        assertEquals(6.28, activite12.getVitesseMetrique(), 0.0);
        assertEquals(9.8, activite13.getVitesseMetrique(), 0.0);
      //  assertEquals(8.16, activite14.getVitesseMetrique(), 0.0);
        assertEquals(14.4, activite15.getVitesseMetrique(), 0.0);
        assertEquals(9.5, activite16.getVitesseMetrique(), 0.0);
    }

    @Test
    public void setVitesseMetrique()
    {
        activite1.setVitesseMetrique(8);
        assertEquals(8, activite1.getVitesseMetrique(), 0.0);
    }

    @Test
    public void getVitesseImperiale()
    {
        assertEquals(activite1.getVitesseImperiale(), activite1.getVitesseMetrique() * 1000 * Activite.METRE_MILES, 0.0);
        assertEquals(activite2.getVitesseImperiale(), activite2.getVitesseMetrique() * 1000 * Activite.METRE_MILES, 0.0);
        assertEquals(activite3.getVitesseImperiale(), activite3.getVitesseMetrique() * 1000 * Activite.METRE_MILES, 0.0);
        assertEquals(activite4.getVitesseImperiale(), activite4.getVitesseMetrique() * 1000 * Activite.METRE_MILES, 0.0);
        assertEquals(activite5.getVitesseImperiale(), activite5.getVitesseMetrique() * 1000 * Activite.METRE_MILES, 0.0);
        assertEquals(activite6.getVitesseImperiale(), activite6.getVitesseMetrique() * 1000 * Activite.METRE_MILES, 0.0);
        assertEquals(activite7.getVitesseImperiale(), activite7.getVitesseMetrique() * 1000 * Activite.METRE_MILES, 0.0);
        assertEquals(activite8.getVitesseImperiale(), activite8.getVitesseMetrique() * 1000 * Activite.METRE_MILES, 0.0);
        assertEquals(activite9.getVitesseImperiale(), activite9.getVitesseMetrique() * 1000 * Activite.METRE_MILES, 0.0);
        assertEquals(activite10.getVitesseImperiale(), activite10.getVitesseMetrique() * 1000 * Activite.METRE_MILES, 0.0);
        assertEquals(activite11.getVitesseImperiale(), activite11.getVitesseMetrique() * 1000 * Activite.METRE_MILES, 0.0);
        assertEquals(activite12.getVitesseImperiale(), activite12.getVitesseMetrique() * 1000 * Activite.METRE_MILES, 0.0);
        assertEquals(activite13.getVitesseImperiale(), activite13.getVitesseMetrique() * 1000 * Activite.METRE_MILES, 0.0);
        assertEquals(activite14.getVitesseImperiale(), activite14.getVitesseMetrique() * 1000 * Activite.METRE_MILES, 0.0);
        assertEquals(activite15.getVitesseImperiale(), activite15.getVitesseMetrique() * 1000 * Activite.METRE_MILES, 0.0);
        assertEquals(activite16.getVitesseImperiale(), activite16.getVitesseMetrique() * 1000 * Activite.METRE_MILES, 0.0);
    }

    @Test
    public void setVitesseImperiale()
    {
        activite1.setVitesseMetrique(8);
        assertEquals(8, activite1.getVitesseMetrique(), 0.0);
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


        assertEquals(6, activite1.calculerVitesseMoyenne(activite1.getDuree().toMillis()/1000)*3600, 0.0);
        //assertEquals(4.8, activite2.calculerVitesseMoyenne(activite2.getDuree().toMillis()/1000)*3600, 0.0);
        assertEquals(13.26, activite3.calculerVitesseMoyenne(activite3.getDuree().toMillis()/1000) *3600, 0.0);
        assertEquals(9.2, activite4.calculerVitesseMoyenne(activite4.getDuree().toMillis()/1000)*3600, 0.0);
        assertEquals(13.82, activite5.calculerVitesseMoyenne(activite5.getDuree().toMillis()/1000)*3600, 0.0);
        //  assertEquals(30.6, activite6.calculerVitesseMoyenne(activite6.getDuree().toMillis()/1000)*3600, 0.0);
        assertEquals(5.7, activite7.calculerVitesseMoyenne(activite7.getDuree().toMillis()/1000)*3600, 0.0);
        assertEquals(5.2, activite8.calculerVitesseMoyenne(activite8.getDuree().toMillis()/1000)*3600, 0.0);
        assertEquals(6.15, activite9.calculerVitesseMoyenne(activite9.getDuree().toMillis()/1000)*3600, 0.0);
        assertEquals(2.94, activite10.calculerVitesseMoyenne(activite10.getDuree().toMillis()/1000)*3600, 0.0);
        assertEquals(6.08, activite11.calculerVitesseMoyenne(activite11.getDuree().toMillis()/1000)*3600, 0.0);
        assertEquals(6.28, activite12.calculerVitesseMoyenne(activite12.getDuree().toMillis()/1000)*3600, 0.0);
        assertEquals(9.8, activite13.calculerVitesseMoyenne(activite13.getDuree().toMillis()/1000)*3600, 0.0);
        //  assertEquals(8.16, activite14.calculerVitesseMoyenne(activite14.getDuree().toMillis()/1000)*3600, 0.0);
        assertEquals(14.4, activite15.calculerVitesseMoyenne(activite15.getDuree().toMillis()/1000)*3600, 0.0);
        assertEquals(9.5, activite16.calculerVitesseMoyenne(activite16.getDuree().toMillis()/1000)*3600, 0.0);    }

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

    @Test
    public void lireFichier()
    {
    }

    @Test
    public void ecrireFichier()
    {
    }
}