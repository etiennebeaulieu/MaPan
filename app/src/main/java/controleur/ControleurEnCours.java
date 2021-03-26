package controleur;

        import android.Manifest;
        import android.annotation.SuppressLint;
        import android.app.AlertDialog;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.location.Location;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;

        import com.example.mapan.R;
        import com.mapbox.android.core.location.LocationEngine;
        import com.mapbox.android.core.location.LocationEngineCallback;
        import com.mapbox.android.core.location.LocationEngineProvider;
        import com.mapbox.android.core.location.LocationEngineRequest;
        import com.mapbox.android.core.location.LocationEngineResult;
        import com.mapbox.android.core.permissions.PermissionsListener;
        import com.mapbox.android.core.permissions.PermissionsManager;
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

        import java.lang.ref.WeakReference;
        import java.time.Instant;
        import java.util.ArrayList;
        import java.util.List;

        import modele.Activite;

public class ControleurEnCours extends AppCompatActivity implements OnMapReadyCallback {

    private static Activite activiteEnCours;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private LocationComponent locationComponent;
    private Location lastLocation;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private long INTERVAL_DEFAUT_MILLIS = 1000;
    private long TEMPS_ATTENTE_DEFAUT = INTERVAL_DEFAUT_MILLIS*5;

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

        TextView nomActivite = findViewById(R.id.nom_activite);
        ImageView imageSport = findViewById(R.id.icon_activite);

        nomActivite.setText(activiteEnCours.getNom());
        imageSport.setImageResource(activiteEnCours.getSport().getImage());


        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        //Vérifie si l'application a accès à la localisation ou doit la demander
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapView.getMapAsync(this);
        } else {
            demanderPermissionLocation();
        }
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

    public Location getLastLocation(){
        return lastLocation;
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
                    ControleurEnCours.setTabGPS(activity.mapboxMap.getLocationComponent().getLastKnownLocation());


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
    }

    //Centre la caméra sur la position de l'utilisateur
    public void centrer(View view){
        Location lastKnownLocation = mapboxMap.getLocationComponent().getLastKnownLocation();

        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 15), 1000);
    }


    public void afficherParametre(View view) {
        startActivity(new Intent(this, ControleurParametre.class));
    }

    public void afficherHistorique(View view) {
        startActivity(new Intent(this, ControleurHistorique.class));

    }

    public void afficherNouvelleActivite(View view) {
        startActivity(new Intent(this, ControleurNouvelleActivite.class));
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
