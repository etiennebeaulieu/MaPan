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

    private String[] nom = { "Sport - Date", "Date", "Date - Durée", "Date - Distance"};
    private String[] type = { "Course à pied", "Randonnée pédestre", "Vélo", "Raquette", "Ski de randonnée", "Ski alpin", "Patin à glace", "Ski de fond"};
    private SharedPreferences.Editor editor;
    private Spinner spin_nom;
    private Spinner spin_type;
    private ArrayAdapter aa1;
    private ArrayAdapter aa2;
    private Sport sportDefaut;
    private String nomDefaut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.parametre);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Preferences", 0);
        editor = pref.edit();

        spin_nom = (Spinner) findViewById(R.id.choix_nom_defaut);
        spin_nom.setOnItemSelectedListener(this);

        aa1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,nom);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_nom.setAdapter(aa1);


        spin_type = (Spinner) findViewById(R.id.choix_type_defaut);
        spin_type.setOnItemSelectedListener(this);

        aa2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,type);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_type.setAdapter(aa2);

        /*if(spin_type.isSelected()){
            choisirTypeDefaut();
            aa2.notifyDataSetChanged();
        }

        if(spin_nom.isSelected()){
            choisirNomDefaut();
            aa1.notifyDataSetChanged();
        }*/
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

    public void ouvrirHistorique(View view){
        startActivity(new Intent(ControleurParametre.this, ControleurHistorique.class));
    }

    public void choisirNomDefaut(){
        String nom = spin_nom.getSelectedItem().toString();

        if(!nom.isEmpty() && !nom.equals(null)){
            nomDefaut = nom;
            aa1.notifyDataSetChanged();
        }
    }

    public String getNomDefaut(){
        return nomDefaut;
    }

    public void choisirTypeDefaut(){
        String sport = spin_type.getSelectedItem().toString();

        if(!sport.equals(null) && !sport.isEmpty()) {
            sportDefaut = Sport.valueOf(sport);
            aa2.notifyDataSetChanged();
        }
    }

    public Sport getTypeDefaut(){
        return sportDefaut;
    }
}
