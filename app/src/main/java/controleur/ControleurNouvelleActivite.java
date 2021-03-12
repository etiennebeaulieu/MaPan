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
    private ImageButton nouvelle_annuler;
    private ImageButton commencer;
    private ImageButton parametre;
    private NouvelleActiviteAdapter adapter;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.nouvelle_activite);

        nouveau_nom = (EditText)findViewById(R.id.nouveau_nom);
        nouvelle_activitesList = (ListView)findViewById(R.id.nouvelle_activitesList);
        nouvelle_annuler = (ImageButton)findViewById(R.id.nouvelle_annuler);
        commencer = (ImageButton)findViewById(R.id.commencer);
        parametre = (ImageButton)findViewById(R.id.parametre);


        adapter = new NouvelleActiviteAdapter(this, R.layout.list_activite_row, (ArrayList<Sport>) Arrays.asList(Sport.values()));
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

    public void ouvrirParametre(View view) {
        startActivity(new Intent(ControleurNouvelleActivite.this, ControleurParametre.class));
    }

    public void retourAcceuil(View view) {
        startActivity(new Intent(ControleurNouvelleActivite.this, ControleurAcceuil.class));
    }
}
