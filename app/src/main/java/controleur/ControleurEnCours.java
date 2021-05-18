package controleur;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mapan.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import modele.Activite;
import modele.Fichier;
import modele.Graphique;
import service.ServiceLocation;
import service.ServiceStats;

//Classe s'occupant de gérer les données et statistiques pendant l'activité ainsi que de les enregistrer lors de l'achèvement de l'activité.
public class ControleurEnCours extends AppCompatActivity implements OnMapReadyCallback
{

    public static final double METRE_PIED = 3.28084;
    public static final double METRE_MILES = 0.000621371;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private LocationComponent locationComponent;
    private LocationEngine locationEngine;
    private long INTERVAL_DEFAUT_MILLIS = 1000;
    private long TEMPS_ATTENTE_DEFAUT = INTERVAL_DEFAUT_MILLIS * 5;
    private FloatingActionButton fabPause;
    private FloatingActionButton fabEnregistrer;
    private FloatingActionButton fabSupprimer;
    private boolean fabOuvert;
    private CheckBox choixAxeX;
    private TextView labelX;
    private BottomSheetBehavior<View> behavior;
    private ReceveurLocation receveur;
    protected LineString lineString;
    protected GeoJsonSource geoJsonSource;
    private LineChart chart = null;
    private LineDataSet setAltitudeTemps = null;
    private LineDataSet setAltitudeDistance = null;
    private LineDataSet setVitesseTemps = null;
    private LineDataSet setVitesseDistance = null;
    private LineData data = null;

    protected int nbrPoint = 0;


    @CameraMode.Mode
    private int cameraMode = CameraMode.TRACKING;

    @RenderMode.Mode
    private int renderMode = RenderMode.COMPASS;
    private AccueilLocationCallback callback = new AccueilLocationCallback(this);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activite_en_cours);

        //On trouve la location de l'utilisateur
        receveur = new ReceveurLocation();
        registerReceiver(receveur, new IntentFilter("DERNIERE_LOCATION"));
        registerReceiver(receveur, new IntentFilter("DERNIERE_STATS"));

        //On associe les éléments xml aux variables de la classe.
        TextView nomActivite = findViewById(R.id.nom_activite);
        ImageView imageSport = findViewById(R.id.icon_activite);
        nomActivite.setText(ControleurNouvelleActivite.activiteEnCours.getNom());
        imageSport.setImageResource(ControleurNouvelleActivite.activiteEnCours.getSport().getImage());
        choixAxeX = findViewById(R.id.choixAxeX);
        labelX = findViewById(R.id.TextViewX);

        //On associe les éléments xml aux variables de la classe.
        View bottomSheet = findViewById(R.id.bottomSheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        LinearLayout interieur = findViewById(R.id.interieur);

        interieur.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                interieur.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                View cache = interieur.getChildAt(1);
                behavior.setPeekHeight(cache.getTop());
                behavior.setGestureInsetBottomIgnored(false);
                behavior.setFitToContents(false);
                behavior.setExpandedOffset(bottomSheet.getHeight() - interieur.getHeight());
                behavior.setHalfExpandedRatio(0.4f);
            }
        });

        //On associe les éléments xml aux variables de la classe.
        fabPause = findViewById(R.id.fab_pause);
        fabEnregistrer = findViewById(R.id.fab_enregistrer);
        fabSupprimer = findViewById(R.id.fab_supprimer);
        fabOuvert = false;


        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        lancerServiceLocation("ACTION_COMMENCER_SERVICE");

        if (!(this.getIntent().getAction().equals(Intent.ACTION_MAIN))) {
            //Créer le graphique et instancier tout ce qui doit être instancier
            chart = findViewById(R.id.chart);
            data = new LineData();
            chart.setData(data);
            Graphique.modifierGraphique("Graphique", chart);
            chart.getLegend().setEnabled(false);
            //4 différent sets de données pour les 4 possibilité, mais seulement 2 d'afficher à la fois
            setAltitudeTemps = Graphique.createSetAltitude(true);
            setAltitudeDistance = Graphique.createSetAltitude(false);
            setVitesseTemps = Graphique.createSetVitesse(true);
            setVitesseDistance = Graphique.createSetVitesse(false);

            //Possiblement rajouter une option dans les paramètre ou simplement checkbox pour choisir l'axe x du graphique
            data.addDataSet(setAltitudeTemps);
            data.addDataSet(setVitesseTemps);
            data.addDataSet(setAltitudeDistance);
            data.addDataSet(setVitesseDistance);

        }


    }

    //Lance le service de localisation du téléphone
    private void lancerServiceLocation(String action)
    {
        Intent intent = new Intent(this, ServiceLocation.class);
        intent.setAction(action);

        startService(intent);

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap)
    {
        this.mapboxMap = mapboxMap;
        //Importe notre style de map
        Style.Builder style = new Style.Builder().fromUri("mapbox://styles/etiennebeaulieu2/ckksiyxzv188t18oa1u88mp2c");
        mapboxMap.setStyle(style, style1 ->
        {
            //Création du tracer GPS
            lineString = LineString.fromLngLats(ControleurNouvelleActivite.activiteEnCours.listeCoordonnee);
            Feature feature = Feature.fromGeometry(lineString);
            geoJsonSource = new GeoJsonSource("geojson-source", feature);
            style1.addSource(geoJsonSource);

            //Affichage du tracé GPS
            style1.addLayer(new LineLayer("linelayer", "geojson-source").withProperties(PropertyFactory.lineWidth(4f), PropertyFactory.lineCap(Property.LINE_CAP_ROUND), PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND), PropertyFactory.lineColor(Color.parseColor("#FFF44336"))));


            //Pointeur de localisation personnalisé
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this).
                    layerAbove("linelayer").
                    accuracyColor(Color.parseColor("#00FFFFFF")).
                    accuracyAlpha(0.4f).
                    foregroundTintColor(Color.parseColor("#FFF44336")).
                    build();


            //Paramètre la camera par rapport à la map
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(ControleurEnCours.this, style1).locationComponentOptions(customLocationComponentOptions).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(cameraMode);
            locationComponent.setRenderMode(renderMode);


            initLocationEngine();
        });
    }

    @SuppressLint("MissingPermission")
    private void initLocationEngine()
    {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(INTERVAL_DEFAUT_MILLIS).setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY).setMaxWaitTime(TEMPS_ATTENTE_DEFAUT).build();


        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }

    //S'occupe de faire le tracé du parcours de l'utilisateur.
    private static class AccueilLocationCallback implements LocationEngineCallback<LocationEngineResult>
    {

        private final WeakReference<ControleurEnCours> activityWeakReference;

        AccueilLocationCallback(ControleurEnCours activity)
        {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(LocationEngineResult result)
        {
            ControleurEnCours activity = activityWeakReference.get();

            if (activity != null)
            {
                Location location = result.getLastLocation();

                if (location == null)
                {
                    return;
                }

                if (activity.mapboxMap != null && result.getLastLocation() != null)
                {
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                    activity.updateTracer();

                }
            }
        }

        @Override
        public void onFailure(@NonNull Exception exception)
        {
            Log.d("LocationChangeActivity", exception.getLocalizedMessage());
            ControleurEnCours activity = activityWeakReference.get();
            if (activity != null)
            {
                Toast.makeText(activity, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //On remplis les tableaux de données avec celles enregistrées en temps réel.
    private static void setTabGPS(Location location)
    {
        ControleurNouvelleActivite.activiteEnCours.getTabLatitude().add(location.getLatitude());
        ControleurNouvelleActivite.activiteEnCours.getTabLongitude().add(location.getLongitude());
        ControleurNouvelleActivite.activiteEnCours.getTabElevation().add(location.getAltitude());
        ControleurNouvelleActivite.activiteEnCours.getTabTemps().add(Instant.ofEpochMilli(location.getTime()));
        ControleurNouvelleActivite.activiteEnCours.setDuree(Duration.between(ControleurNouvelleActivite.activiteEnCours.tabTemps.get(0), ControleurNouvelleActivite.activiteEnCours.tabTemps.get(ControleurNouvelleActivite.activiteEnCours.tabTemps.size() - 1)));
        ControleurNouvelleActivite.activiteEnCours.listeCoordonnee.add(Point.fromLngLat(location.getLongitude(), location.getLatitude()));
        if (ControleurNouvelleActivite.activiteEnCours.getTabTemps().size() > 1)
            ControleurNouvelleActivite.activiteEnCours.tabDistance.add(ControleurNouvelleActivite.activiteEnCours.calculerDistance(ControleurNouvelleActivite.activiteEnCours.tabTemps.size() - 2, ControleurNouvelleActivite.activiteEnCours.tabTemps.size() - 1));
        ControleurNouvelleActivite.activiteEnCours.tabVitesse.add((double) location.getSpeed());


    }

    //Mettre à jour le tracer selon les nouveaux point GPS
    private void updateTracer()
    {
        lineString = LineString.fromLngLats(ControleurNouvelleActivite.activiteEnCours.listeCoordonnee);
        geoJsonSource.setGeoJson(lineString);
    }

    //Centre la caméra sur la position de l'utilisateur
    public void centrer(View view)
    {
        Location lastKnownLocation = mapboxMap.getLocationComponent().getLastKnownLocation();

        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 15), 1000);
    }

    //Gère le bouton pause de la fenêtre. Si l'activité est mise en pause,
    // on arrête d'enregistrer les données puis on recommence lorsque l'utilisateur clique sur "play".
    public void pause(View view)
    {


        if (!fabOuvert)
        {
            lancerServiceLocation("ACTION_PAUSE_SERVICE");
            locationEngine.removeLocationUpdates(callback);
            fabOuvert = true;

            fabPause.animate().rotation(360);
            fabPause.setImageDrawable(getDrawable(R.drawable.ic_play));
            fabEnregistrer.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
            fabSupprimer.animate().translationY(-getResources().getDimension(R.dimen.standard_105));

            System.out.println(ControleurNouvelleActivite.activiteEnCours.calculerDistance(0, ControleurNouvelleActivite.activiteEnCours.getTabTemps().size() - 1));
        } else
        {
            initLocationEngine();
            lancerServiceLocation("ACTION_COMMENCER_SERVICE");
            fabOuvert = false;

            fabPause.animate().rotation(-360);
            fabPause.setImageDrawable(getDrawable(R.drawable.ic_pause));
            fabSupprimer.animate().translationY(0);
            fabEnregistrer.animate().translationY(0);

        }

    }

    //Enregistre l'activité lorsque l'utilisateur la met en pause et sélectionne l'icone "Enregistrer".
    public void enregistrer(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Enregistrer").setMessage("Voulez-vous vraiment enregistrer l'activité?").setPositiveButton("Enregistrer", (dialog, which) ->
        {
            lancerServiceLocation("ACTION_STOP_SERVICE");
            ControleurNouvelleActivite.activiteEnCours.setDistanceMetrique(ControleurNouvelleActivite.activiteEnCours.calculerDistance(0, ControleurNouvelleActivite.activiteEnCours.getTabTemps().size() - 1));

            ControleurNouvelleActivite.activiteEnCours.setDuree(Duration.between(ControleurNouvelleActivite.activiteEnCours.getTabTemps().get(0), ControleurNouvelleActivite.activiteEnCours.getTabTemps().get(ControleurNouvelleActivite.activiteEnCours.getTabTemps().size() - 1)));


            File fichier = Fichier.partager(this.getApplicationContext(), ControleurNouvelleActivite.activiteEnCours);
            Activite activite = new Activite(ControleurNouvelleActivite.activiteEnCours.getNom(), ControleurNouvelleActivite.activiteEnCours.getSport(), fichier);
            Fichier.enregistrer(this, activite);


            startActivity(new Intent(ControleurEnCours.this, ControleurHistorique.class));
        }).setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss()).show();
    }

    //Supprime l'activité lorsque l'utilisatuer la met en pause et sélectionne l'icone "Supprimer".
    public void supprimer(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Supprimer").setMessage("Voulez-vous vraiment supprimer l'activité?").setPositiveButton("Supprimer", (dialog, which) ->
        {
            lancerServiceLocation("ACTION_STOP_SERVICE");
            startActivity(new Intent(ControleurEnCours.this, ControleurAccueil.class));
        }).setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss()).show();
    }

    //Classe s'occupant des méthodes accomplies pour le graphique.
    class ReceveurLocation extends BroadcastReceiver
    {

        //S'occupe d'aller chercher toutes les données nécessaire à la création du graphique en fonction de toutes les préférences de l'utilisateur.
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals("DERNIERE_LOCATION"))
            {
                setTabGPS((Location) intent.getExtras().get("Location"));

                nbrPoint = ControleurNouvelleActivite.activiteEnCours.tabTemps.size() - 1;


                String unitDist = "";
                //set le temps qui sera utilisé pour le graphique.
                double temps = (double) ControleurNouvelleActivite.activiteEnCours.tabTemps.get(nbrPoint).getEpochSecond() - ControleurNouvelleActivite.activiteEnCours.tabTemps.get(0).getEpochSecond();
                double distance = 0;
                for (double dx : ControleurNouvelleActivite.activiteEnCours.tabDistance)
                {
                    distance += dx;
                }
                //On vérifie si la préférence "impériale pour distance" a été sélectionnée par l'utilisateur avant de débuter l'activité. Puis on change les unités du graphique en fonction.
                if (context.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour distance", false))
                {
                    distance = distance * METRE_MILES;
                    unitDist = " (mi)";

                } else
                {
                    //mettre la distance en km
                    distance /= 1000;
                    unitDist = " (km)";
                }

                //mettre le temps en minutes.
                temps /= 60;


                //Vérifie au fur et à mesure la préférence "impériale pour altitude" et ajoute l'altitude dans le graphique en fonction du temps et dans celui en fonction de la distance.
                if (context.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour altitude", false))
                {
                    Graphique.ajouterDonnee(temps, ControleurNouvelleActivite.activiteEnCours.tabElevation.get(nbrPoint) * METRE_PIED, chart, "setAltitudeTemps");
                    Graphique.ajouterDonnee(distance, ControleurNouvelleActivite.activiteEnCours.tabElevation.get(nbrPoint) * METRE_PIED, chart, "setAltitudeDistance");
                } else
                {
                    Graphique.ajouterDonnee(temps, ControleurNouvelleActivite.activiteEnCours.tabElevation.get(nbrPoint), chart, "setAltitudeTemps");
                    Graphique.ajouterDonnee(distance, ControleurNouvelleActivite.activiteEnCours.tabElevation.get(nbrPoint), chart, "setAltitudeDistance");
                }

                //Vérifie au fur et à mesure la préférence "impérial pour vitesse" et ajoute la vitesse dans le graphique en fonction du temps et dans celui en fonction de la distance.
                if (context.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour vitesse", false))
                {
                    Graphique.ajouterDonnee(temps, ControleurNouvelleActivite.activiteEnCours.tabVitesse.get(nbrPoint) * METRE_MILES * 3600, chart, "setVitesseTemps");
                    Graphique.ajouterDonnee(distance, ControleurNouvelleActivite.activiteEnCours.tabVitesse.get(nbrPoint) * METRE_MILES * 3600, chart, "setVitesseDistance");
                } else
                {
                    Graphique.ajouterDonnee(temps, ControleurNouvelleActivite.activiteEnCours.tabVitesse.get(nbrPoint) * 3.6, chart, "setVitesseTemps");
                    Graphique.ajouterDonnee(distance, ControleurNouvelleActivite.activiteEnCours.tabVitesse.get(nbrPoint) * 3.6, chart, "setVitesseDistance");
                }

                //
                if (!choixAxeX.isChecked())
                {
                    //Change l'étiquette de l'axe X
                    labelX.setText("Temps (min)");

                    //Retire les valeurs en fonction de la distance
                    setAltitudeDistance.setVisible(false);
                    setAltitudeDistance.setDrawValues(false);
                    setVitesseDistance.setVisible(false);
                    setVitesseDistance.setDrawValues(false);

                    //Rend visible les valeurs en fonction du temps
                    setAltitudeTemps.setVisible(true);
                    setAltitudeTemps.setDrawValues(true);
                    setVitesseTemps.setVisible(true);
                    setVitesseTemps.setDrawValues(true);

                    //Limite le range d'affichage jusqu'à la dernière valeur
                    chart.setVisibleXRange(0f, (float) temps);
                } else
                {
                    //Change l'étiquette de l'axe X
                    labelX.setText("Distance" + unitDist);

                    //Retire les valeurs en fonction de la distance
                    setAltitudeDistance.setVisible(true);
                    setAltitudeDistance.setDrawValues(true);
                    setVitesseDistance.setVisible(true);
                    setVitesseDistance.setDrawValues(true);

                    //Rend visible les valeurs en fonction du temps
                    setAltitudeTemps.setVisible(false);
                    setAltitudeTemps.setDrawValues(false);
                    setVitesseTemps.setVisible(false);
                    setVitesseTemps.setDrawValues(false);

                    //Limite le range d'affichage jusqu'à la dernière valeur
                    chart.setVisibleXRange(0f, (float) distance);
                }


                Intent intent2 = new Intent(context, ServiceStats.class);
                intent2.setAction("ACTION_CALCULER_STATS");
                context.startService(intent2);

            } else if (intent.getAction().equals("DERNIERE_STATS"))
            {
                ControleurNouvelleActivite.activiteEnCours.setDistanceMetrique((Double) intent.getExtras().get("Distance"));
                ControleurNouvelleActivite.activiteEnCours.setVitesseMoyenne((Double) intent.getExtras().get("Vitesse moyenne"));
                ControleurNouvelleActivite.activiteEnCours.setVitesseActuelle((Double) intent.getExtras().get("Vitesse actuelle"));
                ArrayList<Double> denivele = (ArrayList<Double>) intent.getExtras().get("Dénivelé");
                ControleurNouvelleActivite.activiteEnCours.setDuree((Duration) intent.getExtras().get("Durée"));
                ControleurNouvelleActivite.activiteEnCours.setDenivelePositif(denivele.get(0));
                ControleurNouvelleActivite.activiteEnCours.setDeniveleNegatif(denivele.get(1));
                ControleurNouvelleActivite.activiteEnCours.setAltitudeActuelle((Double) intent.getExtras().get("Altitude"));
                formatterDonnees();
            }
        }
    }

    public void formatterDonnees()
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
        NumberFormat formatterCoord = new DecimalFormat("#0.000000'°'");

        txtDuree.setText(DateTimeFormatter.ofPattern("HH'h'mm'min'ss'sec'").withZone(ZoneId.of("UTC")).format(ControleurNouvelleActivite.activiteEnCours.getDuree().addTo(Instant.ofEpochSecond(0))));
        txtLatitude.setText(formatterCoord.format(ControleurNouvelleActivite.activiteEnCours.getTabLatitude().get(ControleurNouvelleActivite.activiteEnCours.getTabLatitude().size() - 1)));
        txtLongitude.setText(formatterCoord.format(ControleurNouvelleActivite.activiteEnCours.getTabLongitude().get(ControleurNouvelleActivite.activiteEnCours.getTabLongitude().size() - 1)));

        //Affiche la distance selon les préférences
        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour distance", false))
        {
            txtDistance.setText(formatterDistance.format(ControleurNouvelleActivite.activiteEnCours.getDistanceMetrique() * METRE_MILES) + "mi");
        } else
        {
            txtDistance.setText(formatterDistance.format(ControleurNouvelleActivite.activiteEnCours.getDistanceMetrique() / 1000) + "km");
        }

        //Affiche les vitesse selon les préférences
        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour vitesse", false))
        {
            txtVitesse.setText(formatterDistance.format(ControleurNouvelleActivite.activiteEnCours.getVitesseActuelle() * METRE_MILES * 3600) + "mi/h");
            txtVitesseMoyenne.setText("moy : " + formatterDistance.format(ControleurNouvelleActivite.activiteEnCours.getVitesseMoyenne() * METRE_MILES * 3600) + "mi/h");
        } else
        {
            txtVitesse.setText(formatterDistance.format(ControleurNouvelleActivite.activiteEnCours.getVitesseActuelle() * 3.6) + "km/h");
            txtVitesseMoyenne.setText("moy : " + formatterDistance.format(ControleurNouvelleActivite.activiteEnCours.getVitesseMoyenne() * 3.6) + "km/h");
        }

        //Affiche les altitudes selon les préférences
        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour altitude", false))
        {
            txtAltitude.setText(formatterHauteur.format(ControleurNouvelleActivite.activiteEnCours.getAltitudeActuelle() * METRE_PIED) + "'");
        } else
        {
            txtAltitude.setText(formatterHauteur.format(ControleurNouvelleActivite.activiteEnCours.getAltitudeActuelle()) + "m");
        }

        //Affiche les dénivelé selon les préférences
        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour denivele", false))
        {
            txtDenivelePos.setText(formatterHauteur.format(ControleurNouvelleActivite.activiteEnCours.getDenivelePositif() * METRE_PIED) + "'");
            txtDeniveleNeg.setText(formatterHauteur.format(ControleurNouvelleActivite.activiteEnCours.getDeniveleNegatif() * METRE_PIED) + "'");

        } else
        {
            txtDenivelePos.setText(formatterHauteur.format(ControleurNouvelleActivite.activiteEnCours.getDenivelePositif()) + "m");
            txtDeniveleNeg.setText(formatterHauteur.format(ControleurNouvelleActivite.activiteEnCours.getDeniveleNegatif()) + "m");
        }
    }

    @Override
    public void onBackPressed()
    {

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
    }


}
