package controleur;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.Application;
import com.example.mapan.R;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;

import modele.Activite;
import modele.Sport;

public class ControleurHistorique extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private ArrayList<Activite> listeActivites = new ArrayList<>();
    private ListView historique_list;
    private boolean isDistanceMetrique = true;
    private Button modifier_DeleteActivity;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.historique);
        historique_list = findViewById(R.id.modifier_list);

        test();

        historique_list.setAdapter(new activiteAdapter(this, R.layout.list_row, listeActivites));

    }

    public void test() {
        Activite a1 = new Activite("Activité 1", Instant.ofEpochMilli(170000), Sport.SKI_RANDONNEE, Duration.ofMillis(4500000), 19.5);
        Activite a2 = new Activite("Activité 2", Instant.ofEpochMilli(170000000), Sport.COURSE, Duration.ofMillis(4500000), 20.5);
        Activite a3 = new Activite("Activité 3", Instant.ofEpochMilli(170000000), Sport.RANDONNEE, Duration.ofMillis(4500000), 21.5);
        Activite a4 = new Activite("Activité 4", Instant.ofEpochMilli(170000000), Sport.RAQUETTE, Duration.ofMillis(4500000), 90.5);
        Activite a5 = new Activite("Activité 5", Instant.ofEpochMilli(170000000), Sport.VELO, Duration.ofMillis(4500000), 50.5);

        listeActivites.add(a3);
        listeActivites.add(a1);
        listeActivites.add(a5);
        listeActivites.add(a2);
        listeActivites.add(a4);


    }


    private class activiteAdapter extends ArrayAdapter<Activite> {

        private Context mContext;
        private int mResource;

        public activiteAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Activite> objects) {
            super(context, resource, objects);
            this.mContext = context;
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
            if (isDistanceMetrique)
                txtDistance.setText(getItem(position).getDistanceMetrique() + "km");
            else
                txtDistance.setText(getItem(position).getDistanceImperiale() + "mi");


            return convertView;
        }
    }


    public ArrayList<Activite> getListeActivite() {
        return listeActivites;
    }


    public void trierListeDate() {
        listeActivites.sort(Comparator.comparing(Activite::getDate));
        historique_list.setAdapter(new activiteAdapter(this, R.layout.list_row, listeActivites));
    }

    public void trierListeDuree() {
        listeActivites.sort(Comparator.comparing(Activite::getDuree));
        historique_list.setAdapter(new activiteAdapter(this, R.layout.list_row, listeActivites));
    }

    public void trierListeDistance() {
        listeActivites.sort(Comparator.comparingDouble(Activite::getDistanceMetrique).reversed());
        historique_list.setAdapter(new activiteAdapter(this, R.layout.list_row, listeActivites));
    }

    public void trierListeNom() {
        listeActivites.sort(Comparator.comparing(Activite::getNom));
        historique_list.setAdapter(new activiteAdapter(this, R.layout.list_row, listeActivites));
    }

    public void trierListeSport() {
        listeActivites.sort(Comparator.comparing(Activite::getSport));
        historique_list.setAdapter(new activiteAdapter(this, R.layout.list_row, listeActivites));
    }

    public void ouvrirAccueil(View view){
        startActivity(new Intent(ControleurHistorique.this, Application.class));
    }

    public void ouvrirModifier(View view){
        startActivity(new Intent(ControleurHistorique.this, ControleurHistoriqueModifier.class));
    }

    public void ouvrirParametre(View view){
        startActivity(new Intent(ControleurHistorique.this, ControleurParametre.class));
    }

    public void deleteActivity(View view) {
        modifier_DeleteActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historique_list.removeViewAt(historique_list.getSelectedItemPosition());
                historique_list.deferNotifyDataSetChanged();
            }
        });

    }


    public void afficherMenuTri(View view) {
        PopupMenu menuTri = new PopupMenu(this, view);
        menuTri.setOnMenuItemClickListener(this);
        MenuInflater inflater = menuTri.getMenuInflater();
        inflater.inflate(R.menu.menu_tri, menuTri.getMenu());


        menuTri.show();

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
       boolean retour = false;
        switch (item.getItemId())
        {
            case R.id.triNom:
                trierListeNom();
                retour = true;
                break;

            case R.id.triSport:
                trierListeSport();
                retour = true;
                break;

            case R.id.triDistance:
                trierListeDistance();
                retour = true;
                break;

            case R.id.triDuree:
                trierListeDuree();
                retour = true;
                break;

            default:
                retour = false;
        }
        return retour;
    }

}


