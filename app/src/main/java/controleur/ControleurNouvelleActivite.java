package controleur;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
    private ListView nouvelle_activitesList;
    private NouvelleActiviteAdapter adapter;
    private View background;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.nouvelle_activite);
        background = findViewById(R.id.background);
        if(savedInstanceState == null){
            background.setVisibility(View.INVISIBLE);
            final ViewTreeObserver viewTreeObserver = background.getViewTreeObserver();
            if(viewTreeObserver.isAlive()){
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        CircularReveal();
                        background.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        }

        nouveau_nom = (EditText)findViewById(R.id.nouveau_nom);
        nouvelle_activitesList = (ListView)findViewById(R.id.nouvelle_activitesList);


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


    }

    private void CircularReveal(){
        int cx = (int) background.getLeft() + getDips(44);
        int cy = (int) background.getBottom() - getDips(44)- 80;
        float rayonFinal = (float) Math.hypot(background.getWidth(), background.getHeight());
        Animator animation = ViewAnimationUtils.createCircularReveal(background, cx,cy, 0, rayonFinal);
        background.setVisibility(View.VISIBLE);
        animation.setDuration(300);
        animation.start();
    }

    private int getDips(int i){
        Resources resources = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, resources.getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {
        int cx = (int) getDips(44);
        int cy = (int) background.getHeight() - getDips(44)- 80;
        float rayonInitial = (float) Math.hypot(background.getWidth(), background.getHeight());
        Animator animation = ViewAnimationUtils.createCircularReveal(background, cx,cy, rayonInitial, 0);
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                background.setVisibility(View.INVISIBLE);
                finish();
            }
        });
        animation.setDuration(300);
        animation.start();

    }

    public void creerNouvelleActivite(View view)
        {
            String nom;
            Sport sp = Sport.RANDONNEE;
            if(nouveau_nom.getText() != null && nouvelle_activitesList.getSelectedItem() !=null) {
                nom = nouveau_nom.getText().toString();
                sp = (Sport) nouvelle_activitesList.getSelectedItem();
                Activite act = new Activite(nom, sp);
            }else if(nouveau_nom.getText() == null && nouvelle_activitesList.getSelectedItem() !=null){
                String formatNom = this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("nom_défaut", "Activité");
                nom = "Activité";

                if(!formatNom.equals("Activité")){
                    switch (formatNom){
                        case "Sport - Date":
                            nom = ((Sport) nouvelle_activitesList.getSelectedItem()).getNom();
                            nom += " - " + DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.of("EST")).format(Instant.now());
                            break;
                        case "Date":
                            nom = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.of("EST")).format(Instant.now());
                            break;

                    }
                    sp = (Sport) nouvelle_activitesList.getSelectedItem();

                }

            }else if(nouveau_nom.getText() != null && nouvelle_activitesList.getSelectedItem() ==null){
                    nom = nouveau_nom.getText().toString();
                    String nomSport = this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("type_défaut", "Randonnée pédestre");
                    for(Sport sport: Sport.values()){
                        if(nomSport.equals(sport.getNom()))
                            sp = sport;

                    }


            }
            else{
                String formatNom = this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("nom_défaut", "Activité");
                nom = "Activité";

                if(!formatNom.equals("Activité")){
                    switch (formatNom){
                        case "Sport - Date":
                            nom = this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("type_défaut", "Randonnée pédestre");
                            nom += " - " + DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.of("EST")).format(Instant.now());
                            break;
                        case "Date":
                            nom = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.of("EST")).format(Instant.now());
                            break;

                    }
                    String nomSport = this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("type_défaut", "Randonnée pédestre");
                    for(Sport sport: Sport.values()){
                        if(nomSport.equals(sport.getNom()))
                            sp = sport;

                    }

                }
            }
            Activite act = new Activite(nom, sp);
        }

    public void ouvrirParametre(View view)
    {
        startActivity(new Intent(ControleurNouvelleActivite.this, ControleurParametre.class));
    }

    public void ouvrirAccueil(View view) throws InterruptedException {

        int cx = (int) getDips(44);
        int cy = (int) background.getHeight() - getDips(44)- 80;
        float rayonInitial = (float) Math.hypot(background.getWidth(), background.getHeight());
        Animator animation = ViewAnimationUtils.createCircularReveal(background, cx,cy, rayonInitial, 0);
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
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
