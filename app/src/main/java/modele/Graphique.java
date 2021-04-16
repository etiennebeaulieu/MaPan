package modele;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class Graphique{


    // crée les bases du lineChart: les paramètres nécessaires à l'ajout de données en temps réel, définit les axes x et y
    public static void modifierGraphique(String titre, LineChart chart){
        // enable value highlighting
        chart.setDefaultFocusHighlightEnabled(true);

        // enable touch gesture
        chart.setTouchEnabled(false);

        // enable scaling and dragging
        chart.setDragEnabled(false);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.setDrawGridBackground(false);

        // enable pinch zoom to avoid scaling x and y axis separately
        chart.setPinchZoom(false);

        // background color
        chart.setBackgroundColor(Color.LTGRAY);

        // get legend object
        Legend l = chart.getLegend();

        // customize legend
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);

        //Axe soit temps ou distance selon
        XAxis x1 = chart.getXAxis();
        x1.setTextColor(Color.BLACK);
        x1.setDrawGridLines(false);
        x1.setAvoidFirstLastClipping(true);
        x1.setPosition(XAxis.XAxisPosition.BOTTOM);

        //Axe pour l'altitude. Ça serait bien de trouver comment mettre un nom à l'axe et l'afficher
        YAxis y1 = chart.getAxisLeft();
        y1.setTextColor(Color.BLACK);
        y1.setDrawGridLines(false);


        //Axe pour la vitesse. Ça serait bien de trouver comment mettre un nom à l'axe et l'afficher
        YAxis y12 = chart.getAxisRight();
        y12.setEnabled(true);
        y12.setDrawGridLines(false);
    }

    // crée un point avec les coordonnées reçues et l'ajoute dans le lineChart
    public static void ajouterDonnee(double xValue, double yValue, LineChart chart, String nomSet){
        LineData data = chart.getData();

        if(data != null){
            //Get le bon set selon le paramètre
            LineDataSet set = (LineDataSet) data.getDataSetByLabel(nomSet, true);


            set.addEntry(new Entry((float) xValue,(float) yValue));

            //Update la largeur affiché, les donnée, et l'affichage
            chart.setVisibleXRange(0, (float) xValue);
            chart.notifyDataSetChanged();
            chart.invalidate();
        }
    }

    // crée un lineDataSet pour la méthode ajouterDonnee si le LineData n'en possède pas
    public static LineDataSet createSetAltitude(Boolean isTemps){
        LineDataSet set = new LineDataSet(null, "");
        if(isTemps)
            set.setLabel("setAltitudeTemps");
        else
            set.setLabel("setAltitudeDistance");


        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(0f);
        set.setFillAlpha(100);
        set.setDrawFilled(true);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setValueTextSize(0f);
        set.setDrawCircles(false);

        return set;
    }

    public static LineDataSet createSetVitesse(Boolean isTemps){
        LineDataSet set = new LineDataSet(null, "");
        if(isTemps)
            set.setLabel("setVitesseTemps");
        else
            set.setLabel("setVitesseDistance");


        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set.setColor(Color.RED);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.05f);
        set.setLineWidth(2f);
        set.setFillAlpha(100);
        set.setValueTextSize(0f);
        set.setDrawCircles(false);

        return set;
    }
}
