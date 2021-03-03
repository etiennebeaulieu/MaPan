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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.Application;
import com.example.mapan.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;

import modele.Activite;
import modele.ActiviteAdapter;
import modele.Fichier;
import modele.Sport;

public class ControleurHistorique extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private ArrayList<Activite> listeActivites = new ArrayList<>();
    private ListView historique_list;
    private boolean isDistanceMetrique = true;
    private Button modifier_DeleteActivity;
    private ActiviteAdapter adapter;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.historique);
        historique_list = findViewById(R.id.modifier_list);
        //adapter = new ActiviteAdapter(this, R.layout.list_row, listeActivites);
        adapter = new ActiviteAdapter(this, R.layout.list_row, Fichier.getListeActivites());
        historique_list.setAdapter(adapter);


        //enregistrerActivitesTest();
        Fichier.rafraichir(this.getApplicationContext());
        adapter.notifyDataSetChanged();

    }

    public void enregistrerActivitesTest() {
        Activite a1 = new Activite("Activité 1", Instant.ofEpochMilli(1700000), Sport.SKI_RANDONNEE, 75, 19.5);
        Activite a2 = new Activite("Activité 2", Instant.ofEpochMilli(170000000), Sport.COURSE, 75, 20.5);
        Activite a3 = new Activite("Activité 3", Instant.ofEpochMilli(170000000), Sport.RANDONNEE, 75, 21.5);
        Activite a4 = new Activite("Activité 4", Instant.ofEpochMilli(170000000), Sport.RAQUETTE, 75, 90.5);
        Activite a5 = new Activite("Activité 5", Instant.ofEpochMilli(170000000), Sport.VELO, 75, 50.5);

        Fichier.enregistrer(this.getApplicationContext(), a1);
        Fichier.enregistrer(this.getApplicationContext(), a2);
        Fichier.enregistrer(this.getApplicationContext(), a3);
        Fichier.enregistrer(this.getApplicationContext(), a4);
        Fichier.enregistrer(this.getApplicationContext(), a5);
    }

    public void trierListeDate() {
        Fichier.getListeActivites().sort(Comparator.comparing(Activite::getDate));
        adapter.notifyDataSetChanged();
    }

    public void trierListeDuree() {
        Fichier.getListeActivites().sort(Comparator.comparing(Activite::getDuree));
        adapter.notifyDataSetChanged();
    }

    public void trierListeDistance() {
        Fichier.getListeActivites().sort(Comparator.comparingDouble(Activite::getDistanceMetrique).reversed());
        adapter.notifyDataSetChanged();
    }

    public void trierListeNom() {
        Fichier.getListeActivites().sort(Comparator.comparing(Activite::getNom));
        adapter.notifyDataSetChanged();
    }

    public void trierListeSport() {
        Fichier.getListeActivites().sort(Comparator.comparing(Activite::getSport));
        adapter.notifyDataSetChanged();
    }

    public void ouvrirAccueil(View view) {
        startActivity(new Intent(ControleurHistorique.this, Application.class));
    }

    public void ouvrirModifier(View view) {
        startActivity(new Intent(ControleurHistorique.this, ControleurHistoriqueModifier.class));
    }

    public void ouvrirParametre(View view) {
        startActivity(new Intent(ControleurHistorique.this, ControleurParametre.class));
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
        switch (item.getItemId()) {
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


