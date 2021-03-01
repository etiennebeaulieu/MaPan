package controleur;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import controleur.ControleurHistorique;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.Application;
import com.example.mapan.R;

import modele.Activite;
import modele.ActiviteAdapter;
import modele.Sport;

import static com.example.mapan.R.id.modifier_exporter;

public class ControleurHistoriqueModifier extends AppCompatActivity{

    private ListView modifier_list;
    private ImageButton modifier_exporter;
    private ArrayList<Activite> listeActivites;
    private ControleurHistorique historique;
    private boolean isDistanceMetrique = true;
    private ActiviteAdapter adapter;
    private Activite activiteSelect;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historique_modifier);

        listeActivites = new ArrayList<Activite>();
        modifier_list = findViewById(R.id.modifier_list);
        modifier_exporter = findViewById(R.id.modifier_exporter);
        activiteSelect = null;

        adapter = new ActiviteAdapter(this, R.layout.list_row, listeActivites);
        modifier_list.setAdapter(adapter);

        modifier_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activiteSelect = (Activite) modifier_list.getItemAtPosition(position);
            }
        });




        loadActivites();
        adapter.notifyDataSetChanged();
    }



    public void ouvrirAccueil(View view){
        startActivity(new Intent(ControleurHistoriqueModifier.this, Application.class));
    }

    public void ouvrirHistorique(View view){
        startActivity(new Intent(ControleurHistoriqueModifier.this, ControleurHistorique.class));
    }

    public void exporterGPX(View view){

        Toast.makeText(ControleurHistoriqueModifier.this, activiteSelect.getNom(), Toast.LENGTH_SHORT).show();
        partager(activiteSelect);

    }

    public void importerGPX(View view){
        //Faire choisir le fichier par l'utilisateur + popup pour choisir nom et sport
        String nom = "Test Import";
        Sport sport = Sport.VELO;
        File fichier = new File("D:/test fichiers.gpx");

        Activite importation = new Activite(nom, sport, fichier);
       enregistrer(importation);
       adapter.notifyDataSetChanged();

    }

    public void loadActivites(){
        String[] fichiers = this.getApplicationContext().fileList();

        for(int i = 0; i<fichiers.length; i++) {
            Activite activite = null;
            try {
                FileInputStream fis = new FileInputStream(new File(this.getApplicationContext().getFilesDir(),fichiers[i]));
                ObjectInputStream ois = new ObjectInputStream(fis);
                activite = (Activite) ois.readObject();
                ois.close();
                fis.close();
                listeActivites.add(activite);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public void partager(Activite activite){
        File fichier = new File(this.getApplicationContext().getFilesDir(), activite.getNom()+".gpx");
        activite.ecrireFichier(fichier);
    }

    public void enregistrer(Activite activite) {
        String nomFichier = activite.getNom() + ".mp";

        try{
            //FileOutputStream fos = this.getApplicationContext().openFileOutput(nomFichier, Context.MODE_PRIVATE);
            FileOutputStream fos = new FileOutputStream(new File(this.getApplicationContext().getFilesDir(), nomFichier));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(activite);
            oos.close();
            fos.close();
            Toast.makeText(this.getApplicationContext(), activite.getNom() + " enregistré", Toast.LENGTH_SHORT).show();
        }catch (IOException io){
            io.printStackTrace();
        }
    }
}
