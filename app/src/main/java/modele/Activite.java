package modele;

import java.io.File;
import java.util.Date;
import java.util.Map;

public class Activite {

    private String nom = null;
    private Date date = null;
    private Sport sport = null;
    private Date duree = null;
    private Date heureDebut = null;
    private Date heureFin = null;
    private double distance = 0;
    private Graphique graphique = null;
    private Map<String, Double> statistique = null;

    public Activite(String pNom, Date pDate, Sport pSport, Date pDuree, Date pHeureDebut, Date pHeureFin, double pDistance) {
if(validernom(pNom))
{
    setNom(pNom);
    setDate(pDate);
    setSport(pSport);
    setDuree(pDuree);
    setHeureDebut(heureDebut);
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
        return  0;
    }

    public double getDistanceImperiale()
    {
        return  0;
    }

    public double getDenivelePositifMetrique()
    {
        return  0;
    }

    public double getDenivelePositifImperiale()
    {
        return  0;
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

    public double getAltitudeMaxMetrique()
    {
        return  0;
    }

    public double getAltitudeMaxImperiale()
    {
        return  0;
    }

    public double getAltitudeMinMetrique()
    {
        return  0;
    }

    public double getAltitudeMinImperiale()
    {
        return  0;
    }

    public double getAltitudeActuelleMetrique()
    {
        return  0;
    }

    public double getAltitudeActuelleImperiale()
    {
        return  0;
    }

}
