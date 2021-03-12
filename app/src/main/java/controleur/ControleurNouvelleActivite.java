package controleur;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ControleurNouvelleActivite extends AppCompatActivity
{

    public void ouvrirParametre(View view) {
        startActivity(new Intent(ControleurNouvelleActivite.this, ControleurParametre.class));
    }
}
