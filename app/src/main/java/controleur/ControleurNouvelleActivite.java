package controleur;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mapan.R;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import modele.Activite;
import modele.NouvelleActiviteAdapter;
import modele.Sport;

public class ControleurNouvelleActivite extends AppCompatActivity
{
    private EditText nouveau_nom;
    public static Activite activiteEnCours;

    //Liste des types d'activité
    private ListView nouvelle_activitesList;

    //Affichage personalisé pour ListView
    private NouvelleActiviteAdapter adapter;

    //Élément pour pouvoir faire un animation
    private View background;
    private Sport sportSelect;


    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.nouvelle_activite);

        //Vérifie si l'animation doit être déclenché et la lance
        background = findViewById(R.id.background);
        if (savedInstanceState == null)
        {
            background.setVisibility(View.INVISIBLE);
            final ViewTreeObserver viewTreeObserver = background.getViewTreeObserver();
            if (viewTreeObserver.isAlive())
            {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
                {
                    @Override
                    public void onGlobalLayout()
                    {
                        CircularReveal();
                        background.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        }

        nouveau_nom = (EditText) findViewById(R.id.nouveau_nom);
        nouvelle_activitesList = (ListView) findViewById(R.id.nouvelle_activitesList);

        //Création d'une liste des type de sport à partir de l'enum pour l'affichage
        ArrayList<Sport> listSport = new ArrayList<>();
        listSport.add(Sport.COURSE);
        listSport.add(Sport.RANDONNEE);
        listSport.add(Sport.RAQUETTE);
        listSport.add(Sport.PATIN);
        listSport.add(Sport.SKI_RANDONNEE);
        listSport.add(Sport.SKI);
        listSport.add(Sport.SKI_FOND);
        listSport.add(Sport.VELO);
        listSport.add(Sport.AUTRE);

        adapter = new NouvelleActiviteAdapter(this, R.layout.list_activite_row, listSport);
        nouvelle_activitesList.setAdapter(adapter);

        nouvelle_activitesList.setOnItemClickListener((parent, view, position, id) -> sportSelect = (Sport) nouvelle_activitesList.getItemAtPosition(position));


    }

    //Méthode gérant l'animation de l'ouverture du menu
    private void CircularReveal()
    {
        //Indique d'où doit commencer l'animation
        int cx = (int) background.getLeft() + getDips(44);
        int cy = (int) background.getBottom() - getDips(44) - 80;
        float rayonFinal = (float) Math.hypot(background.getWidth(), background.getHeight());

        //L'animation intégré dans un classe Android
        Animator animation = ViewAnimationUtils.createCircularReveal(background, cx, cy, 0, rayonFinal);
        background.setVisibility(View.VISIBLE);
        animation.setDuration(300);
        animation.start();
    }

    private int getDips(int i)
    {
        Resources resources = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, resources.getDisplayMetrics());
    }

    //Lance l'animation de fermeture si l'utilisateur appui sur retour
    @Override
    public void onBackPressed()
    {
        int cx = (int) getDips(44);
        int cy = (int) background.getHeight() - getDips(44) - 80;
        float rayonInitial = (float) Math.hypot(background.getWidth(), background.getHeight());
        Animator animation = ViewAnimationUtils.createCircularReveal(background, cx, cy, rayonInitial, 0);
        animation.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                background.setVisibility(View.INVISIBLE);
                finish();
            }
        });
        animation.setDuration(300);
        animation.start();

    }

    //Méthode pour créer une nouvelle activité à partir des données rentrées par l'utilisateur
    public void creerNouvelleActivite(View view)
    {
        String nom;
        Sport sp = Sport.RANDONNEE;
        /*Vérifie si l'utilisateur a sélectionné un type et inscrit un nom ou si l'un ou l'autre
         * doit être réglé selon la valeur par défaut inscrite dans les paramètres
         * */


        if (!nouveau_nom.getText().toString().equals("") && sportSelect != null)
        {
            nom = nouveau_nom.getText().toString();
            sp = (Sport) sportSelect;
            Activite act = new Activite(nom, sp);
        } else if (nouveau_nom.getText().toString().equals("") && sportSelect != null)
        {
            //Vérifie le format de nom par défaut puis crée un nom de ce format pour donner à l'activité
            String formatNom = this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("nom_défaut", "Activité");
            nom = formatNom;

            if (!formatNom.equals("Activité"))
            {
                switch (formatNom)
                {
                    case "Sport - Date":
                        nom = sportSelect.getNom();
                        nom += " - " + DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.of("EST")).format(Instant.now());
                        break;
                    case "Date":
                        nom = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.of("EST")).format(Instant.now());
                        break;

                }
                sp = sportSelect;

            }

        } else if (!nouveau_nom.getText().toString().equals("") && sportSelect == null)
        {
            nom = nouveau_nom.getText().toString();
            //Vérifie le sport par défaut et met ce sport pour l'activité
            String nomSport = this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("type_défaut", "Randonnée pédestre");
            for (Sport sport : Sport.values())
            {
                if (nomSport.equals(sport.getNom())) sp = sport;

            }


        } else
        {
            String formatNom = this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("nom_défaut", "Activité");
            nom = formatNom;

            if (!formatNom.equals("Activité"))
            {
                switch (formatNom)
                {
                    case "Sport - Date":
                        nom = this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("type_défaut", "Randonnée pédestre");
                        nom += " - " + DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.of("EST")).format(Instant.now());
                        break;
                    case "Date":
                        nom = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.of("EST")).format(Instant.now());
                        break;

                }
                String nomSport = this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("type_défaut", "Randonnée pédestre");
                for (Sport sport : Sport.values())
                {
                    if (nomSport.equals(sport.getNom())) sp = sport;

                }

            }
        }
        activiteEnCours = new Activite(nom, sp);

        Intent intent = new Intent(ControleurNouvelleActivite.this, ControleurEnCours.class);
        intent.setAction("COMMENCER_ACTIVITE");
        startActivity(intent);
    }

    public void ouvrirParametre(View view)
    {
        startActivity(new Intent(ControleurNouvelleActivite.this, ControleurParametre.class));
    }

    public void ouvrirAccueil(View view)
    {
        //Lance l'animation si l'utilisateur retourne à l'accueil
        int cx = (int) getDips(44);
        int cy = (int) background.getHeight() - getDips(44) - 80;
        float rayonInitial = (float) Math.hypot(background.getWidth(), background.getHeight());
        Animator animation = ViewAnimationUtils.createCircularReveal(background, cx, cy, rayonInitial, 0);
        animation.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                background.setVisibility(View.INVISIBLE);
                finish();
            }
        });
        animation.setDuration(300);
        animation.start();
        while (!animation.isRunning())
            startActivity(new Intent(ControleurNouvelleActivite.this, ControleurAccueil.class));
    }
}
