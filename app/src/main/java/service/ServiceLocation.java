package service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

public class ServiceLocation extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       if(intent != null){
           switch (intent.getAction()){
               case "ACTION_COMMENCER_SERVICE" :

                   break;
               case "ACTION_PAUSE_SERVICE":

                   break;
               case "ACTION_STOP_SERVICE":

                   break;
           }
       }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
