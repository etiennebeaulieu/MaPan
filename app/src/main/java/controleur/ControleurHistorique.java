package controleur;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mapan.R;

import java.util.Comparator;

import modele.Activite;
import modele.ActiviteAdapter;
import modele.Fichier;

public class ControleurHistorique extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    //Affichage liste des activités
    private ListView historique_list;

    //Affichage personalisé
    private ActiviteAdapter adapter;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.historique);
        historique_list = findViewById(R.id.modifier_list);
        adapter = new ActiviteAdapter(this, R.layout.list_row, Fichier.getListeActivites());
        historique_list.setAdapter(adapter);

        historique_list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(ControleurHistorique.this, ControleurPostActivite.class);
            intent.putExtra("activité", (Activite) historique_list.getItemAtPosition(position));
            startActivity(intent);
        });


        Fichier.rafraichir(this.getApplicationContext());
        adapter.notifyDataSetChanged();
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
        startActivity(new Intent(ControleurHistorique.this, ControleurAccueil.class));
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

    public void ajouterActivite(View view) {
        startActivity(new Intent(ControleurHistorique.this, ControleurAjouterActivite.class));
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

            case R.id.triDate:
                trierListeDate();
                retour = true;
                break;

            default:
                retour = false;
        }
        return retour;
    }
}


