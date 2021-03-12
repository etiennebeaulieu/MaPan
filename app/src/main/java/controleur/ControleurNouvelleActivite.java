package controleur;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.mapan.R;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import modele.Activite;
import modele.ActiviteAdapter;
import modele.Fichier;
import modele.NouvelleActiviteAdapter;
import modele.Sport;

public class ControleurNouvelleActivite extends AppCompatActivity
{
    private EditText nouveau_nom;
    private ListView nouvelle_activitesList;
    private NouvelleActiviteAdapter adapter;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.nouvelle_activite);

        nouveau_nom = (EditText)findViewById(R.id.nouveau_nom);
        nouvelle_activitesList = (ListView)findViewById(R.id.nouvelle_activitesList);


        ArrayList<Sport> listSport = new ArrayList<>();

        listSport.add(Sport.COURSE);
        listSport.add(Sport.RANDONNEE);
        listSport.add(Sport.RAQUETTE);
        listSport.add(Sport.PATIN);
        listSport.add(Sport.SKI_RANDONNEE);
        listSport.add(Sport.SKI);
        listSport.add(Sport.SKI_FOND);
        listSport.add(Sport.VELO);
        listSport.add(Sport.AUTRE);

        adapter = new NouvelleActiviteAdapter(this, R.layout.list_activite_row, listSport);
        nouvelle_activitesList.setAdapter(adapter);


    }

    public void creerNouvelleActivite()
        {
            String nom = nouveau_nom.getText().toString();
            Sport sp = (Sport) nouvelle_activitesList.getSelectedItem();

            if(nom != null && sp!= null)
            {
             Activite act = new Activite(nom, sp);
            }
        }

    public void ouvrirParametre(View view)
    {
        startActivity(new Intent(ControleurNouvelleActivite.this, ControleurParametre.class));
    }

    public void ouvrirAccueil(View view) {

        startActivity(new Intent(ControleurNouvelleActivite.this, ControleurAccueil.class));
    }
}
