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

//Classe contenant les méthodes qui controlent le fichier .xml de la fenêtre servant à ajouter une activité.
public class ControleurAjouterActivite extends AppCompatActivity
{

    //Champ de texte pour le nom de l'activité à ajouter
    private EditText editTextNom;
    //Menu déroulant pour choisir le sport de l'activité à ajouter
    private Spinner spinnerSport;
    //Champ de texte pour la date de l'activité à ajouter
    private EditText editTextDate;
    private Instant date;
    //Champ de texte pour la durée de l'activité à ajouter
    private EditText editTextDuree;
    //Champ de texte pour la distance de l'activité à ajouter
    private EditText editTextDistance;
    //Variable servant à contenir la nouvelle activité
    private Activite activiteAjoutee;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //On associe chaque élément de code à leur équivalent du fichier .xml correspondant avec le controleur.
        setContentView(R.layout.ajouter_activite);
        editTextNom = findViewById(R.id.editTextNom);
        editTextDate = findViewById(R.id.editTextDate);
        spinnerSport = (Spinner) findViewById(R.id.spinnerSport);
        ArrayAdapter aa1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Sport.values());
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSport.setAdapter(aa1);
        editTextDuree = findViewById(R.id.editTextDuree);
        editTextDistance = findViewById(R.id.editTextDistance);
    }

    public void choisirDate(View view){
        //On instancie un calendrier. Puis, on instancie la date courante(année/mois/jour)
        Calendar c = Calendar.getInstance();
        int selectedYear = c.get(Calendar.YEAR);
        int selectedMonth = c.get(Calendar.MONTH);
        int selectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        //On définit le format de date pour le TextView et on fait (mois + 1) puisque le premier mois du calendrier est 0 par défaut.
        DatePickerDialog.OnDateSetListener dateSetListener = (view1, year, month, dayOfMonth) -> editTextDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);

        //Créer le DatePickerDialog et ajouter une date minimale et maximale. Puis, on affiche le DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,dateSetListener, selectedYear,selectedMonth,selectedDayOfMonth);
        Calendar minDate = (Calendar) c.clone();
        minDate.add(Calendar.YEAR, -1);
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        Calendar maxDate = (Calendar) c.clone();
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        datePickerDialog.show();
    }

    //Cette méthode s'occupe de créer l'activité à ajouter après avoir vérifié que ses composantes soient conformes.
    //Par contre, si une ou plusieurs des composantes ne sont pas conformes, elle affiche un message d'erreur à l'utilisateur afin qu'il corrige le problème.
    public void confirmer(View view){
        //Vérifier si tout est conforme avant de créer l'activité à ajouter.
        if(((!editTextNom.getText().toString().isEmpty())&&(!editTextNom.getText().toString().equals(null)))
                &&((!editTextDate.getText().toString().isEmpty()) &&(!editTextDate.getText().toString().equals(null)))
                &&((!editTextDuree.getText().toString().isEmpty()) &&(!editTextDuree.getText().toString().equals(null))&&(!editTextDuree.getText().toString().contains(":"))&&(!editTextDuree.getText().toString().equals("0")))
                &&((!editTextDistance.getText().toString().isEmpty())&&(!editTextDistance.getText().toString().equals(null))&&(!editTextDistance.getText().toString().equals(".")))){
        activiteAjoutee = new Activite(editTextNom.getText().toString().trim(), Date.valueOf(editTextDate.getText().toString()).toInstant().plusSeconds(43200),
                Sport.valueOf(spinnerSport.getSelectedItem().toString()), Integer.valueOf(editTextDuree.getText().toString()),
                Double.valueOf(editTextDistance.getText().toString()));

        //On enregistre l'activité
        Fichier.enregistrer(ControleurAjouterActivite.this, activiteAjoutee);

        //On revient à la page d'historique
        startActivity(new Intent(ControleurAjouterActivite.this, ControleurHistorique.class));
        }
        //Si un des éléments vérifiés plus haut est incorforme, on affiche un message d'erreur en indiquant à l'utilisateur quel élément était en faute et ce qu'il doit faire afin de remédier au problème.
        else{
            //Ici on crée la variable String nécessaire pour chacun message en la laissant vide
            String message = "";

            //Puis, on crée la base de la fenêtre d'information qui sera affichée pour chaque erreur.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            TextView input = new TextView(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            //On vérifie le type d'erreur individuellement et on crée un message personnalisé pour chaque type d'erreur.
            if((editTextNom.getText().toString().isEmpty()||editTextNom.getText().toString().equals(null))){
                message = "L'activité que vous tentez de créer ne possède pas de nom. Veuillez insérer un nom.";
                builder.setTitle("Attention!").setMessage(message).setPositiveButton("Confirmer", (dialog, which) ->{
                    dialog.dismiss();
                }).show();
            }
            else if((editTextDate.getText().toString().isEmpty())||((editTextDate.getText().toString().equals(null)))){
                message = "L'activité que vous tentez de créer ne possède pas de date ou sa date est inscrite incorrectement. Veuillez inscrire une date. (ex: 2020-9-6)";
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

    //Permet de fermer la fenêtre de création d'une activité à ajouter si on ne veut pas sauvegarder les changements.
    public void annuler(View view){
        startActivity(new Intent(ControleurAjouterActivite.this, ControleurHistorique.class));
    }
}
