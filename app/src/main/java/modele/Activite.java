package modele;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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


    public Activite(String pNom, Instant pDate, Sport pSport, Duration pDuree, Instant pHeureDebut,Instant pHeureFin,double pDistance) {
if(validerNom(pNom))
{
    setNom(pNom);
    setDate(pDate);
    setSport(pSport);
    setDuree(pDuree);
    setHeureDebut(pHeureDebut);
    setHeureFin(pHeureFin);
    setDistanceMetrique(pDistance);
    setDistanceImperiale(pDistance);
    setVitesseMetrique(calculerVitesseMoyenne(getHeureDebut().getTime(), getHeureFin().getTime())*3.6);
}
    }
    public Activite(String pNom, Instant pDate, Sport pSport, File pFichier)
    {
        if(validerNom(pNom) && validerFichier(pFichier))
        {

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
            Fichiers.lireFichier(tabLatitude, tabLongitude, tabElevationMetrique, tabTemps);

            heureDebut = tabTemps.get(0);
            heureFin = tabTemps.get(tabTemps.size()-1);
            duree = Duration.between(heureDebut, heureFin);
            //heureDebut  = 1er point ficher
            //heureFin = dernier point fichier
            //duree = dernier-1er

            setDistanceMetrique(calculerDistance(0,tabDistanceMetrique.size()-1));
            setDistanceImperiale(getDistanceImperiale());
            calculerDenivele();
            setVitesseActuelleMetrique(getVitesseActuelleMetrique());
            setVitesseActuelleImperiale(getVitesseActuelleImperiale());
            setVitesseMetrique(getVitesseMetrique());
            setVitesseImperiale(getVitesseImperiale());
            setAltitudeMaxMetrique(getAltitudeMaxMetrique());
            setAltitudeMaxImperiale(getAltitudeMaxImperiale());
            setAltitudeMinMetrique(getAltitudeMinMetrique());
            setAltitudeMinImperiale(getAltitudeMinImperiale());
            setAltitudeActuelleMetrique(getAltitudeActuelleMetrique());
            setAltitudeActuelleImperiale(getAltitudeActuelleImperiale());
        }
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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
        this.sport = sport;
    }

    public Duration getDuree() {
        return duree;
    }

    public void setDuree(Duration duree) {
        this.duree = duree;
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

    public double getDistanceMetrique()
    {
        return  this.distanceMetrique;
    }

    public void setDistanceMetrique(double distanceMetrique) {
        this.distanceMetrique = distanceMetrique;
    }

    public double getDenivelePositifMetrique() {
        return denivelePositifMetrique;
    }

    public double getDistanceImperiale()
    {
        return  this.getDistanceMetrique()*METRE_MILES;
    }

    public void setDistanceImperiale(double distanceImperiale) {
        this.distanceImperiale = distanceImperiale;
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

    public double getDenivelePositifImperiale()
    {
        return  getDenivelePositifMetrique()*METRE_PIED;
    }

    public void setDenivelePositifImperiale(double denivelePositifImperiale) {
        this.denivelePositifImperiale = denivelePositifImperiale;
    }

    public double getDeniveleNegatifImperiale()
    {
        return  getDeniveleNegatifMetrique()*METRE_PIED;
    }

    public void setDeniveleNegatifImperiale(double deniveleNegatifImperiale) {
        this.deniveleNegatifImperiale = deniveleNegatifImperiale;
    }

    //calculer en km/h
    public double getVitesseActuelleMetrique()
    {
        return  calculerDistance(tabDistanceMetrique.size()-2,tabDistanceMetrique.size()-1)*3.6;
    }

    public void setVitesseActuelleMetrique(double vitesseActuelleMetrique) {
        this.vitesseActuelleMetrique = vitesseActuelleMetrique;
    }

    //calculer en miles/h
    public double getVitesseActuelleImperiale()
    {
        return  (calculerDistance(tabDistanceMetrique.size()-2,tabDistanceMetrique.size()-1)*METRE_MILES)/3600;
    }

    public void setVitesseActuelleImperiale(double vitesseActuelleImperiale) {
        this.vitesseActuelleImperiale = vitesseActuelleImperiale;
    }

    //calculer en km/h
    public double getVitesseMetrique() {
        return  vitesseMetrique;
    }

    public void setVitesseMetrique(double vitesseMetrique) {
        this.vitesseMetrique = vitesseMetrique;
    }

    //calculer en miles/h
    public double getVitesseImperiale() {
        return getDistanceImperiale()/3600;
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

    private boolean validerNom(String pNom)
    {
        return !pNom.isEmpty();
    }

    private boolean validerDate()
    {
        return true;
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

        for(int i = debut; i<fin;i++)
        {
            lat1 = tabLatitude.get(i);
            lon1 = tabLongitude.get(i);
            lat2 = tabLatitude.get(i+1);
            lon2 = tabLongitude.get(i+1);
            ele1 = tabElevationMetrique.get(i);
            ele2 = tabElevationMetrique.get(i+1);

            double theta = lon1 - lon2;
            double dx = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dx = Math.acos(dx);
            dx = Math.toDegrees(dx);
            dx = dx * 6371009;

            dx = Math.sqrt(Math.pow(dx,2)+Math.pow(ele2-ele1,2));


            distance += dx;
        }
        return distance;
    }

    ///calcul en mètre et converti en impériale via setters
    public void calculerDenivele()
    {

        double montee = 0;
        double descente = 0;

        for(int i = 0; i< tabElevationMetrique.size()-1;i++)
        {
            if(tabElevationMetrique.get(i)<tabElevationMetrique.get(i+1))
            {
                montee+=tabElevationMetrique.get(i+1) -tabElevationMetrique.get(i);
            }
            else if(tabElevationMetrique.get(i)>tabElevationMetrique.get(i+1))
            {
                descente+=tabElevationMetrique.get(i+1) -tabElevationMetrique.get(i);
        }
            setDenivelePositifMetrique(montee);
            setDeniveleNegatifMetrique(descente);
            setDenivelePositifImperiale(getDenivelePositifImperiale());
            setDeniveleNegatifImperiale(getDeniveleNegatifImperiale());
        }
    }

    public double calculerVitesseMoyenne(long debut, long fin)
    {
        long temps = fin - debut /1000;

        return getDistanceMetrique()/temps;
    }


    public double getAltitudeMaxMetrique()
    {
        double maxVal = Double.MAX_VALUE;

        for(int i = 0; i < tabElevationMetrique.size(); i++) {

            if (tabElevationMetrique.get(i) > maxVal) {
                maxVal = tabElevationMetrique.get(i);
            }
        }
        return  maxVal;
    }

    public void setAltitudeMaxMetrique(double altitudeMaxMetrique) {
        this.altitudeMaxMetrique = altitudeMaxMetrique;
    }

    public double getAltitudeMaxImperiale()
    {
        return  getAltitudeMaxMetrique()*METRE_PIED;
    }

    public void setAltitudeMaxImperiale(double altitudeMaxImperiale) {
        this.altitudeMaxImperiale = altitudeMaxImperiale;
    }

    public double getAltitudeMinMetrique()
    {
        double minVal = Double.MIN_VALUE;

        for(int i = 0; i < tabElevationMetrique.size(); i++) {

            if (tabElevationMetrique.get(i) > minVal) {
                minVal = tabElevationMetrique.get(i);
            }
        }
        return  minVal;
    }

    public void setAltitudeMinMetrique(double altitudeMinMetrique) {
        this.altitudeMinMetrique = altitudeMinMetrique;
    }

    public double getAltitudeMinImperiale()
    {
        return  getAltitudeMinMetrique()*METRE_PIED;
    }

    public void setAltitudeMinImperiale(double altitudeMinImperiale) {
        this.altitudeMinImperiale = altitudeMinImperiale;
    }

    public double getAltitudeActuelleMetrique()
    {
        return  this.tabElevationMetrique.get(tabElevationMetrique.size()-1);
    }

    public void setAltitudeActuelleMetrique(double altitudeActuelleMetrique) {
        this.altitudeActuelleMetrique = altitudeActuelleMetrique;
    }

    public double getAltitudeActuelleImperiale()
    {
        return  METRE_PIED *getAltitudeActuelleMetrique();
    }

    public void setAltitudeActuelleImperiale(double altitudeActuelleImperiale) {
        this.altitudeActuelleImperiale = altitudeActuelleImperiale;
    }
}
