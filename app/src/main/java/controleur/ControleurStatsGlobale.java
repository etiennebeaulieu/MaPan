package controleur;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mapan.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import modele.Activite;
import modele.Fichier;


public class ControleurStatsGlobale extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    public static final double METRE_PIED = 3.28084;
    public static final double METRE_MILES = 0.000621371;

    private ArrayAdapter aa;
    private Spinner spin_type;
    private String[] type = {"Tout", "Course à pied", "Randonnée pédestre", "Vélo", "Raquette", "Ski de randonnée", "Ski alpin", "Patin à glace", "Ski de fond", "Autre"};

    int nb = 0;
    long duree = 0;
    double distance = 0;
    int denivelePositif = 0;
    double distanceMax = 0;
    int vitesseMoy = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistiques_globale);

        spin_type = (Spinner) findViewById(R.id.choix_sport);
        spin_type.setOnItemSelectedListener(this);

        aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_type.setAdapter(aa);
        spin_type.setSelection(aa.getPosition("Tout"));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        String s = type[position];
        Fichier.loadActivites(this);

        nb = 0;
        duree = 0;
        distance = 0;
        denivelePositif = 0;
        distanceMax = 0;
        vitesseMoy = 0;

        for (Activite a : Fichier.getListeActivites())
        {


            if (s.equals("Tout"))
            {
                nb = Fichier.getListeActivites().size();
                duree += a.getDuree().getSeconds();
                distance += a.getDistanceMetrique();
                if (a.getDistanceMetrique() > distanceMax)
                {
                    distanceMax = a.getDistanceMetrique();
                }
                denivelePositif += a.getDenivelePositif();
            } else if (a.getSport().getNom().equals(s))
            {
                nb++;
                duree += a.getDuree().getSeconds();
                distance += a.getDistanceMetrique();
                if (a.getDistanceMetrique() > distanceMax)
                {
                    distanceMax = a.getDistanceMetrique();
                }
                denivelePositif += a.getDenivelePositif();
            }
            vitesseMoy = (int) (distance / duree);
        }
        afficher();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    private void afficher()
    {
        TextView txtNbr = findViewById(R.id.nombreActivite);
        TextView txtDuree = findViewById(R.id.dureeTotale);
        TextView txtDistance = findViewById(R.id.distanceTotale);
        TextView txtDistanceMax = findViewById(R.id.plusLongueActivite);
        TextView txtVitesseMoyenne = findViewById(R.id.vitesseMoyenne);
        TextView txtDenivele = findViewById(R.id.denivelePositifTotal);
        TextView labelVitesseMoyenne = findViewById(R.id.vitMoyenneLabel);


        NumberFormat formatterDistance = new DecimalFormat("#0.00");
        NumberFormat formatterHauteur = new DecimalFormat("#0");

        txtNbr.setText(nb + "");
        txtDuree.setText(DateTimeFormatter.ofPattern("HH'h'mm'min'").withZone(ZoneId.of("UTC")).format(Duration.ofSeconds(duree).addTo(Instant.ofEpochSecond(0))));

        //Affiche la distance selon les préférences
        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour distance", false))
        {
            txtDistance.setText(formatterDistance.format(distance * METRE_MILES) + "mi");
            txtDistanceMax.setText(formatterDistance.format(distanceMax * METRE_MILES) + "mi");
        } else
        {
            txtDistance.setText(formatterDistance.format(distance / 1000) + "km");
            txtDistanceMax.setText(formatterDistance.format(distanceMax / 1000) + "km");
        }

        //Affiche les vitesse selon les préférences
        if(Fichier.getListeActivites().size() > nb) {
            labelVitesseMoyenne.setText("Vitesse moyenne");
            if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour vitesse", false)) {
                txtVitesseMoyenne.setText(formatterDistance.format(vitesseMoy * METRE_MILES * 3600) + "mi/h");
            } else {
                txtVitesseMoyenne.setText(formatterDistance.format(vitesseMoy * 3.6) + "km/h");
            }
        }else
        {
            txtVitesseMoyenne.setText("");
            labelVitesseMoyenne.setText("");
        }

        //Affiche les dénivelé selon les préférences
        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour denivele", false))
        {
            txtDenivele.setText(formatterHauteur.format(denivelePositif * METRE_PIED) + "'");

        } else
        {
            txtDenivele.setText(formatterHauteur.format(denivelePositif) + "m");
        }
    }

    public void ouvrirAccueil(View view)
    {
        startActivity(new Intent(ControleurStatsGlobale.this, ControleurAccueil.class));
    }

    public void ouvrirHistorique(View view)
    {
        startActivity(new Intent(ControleurStatsGlobale.this, ControleurHistorique.class));
    }

    public void ouvrirParametre(View view)
    {
        startActivity(new Intent(ControleurStatsGlobale.this, ControleurParametre.class));
    }
}
