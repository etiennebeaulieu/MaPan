package service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.time.Duration;
import java.util.Timer;

import controleur.ControleurEnCours;
import controleur.ControleurStats;

public class ServiceStats extends Service
{
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        ControleurEnCours.activiteEnCours.setDistanceMetrique(ControleurEnCours.activiteEnCours.calculerDistance(0,ControleurEnCours.activiteEnCours.getTabTemps().size()-1));
        ControleurEnCours.activiteEnCours.setVitesseMetrique( ControleurEnCours.activiteEnCours.calculerVitesseMoyenne());
        ControleurEnCours.activiteEnCours.setVitesseActuelleMetrique(  ControleurEnCours.activiteEnCours.calculerVitesse(ControleurEnCours.activiteEnCours.getTabTemps().size()-2,ControleurEnCours.activiteEnCours.getTabTemps().size()-1));
        ControleurEnCours.activiteEnCours.setDuree(Duration.between(ControleurEnCours.activiteEnCours.getTabTemps().get(ControleurEnCours.activiteEnCours.getTabTemps().size()-1),
                ControleurEnCours.activiteEnCours.getTabTemps().get(0)));
        ControleurEnCours.activiteEnCours.calculerDenivele();
        ControleurStats.formatterDonnees();
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
