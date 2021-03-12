package controleur;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.Application;
import com.example.mapan.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;

import modele.Activite;
import modele.ActiviteAdapter;
import modele.Fichier;
import modele.Sport;

public class ControleurHistorique extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private ArrayList<Activite> listeActivites = new ArrayList<>();
    private ListView historique_list;
    private boolean isDistanceMetrique = true;
    private Button modifier_DeleteActivity;
    private ActiviteAdapter adapter;

    private Activite ajouterActivite;
    private String ajouterNom = "";
    private Instant ajouterDate = Instant.ofEpochMilli(0);
    private Sport ajouterSport = Sport.COURSE;
    private int ajouterDuree = 0;
    private double ajouterDistance = 0.0;




    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.historique);
        historique_list = findViewById(R.id.modifier_list);
        //adapter = new ActiviteAdapter(this, R.layout.list_row, listeActivites);
        adapter = new ActiviteAdapter(this, R.layout.list_row, Fichier.getListeActivites());
        historique_list.setAdapter(adapter);


        //enregistrerActivitesTest();
        Fichier.rafraichir(this.getApplicationContext());
        adapter.notifyDataSetChanged();

    }

    public void enregistrerActivitesTest() {
        Activite a1 = new Activite("Activité 1", Instant.ofEpochMilli(1700000), Sport.SKI_RANDONNEE, 75, 19.5);
        Activite a2 = new Activite("Activité 2", Instant.ofEpochMilli(170000000), Sport.COURSE, 75, 20.5);
        Activite a3 = new Activite("Activité 3", Instant.ofEpochMilli(170000000), Sport.RANDONNEE, 75, 21.5);
        Activite a4 = new Activite("Activité 4", Instant.ofEpochMilli(170000000), Sport.RAQUETTE, 75, 90.5);
        Activite a5 = new Activite("Activité 5", Instant.ofEpochMilli(170000000), Sport.VELO, 75, 50.5);

        Fichier.enregistrer(this.getApplicationContext(), a1);
        Fichier.enregistrer(this.getApplicationContext(), a2);
        Fichier.enregistrer(this.getApplicationContext(), a3);
        Fichier.enregistrer(this.getApplicationContext(), a4);
        Fichier.enregistrer(this.getApplicationContext(), a5);
    }

    public void trierListeDate() {
        Fichier.getListeActivites().sort(Comparator.comparing(Activite::getDate));
        adapter.notifyDataSetChanged();
    }

    public void trierListeDuree() {
        Fichier.getListeActivites().sort(Comparator.comparing(Activite::getDuree));
        adapter.notifyDataSetChanged();
    }

    public void trierListeDistance() {
        Fichier.getListeActivites().sort(Comparator.comparingDouble(Activite::getDistanceMetrique).reversed());
        adapter.notifyDataSetChanged();
    }

    public void trierListeNom() {
        Fichier.getListeActivites().sort(Comparator.comparing(Activite::getNom));
        adapter.notifyDataSetChanged();
    }

    public void trierListeSport() {
        Fichier.getListeActivites().sort(Comparator.comparing(Activite::getSport));
        adapter.notifyDataSetChanged();
    }

    public void ouvrirAccueil(View view) {
        startActivity(new Intent(ControleurHistorique.this, ControleurAccueil.class));
    }

    public void ouvrirModifier(View view) {
        startActivity(new Intent(ControleurHistorique.this, ControleurHistoriqueModifier.class));
    }

    public void ouvrirParametre(View view) {
        startActivity(new Intent(ControleurHistorique.this, ControleurParametre.class));
    }

    public void afficherMenuTri(View view) {
        PopupMenu menuTri = new PopupMenu(this, view);
        menuTri.setOnMenuItemClickListener(this);
        MenuInflater inflater = menuTri.getMenuInflater();
        inflater.inflate(R.menu.menu_tri, menuTri.getMenu());

        menuTri.show();

    }

    /*public void ajouterActivite(View view) {
        AlertDialog.Builder builderSport = new AlertDialog.Builder(this);
        AlertDialog.Builder builderNom = new AlertDialog.Builder(this);
        AlertDialog.Builder builderDistance = new AlertDialog.Builder(this);

        //View viewAjouter = getLayoutInflater().inflate(, null);

        ajouterSport = null;

        Spinner spinner = new Spinner(this);
        ArrayAdapter aa2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Sport.values());
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa2);

        EditText inputNom = new EditText(this);
        inputNom.setInputType(InputType.TYPE_CLASS_TEXT);

        EditText inputDistance = new EditText(this);
        inputDistance.setInputType(InputType.TYPE_CLASS_TEXT);

        EditText inputDate = new EditText(this);
        inputDate.setInputType(InputType.TYPE_CLASS_TEXT);

        EditText inputDuree = new EditText(this);
        inputDuree.setInputType(InputType.TYPE_CLASS_TEXT);

        if(ajouterNom == null)
        {
            builderNom.setTitle("Nommez l'activité").setMessage("Insérer un nom d'activité")
                    .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ajouterNom = inputNom.getText().toString();
                        }
                    }).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setView(inputNom).show();

        }

        if(ajouterDate == null)
        {
            builderNom.setTitle("Énoncer la date").setMessage("Insérer la date de l'activité")
                    .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ajouterDate = Instant.ofEpochMilli(Long.valueOf(inputDate.getText().toString()));
                        }
                    }).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setView(inputDate).show();
        }

       if(ajouterSport == null)
        {
            builderSport.setTitle("Créez une nouvelle activité").setMessage("Choisir le type d'activité")
                    .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface Dialog, int which) {
                            ajouterSport = Sport.valueOf(spinner.getSelectedItem().toString());
                        }
                    }).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setView(spinner).show();
        }

        if(ajouterDuree == 0.0)
        {
            builderDistance.setTitle("Énoncez la durée").setMessage("Insérez la durée de l'activité")
                    .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ajouterDuree = Integer.valueOf(inputDuree.getText().toString());
                        }
                    }).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setView(inputDuree).show();
        }

        if(ajouterDistance == 0.0)
        {
            builderDistance.setTitle("Énoncez la distance").setMessage("Insérez la distance parcourue lors de l'activité")
                    .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ajouterDistance = Double.valueOf(inputDistance.getText().toString());
                        }
                    }).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setView(inputDistance).show();
        }

        if(!(ajouterNom.equals(null)) && !(ajouterDate.equals(null)) && !(ajouterSport.equals(null))
                && !(ajouterDuree == 0) && !(ajouterDistance == 0.0)){

            ajouterActivite = new Activite(ajouterNom, ajouterDate, ajouterSport, ajouterDuree, ajouterDistance);

            Fichier.enregistrer(this.getApplicationContext(), ajouterActivite);
            Fichier.rafraichir(this.getApplicationContext());
            adapter.notifyDataSetChanged();

            ajouterNom = null;
            ajouterDate = null;
            ajouterSport = null;
            ajouterDuree = 0;
            ajouterDistance = 0.0;
        }
    }*/

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        boolean retour = false;
        switch (item.getItemId()) {
            case R.id.triNom:
                trierListeNom();
                retour = true;
                break;

            case R.id.triSport:
                trierListeSport();
                retour = true;
                break;

            case R.id.triDistance:
                trierListeDistance();
                retour = true;
                break;

            case R.id.triDuree:
                trierListeDuree();
                retour = true;
                break;

            default:
                retour = false;
        }
        return retour;
    }
}


