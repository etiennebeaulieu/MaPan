package service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import java.util.Timer;

public class ServiceStats extends Service
{

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize your service here
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //destroy your service here
    }


    private final Binder binder=new LocalBinder();

    public class LocalBinder extends Binder {
        public ServiceStats getService() {
            return (ServiceStats.this);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public static final String MY_SERVICE_INTENT = "stinfoservices.net.android.MyUniqueItentServiceKey";

    private Intent broadcast = new Intent(MY_SERVICE_INTENT);


    class StatsTask extends AsyncTask<Location, Void, Void>
    {
        @Override
        protected Void doInBackground(Location... locs) {
            // Do something to update your data
            // here is the place where your treatment is done in another thread than in the GUI
            // thread
            // Prevent your listener that something happens


            sendBroadcast(broadcast);
            return (null);
        }

        @Override
        protected void onProgressUpdate(Void... unused) {

        }

        @Override
        protected void onPostExecute(Void unused) {

        }
    }
}
