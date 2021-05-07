package controleur;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mapan.R;



public class ControleurStatsGlobale extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistiques_globale);


    }

    public void ouvrirAccueil(View view)
    {
        startActivity(new Intent(ControleurStatsGlobale.this, ControleurAccueil.class));
    }

    public void ouvrirHistorique(View view)
    {
        startActivity(new Intent(ControleurStatsGlobale.this, ControleurHistorique.class));
    }

    public void ouvrirParametre(View view)
    {
        startActivity(new Intent(ControleurStatsGlobale.this, ControleurAccueil.class));
    }
}
