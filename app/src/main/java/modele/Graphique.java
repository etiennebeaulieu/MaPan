package modele;

import android.graphics.Color;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import controleur.ControleurStats;

public class Graphique {

    //ajouter un point dans le graphique (gérer les exceptions avec des if{} else{} plus tard
    public static void ajouterDonnee(double xValue, double yValue, LineGraphSeries<DataPoint> series, GraphView graphique){
        series.appendData(new DataPoint(xValue, yValue), true, 500);
    }

    public static void onClickPoint(LineGraphSeries<DataPoint> series, ControleurStats controleur){
        if(!series.equals(null) && !series.isEmpty()) {
            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(controleur, series.getTitle()+ dataPoint, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void setGraphInfo(String titreGraph, String titreX, String titreY, int colorSeries, LineGraphSeries<DataPoint> series, GraphView graphique){
        //couleur de la serie
        series.setColor(colorSeries);
        //series.isDrawDataPoints();
        //titre du graphique
        graphique.setTitle(titreGraph);
        graphique.setTitleTextSize(90);
        //titre des axes
        GridLabelRenderer gridLabel = graphique.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle(titreX);
        gridLabel.setHorizontalAxisTitleTextSize(50);
        gridLabel.setVerticalAxisTitle(titreY);
        gridLabel.setVerticalAxisTitleTextSize(50);

        //setManual X bounds
        graphique.getViewport().setXAxisBoundsManual(true);
        graphique.getViewport().setMinX(0.5);
        graphique.getViewport().setMaxX(3.5);

        //set manual Y bounds
        graphique.getViewport().setYAxisBoundsManual(true);
        graphique.getViewport().setMinY(3.5);
        graphique.getViewport().setMaxY(8);
    }
}
