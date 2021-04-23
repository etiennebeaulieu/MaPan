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

public class Activite implements Serializable {

    private String nom = null;
    private Instant date = null;
    private Sport sport = null;
    private Duration duree = null;
    private Instant heureDebut = null;
    private Instant heureFin = null;
    private double distanceMetrique = 0;
    private double denivelePositifMetrique = 0;
    private double deniveleNegatifMetrique = 0;
    private double vitesseActuelleMetrique = 0;
    private double vitesseMetrique = 0;
    private double altitudeMaxMetrique = 0;
    private double altitudeMinMetrique = 0;
    private double altitudeActuelleMetrique = 0;

    private Graphique graphique = null;
    public ArrayList<Double> tabLatitude = null;
    public ArrayList<Double> tabLongitude = null;
    public ArrayList<Double> tabElevationMetrique = null;
    public ArrayList<Double> tabDistanceMetrique = null;
    public ArrayList<Double> tabVitesseMetrique = null;
    public ArrayList<Instant> tabTemps = null;
    public ArrayList<Point> listeCoordonnee = null;


    /*Constructeur sans fichier
    Permet de créer une activité à partir de données fournis par l'utilisateur*/
    public Activite(String pNom, Instant pDate, Sport pSport, int pDuree, double pDistance) {

        //Valide les paramètres fournis par l'utilisateur (non-null et non-vide) puis initialize les attributs avec ces données
            if (validerNom(pNom) && validerDistance(pDistance) && validerDuree(pDuree) && validerSport(pSport)) {
                setNom(pNom);
                setDate(pDate);
                setSport(pSport);
                //Durée en minute
                setDuree(pDuree);
                setDistanceMetrique(pDistance*1000);
                setVitesseMetrique(calculerVitesseMoyenne());
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
                listeCoordonnee = new ArrayList<>();

                tabDistanceMetrique.add(0.0);
                tabVitesseMetrique.add(0.0);


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

                setVitesseActuelleMetrique(getVitesseActuelleMetrique());

                setVitesseMetrique(calculerVitesseMoyenne());
                setAltitudeMaxMetrique(getAltitudeMaxMetrique());
                setAltitudeMinMetrique(getAltitudeMinMetrique());
                setAltitudeActuelleMetrique(getAltitudeActuelleMetrique());

            }
        } catch (Exception e) {
            System.out.println("Paramètre invalide");
        }
    }

    public Activite(String pNom, Sport pSport){
        //Valide les données et initialize les tableaux de données
        if (validerNom(pNom) && validerSport(pSport)) {

            tabLatitude = new ArrayList<>();
            tabLongitude = new ArrayList<>();
            tabTemps = new ArrayList<>();
            tabElevationMetrique = new ArrayList<>();
            tabDistanceMetrique = new ArrayList<>();
            tabVitesseMetrique = new ArrayList<>();
            listeCoordonnee = new ArrayList<>();

            tabDistanceMetrique.add(0.0);
            tabVitesseMetrique.add(0.0);


            setNom(pNom);
            setSport(pSport);
            setDate(Instant.now());
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

    public void setDuree(Duration duree){
        this.duree = duree;
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

    public void setDistanceMetrique(double distanceMetrique) {
        if (validerDistance(distanceMetrique)) {

            this.distanceMetrique = distanceMetrique;
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

    //calculer en km/h
    public double getVitesseActuelleMetrique() {


        return tabVitesseMetrique.get(tabVitesseMetrique.size()-1);
    }

    public void setVitesseActuelleMetrique(double vitesseActuelleMetrique) {
        this.vitesseActuelleMetrique = vitesseActuelleMetrique;
    }

    //calculer en km/h
    public double getVitesseMetrique() {
        return vitesseMetrique;
    }

    public void setVitesseMetrique(double vitesseMetrique) {
        this.vitesseMetrique = vitesseMetrique;
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

            if(!((Double) dx).equals(NaN)){
                distance += dx;
            }


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
                descente += tabElevationMetrique.get(i) - tabElevationMetrique.get(i + 1);
            }

        }
        setDenivelePositifMetrique(montee);
        setDeniveleNegatifMetrique(descente);
    }

    public double calculerVitesseMoyenne() {
        double vitesseMoyenne;
        if(tabVitesseMetrique == null){
            double total = 0;
            for(int i = 0; i<tabVitesseMetrique.size(); i++){
                total += tabVitesseMetrique.get(i);
            }
            vitesseMoyenne = (total/tabVitesseMetrique.size()) *3.6;
        }
        else
            vitesseMoyenne = (getDistanceMetrique() / (getDuree().toMillis() / 1000)) *3.6;

        return vitesseMoyenne;
    }

    public double calculerVitesse(int i1, int i2){
        double vitesse;

        double d1 = 0;
        double d2 = 0;
        long t1   = 0;
        long t2 =   0;

        if (tabTemps!= null && tabTemps.size()>2 && tabDistanceMetrique.size()>2 && i2+1 < tabTemps.size()) {
            d1 = tabDistanceMetrique.get(i1);
            d2 = tabDistanceMetrique.get(i2+1)+ tabDistanceMetrique.get(i2);
            t1 = tabTemps.get(i1).toEpochMilli();
            t2 = tabTemps.get(i2+1).toEpochMilli();
           /* t1 = tabTemps.get(i1).getEpochSecond();
            t2 = tabTemps.get(i2).getEpochSecond();*/
        }
        if(t2 != t1)
            vitesse = (d2) / ((t2 - t1)/1000.0);
        else
            vitesse = calculerVitesseMoyenne();

        return vitesse;
    }

    public double trouverVitesseMax()
    {
        double maxVal = Double.MIN_VALUE;

        for (int i = 0; i < tabVitesseMetrique.size(); i++) {

            if (tabVitesseMetrique.get(i) > maxVal) {
                maxVal = tabVitesseMetrique.get(i);
            }
        }
        return maxVal;
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

    public void setAltitudeMaxMetrique(double altitudeMaxMetrique) {
        this.altitudeMaxMetrique = altitudeMaxMetrique;
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

    public void setAltitudeMinMetrique(double altitudeMinMetrique) {
        this.altitudeMinMetrique = altitudeMinMetrique;
    }

    public double getAltitudeActuelleMetrique() {
        return this.tabElevationMetrique.get(tabElevationMetrique.size() - 1);
    }

    public void setAltitudeActuelleMetrique(double altitudeActuelleMetrique) {
        this.altitudeActuelleMetrique = altitudeActuelleMetrique;
    }

    private void construireTabDistance() {
        for (int i = 0; i < tabTemps.size() - 1; i++) {
            tabDistanceMetrique.add(calculerDistance(i, i + 1));
        }

    }

    private void construireTabVitesse()
        {
            for (int i = 0;i<tabTemps.size()-1;i++)
            {
                tabVitesseMetrique.add(calculerVitesse(i, i+1));
            }
        }


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

                    Instant time2 = Instant.from((OffsetDateTime) OffsetDateTime.parse(node.getChildText("time", node.getNamespace())).withOffsetSameInstant(ZoneOffset.UTC));
                    //Instant time = Instant.parse(node.getChildText("time", node.getNamespace()));
                    this.tabTemps.add(time2);

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
