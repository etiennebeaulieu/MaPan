package modele;

import android.graphics.Color;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Graphique {

    //ajouter un point dans le graphique en fonction du temps
    public static void ajouterDonneeTemps(double xValueTemps, double yValue, LineGraphSeries<DataPoint> series){
        series.appendData(new DataPoint(xValueTemps, yValue), true, 100);
    }

    //ajouter un point dans le graphique en fonction de la distance
    public static void ajouterDonneeDistance(double xValueDistance, double yValue, LineGraphSeries<DataPoint> series){
        series.appendData(new DataPoint(xValueDistance, yValue), true, 100);
    }

    public static void setGraphInfo(String titreGraph, String titreX, String titreY, int colorSeries, LineGraphSeries<DataPoint> series, GraphView graphique){
        //couleur de la serie
        series.setColor(colorSeries);
        //titre du graphique
        graphique.setTitle(titreGraph);
        graphique.setTitleTextSize(90);
        //titre des axes
        GridLabelRenderer gridLabel = graphique.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle(titreX);
        gridLabel.setHorizontalAxisTitleTextSize(50);
        gridLabel.setVerticalAxisTitle(titreY);
        gridLabel.setVerticalAxisTitleTextSize(50);

    }
}
