package controleur;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mapan.R;

public class ControleurParametre extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] noms = { "Sport - Date", "Date", "Date - Durée", "Date - Distance"};
    String[] sport = { "Course à pied", "Randonnée pédestre", "Vélo", "Raquette", "Ski de randonnée", "Ski alpin", "Patin à glace", "Ski de fond"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.parametre);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Preferences", 0);
        SharedPreferences.Editor editor = pref.edit();

        Spinner spin_nom = (Spinner) findViewById(R.id.choix_nom_defaut);
        spin_nom.setOnItemSelectedListener(this);

        Spinner spin_sport = (Spinner) findViewById(R.id.choix_type_defaut);
        spin_sport.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
