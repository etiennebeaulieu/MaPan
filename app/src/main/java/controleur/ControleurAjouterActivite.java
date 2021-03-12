package controleur;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.mapan.R;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Calendar;

import modele.Activite;
import modele.ActiviteAdapter;
import modele.Fichier;
import modele.Sport;

public class ControleurAjouterActivite extends AppCompatActivity
{

    private EditText editTextNom;
    private Spinner spinnerSport;
    private EditText editTextDate;
    private Instant date;
    private EditText editTextDuree;
    private EditText editTextDistance;
    private Button boutonDate;
    private Activite activiteAjoutee;
    //private ListView listeHistorique;
    //private ActiviteAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ajouter_activite);
        editTextNom = findViewById(R.id.editTextNom);
        editTextDate = findViewById(R.id.editTextDate);
        spinnerSport = (Spinner) findViewById(R.id.spinnerSport);
        ArrayAdapter aa1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Sport.values());
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSport.setAdapter(aa1);
        editTextDuree = findViewById(R.id.editTextDuree);
        editTextDistance = findViewById(R.id.editTextDistance);
        boutonDate = findViewById(R.id.boutonDate);

        /*adapter = new ActiviteAdapter(this, R.layout.list_row, Fichier.getListeActivites());
        listeHistorique = findViewById(R.id.modifier_list);
        listeHistorique.setAdapter(adapter);*/
    }

    public Activite getActiviteAjoutee(){
        return activiteAjoutee;
    }

    public void choisirDate(View view){
        int selectedYear = Calendar.YEAR;
        int selectedMonth = Calendar.MONTH;
        int selectedDayOfMonth = Calendar.DAY_OF_MONTH;

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                 editTextDate.setText(year + "-" + month + "-" + dayOfMonth);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,dateSetListener, selectedYear,selectedMonth,selectedDayOfMonth);
        datePickerDialog.show();
    }

    public void confirmer(View view){
        activiteAjoutee = new Activite(editTextNom.getText().toString(), Date.valueOf(editTextDate.getText().toString()).toInstant(),
                Sport.valueOf(spinnerSport.getSelectedItem().toString()), Integer.valueOf(editTextDuree.getText().toString()),
                Double.valueOf(editTextDistance.getText().toString()));

        Fichier.enregistrer(ControleurAjouterActivite.this, activiteAjoutee);

        startActivity(new Intent(ControleurAjouterActivite.this, ControleurHistorique.class));
    }

    public void annuler(View view){

    }
}
