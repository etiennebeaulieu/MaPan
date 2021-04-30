package modele;

import com.mapbox.geojson.Point;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.NaN;

public class Activite implements Serializable
{

    private String nom = null;
    private Instant date = null;
    private Sport sport = null;
    private Duration duree = null;
    private Instant heureDebut = null;
    private Instant heureFin = null;

    //En mètre
    private double distanceMetrique = 0;
    //En mètre
    private double denivelePositif = 0;
    private double deniveleNegatif = 0;
    //En m/s
    private double vitesseActuelle = 0;
    //En m/s
    private double vitesseMoyenne = 0;
    //En mètre
    private double altitudeMax = 0;
    private double altitudeMin = 0;
    private double altitudeActuelle = 0;

    public ArrayList<Double> tabLatitude = null;
    public ArrayList<Double> tabLongitude = null;
    //En mètre
    public ArrayList<Double> tabElevation = null;
    //En mètre
    public ArrayList<Double> tabDistance = null;
    //En m/s
    public ArrayList<Double> tabVitesse = null;
    public ArrayList<Instant> tabTemps = null;
    public ArrayList<Point> listeCoordonnee = null;


    /*Constructeur sans fichier
    Permet de créer une activité à partir de données fournis par l'utilisateur*/
    public Activite(String pNom, Instant pDate, Sport pSport, int pDuree, double pDistance)
    {

        //Valide les paramètres fournis par l'utilisateur (non-null et non-vide) puis initialize les attributs avec ces données
        if (validerNom(pNom) && validerDistance(pDistance) && validerDuree(pDuree) && validerSport(pSport))
        {
            setNom(pNom);
            setDate(pDate);
            setSport(pSport);
            //Durée en minute
            setDuree(pDuree);
            setDistanceMetrique(pDistance * 1000);
            setVitesseMoyenne(calculerVitesseMoyenne());
        }

    }

    /*Contructeur avec fichier
    Permet de créer une activité à partir d'un fichier et d'un nom + sport fournis par l'utilisateur
     */
    public Activite(String pNom, Sport pSport, File pFichier)
    {

        try
        {

            //Valide les données et initialize les tableaux de données
            if (validerNom(pNom) && validerFichier(pFichier) && validerSport(pSport))
            {

                tabLatitude = new ArrayList<>();
                tabLongitude = new ArrayList<>();
                tabTemps = new ArrayList<>();
                tabElevation = new ArrayList<>();
                tabDistance = new ArrayList<>();
                tabVitesse = new ArrayList<>();
                listeCoordonnee = new ArrayList<>();

                tabDistance.add(0.0);
                tabVitesse.add(0.0);


                setNom(pNom);
                setSport(pSport);
                //Lis le fichier gpx pour en extaire les données et les ajouter aux tableaux
                this.lireFichier(pFichier);
                setDate(tabTemps.get(0).truncatedTo(ChronoUnit.DAYS));

                setHeureDebut(tabTemps.get(0));
                setHeureFin(tabTemps.get(tabTemps.size() - 1));
                duree = Duration.between(heureDebut, heureFin);

                construireTabDistance();
                construireTabVitesse();

                setDistanceMetrique(calculerDistance(0, tabTemps.size() - 1));
                calculerDenivele();

                setVitesseActuelle(getVitesseActuelle());

                setVitesseMoyenne(calculerVitesseMoyenne());
                setAltitudeMax(getAltitudeMax());
                setAltitudeMin(getAltitudeMin());
                setAltitudeActuelle(getAltitudeActuelle());

            }
        } catch (Exception e)
        {
            System.out.println("Paramètre invalide");
        }
    }

    public Activite(String pNom, Sport pSport)
    {
        //Valide les données et initialize les tableaux de données
        if (validerNom(pNom) && validerSport(pSport))
        {

            tabLatitude = new ArrayList<>();
            tabLongitude = new ArrayList<>();
            tabTemps = new ArrayList<>();
            tabElevation = new ArrayList<>();
            tabDistance = new ArrayList<>();
            tabVitesse = new ArrayList<>();
            listeCoordonnee = new ArrayList<>();

            tabDistance.add(0.0);
            tabVitesse.add(0.0);


            setNom(pNom);
            setSport(pSport);
            setDate(Instant.now());
        }
    }


    public String getNom()
    {
        return nom;
    }

    public void setNom(String nom)
    {
        if (validerNom(nom))
        {
            this.nom = nom;
        }
    }

    public Instant getDate()
    {
        return date;
    }

    private void setDate(Instant date)
    {
        this.date = date;
    }

    public Sport getSport()
    {
        return sport;
    }

    public void setSport(Sport sport)
    {
        if (validerSport(sport))
        {
            this.sport = sport;
        }
    }


    public Duration getDuree()
    {
        return duree;
    }

    public void setDuree(Duration duree)
    {
        this.duree = duree;
    }

    private void setDuree(int duree)
    {
        if (validerDuree(duree))
        {
            this.duree = Duration.ofMinutes(duree);
        }
    }

    public Instant getHeureDebut()
    {
        return heureDebut;
    }

    private void setHeureDebut(Instant heureDebut)
    {
        this.heureDebut = heureDebut;
    }

    public Instant getHeureFin()
    {
        return heureFin;
    }

    private void setHeureFin(Instant heureFin)
    {
        this.heureFin = heureFin;
    }

    public double getDistanceMetrique()
    {
        return this.distanceMetrique;
    }

    public void setDistanceMetrique(double distanceMetrique)
    {
        if (validerDistance(distanceMetrique))
        {

            this.distanceMetrique = distanceMetrique;
        }
    }

    public double getDenivelePositif()
    {
        return denivelePositif;
    }

    public void setDenivelePositif(double denivelePositif)
    {
        this.denivelePositif = denivelePositif;
    }

    public double getDeniveleNegatif()
    {
        return deniveleNegatif;
    }

    public void setDeniveleNegatif(double deniveleNegatif)
    {
        this.deniveleNegatif = deniveleNegatif;
    }

    //calculer en km/h
    public double getVitesseActuelle()
    {


        return tabVitesse.get(tabVitesse.size() - 1);
    }

    public void setVitesseActuelle(double vitesseActuelle)
    {
        this.vitesseActuelle = vitesseActuelle;
    }

    //calculer en km/h
    public double getVitesseMoyenne()
    {
        return vitesseMoyenne;
    }

    public void setVitesseMoyenne(double vitesseMoyenne)
    {
        this.vitesseMoyenne = vitesseMoyenne;
    }

    public ArrayList<Double> getTabLatitude()
    {
        return tabLatitude;
    }

    public ArrayList<Double> getTabLongitude()
    {
        return tabLongitude;
    }

    public ArrayList<Double> getTabDistance()
    {
        return tabDistance;
    }

    public ArrayList<Double> getTabElevation()
    {
        return tabElevation;
    }

    public ArrayList<Instant> getTabTemps()
    {
        return tabTemps;
    }

    private boolean validerNom(String pNom)
    {
        return pNom != null && !pNom.isEmpty();
    }

    private boolean validerDistance(double pDistance)
    {
        return pDistance > 0;
    }

    private boolean validerDuree(int pDuree)
    {
        return pDuree > 0;
    }

    private boolean validerSport(Sport pSport)
    {
        return pSport != null;
    }

    private boolean validerFichier(File pFichier)
    {
        return pFichier != null;
    }

    //Calcul en mètre
    public double calculerDistance(int debut, int fin)
    {
        double distance = 0;
        double lat1;
        double lon1;
        double lat2;
        double lon2;
        double ele1;
        double ele2;

        for (int i = debut; i < fin; i++)
        {
            lat1 = tabLatitude.get(i);
            lon1 = tabLongitude.get(i);
            lat2 = tabLatitude.get(i + 1);
            lon2 = tabLongitude.get(i + 1);
            ele1 = tabElevation.get(i);
            ele2 = tabElevation.get(i + 1);

            double theta = lon1 - lon2;
            //Calcule la la distance entre 2 points
            double dx = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dx = 60 * Math.acos(dx);
            dx = Math.toDegrees(dx);
            dx = dx * 1852;

            //Prend en compte le dénivelé dans la distance calculée
            dx = Math.sqrt(Math.pow(dx, 2) + Math.pow(ele2 - ele1, 2));

            if (!((Double) dx).equals(NaN))
            {
                distance += dx;
            }


        }
        return distance;
    }

    ///calcul en mètre et converti en impériale via setters
    public void calculerDenivele()
    {

        double montee = 0;
        double descente = 0;

        for (int i = 0; i < tabElevation.size() - 1; i++)
        {
            if (tabElevation.get(i) < tabElevation.get(i + 1))
            {
                montee += tabElevation.get(i + 1) - tabElevation.get(i);
            } else if (tabElevation.get(i) > tabElevation.get(i + 1))
            {
                descente += tabElevation.get(i) - tabElevation.get(i + 1);
            }

        }
        setDenivelePositif(montee);
        setDeniveleNegatif(descente);
    }

    //Vitesse moyenne en m/s
    public double calculerVitesseMoyenne()
    {

        return (getDistanceMetrique() / (getDuree().toMillis() / 1000));
    }

    //En m/s
    public double calculerVitesse(int i1, int i2)
    {
        double vitesse;

        double d1 = 0;
        double d2 = 0;
        long t1 = 0;
        long t2 = 0;

        if (tabTemps != null && tabTemps.size() > 2 && tabDistance.size() > 2 && i2 + 1 < tabTemps.size())
        {
            d1 = tabDistance.get(i1);
            d2 = tabDistance.get(i2 + 1) + tabDistance.get(i2);
            t1 = tabTemps.get(i1).toEpochMilli();
            t2 = tabTemps.get(i2 + 1).toEpochMilli();
        }
        if (t2 != t1) vitesse = (d2) / ((t2 - t1) / 1000.0);
        else vitesse = calculerVitesseMoyenne();

        return vitesse;
    }

    public double trouverVitesseMax()
    {
        double maxVal = Double.MIN_VALUE;

        for (int i = 0; i < tabVitesse.size(); i++)
        {

            if (tabVitesse.get(i) > maxVal)
            {
                maxVal = tabVitesse.get(i);
            }
        }
        return maxVal;
    }

    public double getAltitudeMax()
    {
        double maxVal = Double.MIN_VALUE;

        for (int i = 0; i < tabElevation.size(); i++)
        {

            if (tabElevation.get(i) > maxVal)
            {
                maxVal = tabElevation.get(i);
            }
        }
        return maxVal;
    }

    public void setAltitudeMax(double altitudeMax)
    {
        this.altitudeMax = altitudeMax;
    }

    public double getAltitudeMin()
    {
        double minVal = Double.MAX_VALUE;

        for (int i = 0; i < tabElevation.size(); i++)
        {

            if (tabElevation.get(i) < minVal)
            {
                minVal = tabElevation.get(i);
            }
        }
        return minVal;
    }

    public void setAltitudeMin(double altitudeMin)
    {
        this.altitudeMin = altitudeMin;
    }

    public double getAltitudeActuelle()
    {
        return this.tabElevation.get(tabElevation.size() - 1);
    }

    public void setAltitudeActuelle(double altitudeActuelle)
    {
        this.altitudeActuelle = altitudeActuelle;
    }

    private void construireTabDistance()
    {
        for (int i = 0; i < tabTemps.size() - 1; i++)
        {
            tabDistance.add(calculerDistance(i, i + 1));
        }

    }

    private void construireTabVitesse()
    {
        for (int i = 0; i < tabTemps.size() - 1; i++)
        {
            tabVitesse.add((calculerVitesse(i, i + 1)));
        }
    }


    // Lis un fichier GPX pour inscrire ses données dans un objet de type activité
    public void lireFichier(File fichier)
    {

        SAXBuilder builder = new SAXBuilder();
        File xmlFile = fichier;

        if (fichier != null)
        {
            try
            {

                Document document = (Document) builder.build(xmlFile);
                Element rootNode = document.getRootElement();

                //Sort l'élément "trk" du fichier
                Element trk = rootNode.getChild("trk", rootNode.getNamespace());
                //Sort l'élément "trkseg" du fichier
                Element trkseg = trk.getChild("trkseg", trk.getNamespace());

                //Sort les élément "trkpt" du fichier
                List list = trkseg.getChildren("trkpt", trkseg.getNamespace());

                //Itère sur chaque élément "trkpt" pour en sortir les différents attributs
                for (int i = 0; i < list.size(); i++)
                {

                    Element node = (Element) list.get(i);


                    Double lat = Double.parseDouble(node.getAttributeValue("lat"));
                    this.tabLatitude.add(lat);


                    Double lon = Double.parseDouble(node.getAttributeValue("lon"));
                    this.tabLongitude.add(lon);


                    Double ele = Double.parseDouble(node.getChildText("ele", node.getNamespace()));
                    this.tabElevation.add(ele);

                    Instant time2 = Instant.from((OffsetDateTime) OffsetDateTime.parse(node.getChildText("time", node.getNamespace())).withOffsetSameInstant(ZoneOffset.UTC));
                    //Instant time = Instant.parse(node.getChildText("time", node.getNamespace()));
                    this.tabTemps.add(time2);

                }


            } catch (IOException | JDOMException io)
            {
                System.out.println(io.getMessage());
            }
        }


    }


    //Écrit un fichier GPX à partir d'un objet de type activité
    public void ecrireFichier(File fichier)
    {
        ArrayList<Double> tabLatitude = this.getTabLatitude();
        ArrayList<Double> tabLongitude = this.getTabLongitude();
        ArrayList<Double> tabElevation = this.getTabElevation();
        ArrayList<Instant> tabTemps = this.getTabTemps();

        try
        {
            Element gpx = new Element("gpx");
            Document doc = new Document(gpx);

            Element trace = new Element("trk");
            gpx.addContent(trace);
            Element segment = new Element("trkseg");
            trace.addContent(segment);

            //Itere sur chaque éléments des tableaux de données pour créer les éléments nécessaire dans le fichiers gpx
            for (int i = 0; i < tabLatitude.size(); i++)
            {
                Element point = new Element("trkpt");
                point.setAttribute(new Attribute("lat", tabLatitude.get(i).toString()));
                point.setAttribute(new Attribute("lon", tabLongitude.get(i).toString()));
                point.addContent(new Element("ele").setText(tabElevation.get(i).toString()));
                point.addContent(new Element("time").setText(tabTemps.get(i).toString()));

                segment.addContent(point);
            }

            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, new FileWriter(fichier));

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
