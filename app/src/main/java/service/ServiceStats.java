package service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Timer;

import controleur.ControleurEnCours;
import controleur.ControleurStats;

public class ServiceStats extends Service {
    public static MutableLiveData<Double> distance;
    public static MutableLiveData<Double> vitesseMoyenne;
    public static MutableLiveData<Double> vitesseActuelle;
    public static MutableLiveData<Duration> duree;
    public static MutableLiveData<ArrayList<Double>> denivele;
    public static MutableLiveData<Double> altitude;
    ArrayList<Double> deniveleList;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        distance = new MutableLiveData<>();
        vitesseMoyenne = new MutableLiveData<>();
        vitesseActuelle = new MutableLiveData<>();
        duree = new MutableLiveData<>();
        denivele = new MutableLiveData<>();
        altitude = new MutableLiveData<>();
        deniveleList = new ArrayList<>();
        if (intent != null && intent.getAction().equals("ACTION_CALCULER_STATS")) {
            instancierValeur();
            envoyerStats();
        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    public void instancierValeur() {
        distance.setValue(ControleurEnCours.activiteEnCours.calculerDistance(0, ControleurEnCours.activiteEnCours.getTabTemps().size() - 1));
        vitesseMoyenne.setValue(ControleurEnCours.activiteEnCours.calculerVitesseMoyenne());
        vitesseActuelle.setValue(ControleurEnCours.activiteEnCours.calculerVitesse(ControleurEnCours.activiteEnCours.getTabTemps().size() - 2, ControleurEnCours.activiteEnCours.getTabTemps().size() - 1));
        duree.setValue(Duration.between(ControleurEnCours.activiteEnCours.getTabTemps().get(ControleurEnCours.activiteEnCours.getTabTemps().size() - 1),
                ControleurEnCours.activiteEnCours.getTabTemps().get(0)));
        calculerDenivele();
        denivele.setValue(deniveleList);
        altitude.setValue(ControleurEnCours.activiteEnCours.getTabElevationMetrique().get(ControleurEnCours.activiteEnCours.getTabTemps().size() - 1));
    }

    public void calculerDenivele() {

        double montee = 0;
        double descente = 0;

        for (int i = 0; i < ControleurEnCours.activiteEnCours.tabElevationMetrique.size() - 1; i++) {
            if (ControleurEnCours.activiteEnCours.tabElevationMetrique.get(i) < ControleurEnCours.activiteEnCours.tabElevationMetrique.get(i + 1)) {
                montee += ControleurEnCours.activiteEnCours.tabElevationMetrique.get(i + 1) - ControleurEnCours.activiteEnCours.tabElevationMetrique.get(i);
            } else if (ControleurEnCours.activiteEnCours.tabElevationMetrique.get(i) > ControleurEnCours.activiteEnCours.tabElevationMetrique.get(i + 1)) {
                descente += ControleurEnCours.activiteEnCours.tabElevationMetrique.get(i) - ControleurEnCours.activiteEnCours.tabElevationMetrique.get(i + 1);
            }
        }
        deniveleList.add(0, montee);
        deniveleList.add(1, descente);
    }

    private void envoyerStats(){
        Intent intent = new Intent();
        intent.setAction("DERNIERE_STATS");
        intent.putExtra("Distance", distance.getValue());
        intent.putExtra("Vitesse moyenne", vitesseMoyenne.getValue());
        intent.putExtra("Vitesse actuelle", vitesseActuelle.getValue());
        intent.putExtra("Durée", duree.getValue());
        intent.putExtra("Dénivelé", denivele.getValue());
        intent.putExtra("Altitude", altitude.getValue());
        sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
