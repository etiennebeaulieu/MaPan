package controleur;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.mapan.R;

import modele.Sport;

public class ControleurParametre extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

    private String[] nom = {"Sport - Date", "Date"};
    private String[] type = {"Course à pied", "Randonnée pédestre", "Vélo", "Raquette", "Ski de randonnée", "Ski alpin", "Patin à glace", "Ski de fond"};
    private SharedPreferences.Editor editor;
    private Spinner spin_nom;
    private Spinner spin_type;
    private ArrayAdapter aa1;
    private ArrayAdapter aa2;
    private SwitchCompat imperial_pour_tout;
    private SwitchCompat imperial_pour_distance;
    private SwitchCompat imperial_pour_vitesse;
    private SwitchCompat imperial_pour_altitude;
    private SwitchCompat imperial_pour_denivele;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.parametre);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Preferences", 0);
        editor = pref.edit();

        spin_nom = (Spinner) findViewById(R.id.choix_nom_defaut);
        spin_nom.setOnItemSelectedListener(this);

        aa1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, nom);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_nom.setAdapter(aa1);
        spin_nom.setSelection(aa1.getPosition(pref.getString("nom_défaut", "Date")));


        spin_type = (Spinner) findViewById(R.id.choix_type_defaut);
        spin_type.setOnItemSelectedListener(this);

        aa2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, type);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_type.setAdapter(aa2);
        spin_type.setSelection(aa2.getPosition(pref.getString("type_défaut", "Randonnée pédestre")));

        imperial_pour_tout = (SwitchCompat) findViewById(R.id.imperial_pour_tout);
        imperial_pour_tout.setChecked(getSharedPreferences("Preferences", 0).getBoolean("impérial pour tout", false));
        imperial_pour_tout.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            //Coche tous les sliders si impérial pour tout est coché
            imperial_pour_distance.setChecked(imperial_pour_tout.isChecked());
            imperial_pour_vitesse.setChecked(imperial_pour_tout.isChecked());
            imperial_pour_altitude.setChecked(imperial_pour_tout.isChecked());
            imperial_pour_denivele.setChecked(imperial_pour_tout.isChecked());

            imperial_pour_distance.setEnabled(!imperial_pour_tout.isChecked());
            imperial_pour_vitesse.setEnabled(!imperial_pour_tout.isChecked());
            imperial_pour_altitude.setEnabled(!imperial_pour_tout.isChecked());
            imperial_pour_denivele.setEnabled(!imperial_pour_tout.isChecked());
            editor.putBoolean("impérial pour tout", imperial_pour_tout.isChecked()).apply();

        });
        //Change les SharedPreference selon l'état du bouton
        imperial_pour_distance = (SwitchCompat) findViewById(R.id.imperial_pour_distance);
        imperial_pour_distance.setChecked(getSharedPreferences("Preferences", 0).getBoolean("impérial pour distance", false));
        imperial_pour_distance.setOnCheckedChangeListener((buttonView, isChecked) -> editor.putBoolean("impérial pour distance", imperial_pour_distance.isChecked()).apply());

        imperial_pour_vitesse = (SwitchCompat) findViewById(R.id.imperial_pour_vitesse);
        imperial_pour_vitesse.setChecked(getSharedPreferences("Preferences", 0).getBoolean("impérial pour vitesse", false));
        imperial_pour_vitesse.setOnCheckedChangeListener((buttonView, isChecked) -> editor.putBoolean("impérial pour vitesse", imperial_pour_vitesse.isChecked()).apply());

        imperial_pour_altitude = (SwitchCompat) findViewById(R.id.imperial_pour_altitude);
        imperial_pour_altitude.setChecked(getSharedPreferences("Preferences", 0).getBoolean("impérial pour altitude", false));
        imperial_pour_altitude.setOnCheckedChangeListener((buttonView, isChecked) -> editor.putBoolean("impérial pour altitude", imperial_pour_altitude.isChecked()).apply());

        imperial_pour_denivele = (SwitchCompat) findViewById(R.id.imperial_pour_denivele);
        imperial_pour_denivele.setChecked(getSharedPreferences("Preferences", 0).getBoolean("impérial pour denivele", false));
        imperial_pour_denivele.setOnCheckedChangeListener((buttonView, isChecked) -> editor.putBoolean("impérial pour denivele", imperial_pour_denivele.isChecked()).apply());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId())
        {
            case R.id.choix_nom_defaut:
                editor.putString("nom_défaut", parent.getItemAtPosition(position).toString()).apply();
                break;

            case R.id.choix_type_defaut:
                editor.putString("type_défaut", parent.getItemAtPosition(position).toString()).apply();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    public void ouvrirAccueil(View view)
    {
        startActivity(new Intent(ControleurParametre.this, ControleurAccueil.class));
    }

    public void ouvrirHistorique(View view)
    {
        startActivity(new Intent(ControleurParametre.this, ControleurHistorique.class));
    }

    public void ouvrirStatsGlobale(View view)
    {
        startActivity(new Intent(ControleurParametre.this,ControleurStatsGlobale.class));
    }

    public void ouvrirCredit(View view)
    {
        Dialog dialog = new Dialog(this, R.style.DialogTransparent);
        dialog.setContentView(R.layout.credit);
        dialog.show();
    }
}

