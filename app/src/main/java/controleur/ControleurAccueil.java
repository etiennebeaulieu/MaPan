package controleur;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Camera;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.Application;
import com.example.mapan.R;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.OnLocationClickListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

public class ControleurAccueil extends AppCompatActivity implements OnMapReadyCallback{

    private MapboxMap mapboxMap;
    private MapView mapView;
    private LocationComponent locationComponent;
    private Location lastLocation;
    private PermissionsManager permissionsManager;

   @CameraMode.Mode
   private int cameraMode = CameraMode.TRACKING;

   @RenderMode.Mode
   private int renderMode = RenderMode.NORMAL;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.accueil);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        if(PermissionsManager.areLocationPermissionsGranted(this)){
            mapView.getMapAsync(this);
        }
        else{
            permissionsManager = new PermissionsManager(new PermissionsListener() {
                @Override
                public void onExplanationNeeded(List<String> permissionsToExplain) {
                    Toast.makeText(ControleurAccueil.this, "La localisation est nécessaire au fonctionnement de l'application", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPermissionResult(boolean granted) {
                    if(granted){
                        mapView.getMapAsync( ControleurAccueil.this);
                    }else{
                        finish();
                    }
                }
            });
            permissionsManager.requestLocationPermissions(this);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        Style.Builder style = new Style.Builder().fromUri("mapbox://styles/etiennebeaulieu2/ckksiyxzv188t18oa1u88mp2c");
        mapboxMap.setStyle(style, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                locationComponent = mapboxMap.getLocationComponent();
                locationComponent.activateLocationComponent(
                        LocationComponentActivationOptions
                                .builder(ControleurAccueil.this, style).build());
                locationComponent.setLocationComponentEnabled(true);
                locationComponent.setCameraMode(cameraMode);
                locationComponent.setRenderMode(renderMode);
            }
        });
    }

    public void afficherParametre(View view){
        startActivity(new Intent(this, ControleurParametre.class));
    }

    public void afficherHistorique(View view){
        startActivity(new Intent(this, ControleurHistorique.class));

    }

    public void afficherNouvelleActivite(View view){
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
