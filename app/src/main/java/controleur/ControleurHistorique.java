package controleur;

import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Comparator;
import java.util.List;
import java.util.Observer;

import modele.Activite;

public class ControleurHistorique extends AppCompatActivity{

    private List<Activite> listeActivites;
    private ListView historique_List;




    public List<Activite> getListeActivite() {
        return listeActivites;
    }


   public void trierListeDate(){
    listeActivites.sort(Comparator.comparing(Activite::getDate));
   }

   public void trierListeDuree(){
    listeActivites.sort(Comparator.comparing(Activite::getDuree));
   }

   public void trierListeDistance(){
    listeActivites.sort(Comparator.comparingDouble(Activite::getDistanceMetrique).reversed());
   }

   public void trierListeNom(){
    listeActivites.sort(Comparator.comparing(Activite::getNom));
   }

   public void trierListeSport(){
    listeActivites.sort(Comparator.comparing(Activite::getSport));
   }
}
