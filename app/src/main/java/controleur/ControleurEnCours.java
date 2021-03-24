package controleur;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mapan.R;

import modele.Activite;

public class ControleurEnCours extends AppCompatActivity {

    Activite activiteEnCours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activite_en_cours);

        /*activiteEnCours = (Activite) getIntent().getSerializableExtra("Activité");

        TextView info = findViewById(R.id.infoActivite);

        info.setText(activiteEnCours.getNom() + "\n" + activiteEnCours.getSport().getNom());*/



    }
}
