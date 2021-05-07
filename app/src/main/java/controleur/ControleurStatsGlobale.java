package controleur;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mapan.R;

import modele.Activite;
import modele.Fichier;


public class ControleurStatsGlobale extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    private ArrayAdapter aa;
    private Spinner spin_type;
    private String[] type = {"Tout", "Course à pied", "Randonnée pédestre", "Vélo", "Raquette", "Ski de randonnée", "Ski alpin", "Patin à glace", "Ski de fond"};

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
        int indice = spin_type.getSelectedItemPosition();
        String s = type[indice];
        Fichier.loadActivites(this);


        for (Activite a : Fichier.getListeActivites())
        {
            nb = 0;
            duree = 0;
            distance = 0;
            denivelePositif = 0;
            distanceMax = 0;
            vitesseMoy = 0;

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
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

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
        startActivity(new Intent(ControleurStatsGlobale.this, ControleurAccueil.class));
    }
}
