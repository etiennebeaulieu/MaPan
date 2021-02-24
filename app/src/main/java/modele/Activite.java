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

    private double[] tabDistanceMetrique = null;
    private double[] tabElevationMetrique = null;
    private double[] tabVitesseMetrique = null;


    public Activite(String pNom, Date pDate, Sport pSport, Date pDuree, Date pHeureDebut,Date pHeureFin,double pDistance) {
if(validernom(pNom))
{
    setNom(pNom);
    setDate(pDate);
    setSport(pSport);
    setDuree(pDuree);
    setHeureDebut(pHeureDebut);
    setHeureFin(pHeureFin);
    setDistanceMetrique(pDistance);
    setVitesseMetrique(getDistanceMetrique()*3.6);
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

            setDistanceMetrique(calculerDistance(0,tabDistanceMetrique.length));
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

    public double getVitesseActuelleMetrique()
    {
        return  calculerDistance(tabDistanceMetrique.length-1,tabDistanceMetrique.length)*3.6;
    }

    public void setVitesseActuelleMetrique(double vitesseActuelleMetrique) {
        this.vitesseActuelleMetrique = vitesseActuelleMetrique;
    }

    public double getVitesseActuelleImperiale()
    {
        return  (calculerDistance(tabDistanceMetrique.length-1,tabDistanceMetrique.length)*METRE_MILES)/3600;
    }

    public void setVitesseActuelleImperiale(double vitesseActuelleImperiale) {
        this.vitesseActuelleImperiale = vitesseActuelleImperiale;
    }


    public double getVitesseMetrique() {
        return  getDistanceMetrique()*3.6;
    }

    public void setVitesseMetrique(double vitesseMetrique) {
        this.vitesseMetrique = vitesseMetrique;
    }

    public double getVitesseImperiale() {
        return getDistanceImperiale()/3600;
    }

    public void setVitesseImperiale(double vitesseImperiale) {
        this.vitesseImperiale = vitesseImperiale;
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

    public double calculerDistance(int debut, int fin)
    {
        double distance =0;

        for(int i = 0; i<tabDistanceMetrique.length-2;i++)
        {
            distance += tabDistanceMetrique[i+1] -tabDistanceMetrique[i];
        }
        return distance;
    }

    public void calculerDenivele()
    {

        double montee = 0;
        double descente = 0;

        for(int i = 0; i< tabElevationMetrique.length-2;i++)
        {
            if(tabElevationMetrique[i]<tabElevationMetrique[i+1])
            {
                montee+=tabElevationMetrique[i+1] -tabElevationMetrique[i];
            }
            else if(tabElevationMetrique[i]<tabElevationMetrique[i+1])
            {
                descente+=tabElevationMetrique[i+1] -tabElevationMetrique[i];
        }
            setDenivelePositifMetrique(montee);
            setDeniveleNegatifMetrique(descente);
            setDenivelePositifImperiale(getDenivelePositifImperiale());
            setDeniveleNegatifImperiale(getDeniveleNegatifImperiale());
        }
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

        for(int i = 0; i < tabElevationMetrique.length; i++) {

            if (tabElevationMetrique[i] > minVal) {
                minVal = tabElevationMetrique[i];
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
        return  this.tabElevationMetrique[this.tabElevationMetrique.length];
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
