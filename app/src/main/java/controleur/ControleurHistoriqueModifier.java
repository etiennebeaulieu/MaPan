package controleur;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import controleur.ControleurHistorique;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.Application;
import com.example.mapan.R;

import modele.Activite;

public class ControleurHistoriqueModifier extends AppCompatActivity{

    private ListView modifier_list;
    private ArrayList<Activite> listeActivites;
    private ControleurHistorique historique;
    private boolean isDistanceMetrique = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historique_modifier);
    }



    public void ouvrirAccueil(View view){
        startActivity(new Intent(ControleurHistoriqueModifier.this, Application.class));
    }

    public void ouvrirHistorique(View view){
        startActivity(new Intent(ControleurHistoriqueModifier.this, ControleurHistorique.class));
    }
}
