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


public class ControleurStats extends AppCompatActivity
{
    private Handler mHandler;
    Runnable refresh;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistiques_en_cours);
        this.mHandler = new Handler();
        m_Runnable.run();

        this.mHandler.postDelayed(m_Runnable,1000);

//        TextView txtDuree = (TextView)findViewById(R.id.duree_en_cours);
//        TextView txtDistance = (TextView)findViewById(R.id.distance_en_cours);
//        TextView txtVitesse = (TextView)findViewById(R.id.vitesse_en_cours);
//        TextView txtVitesseMoyenne = (TextView)findViewById(R.id.vitesse_moyenne);
//        TextView txtAltitude = (TextView)findViewById(R.id.altitude_en_cours);
//        TextView txtDenivelePos = (TextView)findViewById(R.id.denivele_positif);
//        TextView txtDeniveleNeg = (TextView)findViewById(R.id.denivele_negatif);
//        TextView txtLatitude = (TextView)findViewById(R.id.latitude);
//        TextView txtLongitude = (TextView)findViewById(R.id.longitude);
//
//        NumberFormat formatterDistance = new DecimalFormat("#0.00");
//        NumberFormat formatterHauteur = new DecimalFormat("#0");
//        NumberFormat formatterCoord = new DecimalFormat("#0.0000'°'");
//
//
//        txtDuree.setText(DateTimeFormatter.ofPattern("HH'h'mm'min'").withZone(ZoneId.of("UTC")).format(ControleurEnCours.activiteEnCours.getDuree().addTo(Instant.ofEpochSecond(0))));
//        txtLatitude.setText(formatterCoord.format(ControleurEnCours.activiteEnCours.getTabLatitude().get(ControleurEnCours.activiteEnCours.getTabLatitude().size()-1)));
//        txtLongitude.setText(formatterCoord.format(ControleurEnCours.activiteEnCours.getTabLongitude().get(ControleurEnCours.activiteEnCours.getTabLongitude().size()-1)));
//        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour distance", false))
//        {
//            txtDistance.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getDistanceImperiale()) + "mi");
//        } else
//        {
//            txtDistance.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getDistanceMetrique() / 1000) + "km");
//        }
//
//        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour vitesse", false))
//        {
//            txtVitesse.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getVitesseActuelleImperiale()) + "mi/h");
//            txtVitesseMoyenne.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getVitesseImperiale()) + "mi/h");
//        } else
//        {
//            txtVitesse.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getVitesseActuelleMetrique() *3.6) + "km/h");
//            txtVitesseMoyenne.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getVitesseMetrique()) + "km/h");
//        }
//
//        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour altitude", false))
//        {
//            txtAltitude.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getAltitudeActuelleImperiale()) + "'");
//        } else
//        {
//            txtAltitude.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getAltitudeActuelleMetrique() / 1000) + "m");
//        }
//
//        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour denivele", false))
//        {
//            txtDenivelePos.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getDenivelePositifImperiale()) + "'");
//            txtDeniveleNeg.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getDeniveleNegatifImperiale()) + "'");
//
//        } else
//        {
//            txtDenivelePos.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getDenivelePositifMetrique()) + "m");
//            txtDeniveleNeg.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getDeniveleNegatifMetrique()) + "m");
//        }
    }

    private final Runnable m_Runnable = new Runnable()

    {
        public void run()

        {
            //Toast.makeText(refresh.this,"in runnable",Toast.LENGTH_SHORT).show();
            ControleurEnCours.activiteEnCours.setDistanceMetrique(ControleurEnCours.activiteEnCours.calculerDistance(0,ControleurEnCours.activiteEnCours.getTabTemps().size()-1));
            ControleurEnCours.activiteEnCours.setVitesseMetrique( ControleurEnCours.activiteEnCours.calculerVitesseMoyenne());
            ControleurEnCours.activiteEnCours.setVitesseActuelleMetrique(  ControleurEnCours.activiteEnCours.calculerVitesse(ControleurEnCours.activiteEnCours.getTabTemps().size()-2,ControleurEnCours.activiteEnCours.getTabTemps().size()-1));
            ControleurEnCours.activiteEnCours.setDuree(Duration.between(ControleurEnCours.activiteEnCours.getTabTemps().get(ControleurEnCours.activiteEnCours.getTabTemps().size()-1),
                    ControleurEnCours.activiteEnCours.getTabTemps().get(0)));
            ControleurEnCours.activiteEnCours.calculerDenivele();
            formaterDonnees();
            ControleurStats.this.mHandler.postDelayed(m_Runnable,1000);
        }

    };//runnable


    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(m_Runnable);
        finish();

    }

    private void formaterDonnees()
    {
        TextView txtDuree = (TextView)findViewById(R.id.duree_en_cours);
        TextView txtDistance = (TextView)findViewById(R.id.distance_en_cours);
        TextView txtVitesse = (TextView)findViewById(R.id.vitesse_en_cours);
        TextView txtVitesseMoyenne = (TextView)findViewById(R.id.vitesse_moyenne);
        TextView txtAltitude = (TextView)findViewById(R.id.altitude_en_cours);
        TextView txtDenivelePos = (TextView)findViewById(R.id.denivele_positif);
        TextView txtDeniveleNeg = (TextView)findViewById(R.id.denivele_negatif);
        TextView txtLatitude = (TextView)findViewById(R.id.latitude);
        TextView txtLongitude = (TextView)findViewById(R.id.longitude);

        NumberFormat formatterDistance = new DecimalFormat("#0.00");
        NumberFormat formatterHauteur = new DecimalFormat("#0");
        NumberFormat formatterCoord = new DecimalFormat("#0.0000'°'");

        txtDuree.setText(DateTimeFormatter.ofPattern("HH'h'mm'min'").withZone(ZoneId.of("UTC")).format(ControleurEnCours.activiteEnCours.getDuree().addTo(Instant.ofEpochSecond(0))));
        txtLatitude.setText(formatterCoord.format(ControleurEnCours.activiteEnCours.getTabLatitude().get(ControleurEnCours.activiteEnCours.getTabLatitude().size()-1)));
        txtLongitude.setText(formatterCoord.format(ControleurEnCours.activiteEnCours.getTabLongitude().get(ControleurEnCours.activiteEnCours.getTabLongitude().size()-1)));
        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour distance", false))
        {
            txtDistance.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getDistanceImperiale()) + "mi");
        } else
        {
            txtDistance.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getDistanceMetrique() / 1000) + "km");
        }

        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour vitesse", false))
        {
            txtVitesse.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getVitesseActuelleImperiale()) + "mi/h");
            txtVitesseMoyenne.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getVitesseImperiale()) + "mi/h");
        } else
        {
            txtVitesse.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getVitesseActuelleMetrique() *3.6) + "km/h");
            txtVitesseMoyenne.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getVitesseMetrique()) + "km/h");
        }

        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour altitude", false))
        {
            txtAltitude.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getAltitudeActuelleImperiale()) + "'");
        } else
        {
            txtAltitude.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getAltitudeActuelleMetrique() / 1000) + "m");
        }

        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour denivele", false))
        {
            txtDenivelePos.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getDenivelePositifImperiale()) + "'");
            txtDeniveleNeg.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getDeniveleNegatifImperiale()) + "'");

        } else
        {
            txtDenivelePos.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getDenivelePositifMetrique()) + "m");
            txtDeniveleNeg.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getDeniveleNegatifMetrique()) + "m");
        }
    }
}
