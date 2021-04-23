package service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import java.time.Duration;
import java.util.ArrayList;

import controleur.ControleurNouvelleActivite;

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
        double d = 0;
        for(double dx: ControleurNouvelleActivite.activiteEnCours.tabDistance){
            d+=dx;
        }
        distance.setValue(d);
        vitesseMoyenne.setValue(ControleurNouvelleActivite.activiteEnCours.calculerVitesseMoyenne());
        vitesseActuelle.setValue(ControleurNouvelleActivite.activiteEnCours.tabVitesse.get(ControleurNouvelleActivite.activiteEnCours.tabVitesse.size()-1));
            duree.setValue(Duration.between(ControleurNouvelleActivite.activiteEnCours.getTabTemps().get(0), ControleurNouvelleActivite.activiteEnCours.getTabTemps().get(ControleurNouvelleActivite.activiteEnCours.getTabTemps().size() - 1)));
        calculerDenivele();
        denivele.setValue(deniveleList);
        altitude.setValue(ControleurNouvelleActivite.activiteEnCours.getTabElevation().get(ControleurNouvelleActivite.activiteEnCours.getTabTemps().size() - 1));
    }

    public void calculerDenivele() {

        double montee = 0;
        double descente = 0;

        for (int i = 0; i < ControleurNouvelleActivite.activiteEnCours.tabElevation.size() - 1; i++) {
            if (ControleurNouvelleActivite.activiteEnCours.tabElevation.get(i) < ControleurNouvelleActivite.activiteEnCours.tabElevation.get(i + 1)) {
                montee += ControleurNouvelleActivite.activiteEnCours.tabElevation.get(i + 1) - ControleurNouvelleActivite.activiteEnCours.tabElevation.get(i);
            } else if (ControleurNouvelleActivite.activiteEnCours.tabElevation.get(i) > ControleurNouvelleActivite.activiteEnCours.tabElevation.get(i + 1)) {
                descente += ControleurNouvelleActivite.activiteEnCours.tabElevation.get(i) - ControleurNouvelleActivite.activiteEnCours.tabElevation.get(i + 1);
            }
        }
        System.out.println(montee);
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
