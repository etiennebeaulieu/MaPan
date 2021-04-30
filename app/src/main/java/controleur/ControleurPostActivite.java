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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
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
import java.util.ArrayList;

import modele.Activite;
import modele.Graphique;

public class ControleurPostActivite extends AppCompatActivity implements OnMapReadyCallback {

    public static final double METRE_PIED = 3.28084;
    public static final double METRE_MILES = 0.000621371;
    private static final boolean AXE_IS_TEMPS = true;
    private static final boolean AXE_IS_DISTANCE = false;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private CheckBox choixAxeX;
    private TextView labelX;
    //Page de stats
    private BottomSheetBehavior<View> behavior;
    //Tracé GPS
    protected LineString lineString;
    protected GeoJsonSource geoJsonSource;
    //Graphique
    private LineChart chart = null;
    //Sets des données
    private LineDataSet setAltitudeTemps = null;
    private LineDataSet setAltitudeDistance = null;
    private LineDataSet setVitesseTemps = null;
    private LineDataSet setVitesseDistance = null;
    //Listes des données
    private ArrayList<Entry> listAltitudeTemps = null;
    private ArrayList<Entry> listAltitudeDistance = null;
    private ArrayList<Entry> listVitesseTemps = null;
    private ArrayList<Entry> listVitesseDistance = null;
    //L'activité dont on regarde les stats
    private Activite activite = null;
    private String unitDist = "";




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.post_activite);


        //Récupère l'activité à partir de l'intent envoyé par historique
        activite = (Activite) this.getIntent().getSerializableExtra("activité");

        //Crée un liste de coordonnée à partir des tableaux latitude et longitude
        for(int i= 0; i < activite.tabTemps.size(); i++){
            activite.listeCoordonnee.add(Point.fromLngLat(activite.tabLongitude.get(i), activite.tabLatitude.get(i)));
        }


        //Affiche le bon nom et icon pour l'activité
        TextView nomActivite = findViewById(R.id.nom_activite);
        ImageView imageSport = findViewById(R.id.icon_activite);
        nomActivite.setText(activite.getNom());
        imageSport.setImageResource(activite.getSport().getImage());

        choixAxeX = findViewById(R.id.postChoixAxeX);
        labelX = findViewById(R.id.TextViewX);

        //Paramètre la page de statistique
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

        //Instancie la map
        mapView.getMapAsync(this);


        //Rempli les listes de données
        remplirListGraph();

        //Créer le graphique et instancier tout ce qui doit être instancier
        chart = findViewById(R.id.postChart);
        LineData data = new LineData();
        chart.setData(data);
        Graphique.modifierGraphique("Graphique", chart);
        chart.getLegend().setEnabled(false);
        //4 différent sets de données pour les 4 possibilité, mais seulement 2 d'afficher à la fois
        setAltitudeTemps = Graphique.createSetAltitude(true);
        setAltitudeDistance = Graphique.createSetAltitude(false);
        setVitesseTemps = Graphique.createSetVitesse(true);
        setVitesseDistance = Graphique.createSetVitesse(false);

        //Atribue les listes de données pour les bons sets
        setAltitudeTemps.setValues(listAltitudeTemps);
        setAltitudeDistance.setValues(listAltitudeDistance);
        setVitesseTemps.setValues(listVitesseTemps);
        setVitesseDistance.setValues(listVitesseDistance);

        data.addDataSet(setAltitudeTemps);
        data.addDataSet(setVitesseTemps);
        data.addDataSet(setAltitudeDistance);
        data.addDataSet(setVitesseDistance);


        //Vérifie le checkbox du choix d'axe X
        choixAxeX.setOnClickListener(l -> {
            if (!choixAxeX.isChecked())
                unitAxeX(AXE_IS_TEMPS);
            else
                unitAxeX(AXE_IS_DISTANCE);
        });

        unitAxeX(AXE_IS_TEMPS);


        //Rempli les champs de statistique
        formatterDonnees();

    }

    public void unitAxeX(boolean unit) {

        if (unit) {
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
            chart.setVisibleXRange(0f, listAltitudeTemps.get(listAltitudeTemps.size() - 1).getX());
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
            chart.setVisibleXRange(0f, listAltitudeDistance.get(listAltitudeDistance.size() - 1).getX());
        }
        chart.notifyDataSetChanged();
        chart.invalidate();
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
                    PropertyFactory.lineColor(Color.parseColor("#FF5722"))
            ));
        });

        //Centre la caméra sur la première position de l'activité
        mapboxMap.setCameraPosition(new CameraPosition.Builder().target(new LatLng(activite.tabLatitude.get(0), activite.tabLongitude.get(0))).zoom(13).build());
    }


    //Centre la caméra sur la position de l'utilisateur
    public void centrer(View view){
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(activite.tabLatitude.get(0), activite.tabLongitude.get(0)), 13), 1000);
    }


    //Ouvre l'historique lorsque l'utilisateur clique sur back
    public void ouvrirHistorique(View view){
        startActivity(new Intent(ControleurPostActivite.this, ControleurHistorique.class));
    }

    public void remplirListGraph() {

        listAltitudeTemps = new ArrayList<>();
        listAltitudeDistance = new ArrayList<>();
        listVitesseTemps = new ArrayList<>();
        listVitesseDistance = new ArrayList<>();

        float distance = 0f;
        float temps = 0f;

        //Parcours le tableau des temps de l'activité
        for (int i = 0; i < activite.tabTemps.size(); i++) {
            //Calcule les données à mettre pour l'axe X
            distance = (float) activite.calculerDistance(0, i);

            if(temps != (float) ((activite.tabTemps.get(i).getEpochSecond() - activite.tabTemps.get(0).getEpochSecond()) / 60.0)) {

                temps = (float) ((activite.tabTemps.get(i).getEpochSecond() - activite.tabTemps.get(0).getEpochSecond()) / 60.0);

                //Règle la bonne unité de distance pour l'étiquette de l'axe X selon les préférences
                if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour distance", false)) {
                    distance = (float) (distance * METRE_MILES);
                    unitDist = " (mi)";

                } else {
                    distance /= 1000;
                    unitDist = " (km)";
                }

                //Règle l'altitude en fonction du temps et de la distance avec les bonnes unités selon les préférences
                if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour altitude", false)) {
                    listAltitudeTemps.add(new Entry(temps, (float) (activite.tabElevation.get(i) * METRE_PIED)));

                    listAltitudeDistance.add(new Entry(distance, (float) (activite.tabElevation.get(i) * METRE_PIED)));
                } else {
                    listAltitudeTemps.add(new Entry(temps, (activite.tabElevation.get(i)).floatValue()));

                    listAltitudeDistance.add(new Entry(distance, (activite.tabElevation.get(i)).floatValue()));
                }

                double vitesseMetreSec;
            /*if(i > 0 && i < activite.tabVitesse.size() -1)
                vitesseMetreSec = (activite.tabVitesse.get(i - 1) + activite.tabVitesse.get(i + 1))/2.0;
            else*/
                vitesseMetreSec = activite.tabVitesse.get(i);

                //Règle la vitesse en fonction du temps et de la distance avec les bonnes unités selon les préférences
                if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour vitesse", false)) {
                    listVitesseTemps.add(new Entry(temps, (float) (vitesseMetreSec * METRE_MILES * 3600)));

                    listVitesseDistance.add(new Entry(distance, (float) (vitesseMetreSec * METRE_MILES * 3600)));
                } else {
                    listVitesseTemps.add(new Entry(temps, (float) (vitesseMetreSec * 3.6)));

                    listVitesseDistance.add(new Entry(distance, (float) (vitesseMetreSec * 3.6)));
                }

            }


        }


    }


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

        //Affiche la distance selon les préférences
        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour distance", false))
        {
            txtDistance.setText(formatterDistance.format(activite.getDistanceMetrique() * METRE_MILES) + "mi");
        }
        else
        {
            txtDistance.setText(formatterDistance.format(activite.getDistanceMetrique() / 1000) + "km");
        }

        //Affiche les vitesse selon les préférences
        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour vitesse", false))
        {
            txtVitesse.setText("max : " + formatterDistance.format(activite.trouverVitesseMax() * METRE_MILES * 3600) + "mi/h");
            txtVitesseMoyenne.setText("moy : " + formatterDistance.format(activite.getVitesseMoyenne() * METRE_MILES * 3600) + "mi/h");
        }
        else
        {
            txtVitesse.setText("max : " + formatterDistance.format(activite.trouverVitesseMax() * 3.6) + "km/h");
            txtVitesseMoyenne.setText("moy : " + formatterDistance.format(activite.getVitesseMoyenne() * 3.6) + "km/h");
        }

        //Affiche les altitudes selon les préférences
        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour altitude", false)) {
            txtAltitudeMax.setText(formatterHauteur.format(activite.getAltitudeMax() * METRE_PIED) + "'");
            txtAltitudeMin.setText(formatterHauteur.format(activite.getAltitudeMin() * METRE_PIED) + "'");
        } else {
            txtAltitudeMax.setText(formatterHauteur.format(activite.getAltitudeMax()) + "m");
            txtAltitudeMin.setText(formatterHauteur.format(activite.getAltitudeMin()) + "m");
        }

        //Affiche les dénivelé selon les préférences
        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour denivele", false)) {
            txtDenivelePos.setText(formatterHauteur.format(activite.getDenivelePositif() * METRE_PIED) + "'");
            txtDeniveleNeg.setText(formatterHauteur.format(activite.getDeniveleNegatif() * METRE_PIED) + "'");

        } else {
            txtDenivelePos.setText(formatterHauteur.format(activite.getDenivelePositif()) + "m");
            txtDeniveleNeg.setText(formatterHauteur.format(activite.getDeniveleNegatif()) + "m");
        }

        unitAxeX(AXE_IS_TEMPS);
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
