package controleur;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mapan.R;

import java.sql.Date;
import java.time.Instant;
import java.util.Calendar;

import modele.Activite;
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
        Calendar c = Calendar.getInstance();
        int selectedYear = c.get(Calendar.YEAR);
        int selectedMonth = c.get(Calendar.MONTH);
        int selectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = (view1, year, month, dayOfMonth) -> editTextDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,dateSetListener, selectedYear,selectedMonth,selectedDayOfMonth);
        Calendar minDate = (Calendar) c.clone();
        minDate.add(Calendar.YEAR, -1);
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        Calendar maxDate = (Calendar) c.clone();
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        datePickerDialog.show();
    }

    public void confirmer(View view){
        if(((!editTextNom.getText().toString().isEmpty())&&(!editTextNom.getText().toString().equals(null)))
                &&((!editTextDate.getText().toString().isEmpty()) &&(!editTextDate.getText().toString().equals(null)))
                &&((!editTextDuree.getText().toString().isEmpty()) &&(!editTextDuree.getText().toString().equals(null))&&(!editTextDuree.getText().toString().contains(":"))&&(!editTextDuree.getText().toString().equals("0")))
                &&((!editTextDistance.getText().toString().isEmpty())&&(!editTextDistance.getText().toString().equals(null))&&(!editTextDistance.getText().toString().equals(".")))){
        activiteAjoutee = new Activite(editTextNom.getText().toString().trim(), Date.valueOf(editTextDate.getText().toString()).toInstant().plusSeconds(43200),
                Sport.valueOf(spinnerSport.getSelectedItem().toString()), Integer.valueOf(editTextDuree.getText().toString()),
                Double.valueOf(editTextDistance.getText().toString()));

        Fichier.enregistrer(ControleurAjouterActivite.this, activiteAjoutee);

        startActivity(new Intent(ControleurAjouterActivite.this, ControleurHistorique.class));
        }
        else{
            String message = "";

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            TextView input = new TextView(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            if((editTextNom.getText().toString().isEmpty()||editTextNom.getText().toString().equals(null))){
                message = "L'activité que vous tentez de créer ne possède pas de nom. Veuillez insérer un nom.";
                builder.setTitle("Attention!").setMessage(message).setPositiveButton("Confirmer", (dialog, which) ->{
                    dialog.dismiss();
                }).show();
            }
            else if((editTextDate.getText().toString().isEmpty())||((editTextDate.getText().toString().equals(null)))){
                message = "L'activité que vous tentez de créer ne possède pas de date ou sa date est inscrite incorrectement. Veuillez inscrire une date.";
                builder.setTitle("Attention!").setMessage(message).setPositiveButton("Confirmer", (dialog, which) ->{
                    dialog.dismiss();
                }).show();
            }
            else if(((editTextDuree.getText().toString().contains(":"))||(editTextDuree.getText().toString().equals("0"))||(editTextDuree.getText().toString().isEmpty())||(editTextDuree.getText().toString().equals(null)))){
                message = "L'activité que vous tentez de créer ne possède pas de durée ou la durée est inscrite incorrectement. Veuillez inscrire une durée en minutes.(ex: 90)";
                builder.setTitle("Attention!").setMessage(message).setPositiveButton("Confirmer", (dialog, which) ->{
                    dialog.dismiss();
                }).show();
            }
            else if(((editTextDistance.getText().toString().equals("."))||(editTextDistance.getText().toString().isEmpty())||(editTextDistance.getText().toString().equals(null)))){
                message = "L'activité que vous tentez de créer ne possède pas de distance parcourue ou la distance est inscrite incorrectement. Veuillez inscrire une distance en kilomètres(ex: 4.55)";
                builder.setTitle("Attention!").setMessage(message).setPositiveButton("Confirmer", (dialog, which) ->{
                    dialog.dismiss();
                }).show();
            }
        }
    }

    public void annuler(View view){
        startActivity(new Intent(ControleurAjouterActivite.this, ControleurHistorique.class));
    }
}
