package controleur;

import android.content.Context;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapan.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Observer;

import modele.Activite;
import modele.Sport;

public class ControleurHistorique extends AppCompatActivity{

    private ArrayList<Activite> listeActivites = new ArrayList<>();
    private ListView historique_list;
    private boolean isDistanceMetrique = true;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.historique);
        historique_list = findViewById(R.id.historique_list);

        test();

        historique_list.setAdapter(new activiteAdapter(this, R.layout.list_row,listeActivites));

    }

    public void test(){
        Activite a1 = new Activite("Activité 1", new Date(170000000), Sport.SKI_RANDONNEE, new Date(15000), new Date(15000000), new Date(450000000), 20.5);
        Activite a2 = new Activite("Activité 2", new Date(170000000), Sport.COURSE, new Date(15000), new Date(15000000), new Date(450000000), 20.5);
        Activite a3 = new Activite("Activité 3", new Date(170000000), Sport.RANDONNEE, new Date(15000), new Date(15000000), new Date(450000000), 20.5);
        Activite a4 = new Activite("Activité 4", new Date(170000000), Sport.RAQUETTE, new Date(15000), new Date(15000000), new Date(450000000), 20.5);
        Activite a5 = new Activite("Activité 5", new Date(170000000), Sport.VELO, new Date(15000), new Date(15000000), new Date(450000000), 20.5);

        listeActivites.add(a1);
        listeActivites.add(a2);
        listeActivites.add(a3);
        listeActivites.add(a4);
        listeActivites.add(a5);


    }


    private class activiteAdapter extends ArrayAdapter<Activite> {

        private Context mContext;
        private int mResource;

        public activiteAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Activite> objects) {
            super(context, resource, objects);
            this.mContext=context;
            this.mResource = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);

            convertView = layoutInflater.inflate(mResource, parent, false);

            ImageView iconSport = convertView.findViewById(R.id.iconSport);
            TextView txtNom = convertView.findViewById(R.id.textViewNom);
            TextView txtDate = convertView.findViewById(R.id.textViewDate);
            TextView txtDuree = convertView.findViewById(R.id.textViewDuree);
            TextView txtDistance = convertView.findViewById(R.id.textViewDistance);

            iconSport.setImageResource(getItem(position).getSport().getImage());
            txtNom.setText(getItem(position).getNom());
            txtDate.setText(getItem(position).getDate().toString());
            txtDuree.setText(getItem(position).getDuree().toString());
            if(isDistanceMetrique)
                txtDistance.setText(getItem(position).getDistanceMetrique() + "km");
            else
                txtDistance.setText(getItem(position).getDistanceImperiale() + "mi");


            return convertView;
        }
    }


    public ArrayList<Activite> getListeActivite() {
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
