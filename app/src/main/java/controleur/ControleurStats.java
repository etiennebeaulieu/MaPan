package controleur;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mapan.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import service.ServiceStats;


public class ControleurStats extends AppCompatActivity
{
ServiceStats service = null;

    public static final double METRE_PIED = 3.28084;
    public static final double METRE_MILES = 0.000621371;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistiques_en_cours);


        //Toast.makeText(refresh.this,"in runnable",Toast.LENGTH_SHORT).show();
        ControleurEnCours.activiteEnCours.setDistanceMetrique(ControleurEnCours.activiteEnCours.calculerDistance(0,ControleurEnCours.activiteEnCours.getTabTemps().size()-1));
        ControleurEnCours.activiteEnCours.setVitesseMetrique( ControleurEnCours.activiteEnCours.calculerVitesseMoyenne());
        ControleurEnCours.activiteEnCours.setVitesseActuelleMetrique(  ControleurEnCours.activiteEnCours.calculerVitesse(ControleurEnCours.activiteEnCours.getTabTemps().size()-2,ControleurEnCours.activiteEnCours.getTabTemps().size()-1));
        ControleurEnCours.activiteEnCours.setDuree(Duration.between(ControleurEnCours.activiteEnCours.getTabTemps().get(ControleurEnCours.activiteEnCours.getTabTemps().size()-1), ControleurEnCours.activiteEnCours.getTabTemps().get(0)));
        ControleurEnCours.activiteEnCours.calculerDenivele();
        formatterDonnees();

    }



    public  void formatterDonnees()
    {

        TextView txtDuree = findViewById(R.id.duree_en_cours);
        TextView txtDistance = findViewById(R.id.distance_en_cours);
        TextView txtVitesse = findViewById(R.id.vitesse_en_cours);
        TextView txtVitesseMoyenne = findViewById(R.id.vitesse_moyenne);
        TextView txtAltitude = findViewById(R.id.altitude_en_cours);
        TextView txtDenivelePos = findViewById(R.id.denivele_positif);
        TextView txtDeniveleNeg = findViewById(R.id.denivele_negatif);
        TextView txtLatitude = findViewById(R.id.latitude);
        TextView txtLongitude = findViewById(R.id.longitude);

        NumberFormat formatterDistance = new DecimalFormat("#0.00");
        NumberFormat formatterHauteur = new DecimalFormat("#0");
        NumberFormat formatterCoord = new DecimalFormat("#0.0000'°'");

        txtDuree.setText(DateTimeFormatter.ofPattern("HH'h'mm'min'").withZone(ZoneId.of("UTC")).format(ControleurEnCours.activiteEnCours.getDuree().addTo(Instant.ofEpochSecond(0))));
        txtLatitude.setText(formatterCoord.format(ControleurEnCours.activiteEnCours.getTabLatitude().get(ControleurEnCours.activiteEnCours.getTabLatitude().size()-1)));
        txtLongitude.setText(formatterCoord.format(ControleurEnCours.activiteEnCours.getTabLongitude().get(ControleurEnCours.activiteEnCours.getTabLongitude().size()-1)));
        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour distance", false))
        {
            txtDistance.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getDistanceMetrique() / 1000*METRE_MILES) + "mi");
        } else
        {
            txtDistance.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getDistanceMetrique() / 1000) + "km");
        }

        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour vitesse", false))
        {
            txtVitesse.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getVitesseActuelleMetrique()*1000 * METRE_MILES) + "mi/h");
            txtVitesseMoyenne.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getVitesseMetrique()*1000 * METRE_MILES) + "mi/h");
        } else
        {
            txtVitesse.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getVitesseActuelleMetrique() *3.6) + "km/h");
            txtVitesseMoyenne.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getVitesseMetrique()) + "km/h");
        }

        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour altitude", false))
        {
            txtAltitude.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getAltitudeActuelleMetrique()*METRE_PIED) + "'");
        } else
        {
            txtAltitude.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getAltitudeActuelleMetrique()) + "m");
        }

        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour denivele", false))
        {
            txtDenivelePos.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getDenivelePositifMetrique()) + "'");
            txtDeniveleNeg.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getDeniveleNegatifMetrique()) + "'");

        } else
        {
            txtDenivelePos.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getDenivelePositifMetrique()*METRE_PIED) + "m");
            txtDeniveleNeg.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getDeniveleNegatifMetrique()*METRE_PIED) + "m");
        }
    }
}
