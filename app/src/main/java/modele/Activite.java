package modele;

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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Activite implements Serializable {

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

    private ArrayList<Double> tabLatitude = null;
    private ArrayList<Double> tabLongitude = null;
    private ArrayList<Double> tabElevationMetrique = null;
    private ArrayList<Double> tabDistanceMetrique = null;
    private ArrayList<Double> tabVitesseMetrique = null;
    private ArrayList<Instant> tabTemps = null;


    /*Constructeur sans fichier
    Permet de créer une activité à partir de données fournis par l'utilisateur*/
    public Activite(String pNom, Instant pDate, Sport pSport, int pDuree, double pDistance) {

        //Valide les paramètres fournis par l'utilisateur (non-null et non-vide) puis initialize les attributs avec ces données
            if (validerNom(pNom) && validerDistance(pDistance) && validerDuree(pDuree) && validerSport(pSport)) {
                setNom(pNom);
                setDate(pDate);
                setSport(pSport);
                setDuree(pDuree);
                setDistanceMetrique(pDistance*1000);
                setDistanceImperiale(pDistance*0.621371);
                setVitesseMetrique(calculerVitesseMoyenne(getDuree().toMillis() / 1000) * 3600);
                setVitesseImperiale(getVitesseMetrique() * 1000 * METRE_MILES);
            }

    }

    /*Contructeur avec fichier
    Permet de créer une activité à partir d'un fichier et d'un nom + sport fournis par l'utilisateur
     */
    public Activite(String pNom, Sport pSport, File pFichier) {

        try {

            //Valide les données et initialize les tableaux de données
            if (validerNom(pNom) && validerFichier(pFichier) && validerSport(pSport)) {

                tabLatitude = new ArrayList<>();
                tabLongitude = new ArrayList<>();
                tabTemps = new ArrayList<>();
                tabElevationMetrique = new ArrayList<>();
                tabDistanceMetrique = new ArrayList<>();
                tabVitesseMetrique = new ArrayList<>();


                setNom(pNom);
                setSport(pSport);
                //Lis le fichier gpx pour en extaire les données et les ajouter aux tableaux
                this.lireFichier(pFichier);
                setDate(tabTemps.get(0).truncatedTo(ChronoUnit.DAYS));

                setHeureDebut(tabTemps.get(0));
                setHeureFin(tabTemps.get(tabTemps.size() - 1));
                duree = Duration.between(heureDebut, heureFin);

                construireTabDistance();
                //construireTabVitesse();

                setDistanceMetrique(calculerDistance(0, tabTemps.size() - 1));
                setDistanceImperiale(getDistanceMetrique() * METRE_MILES);
                calculerDenivele();

                setVitesseActuelleMetrique(getVitesseActuelleMetrique());
                setVitesseActuelleImperiale(getVitesseActuelleMetrique() * 1000 * METRE_MILES);

                long t2 = tabTemps.get(tabTemps.size() - 1).getEpochSecond() - Instant.EPOCH.getEpochSecond();
                long t1 = tabTemps.get(tabTemps.size() - 2).getEpochSecond() - Instant.EPOCH.getEpochSecond();
                long dt = t2 - t1;

                setVitesseMetrique(calculerVitesseMoyenne(dt));
                setVitesseImperiale(getVitesseMetrique() * 1000 * METRE_MILES);
                setAltitudeMaxMetrique(getAltitudeMaxMetrique());
                setAltitudeMaxImperiale(getAltitudeMaxMetrique() * METRE_PIED);
                setAltitudeMinMetrique(getAltitudeMinMetrique());
                setAltitudeMinImperiale(getAltitudeMinMetrique() * METRE_PIED);
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

    private void setDate(Instant date) {
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

    private void setDuree(int duree) {
        if (validerDuree(duree)) {
            this.duree = Duration.ofMinutes(duree);
        }
    }

    public Instant getHeureDebut() {
        return heureDebut;
    }

    private void setHeureDebut(Instant heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Instant getHeureFin() {
        return heureFin;
    }

    private void setHeureFin(Instant heureFin) {
        this.heureFin = heureFin;
    }

    public double getDistanceMetrique() {
        return this.distanceMetrique;
    }

    private void setDistanceMetrique(double distanceMetrique) {
        if (validerDistance(distanceMetrique)) {

            this.distanceMetrique = distanceMetrique;
        }
    }

    public double getDistanceImperiale() {
        return this.distanceImperiale;
    }

    private void setDistanceImperiale(double distance) {
        if (validerDistance(distance)) {
            this.distanceImperiale = distance;
        }
    }

    public double getDenivelePositifMetrique() {
        return denivelePositifMetrique;
    }

    private void setDenivelePositifMetrique(double denivelePositifMetrique) {
        this.denivelePositifMetrique = denivelePositifMetrique;
    }

    public double getDeniveleNegatifMetrique() {
        return deniveleNegatifMetrique;
    }

    private void setDeniveleNegatifMetrique(double deniveleNegatifMetrique) {
        this.deniveleNegatifMetrique = deniveleNegatifMetrique;
    }

    public double getDenivelePositifImperiale() {
        return denivelePositifImperiale;
    }

    private void setDenivelePositifImperiale(double denivelePositifImperiale) {
        this.denivelePositifImperiale = denivelePositifImperiale;
    }

    public double getDeniveleNegatifImperiale() {
        return this.deniveleNegatifImperiale;
    }

    private void setDeniveleNegatifImperiale(double deniveleNegatifImperiale) {
        this.deniveleNegatifImperiale = deniveleNegatifImperiale;
    }

    //calculer en km/h
    public double getVitesseActuelleMetrique() {

        long t2 = tabTemps.get(tabTemps.size() - 1).getEpochSecond() - Instant.EPOCH.getEpochSecond();
        long t1 = tabTemps.get(tabTemps.size() - 2).getEpochSecond() - Instant.EPOCH.getEpochSecond();
        long dt = t2 - t1;
        return (tabDistanceMetrique.get(tabDistanceMetrique.size() - 1) / dt) * 3.6;
    }

    private void setVitesseActuelleMetrique(double vitesseActuelleMetrique) {
        this.vitesseActuelleMetrique = vitesseActuelleMetrique;
    }

    //calculer en miles/h
    public double getVitesseActuelleImperiale() {
        return vitesseActuelleImperiale;
    }

    private void setVitesseActuelleImperiale(double vitesseActuelleImperiale) {
        this.vitesseActuelleImperiale = vitesseActuelleImperiale;
    }

    //calculer en km/h
    public double getVitesseMetrique() {
        return vitesseMetrique;
    }

    private void setVitesseMetrique(double vitesseMetrique) {
        this.vitesseMetrique = vitesseMetrique;
    }

    //calculer en miles/h
    public double getVitesseImperiale() {
        return this.vitesseImperiale;
    }

    private void setVitesseImperiale(double vitesseImperiale) {
        this.vitesseImperiale = vitesseImperiale;
    }

    public ArrayList<Double> getTabLatitude() {
        return tabLatitude;
    }

    public ArrayList<Double> getTabLongitude() {
        return tabLongitude;
    }

    public ArrayList<Double> getTabDistanceMetrique() {
        return tabDistanceMetrique;
    }

    public ArrayList<Double> getTabElevationMetrique() {
        return tabElevationMetrique;
    }

    public ArrayList<Double> getTabVitesseMetrique() {
        return tabVitesseMetrique;
    }

    public ArrayList<Instant> getTabTemps() {
        return tabTemps;
    }

    public Graphique getGraphique() {
        return graphique;
    }

    public void setGraphique(Graphique graphique) {
        this.graphique = graphique;
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
            //Calcule la la distance entre 2 points
            double dx = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dx = 60 * Math.acos(dx);
            dx = Math.toDegrees(dx);
            dx = dx * 1852;

            //Prend en compte le dénivelé dans la distance calculée
            dx = Math.sqrt(Math.pow(dx, 2) + Math.pow(ele2 - ele1, 2));

            distance += dx;
        }
        return distance;
    }

    ///calcul en mètre et converti en impériale via setters
    private void calculerDenivele() {

        double montee = 0;
        double descente = 0;

        for (int i = 0; i < tabElevationMetrique.size() - 1; i++) {
            if (tabElevationMetrique.get(i) < tabElevationMetrique.get(i + 1)) {
                montee += tabElevationMetrique.get(i + 1) - tabElevationMetrique.get(i);
            } else if (tabElevationMetrique.get(i) > tabElevationMetrique.get(i + 1)) {
                descente += tabElevationMetrique.get(i) - tabElevationMetrique.get(i + 1);
            }

        }
        setDenivelePositifMetrique(montee);
        setDeniveleNegatifMetrique(descente);
        setDenivelePositifImperiale(montee * METRE_PIED);
        setDeniveleNegatifImperiale(descente * METRE_PIED);
    }

    public double calculerVitesseMoyenne(long temps) {
        return getDistanceMetrique() / temps;
    }


    public double getAltitudeMaxMetrique() {
        double maxVal = Double.MIN_VALUE;

        for (int i = 0; i < tabElevationMetrique.size(); i++) {

            if (tabElevationMetrique.get(i) > maxVal) {
                maxVal = tabElevationMetrique.get(i);
            }
        }
        return maxVal;
    }

    private void setAltitudeMaxMetrique(double altitudeMaxMetrique) {
        this.altitudeMaxMetrique = altitudeMaxMetrique;
    }

    public double getAltitudeMaxImperiale() {
        return this.altitudeMaxImperiale;
    }

    private void setAltitudeMaxImperiale(double altitudeMaxImperiale) {
        this.altitudeMaxImperiale = altitudeMaxImperiale;
    }

    public double getAltitudeMinMetrique() {
        double minVal = Double.MAX_VALUE;

        for (int i = 0; i < tabElevationMetrique.size(); i++) {

            if (tabElevationMetrique.get(i) < minVal) {
                minVal = tabElevationMetrique.get(i);
            }
        }
        return minVal;
    }

    private void setAltitudeMinMetrique(double altitudeMinMetrique) {
        this.altitudeMinMetrique = altitudeMinMetrique;
    }

    public double getAltitudeMinImperiale() {
        return this.altitudeMinImperiale;
    }

    private void setAltitudeMinImperiale(double altitudeMinImperiale) {
        this.altitudeMinImperiale = altitudeMinImperiale;
    }

    public double getAltitudeActuelleMetrique() {
        return this.tabElevationMetrique.get(tabElevationMetrique.size() - 1);
    }

    private void setAltitudeActuelleMetrique(double altitudeActuelleMetrique) {
        this.altitudeActuelleMetrique = altitudeActuelleMetrique;
    }

    public double getAltitudeActuelleImperiale() {
        return METRE_PIED * getAltitudeActuelleMetrique();
    }

    private void setAltitudeActuelleImperiale(double altitudeActuelleImperiale) {
        this.altitudeActuelleImperiale = altitudeActuelleImperiale;
    }

    private void construireTabDistance() {
        tabDistanceMetrique.add(0.0);
        for (int i = 0; i < tabTemps.size() - 1; i++) {
            tabDistanceMetrique.add(calculerDistance(i, i + 1));
        }

    }

    /*
    private void construireTabVitesse()
        {
            tabVitesseMetrique.add(0.0);
            for (int i = 0;i<tabTemps.size()-1;i++)
            {
                tabDistanceMetrique.add(getVitesseActuelleMetrique());
            }
            System.out.println(tabVitesseMetrique.size());
        }
    */


    // Lis un fichier GPX pour inscrire ses données dans un objet de type activité
    public void lireFichier(File fichier) {

        SAXBuilder builder = new SAXBuilder();
        File xmlFile = fichier;

        if (fichier != null) {
            try {

                Document document = (Document) builder.build(xmlFile);
                Element rootNode = document.getRootElement();

                //Sort l'élément "trk" du fichier
                Element trk = rootNode.getChild("trk", rootNode.getNamespace());
                //Sort l'élément "trkseg" du fichier
                Element trkseg = trk.getChild("trkseg", trk.getNamespace());

                //Sort les élément "trkpt" du fichier
                List list = trkseg.getChildren("trkpt", trkseg.getNamespace());

                //Itère sur chaque élément "trkpt" pour en sortir les différents attributs
                for (int i = 0; i < list.size(); i++) {

                    Element node = (Element) list.get(i);


                    Double lat = Double.parseDouble(node.getAttributeValue("lat"));
                    this.tabLatitude.add(lat);


                    Double lon = Double.parseDouble(node.getAttributeValue("lon"));
                    this.tabLongitude.add(lon);


                    Double ele = Double.parseDouble(node.getChildText("ele", node.getNamespace()));
                    this.tabElevationMetrique.add(ele);

                    Instant time = Instant.parse(node.getChildText("time", node.getNamespace()));
                    this.tabTemps.add(time);

                }


            } catch (IOException | JDOMException io) {
                System.out.println(io.getMessage());
            }
        }


    }


    //Écrit un fichier GPX à partir d'un objet de type activité
    public void ecrireFichier(File fichier) {
        ArrayList<Double> tabLatitude = this.getTabLatitude();
        ArrayList<Double> tabLongitude = this.getTabLongitude();
        ArrayList<Double> tabElevation = this.getTabElevationMetrique();
        ArrayList<Instant> tabTemps = this.getTabTemps();

        try {
            Element gpx = new Element("gpx");
            Document doc = new Document(gpx);
            doc.setRootElement(gpx);

            Element trace = new Element("trk");
            gpx.addContent(trace);
            Element segment = new Element("trkseg");
            trace.addContent(segment);

            //Itere sur chaque éléments des tableaux de données pour créer les éléments nécessaire dans le fichiers gpx
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
