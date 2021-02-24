package controleur;

import java.util.Comparator;
import java.util.List;

import modele.Activite;

public class ControleurHistorique {

    private List<Activite> listeActivite;


    public List<Activite> getListeActivite() {
        return listeActivite;
    }

   public void trierListeDate(){
    listeActivite.sort(Comparator.comparing(Activite::getDate));
   }

   public void trierListeDuree(){
    listeActivite.sort(Comparator.comparing(Activite::getDuree));
   }

   public void trierListeDistance(){
    listeActivite.sort(Comparator.comparingDouble(Activite::getDistanceMetrique).reversed());
   }

   public void trierListeNom(){
    listeActivite.sort(Comparator.comparing(Activite::getNom));
   }

   public void trierListeSport(){
    listeActivite.sort(Comparator.comparing(Activite::getSport));
   }
}
