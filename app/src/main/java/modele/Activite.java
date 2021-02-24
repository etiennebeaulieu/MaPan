package modele;

import java.io.File;
import java.util.Date;
import java.util.Map;

public class Activite {

    public static final double METRE_PIED = 3.28084;
    public static final double METRE_MILES = 0.000621371;

    private String nom = null;
    private Date date = null;
    private Sport sport = null;
    private Date duree = null;
    private Date heureDebut = null;
    private Date heureFin = null;
    private double distance = 0;
    private Graphique graphique = null;
    private Map<String, Double> statistique = null;

    private double[] tabDistanceMetrique = null;
    private double[] tabElevationMetrique = null;
    private double[] tabVitesseMetrique = null;


    public Activite(String pNom, Date pDate, Sport pSport, Date pDuree, Date pHeureDebut, Date pHeureFin, double pDistance) {
if(validernom(pNom))
{
    setNom(pNom);
    setDate(pDate);
    setSport(pSport);
    setDuree(pDuree);
    setHeureDebut(pHeureDebut);
    setHeureFin(pHeureFin);
    setDistance(pDistance);
}
    }
    public Activite(String pNom, Date pDate, Sport pSport, File pFichier)
    {
        if(validernom(pNom) && validerFichier(pFichier))
        {
            setNom(pNom);
            setDate(pDate);
            setSport(pSport);
            // lire fichier

            //heureDebut  = 1er point ficher
            //heureFin = dernier point fichier
            //duree = dernier-1er
            //distance = dernier-1er
        }
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Date getDuree() {
        return duree;
    }

    public void setDuree(Date duree) {
        this.duree = duree;
    }

    public Date getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double[] getTabDistanceMetrique() {
        return tabDistanceMetrique;
    }

    public void setTabDistanceMetrique(double[] tabDistanceMetrique) {
        this.tabDistanceMetrique = tabDistanceMetrique;
    }

    public double[] getTabElevationMetrique() {
        return tabElevationMetrique;
    }

    public double[] getTabVitesseMetrique() {
        return tabVitesseMetrique;
    }

    public void setTabVitesseMetrique(double[] tabVitesseMetrique) {
        this.tabVitesseMetrique = tabVitesseMetrique;
    }

    public void setTabElevationMetrique(double[] tabElevationMetrique) {
        this.tabElevationMetrique = tabElevationMetrique;
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

    private boolean validernom(String pNom)
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

    public double getDistanceMetrique()
    {
        return  this.tabDistanceMetrique[this.tabDistanceMetrique.length]-this.tabDistanceMetrique[0];
    }

    public double getDistanceImperiale()
    {
        return  this.getDistanceMetrique()*METRE_MILES;
    }

    public double getDenivelePositifMetrique()
    {

        return 0;
    }

    public double getDenivelePositifImperiale()
    {
        return  getDenivelePositifMetrique()*METRE_PIED;
    }

    public double getDeniveleNegatifMetrique()
    {
        return  0;
    }

    public double getDeniveleNegatifImperiale()
    {
        return  0;
    }

    public double getVitesseActuelleMetrique()
    {
        

        return  0;
    }

    public double getVitesseActuelleImperiale()
    {
        return  0;
    }

    public double getVitesseMoyenneMetrique()
    {
        return  0;
    }

    public double getVitesseMoyenneImperiale()
    {
        return  0;
    }

    public double getAltitudeMaxMetrique()
    {
        double maxVal = Double.MAX_VALUE;

        for(int i = 0; i < tabElevationMetrique.length; i++) {

            if (tabElevationMetrique[i] > maxVal) {
                maxVal = tabElevationMetrique[i];
            }
        }
        return  maxVal;
    }

    public double getAltitudeMaxImperiale()
    {
        return  getAltitudeMaxMetrique()*METRE_PIED;
    }

    public double getAltitudeMinMetrique()
    {
        double minVal = Double.MIN_VALUE;

        for(int i = 0; i < tabElevationMetrique.length; i++) {

            if (tabElevationMetrique[i] > minVal) {
                minVal = tabElevationMetrique[i];
            }
        }
        return  minVal;
    }

    public double getAltitudeMinImperiale()
    {
        return  getAltitudeMinMetrique()*METRE_PIED;
    }

    public double getAltitudeActuelleMetrique()
    {
        return  this.tabElevationMetrique[this.tabElevationMetrique.length];
    }

    public double getAltitudeActuelleImperiale()
    {
        return  METRE_PIED *getAltitudeActuelleMetrique();
    }

}
