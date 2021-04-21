package service;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.mapan.R;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import controleur.ControleurEnCours;


public class ServiceLocation extends Service {

    private static final String CANAL_ID = "canal_tracking";
    private static final String CANAL_NOM = "Tracking";
    private static final int NOTIFICATION_ID = 1;
    private boolean isPremiereExecution = true;
    private AccueilLocationCallback callback = new AccueilLocationCallback();
    private LocationEngine locationEngine;
    private Instant tempsDebut;
    private String duree;

    public static MutableLiveData<Boolean> isEnCours;
    public static MutableLiveData<Instant> temps;
    public static  MutableLiveData<Location> location;



    private void postValeursInitiales(){
        isEnCours.postValue(new Boolean(false));
        temps.postValue(Instant.now());

    }

    @Override
    public void onCreate() {
        super.onCreate();
        tempsDebut = Instant.now();
        isEnCours = new MutableLiveData<>();
        temps = new MutableLiveData<>();
        location = new MutableLiveData<>();
        duree = "";

        postValeursInitiales();
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        isEnCours.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                updateLocation(aBoolean);
            }
        });
        location.observeForever(new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                envoyerLocation(location);
            }
        });


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent != null){
           switch (intent.getAction()){
               case "ACTION_COMMENCER_SERVICE" :
                   if(isPremiereExecution) {
                       commencerService();
                       isPremiereExecution = false;
                   }
                   else{
                        isEnCours.postValue(true);
                   }
                   break;
               case "ACTION_PAUSE_SERVICE":
                    isEnCours.postValue(false);
                   break;
               case "ACTION_STOP_SERVICE":
                    stopSelf();
                   break;
           }
       }

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateLocation(boolean isEnCours){
        LocationEngineRequest request = null;
        if(isEnCours){
            if (Build.VERSION.SDK_INT > 28) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    request = new LocationEngineRequest.Builder(1000)
                            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                            .setMaxWaitTime(5000).build();
                }
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                request = new LocationEngineRequest.Builder(1000)
                        .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                        .setMaxWaitTime(5000).build();

            }
            locationEngine.requestLocationUpdates(request, callback, Looper.getMainLooper());
        }
        else{
            locationEngine.removeLocationUpdates(callback);
        }
    }

    public void envoyerLocation(Location location){
        Intent intent = new Intent();
        intent.setAction("DERNIERE_LOCATION");
        intent.putExtra("Location", location);
        sendBroadcast(intent);

    }

    private static class AccueilLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        @Override
        public void onSuccess(LocationEngineResult result) {
            if(isEnCours.getValue()) {
                Location location = result.getLastLocation();
                if (location != null) {
                    ServiceLocation.location.postValue(location);
                    temps.postValue(Instant.now());


                }
            }
        }

        @Override
        public void onFailure(@NonNull Exception exception) {
            Log.d("LocationChangeActivity", exception.getLocalizedMessage());
        }
    }

    private void creerCanalNotification(NotificationManager notificationManager){
        NotificationChannel canal = new NotificationChannel(CANAL_ID, CANAL_NOM, NotificationManager.IMPORTANCE_LOW);
        notificationManager.createNotificationChannel(canal);


    }

    private void commencerService(){

        isEnCours.postValue(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, ControleurEnCours.class);
        intent.setAction(Intent.ACTION_MAIN);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            creerCanalNotification(notificationManager);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CANAL_ID)
                .setAutoCancel(false)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.icon_mapan_round)
                .setContentTitle("Enregistrement en cours")
                .setContentText("00:00:00")
                .setContentIntent(pendingIntent);

        startForeground(NOTIFICATION_ID, notificationBuilder.build());


        temps.observeForever(new Observer<Instant>() {
            @Override
            public void onChanged(Instant instant) {
                duree = (DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.of("UTC")).format(Duration.between(tempsDebut, instant).addTo(Instant.ofEpochSecond(0))));
                notificationBuilder.setContentText(duree);

                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
