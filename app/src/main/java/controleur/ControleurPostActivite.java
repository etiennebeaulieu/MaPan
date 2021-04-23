package controleur;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mapan.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import modele.Activite;
import modele.Graphique;

public class ControleurPostActivite extends AppCompatActivity implements OnMapReadyCallback {

    public static final double METRE_PIED = 3.28084;
    public static final double METRE_MILES = 0.000621371;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private LocationComponent locationComponent;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private CheckBox choixAxeX;
    private TextView labelX;
    private BottomSheetBehavior<View> behavior;
    protected LineString lineString;
    protected GeoJsonSource geoJsonSource;
    private LineChart chart = null;
    private LineDataSet setAltitudeTemps = null;
    private LineDataSet setAltitudeDistance = null;
    private LineDataSet setVitesseTemps = null;
    private LineDataSet setVitesseDistance = null;
    private LineData data = null;
    private Activite activite = null;

    protected Boolean graphIsTemps = true;
    protected int nbrPoint = 0;


    @CameraMode.Mode
    private int cameraMode = CameraMode.NONE;

    @RenderMode.Mode
    private int renderMode = RenderMode.NORMAL;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.post_activite);


        activite = (Activite) this.getIntent().getSerializableExtra("activité");

        for(int i= 0; i < activite.tabTemps.size(); i++){
            activite.listeCoordonnee.add(Point.fromLngLat(activite.tabLongitude.get(i), activite.tabLatitude.get(i)));
        }


        TextView nomActivite = findViewById(R.id.nom_activite);
        ImageView imageSport = findViewById(R.id.icon_activite);
        nomActivite.setText(activite.getNom());
        imageSport.setImageResource(activite.getSport().getImage());
        choixAxeX = findViewById(R.id.choixAxeX);
        labelX = findViewById(R.id.TextViewX);

        View bottomSheet = findViewById(R.id.bottomSheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        LinearLayout interieur = findViewById(R.id.interieur);

        interieur.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                interieur.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                View cache = interieur.getChildAt(1);
                behavior.setPeekHeight(cache.getTop());
                behavior.setGestureInsetBottomIgnored(false);
                behavior.setFitToContents(false);
                behavior.setExpandedOffset(bottomSheet.getHeight() - interieur.getHeight());
                behavior.setHalfExpandedRatio(0.4f);
            }
        });

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

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

formatterDonnees();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        //Importe notre style de map
        Style.Builder style = new Style.Builder().fromUri("mapbox://styles/etiennebeaulieu2/ckksiyxzv188t18oa1u88mp2c");
        mapboxMap.setStyle(style, style1 ->
        {
            //Création du tracer GPS
            lineString = LineString.fromLngLats(activite.listeCoordonnee);
            Feature feature = Feature.fromGeometry(lineString);
            geoJsonSource = new GeoJsonSource("geojson-source", feature);
            style1.addSource(geoJsonSource);

            //Affichage du tracé GPS
            style1.addLayer(new LineLayer("linelayer", "geojson-source").withProperties(
                    PropertyFactory.lineWidth(4f),
                    PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                    PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                    PropertyFactory.lineColor(Color.parseColor("#FFF44336"))
            ));
        });

        mapboxMap.setCameraPosition(new CameraPosition.Builder().target(new LatLng(activite.tabLatitude.get(0), activite.tabLongitude.get(0))).zoom(13).build());
    }


    //Centre la caméra sur la position de l'utilisateur
    public void centrer(View view){
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(activite.tabLatitude.get(0), activite.tabLongitude.get(0)), 13), 1000);
    }


    public void ouvrirHistorique(View view){
        startActivity(new Intent(ControleurPostActivite.this, ControleurHistorique.class));
    }

    /*class ReceveurLocation extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("DERNIERE_LOCATION")) {
                setTabGPS((Location) intent.getExtras().get("Location"));

                nbrPoint = ControleurNouvelleActivite.activiteEnCours.tabTemps.size() - 1;


                String unitDist = "";
                double temps = (double) ControleurNouvelleActivite.activiteEnCours.tabTemps.get(nbrPoint).getEpochSecond() - ControleurNouvelleActivite.activiteEnCours.tabTemps.get(0).getEpochSecond();
                double distance = 0;
                for(double dx: ControleurNouvelleActivite.activiteEnCours.tabDistanceMetrique)
                {
                    distance += dx;
                }
                if (context.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour distance", false))
                {
                    distance =  distance*METRE_MILES;
                    unitDist = " (mi)";

                }
                else{
                    distance /= 1000;
                    unitDist = " (km)";
                }

                temps /= 60;



                if (context.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour altitude", false))
                {
                    Graphique.ajouterDonnee(temps, ControleurNouvelleActivite.activiteEnCours.tabElevationMetrique.get(nbrPoint)*METRE_PIED, chart, "setAltitudeTemps");
                    Graphique.ajouterDonnee(distance, ControleurNouvelleActivite.activiteEnCours.tabElevationMetrique.get(nbrPoint)*METRE_PIED, chart, "setAltitudeDistance");
                }
                else
                {
                    Graphique.ajouterDonnee(temps, ControleurNouvelleActivite.activiteEnCours.tabElevationMetrique.get(nbrPoint), chart, "setAltitudeTemps");
                    Graphique.ajouterDonnee(distance, ControleurNouvelleActivite.activiteEnCours.tabElevationMetrique.get(nbrPoint), chart, "setAltitudeDistance");
                }

                if (context.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour vitesse", false))
                {
                    Graphique.ajouterDonnee(temps, ControleurNouvelleActivite.activiteEnCours.tabVitesseMetrique.get(nbrPoint) * METRE_MILES, chart, "setVitesseTemps");
                    Graphique.ajouterDonnee(distance, ControleurNouvelleActivite.activiteEnCours.tabVitesseMetrique.get(nbrPoint) * METRE_MILES, chart, "setVitesseDistance");
                }
                else{
                    Graphique.ajouterDonnee(temps, ControleurNouvelleActivite.activiteEnCours.tabVitesseMetrique.get(nbrPoint) * 3.6, chart, "setVitesseTemps");
                    Graphique.ajouterDonnee(distance, ControleurNouvelleActivite.activiteEnCours.tabVitesseMetrique.get(nbrPoint) * 3.6, chart, "setVitesseDistance");
                }

                if (!choixAxeX.isChecked()) {
                    labelX.setText("Temps (min)");

                    setAltitudeDistance.setVisible(false);
                    setAltitudeDistance.setDrawValues(false);
                    setVitesseDistance.setVisible(false);
                    setVitesseDistance.setDrawValues(false);


                    setAltitudeTemps.setVisible(true);
                    setAltitudeTemps.setDrawValues(true);
                    setVitesseTemps.setVisible(true);
                    setVitesseTemps.setDrawValues(true);

                    chart.setVisibleXRange(0f, (float) temps);
                } else {
                    labelX.setText("Distance" + unitDist);

                    setAltitudeDistance.setVisible(true);
                    setAltitudeDistance.setDrawValues(true);
                    setVitesseDistance.setVisible(true);
                    setVitesseDistance.setDrawValues(true);


                    setAltitudeTemps.setVisible(false);
                    setAltitudeTemps.setDrawValues(false);
                    setVitesseTemps.setVisible(false);
                    setVitesseTemps.setDrawValues(false);

                    chart.setVisibleXRange(0f, (float)distance);
                }


                Intent intent2 = new Intent(context, ServiceStats.class);
                intent2.setAction("ACTION_CALCULER_STATS");
                context.startService(intent2);

            }
             else if(intent.getAction().equals("DERNIERE_STATS")){
                ControleurNouvelleActivite.activiteEnCours.setDistanceMetrique((Double) intent.getExtras().get("Distance"));
                ControleurNouvelleActivite.activiteEnCours.setVitesseMetrique((Double) intent.getExtras().get("Vitesse moyenne"));
                ControleurNouvelleActivite.activiteEnCours.setVitesseActuelleMetrique((Double) intent.getExtras().get("Vitesse actuelle"));
                ArrayList<Double> denivele = (ArrayList<Double>) intent.getExtras().get("Dénivelé");
                ControleurNouvelleActivite.activiteEnCours.setDuree((Duration) intent.getExtras().get("Durée"));
                ControleurNouvelleActivite.activiteEnCours.setDenivelePositifMetrique(denivele.get(0));
                ControleurNouvelleActivite.activiteEnCours.setDeniveleNegatifMetrique(denivele.get(1));
                ControleurNouvelleActivite.activiteEnCours.setAltitudeActuelleMetrique((Double) intent.getExtras().get("Altitude"));
                formatterDonnees();
            }
        }
    }*/

    public void formatterDonnees()
    {
        TextView txtDuree = findViewById(R.id.duree_post);
        TextView txtdebut = findViewById(R.id.temps_debut);
        TextView txtfin = findViewById(R.id.temps_fin);
        TextView txtDistance = findViewById(R.id.distance_post);
        TextView txtVitesseMoyenne = findViewById(R.id.vitesse_moyenne_post);
        TextView txtVitesse = findViewById(R.id.vitesse_max);
        TextView txtAltitudeMax = findViewById(R.id.altitude_max);
        TextView txtAltitudeMin = findViewById(R.id.altitude_min);
        TextView txtDenivelePos = findViewById(R.id.denivele_positif_post);
        TextView txtDeniveleNeg = findViewById(R.id.denivele_negatif_post);


        NumberFormat formatterDistance = new DecimalFormat("#0.00");
        NumberFormat formatterHauteur = new DecimalFormat("#0");

        txtDuree.setText(DateTimeFormatter.ofPattern("HH'h'mm'min'ss'sec'").withZone(ZoneId.of("UTC")).format(activite.getDuree().addTo(Instant.ofEpochSecond(0))));
        txtdebut .setText(DateTimeFormatter.ofPattern("yyyy-MM-dd - HH':'mm':'ss").withZone(ZoneId.systemDefault()).format(activite.tabTemps.get(0)));
        txtfin .setText(DateTimeFormatter.ofPattern("yyyy-MM-dd - HH':'mm':'ss").withZone(ZoneId.systemDefault()).format(activite.tabTemps.get(activite.tabTemps.size()-1)));
        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour distance", false))
        {
            txtDistance.setText(formatterDistance.format(activite.getDistanceMetrique() * METRE_MILES) + "mi");
        }
        else
        {
            txtDistance.setText(formatterDistance.format(activite.getDistanceMetrique() / 1000) + "km");
        }

        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour vitesse", false))
        {
            txtVitesse.setText("max : " + formatterDistance.format(activite.trouverVitesseMax() * METRE_MILES *3600) + "mi/h");
            txtVitesseMoyenne.setText("moy : " + formatterDistance.format(activite.getVitesseMetrique() * METRE_MILES*1000) + "mi/h");
        }
        else
        {
            txtVitesse.setText("max : " + formatterDistance.format(activite.trouverVitesseMax() * 3.6) + "km/h");
            txtVitesseMoyenne.setText("moy : " + formatterDistance.format(activite.getVitesseMetrique()) + "km/h");
        }

        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour altitude", false))
        {
            txtAltitudeMax.setText(formatterHauteur.format(activite.getAltitudeMaxMetrique() * METRE_PIED) + "'");
            txtAltitudeMin.setText(formatterHauteur.format(activite.getAltitudeMinMetrique() * METRE_PIED) + "'");
        }
        else
        {
            txtAltitudeMax.setText(formatterHauteur.format(activite.getAltitudeMaxMetrique()) + "m");
            txtAltitudeMin.setText(formatterHauteur.format(activite.getAltitudeMinMetrique()) + "m");
        }

        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour denivele", false))
        {
            txtDenivelePos.setText(formatterHauteur.format(activite.getDenivelePositifMetrique()* METRE_PIED) + "'");
            txtDeniveleNeg.setText(formatterHauteur.format(activite.getDeniveleNegatifMetrique()* METRE_PIED) + "'");

        }
        else
        {
            txtDenivelePos.setText(formatterHauteur.format(activite.getDenivelePositifMetrique() ) + "m");
            txtDeniveleNeg.setText(formatterHauteur.format(activite.getDeniveleNegatifMetrique() ) + "m");
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ControleurPostActivite.this, ControleurHistorique.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


}
