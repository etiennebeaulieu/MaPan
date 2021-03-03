package controleur;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.Application;
import com.example.mapan.R;

import modele.Sport;

public class ControleurParametre extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] nom = { "Sport - Date", "Date", "Date - Durée", "Date - Distance"};
    String[] type = { "Course à pied", "Randonnée pédestre", "Vélo", "Raquette", "Ski de randonnée", "Ski alpin", "Patin à glace", "Ski de fond"};
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.parametre);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Preferences", 0);
        editor = pref.edit();

        Spinner spin_nom = (Spinner) findViewById(R.id.choix_nom_defaut);
        spin_nom.setOnItemSelectedListener(this);

        ArrayAdapter aa1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,nom);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_nom.setAdapter(aa1);


        Spinner spin_type = (Spinner) findViewById(R.id.choix_type_defaut);
        spin_type.setOnItemSelectedListener(this);

        ArrayAdapter aa2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,type);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_type.setAdapter(aa2);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    switch(parent.getId()){
        case R.id.choix_nom_defaut:{
            editor.putString("nom_défaut", parent.getItemAtPosition(position).toString());
        }
        case R.id.choix_type_defaut:{
            editor.putString("type_défaut", parent.getItemAtPosition(position).toString());
        }
    }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void ouvrirAccueil(View view){
        startActivity(new Intent(ControleurParametre.this, Application.class));
    }

    /*public Sport choisirTypeDéfaut(String type){

    }*/
}
