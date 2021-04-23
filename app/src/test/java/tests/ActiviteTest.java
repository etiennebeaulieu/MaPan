package tests;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

import modele.Activite;
import modele.Sport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ActiviteTest
{

    Activite activite1,activite2,activite3,activite4,
            activite5,activite6,activite7,activite8,
            activite9,activite10,activite11,activite12,
            activite13,activite14,activite15,activite16;

Activite activite17, activite18;

    @Before
    public void Activite()
    {
        activite1 = new Activite("Activité 1", Instant.ofEpochSecond(1000), Sport.RANDONNEE, 60, 6.0);
        activite2 = new Activite("Activité 2",Instant.ofEpochSecond(10000),Sport.RANDONNEE,80,6.40);
        activite3 = new Activite("Activité 3",Instant.ofEpochSecond(100000),Sport.COURSE,10,2.21);
        activite4 = new Activite("Activité 4",Instant.ofEpochSecond(2000),Sport.COURSE,30,4.60);
        activite5 = new Activite("Activité 5",Instant.ofEpochSecond(20000),Sport.VELO,90,20.73);
        activite6 = new Activite("Activité 6",Instant.ofEpochSecond(200000),Sport.VELO,20,10.20);
        activite7 = new Activite("Activité 7",Instant.ofEpochSecond(3000),Sport.RAQUETTE,50,4.75);
        activite8 = new Activite("Activité 8",Instant.ofEpochSecond(30000),Sport.RAQUETTE,60,5.20);
        activite9 = new Activite("Activité 9",Instant.ofEpochSecond(300000),Sport.SKI_RANDONNEE,120,12.30);
        activite10 = new Activite("Activité 10",Instant.ofEpochSecond(4000),Sport.SKI_RANDONNEE,100,4.90);
        activite11 = new Activite("Activité 11",Instant.ofEpochSecond(40000),Sport.SKI,75,7.60);
        activite12 = new Activite("Activité 12",Instant.ofEpochSecond(400000),Sport.SKI,90,9.42);
        activite13 = new Activite("Activité 13",Instant.ofEpochSecond(5000),Sport.PATIN,45,7.35);
        activite14 = new Activite("Activité 14",Instant.ofEpochSecond(50000),Sport.PATIN,25,3.4);
        activite15 = new Activite("Activité 15",Instant.ofEpochSecond(500000),Sport.SKI_FOND,35,8.4);
        activite16 = new Activite("Activité 16",Instant.ofEpochSecond(600000),Sport.SKI_FOND,12,1.9);
    }

    @Before
    public void ActiviteFichier()
    {
        activite17 = new Activite("Activité 17", Sport.RANDONNEE, new File("D:test fichiers.gpx"));

        activite18 = new Activite("Activité 18",  Sport.SKI_FOND, new File("D:test fichiers2.gpx"));
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

        try
        {
            new Activite("Allo", Sport.VELO,null);

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
        assertEquals("Activité 17", activite17.getNom());
        assertEquals("Activité 18", activite18.getNom());
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
        assertEquals(activite1.getDate(), Instant.ofEpochSecond(1000));
        assertEquals(activite2.getDate(), Instant.ofEpochSecond(10000));
        assertEquals(activite3.getDate(), Instant.ofEpochSecond(100000));
        assertEquals(activite4.getDate(), Instant.ofEpochSecond(2000));
        assertEquals(activite5.getDate(), Instant.ofEpochSecond(20000));
        assertEquals(activite6.getDate(), Instant.ofEpochSecond(200000));
        assertEquals(activite7.getDate(), Instant.ofEpochSecond(3000));
        assertEquals(activite8.getDate(), Instant.ofEpochSecond(30000));
        assertEquals(activite9.getDate(), Instant.ofEpochSecond(300000));
        assertEquals(activite10.getDate(), Instant.ofEpochSecond(4000));
        assertEquals(activite11.getDate(), Instant.ofEpochSecond(40000));
        assertEquals(activite12.getDate(), Instant.ofEpochSecond(400000));
        assertEquals(activite13.getDate(), Instant.ofEpochSecond(5000));
        assertEquals(activite14.getDate(), Instant.ofEpochSecond(50000));
        assertEquals(activite15.getDate(), Instant.ofEpochSecond(500000));
        assertEquals(activite16.getDate(), Instant.ofEpochSecond(600000));
        assertEquals(activite17.getDate(), Instant.parse("2012-10-24T00:00:00Z"));
        assertEquals(activite18.getDate(), Instant.parse("2011-07-08T00:00:00Z"));
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
        assertEquals(activite17.getDuree(), Duration.ofSeconds(948));
        assertEquals(activite18.getDuree(), Duration.ofSeconds(74867));

    }

    @Test
    public void getHeureDebut()
    {
        assertEquals(Instant.parse("2012-10-24T23:29:40Z"), activite17.getHeureDebut());
    }

    @Test
    public void getHeureFin()
    {
        assertEquals(Instant.parse("2012-10-24T23:45:28Z"), activite17.getHeureFin());
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
        assertEquals(3125.701786185354, activite17.getDistanceMetrique(), 0.0);
        assertEquals(9248.34046827275, activite18.getDistanceMetrique(), 0.0);
    }


    @Test
    public void getDenivelePositifMetrique()
    {
        assertEquals(158.4000015258789, activite17.getDenivelePositif(), 0.0);
    }

    @Test
    public void getDeniveleNegatifMetrique()
    {
        assertEquals(135.40000343322754, activite17.getDeniveleNegatif(), 0.0);
    }

    @Test
    public void getVitesseActuelleMetrique()
    {
        System.out.println("Vitesse actuelle fin fichier 1\n"+ activite17.getVitesseActuelle() + " km/h\n");
        System.out.println("Vitesse actuelle fin fichier 2\n"+ activite18.getVitesseActuelle() + " km/h\n");
    }

    @Test
    public void getVitesseMetrique()
    {
        assertEquals(6, activite1.getVitesseMoyenne(), 0.0);
        //assertEquals(4.8, activite2.getVitesseMetrique(), 0.0);
        assertEquals(13.26, activite3.getVitesseMoyenne(), 0.0);
        assertEquals(9.2, activite4.getVitesseMoyenne(), 0.0);
        assertEquals(13.82, activite5.getVitesseMoyenne(), 0.0);
        //assertEquals(30.6, activite6.getVitesseMetrique(), 0.0);
        assertEquals(5.7, activite7.getVitesseMoyenne(), 0.0);
        assertEquals(5.2, activite8.getVitesseMoyenne(), 0.0);
        assertEquals(6.15, activite9.getVitesseMoyenne(), 0.0);
        assertEquals(2.94, activite10.getVitesseMoyenne(), 0.0);
        assertEquals(6.08, activite11.getVitesseMoyenne(), 0.0);
        assertEquals(6.28, activite12.getVitesseMoyenne(), 0.0);
        assertEquals(9.8, activite13.getVitesseMoyenne(), 0.0);
        //assertEquals(8.16, activite14.getVitesseMetrique(), 0.0);
        assertEquals(14.4, activite15.getVitesseMoyenne(), 0.0);
        assertEquals(9.5, activite16.getVitesseMoyenne(), 0.0);
    }


    @Test
    public void getTabLatitude()
    {
        assertTrue(activite17.getTabLatitude().size() ==206 && activite17.getTabLatitude().get(0)==38.92747367732227 && activite17.getTabLatitude().get(activite17.getTabLatitude().size()-1)==38.92482256516814);
    }

    @Test
    public void getTabLongitude()
    {
        assertTrue(activite17.getTabLongitude().size() ==206 && activite17.getTabLongitude().get(0)==-77.02016168273985 && activite17.getTabLongitude().get(activite17.getTabLongitude().size()-1)==-77.02575484290719);
    }

    @Test
    public void getTabDistanceMetrique()
    {
        assertEquals(206, activite17.getTabDistance().size());
        double distance = 0;
        for (int i = 0; i < activite17.getTabDistance().size(); i++)
        {
            distance += activite17.getTabDistance().get(i);
        }
        assertEquals(activite17.getDistanceMetrique(), distance, 0.0);

        assertEquals(206, activite17.getTabDistance().size());
         distance = 0;
        for (int i = 0; i < activite18.getTabDistance().size(); i++)
        {
            distance += activite18.getTabDistance().get(i);
        }
        assertEquals(activite18.getDistanceMetrique(), distance, 0.0);
    }

    @Test
    public void getTabElevationMetrique()
    {
        assertTrue(activite17.getTabElevation().size() ==206 && activite17.getTabElevation().get(0)==25.600000381469727 && activite17.getTabElevation().get(activite17.getTabElevation().size()-1)==48.599998474121094);
    }

    @Test
    public void getTabVitesseMetrique()
    {
    }

    @Test
    public void getTabTemps()
    {
        assertTrue(activite17.getTabTemps().size() ==206 && activite17.getTabTemps().get(0).equals(Instant.parse("2012-10-24T23:29:40.000Z")) && activite17.getTabTemps().get(activite17.getTabTemps().size()-1).equals(Instant.parse("2012-10-24T23:45:28.000Z")));
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
    public void calculerDistance()
    {
        System.out.println("Distance fichier 1\n"+activite17.calculerDistance(0,activite17.getTabTemps().size()-1)+" mètres\n");
        System.out.println("Distance fichier 2\n"+activite18.calculerDistance(0,activite18.getTabTemps().size()-1)+" mètres\n");

        //trop long à calculer, mais l'algo fonctionne activite 18 = https://www.visugpx.com/1310202508
    }

    @Test
    public void calculerVitesseMoyenne()
    {
        assertEquals(6,activite1.calculerVitesseMoyenne() , 0.0);
       //assertEquals(4.8, activite2.calculerVitesseMoyenne(), 0.0);
        assertEquals(13.26, activite3.calculerVitesseMoyenne() , 0.0);
        assertEquals(9.2, activite4.calculerVitesseMoyenne(), 0.0);
        assertEquals(13.82, activite5.calculerVitesseMoyenne(), 0.0);
          //assertEquals(30.6, activite6.calculerVitesseMoyenne(activite6.getDuree().toMillis()/1000)*3600, 0.0);
        assertEquals(5.7, activite7.calculerVitesseMoyenne(), 0.0);
        assertEquals(5.2, activite8.calculerVitesseMoyenne(), 0.0);
        assertEquals(6.15, activite9.calculerVitesseMoyenne(), 0.0);
        assertEquals(2.94, activite10.calculerVitesseMoyenne(), 0.0);
        assertEquals(6.08, activite11.calculerVitesseMoyenne(), 0.0);
        assertEquals(6.28, activite12.calculerVitesseMoyenne(), 0.0);
        assertEquals(9.8, activite13.calculerVitesseMoyenne(), 0.0);
         //assertEquals(8.16, activite14.calculerVitesseMoyenne(), 0.0);
        assertEquals(14.4, activite15.calculerVitesseMoyenne(), 0.0);
        assertEquals(9.5, activite16.calculerVitesseMoyenne(), 0.0);

        System.out.println("Vitesse fichier 1\n"+ activite17.calculerVitesseMoyenne() + " km/h\n");
        assertEquals(11.869753618425397, activite17.calculerVitesseMoyenne(), 0.0);

        System.out.println("Vitesse fichier 2\n"+ activite18.calculerVitesseMoyenne() + " km/h\n");
        assertEquals(0.4447089596989582,activite18.calculerVitesseMoyenne(), 0.0);
    }


    @Test
    public void getAltitudeMaxMetrique()
    {
        assertEquals(activite17.getAltitudeMax(), 82.80000305175781,0);
        assertEquals(activite18.getAltitudeMax(), 181.7,0);

    }

    @Test
    public void getAltitudeMinMetrique()
    {
        assertEquals(activite17.getAltitudeMin(), 25.600000381469727,0);
        assertEquals(activite18.getAltitudeMin(), 73.1,0);
    }

    @Test
    public void getAltitudeActuelleMetrique()
    {
        assertEquals(48.599998474121094, activite17.getAltitudeActuelle(), 0.0);
        assertEquals(166, activite18.getAltitudeActuelle(), 0.0);

    }


    @Test
    public void ecrireFichier()
    {

    }
}