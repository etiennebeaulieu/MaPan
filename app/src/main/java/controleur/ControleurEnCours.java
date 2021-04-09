package controleur;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mapan.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
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

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import modele.Activite;
import modele.Fichier;
import service.ServiceLocation;
import service.ServiceStats;

public class ControleurEnCours extends AppCompatActivity implements OnMapReadyCallback {

    public static final double METRE_PIED = 3.28084;
    public static final double METRE_MILES = 0.000621371;
    public static Activite activiteEnCours;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private LocationComponent locationComponent;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private long INTERVAL_DEFAUT_MILLIS = 1000;
    private long TEMPS_ATTENTE_DEFAUT = INTERVAL_DEFAUT_MILLIS*5;
    private  FloatingActionButton fabPause;
    private  FloatingActionButton fabEnregistrer;
    private  FloatingActionButton fabSupprimer;
    private boolean fabOuvert;
    private BottomSheetBehavior<View> behavior;
    private PendingIntent locationIntent;
    private ReceveurLocation receveur;
    protected LineString lineString;
    protected GeoJsonSource geoJsonSource;


    @CameraMode.Mode
    private int cameraMode = CameraMode.TRACKING;

    @RenderMode.Mode
    private int renderMode = RenderMode.COMPASS;
    private AccueilLocationCallback callback = new AccueilLocationCallback(this);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activite_en_cours);

        activiteEnCours = (Activite) getIntent().getSerializableExtra("Activité");
        receveur = new ReceveurLocation();
        registerReceiver(receveur, new IntentFilter("DERNIERE_LOCATION"));
        registerReceiver(receveur, new IntentFilter("DERNIERE_STATS"));

        TextView nomActivite = findViewById(R.id.nom_activite);
        ImageView imageSport = findViewById(R.id.icon_activite);
        nomActivite.setText(activiteEnCours.getNom());
        imageSport.setImageResource(activiteEnCours.getSport().getImage());

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
                behavior.setExpandedOffset(bottomSheet.getHeight()-interieur.getHeight());
                behavior.setHalfExpandedRatio(0.4f);
            }
        });




        fabPause = findViewById(R.id.fab_pause);
        fabEnregistrer = findViewById(R.id.fab_enregistrer);
        fabSupprimer = findViewById(R.id.fab_supprimer);
        fabOuvert = false;


        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        //Vérifie si l'application a accès à la localisation ou doit la demander
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapView.getMapAsync(this);
        } else {
            if(Build.VERSION.SDK_INT< 29)
                demanderPermissionLocation();
            else
                demanderPermissionLocation29();
        }


        lancerServiceLocation("ACTION_COMMENCER_SERVICE");




    }

    private void lancerServiceLocation(String action) {
        Intent intent = new Intent(this, ServiceLocation.class);
        intent.setAction(action);

        startService(intent);

    }

    private void demanderPermissionLocation() {
        //Si la localisation a déjà été refusé, un dialogue expliquant pourquoi elle est nécessaire apparait
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this).setTitle("Permission demandée").setMessage("La localisation est nécessaire pour le bon fonctionnement de l'application")
                    .setPositiveButton("Ok", (dialog, which) -> ActivityCompat.requestPermissions(ControleurEnCours.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1))
                    .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void demanderPermissionLocation29(){
        //Si la localisation a déjà été refusé, un dialogue expliquant pourquoi elle est nécessaire apparait
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            new AlertDialog.Builder(this).setTitle("Permission demandée").setMessage("La localisation est nécessaire pour le bon fonctionnement de l'application")
                    .setPositiveButton("Ok", (dialog, which) -> ActivityCompat.requestPermissions(ControleurEnCours.this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 2))
                    .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission autorisée", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission refusée", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission autorisée", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        //Importe notre style de map
        Style.Builder style = new Style.Builder().fromUri("mapbox://styles/etiennebeaulieu2/ckksiyxzv188t18oa1u88mp2c");
        mapboxMap.setStyle(style, style1 ->
        {
            //Paramètre la camera par rapport à la map
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions
                            .builder(ControleurEnCours.this, style1).useDefaultLocationEngine(false).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(cameraMode);
            locationComponent.setRenderMode(renderMode);


            //new LoadGeoJson(ControleurEnCours.this).execute();

            lineString = LineString.fromLngLats(activiteEnCours.listeCoordonnee);
            Feature feature = Feature.fromGeometry(lineString);
            geoJsonSource = new GeoJsonSource("geojson-source", feature);
            style1.addSource(geoJsonSource);

            style1.addLayer(new LineLayer("linelayer", "geojson-source").withProperties(
                    PropertyFactory.lineWidth(4f),
                    PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                    PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                    PropertyFactory.lineSortKey(0f),
                    PropertyFactory.lineColor(Color.parseColor("#FFF44336"))
            ));


            initLocationEngine();
        });
    }

    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(INTERVAL_DEFAUT_MILLIS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(TEMPS_ATTENTE_DEFAUT).build();


        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }
    private static class AccueilLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<ControleurEnCours> activityWeakReference;

        AccueilLocationCallback(ControleurEnCours activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(LocationEngineResult result) {
            ControleurEnCours activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();

                if (location == null) {
                    return;
                }

                if (activity.mapboxMap != null && result.getLastLocation() != null) {
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                    //ControleurEnCours.setTabGPS(activity.mapboxMap.getLocationComponent().getLastKnownLocation());
                    activity.updateTracer();

                }
            }
        }

        @Override
        public void onFailure(@NonNull Exception exception) {
            Log.d("LocationChangeActivity", exception.getLocalizedMessage());
            ControleurEnCours activity = activityWeakReference.get();
            if (activity != null) {
                Toast.makeText(activity, exception.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    private static void setTabGPS(Location location) {
        activiteEnCours.getTabLatitude().add(location.getLatitude());
        activiteEnCours.getTabLongitude().add(location.getLongitude());
        activiteEnCours.getTabElevationMetrique().add(location.getAltitude());
        activiteEnCours.getTabTemps().add(Instant.ofEpochMilli(location.getTime()));
        activiteEnCours.listeCoordonnee.add(Point.fromLngLat(location.getLongitude(), location.getLatitude()));
        if(activiteEnCours.getTabTemps().size()>1)
            activiteEnCours.tabDistanceMetrique.add(activiteEnCours.calculerDistance(activiteEnCours.tabTemps.size()-2, activiteEnCours.tabTemps.size()-1));
        activiteEnCours.tabVitesseMetrique.add((double)location.getSpeed());


    }

    private void updateTracer(){
        lineString = LineString.fromLngLats(activiteEnCours.listeCoordonnee);
        geoJsonSource.setGeoJson(lineString);
    }

    public static  MapboxMap getMap(ControleurEnCours controleur){
        return controleur.mapboxMap;
    }

    //Centre la caméra sur la position de l'utilisateur
    public void centrer(View view){
        Location lastKnownLocation = mapboxMap.getLocationComponent().getLastKnownLocation();

        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 15), 1000);
    }


    public void pause(View view){


        if(!fabOuvert){
            lancerServiceLocation("ACTION_PAUSE_SERVICE");
            locationEngine.removeLocationUpdates(callback);
            fabOuvert = true;

            fabPause.animate().rotation(360);
            fabPause.setImageDrawable(getDrawable(R.drawable.ic_play));
            fabEnregistrer.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
            fabSupprimer.animate().translationY(-getResources().getDimension(R.dimen.standard_105));

            System.out.println(activiteEnCours.calculerDistance(0, activiteEnCours.getTabTemps().size()-1));
        }
        else{
            initLocationEngine();
            lancerServiceLocation("ACTION_COMMENCER_SERVICE");
            fabOuvert = false;

            fabPause.animate().rotation(-360);
            fabPause.setImageDrawable(getDrawable(R.drawable.ic_pause));
            fabSupprimer.animate().translationY(0);
            fabEnregistrer.animate().translationY(0);

        }

    }

    public void enregistrer(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Enregistrer").setMessage("Voulez-vous vraiment enregistrer l'activité?").setPositiveButton("Enregistrer", (dialog, which) ->
        {
            lancerServiceLocation("ACTION_STOP_SERVICE");
            activiteEnCours.setDistanceMetrique(activiteEnCours.calculerDistance(0, activiteEnCours.getTabTemps().size()-1));

            activiteEnCours.setDuree(Duration.between(activiteEnCours.getTabTemps().get(0), activiteEnCours.getTabTemps().get(activiteEnCours.getTabTemps().size()-1)));

            Fichier.enregistrer(this, activiteEnCours);
            startActivity(new Intent(ControleurEnCours.this, ControleurHistorique.class));
        }).setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss()).show();
    }

    public void supprimer(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Supprimer").setMessage("Voulez-vous vraiment supprimer l'activité?").setPositiveButton("Supprimer", (dialog, which) ->
        {
            lancerServiceLocation("ACTION_STOP_SERVICE");
            startActivity(new Intent(ControleurEnCours.this, ControleurAccueil.class));
        }).setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss()).show();
    }

    class ReceveurLocation extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("DERNIERE_LOCATION")) {
                setTabGPS((Location) intent.getExtras().get("Location"));


                Intent intent2 = new Intent(context, ServiceStats.class);
                intent2.setAction("ACTION_CALCULER_STATS");
                context.startService(intent2);

            }
             else if(intent.getAction().equals("DERNIERE_STATS")){
                ControleurEnCours.activiteEnCours.setDistanceMetrique((Double) intent.getExtras().get("Distance"));
                ControleurEnCours.activiteEnCours.setVitesseMetrique((Double) intent.getExtras().get("Vitesse moyenne"));
                ControleurEnCours.activiteEnCours.setVitesseActuelleMetrique((Double) intent.getExtras().get("Vitesse actuelle"));
                ArrayList<Double> denivele = (ArrayList<Double>) intent.getExtras().get("Dénivelé");
                ControleurEnCours.activiteEnCours.setDuree((Duration) intent.getExtras().get("Durée"));
                ControleurEnCours.activiteEnCours.setDenivelePositifMetrique(denivele.get(0));
                ControleurEnCours.activiteEnCours.setDeniveleNegatifMetrique(denivele.get(1));
                ControleurEnCours.activiteEnCours.setAltitudeActuelleMetrique((Double) intent.getExtras().get("Altitude"));
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
        NumberFormat formatterCoord = new DecimalFormat("#0.0000'°'");

        txtDuree.setText(DateTimeFormatter.ofPattern("HH'h'mm'min'").withZone(ZoneId.of("UTC")).format(ControleurEnCours.activiteEnCours.getDuree().addTo(Instant.ofEpochSecond(0))));
        txtLatitude.setText(formatterCoord.format(ControleurEnCours.activiteEnCours.getTabLatitude().get(ControleurEnCours.activiteEnCours.getTabLatitude().size() - 1)));
        txtLongitude.setText(formatterCoord.format(ControleurEnCours.activiteEnCours.getTabLongitude().get(ControleurEnCours.activiteEnCours.getTabLongitude().size() - 1)));
        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour distance", false))
        {
            txtDistance.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getDistanceMetrique() / 1000 * METRE_MILES) + "mi");
        } else
        {
            txtDistance.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getDistanceMetrique() / 1000) + "km");
        }

        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour vitesse", false))
        {
            txtVitesse.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getVitesseActuelleMetrique() * 1000 * METRE_MILES) + "mi/h");
            txtVitesseMoyenne.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getVitesseMetrique() * 1000 * METRE_MILES) + "mi/h");
        } else
        {
            txtVitesse.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getVitesseActuelleMetrique() * 3.6) + "km/h");
            txtVitesseMoyenne.setText(formatterDistance.format(ControleurEnCours.activiteEnCours.getVitesseMetrique()) + "km/h");
        }

        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour altitude", false))
        {
            txtAltitude.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getAltitudeActuelleMetrique() * METRE_PIED) + "'");
        } else
        {
            txtAltitude.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getAltitudeActuelleMetrique()) + "m");
        }

        if (this.getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("impérial pour denivele", false))
        {
            txtDenivelePos.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getDenivelePositifMetrique()) + "'");
            txtDeniveleNeg.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getDeniveleNegatifMetrique()) + "'");

        } else
        {
            txtDenivelePos.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getDenivelePositifMetrique() * METRE_PIED) + "m");
            txtDeniveleNeg.setText(formatterHauteur.format(ControleurEnCours.activiteEnCours.getDeniveleNegatifMetrique() * METRE_PIED) + "m");
        }
    }



    @Override
    public void onBackPressed() {

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
