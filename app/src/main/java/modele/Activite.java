package modele;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Activite {

    public static final double METRE_PIED = 3.28084;
    public static final double METRE_MILES = 0.000621371;

    private String nom = null;
    private Instant date = null;
    private Sport sport = null;
    private Duration duree = null;
    private Instant heureDebut = null;
    private Instant heureFin = null;
    private double distanceMetrique = 0;
    private double distanceImperiale = 0;
    private double denivelePositifMetrique = 0;
    private double deniveleNegatifMetrique = 0;
    private double denivelePositifImperiale = 0;
    private double deniveleNegatifImperiale = 0;
    private double vitesseActuelleMetrique = 0;
    private double vitesseActuelleImperiale = 0;
    private double vitesseMetrique = 0;
    private double vitesseImperiale = 0;
    private double altitudeMaxMetrique = 0;
    private double altitudeMaxImperiale = 0;
    private double altitudeMinMetrique = 0;
    private double altitudeMinImperiale = 0;
    private double altitudeActuelleMetrique = 0;
    private double altitudeActuelleImperiale = 0;
    private Graphique graphique = null;
    private Map<String, Double> statistique = null;

    private ArrayList<Double> tabLatitude = null;
    private ArrayList<Double> tabLongitude = null;
    private ArrayList<Double> tabElevationMetrique = null;
    private ArrayList<Double> tabDistanceMetrique = null;
    private ArrayList<Double> tabVitesseMetrique = null;
    private ArrayList<Instant> tabTemps = null;


    public Activite(String pNom, Instant pDate, Sport pSport, int pDuree, double pDistance) {

        try {
            if (validerNom(pNom) && validerDistance(pDistance) && validerDuree(pDuree) && validerSport(pSport)) {
                setNom(pNom);
                setDate(pDate);
                setSport(pSport);
                setDuree(pDuree);
                setDistanceMetrique(pDistance);
                setDistanceImperiale(pDistance);
                setVitesseMetrique(calculerVitesseMoyenne(getDuree().toMillis() / 1000) * 3600);
                setVitesseImperiale(getVitesseMetrique() * 1000 * METRE_MILES);
            }
        } catch (Exception e) {
            System.out.println("Paramètre invalide");
        }
    }

    public Activite(String pNom, Instant pDate, Sport pSport, File pFichier) {

        try {


            if (validerNom(pNom) && validerFichier(pFichier) && validerSport(pSport)) {

                tabLatitude = new ArrayList<>();
                tabLongitude = new ArrayList<>();
                tabTemps = new ArrayList<>();
                tabElevationMetrique = new ArrayList<>();
                tabDistanceMetrique = new ArrayList<>();
                tabVitesseMetrique = new ArrayList<>();


                setNom(pNom);
                setDate(pDate);
                setSport(pSport);

                // lire fichier + setTableaux
                this.lireFichier(pFichier);

                heureDebut = tabTemps.get(0);
                heureFin = tabTemps.get(tabTemps.size() - 1);
                duree = Duration.between(heureDebut, heureFin);

                setDistanceMetrique(calculerDistance(0, tabDistanceMetrique.size() - 1));
                setDistanceImperiale(getDistanceMetrique() * METRE_MILES);
                calculerDenivele();
                setVitesseActuelleMetrique(getVitesseActuelleMetrique());
                setVitesseActuelleImperiale(getVitesseActuelleImperiale());
                setVitesseMetrique(getVitesseMetrique());
                setVitesseImperiale(getVitesseMetrique() * METRE_MILES);
                setAltitudeMaxMetrique(getAltitudeMaxMetrique());
                setAltitudeMaxImperiale(getAltitudeMaxImperiale());
                setAltitudeMinMetrique(getAltitudeMinMetrique());
                setAltitudeMinImperiale(getAltitudeMinImperiale());
                setAltitudeActuelleMetrique(getAltitudeActuelleMetrique());
                setAltitudeActuelleImperiale(getAltitudeActuelleImperiale());
            }
        } catch (Exception e) {
            System.out.println("Paramètre invalide");
        }
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (validerNom(nom)) {
            this.nom = nom;
        }
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        if (validerSport(sport)) {
            this.sport = sport;
        }
    }


    public Duration getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        if (validerDuree(duree)) {
            this.duree = Duration.ofMinutes(duree);
        }
    }

    public Instant getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Instant heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Instant getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Instant heureFin) {
        this.heureFin = heureFin;
    }

    public double getDistanceMetrique() {
        return this.distanceMetrique;
    }

    public void setDistanceMetrique(double distanceMetrique) {
        if (validerDistance(distanceMetrique)) {
            this.distanceMetrique = distanceMetrique;
        }
    }

    public double getDistanceImperiale() {
        return this.getDistanceMetrique();
    }

    public void setDistanceImperiale(double distance) {
        if (validerDistance(distance)) {
            this.distanceImperiale = distance;
        }
    }

    public double getDenivelePositifMetrique() {
        return denivelePositifMetrique;
    }

    public void setDenivelePositifMetrique(double denivelePositifMetrique) {
        this.denivelePositifMetrique = denivelePositifMetrique;
    }

    public double getDeniveleNegatifMetrique() {
        return deniveleNegatifMetrique;
    }

    public void setDeniveleNegatifMetrique(double deniveleNegatifMetrique) {
        this.deniveleNegatifMetrique = deniveleNegatifMetrique;
    }

    public double getDenivelePositifImperiale() {
        return getDenivelePositifMetrique() * METRE_PIED;
    }

    public void setDenivelePositifImperiale(double denivelePositifImperiale) {
        this.denivelePositifImperiale = denivelePositifImperiale;
    }

    public double getDeniveleNegatifImperiale() {
        return getDeniveleNegatifMetrique() * METRE_PIED;
    }

    public void setDeniveleNegatifImperiale(double deniveleNegatifImperiale) {
        this.deniveleNegatifImperiale = deniveleNegatifImperiale;
    }

    //calculer en km/h
    public double getVitesseActuelleMetrique() {
        return calculerDistance(tabDistanceMetrique.size() - 2, tabDistanceMetrique.size() - 1) * 3.6;
    }

    public void setVitesseActuelleMetrique(double vitesseActuelleMetrique) {
        this.vitesseActuelleMetrique = vitesseActuelleMetrique;
    }

    //calculer en miles/h
    public double getVitesseActuelleImperiale() {
        return (calculerDistance(tabDistanceMetrique.size() - 2, tabDistanceMetrique.size() - 1) * METRE_MILES) / 3600;
    }

    public void setVitesseActuelleImperiale(double vitesseActuelleImperiale) {
        this.vitesseActuelleImperiale = vitesseActuelleImperiale;
    }

    //calculer en km/h
    public double getVitesseMetrique() {
        return vitesseMetrique;
    }

    public void setVitesseMetrique(double vitesseMetrique) {
        this.vitesseMetrique = vitesseMetrique;
    }

    //calculer en miles/h
    public double getVitesseImperiale() {
        return this.vitesseImperiale;
    }

    public void setVitesseImperiale(double vitesseImperiale) {
        this.vitesseImperiale = vitesseImperiale;
    }

    public ArrayList<Double> getTabLatitude() {
        return tabLatitude;
    }

    public void setTabLatitude(ArrayList<Double> tabLatitude) {
        this.tabLatitude = tabLatitude;
    }

    public ArrayList<Double> getTabLongitude() {
        return tabLongitude;
    }

    public void setTabLongitude(ArrayList<Double> tabLongitude) {
        this.tabLongitude = tabLongitude;
    }

    public ArrayList<Double> getTabDistanceMetrique() {
        return tabDistanceMetrique;
    }

    public void setTabDistanceMetrique(ArrayList<Double> tabDistanceMetrique) {
        this.tabDistanceMetrique = tabDistanceMetrique;
    }

    public ArrayList<Double> getTabElevationMetrique() {
        return tabElevationMetrique;
    }

    public void setTabElevationMetrique(ArrayList<Double> tabElevationMetrique) {
        this.tabElevationMetrique = tabElevationMetrique;
    }

    public ArrayList<Double> getTabVitesseMetrique() {
        return tabVitesseMetrique;
    }

    public void setTabVitesseMetrique(ArrayList<Double> tabVitesseMetrique) {
        this.tabVitesseMetrique = tabVitesseMetrique;
    }

    public ArrayList<Instant> getTabTemps() {
        return tabTemps;
    }

    public void setTabTemps(ArrayList<Instant> tabTemps) {
        this.tabTemps = tabTemps;
    }

    public Graphique getGraphique() {
        return graphique;
    }

    public void setGraphique(Graphique graphique) {
        this.graphique = graphique;
    }

    public Map<String, Double> getStatistique() {
        return statistique;
    }

    public void setStatistique(Map<String, Double> statistique) {
        this.statistique = statistique;
    }

    private boolean validerNom(String pNom) {
        return pNom != null && !pNom.isEmpty();
    }

    private boolean validerDistance(double pDistance) {
        return pDistance > 0;
    }

    private boolean validerDuree(int pDuree) {
        return pDuree > 0;
    }

    private boolean validerSport(Sport pSport) {
        return pSport != null;
    }

    private boolean validerFichier(File pFichier) {
        return pFichier != null;
    }

    //Calcul en mètre
    public double calculerDistance(int debut, int fin) {
        double distance = 0;
        double lat1;
        double lon1;
        double lat2;
        double lon2;
        double ele1;
        double ele2;

        for (int i = debut; i < fin; i++) {
            lat1 = tabLatitude.get(i);
            lon1 = tabLongitude.get(i);
            lat2 = tabLatitude.get(i + 1);
            lon2 = tabLongitude.get(i + 1);
            ele1 = tabElevationMetrique.get(i);
            ele2 = tabElevationMetrique.get(i + 1);

            double theta = lon1 - lon2;
            double dx = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dx = Math.acos(dx);
            dx = Math.toDegrees(dx);
            dx = dx * 6371009;

            dx = Math.sqrt(Math.pow(dx, 2) + Math.pow(ele2 - ele1, 2));


            distance += dx;
        }
        return distance;
    }

    ///calcul en mètre et converti en impériale via setters
    public void calculerDenivele() {

        double montee = 0;
        double descente = 0;

        for (int i = 0; i < tabElevationMetrique.size() - 1; i++) {
            if (tabElevationMetrique.get(i) < tabElevationMetrique.get(i + 1)) {
                montee += tabElevationMetrique.get(i + 1) - tabElevationMetrique.get(i);
            } else if (tabElevationMetrique.get(i) > tabElevationMetrique.get(i + 1)) {
                descente += tabElevationMetrique.get(i + 1) - tabElevationMetrique.get(i);
            }
            setDenivelePositifMetrique(montee);
            setDeniveleNegatifMetrique(descente);
            setDenivelePositifImperiale(getDenivelePositifImperiale());
            setDeniveleNegatifImperiale(getDeniveleNegatifImperiale());
        }
    }

    public double calculerVitesseMoyenne(long temps) {
        return getDistanceMetrique() / temps;
    }


    public double getAltitudeMaxMetrique() {
        double maxVal = Double.MAX_VALUE;

        for (int i = 0; i < tabElevationMetrique.size(); i++) {

            if (tabElevationMetrique.get(i) > maxVal) {
                maxVal = tabElevationMetrique.get(i);
            }
        }
        return maxVal;
    }

    public void setAltitudeMaxMetrique(double altitudeMaxMetrique) {
        this.altitudeMaxMetrique = altitudeMaxMetrique;
    }

    public double getAltitudeMaxImperiale() {
        return getAltitudeMaxMetrique() * METRE_PIED;
    }

    public void setAltitudeMaxImperiale(double altitudeMaxImperiale) {
        this.altitudeMaxImperiale = altitudeMaxImperiale;
    }

    public double getAltitudeMinMetrique() {
        double minVal = Double.MIN_VALUE;

        for (int i = 0; i < tabElevationMetrique.size(); i++) {

            if (tabElevationMetrique.get(i) > minVal) {
                minVal = tabElevationMetrique.get(i);
            }
        }
        return minVal;
    }

    public void setAltitudeMinMetrique(double altitudeMinMetrique) {
        this.altitudeMinMetrique = altitudeMinMetrique;
    }

    public double getAltitudeMinImperiale() {
        return getAltitudeMinMetrique() * METRE_PIED;
    }

    public void setAltitudeMinImperiale(double altitudeMinImperiale) {
        this.altitudeMinImperiale = altitudeMinImperiale;
    }

    public double getAltitudeActuelleMetrique() {
        return this.tabElevationMetrique.get(tabElevationMetrique.size() - 1);
    }

    public void setAltitudeActuelleMetrique(double altitudeActuelleMetrique) {
        this.altitudeActuelleMetrique = altitudeActuelleMetrique;
    }

    public double getAltitudeActuelleImperiale() {
        return METRE_PIED * getAltitudeActuelleMetrique();
    }

    public void setAltitudeActuelleImperiale(double altitudeActuelleImperiale) {
        this.altitudeActuelleImperiale = altitudeActuelleImperiale;
    }

    public void enregistrer(File fichier) {
        this.ecrireFichier(fichier);
    }

    public void lireFichier(File fichier) {

        SAXBuilder builder = new SAXBuilder();
        File xmlFile = fichier;

        if (fichier != null) {
            try {

                Document document = (Document) builder.build(xmlFile);
                Element rootNode = document.getRootElement();
                Element metadata = rootNode.getChild("metadata");

                this.setNom(metadata.getChildText("nom"));
                this.setDate(Instant.parse(metadata.getChildText("date")));
                String sport = metadata.getChildText("sport");
                try {
                    this.setSport(Sport.valueOf(sport));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }


                List list = rootNode.getChild("trk").getChild("trkseg").getChildren("trkpt");

                for (int i = 0; i < list.size(); i++) {

                    Element node = (Element) list.get(i);


                    Double lat = Double.parseDouble(node.getAttributeValue("lat"));
                    this.tabLatitude.add(lat);


                    Double lon = Double.parseDouble(node.getAttributeValue("lon"));
                    this.tabLongitude.add(lon);


                    Double ele = Double.parseDouble(node.getChildText("ele"));
                    this.tabElevationMetrique.add(ele);

                    Instant time = Instant.parse(node.getChildText("time"));
                    this.tabTemps.add(time);

                }


            } catch (IOException | JDOMException io) {
                System.out.println(io.getMessage());
            }
        }


    }


    public void ecrireFichier(File fichier) {
        ArrayList<Double> tabLatitude = this.getTabLatitude();
        ArrayList<Double> tabLongitude = this.getTabLongitude();
        ArrayList<Double> tabElevation = this.getTabElevationMetrique();
        ArrayList<Instant> tabTemps = this.getTabTemps();

        try {
            Element gpx = new Element("gpx");
            Document doc = new Document(gpx);
            doc.setRootElement(gpx);
            Element metadata = new Element("metadata");
            metadata.addContent(new Element("nom").setText(this.getNom()));
            metadata.addContent(new Element("sport").setText(this.getSport().toString()));
            metadata.addContent(new Element("date").setText(this.getDate().toString()));

            gpx.addContent(metadata);


            Element trace = new Element("trk");
            gpx.addContent(trace);
            Element segment = new Element("trkseg");
            trace.addContent(segment);

            for (int i = 0; i < tabLatitude.size(); i++) {
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
