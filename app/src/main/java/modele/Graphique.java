package modele;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

public class Graphique{

    // crée les bases du lineChart: les paramètres nécessaires à l'ajout de données en temps réel, définit les axes x et y
    public static void modifierGaphique(String titre, LineChart chart){
        // enable value highlighting
        chart.setDefaultFocusHighlightEnabled(true);

        // enable touch gesture
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);

        // enable pinch zoom to avoid scaling x and y axis separately
        chart.setPinchZoom(true);

        // background color
        chart.setBackgroundColor(Color.LTGRAY);

        // get legend object
        Legend l = chart.getLegend();

        // customize legend
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);

        XAxis x1 = chart.getXAxis();
        x1.setTextColor(Color.BLACK);
        x1.setDrawGridLines(false);
        x1.setAvoidFirstLastClipping(true);

        YAxis y1 = chart.getAxisLeft();
        y1.setTextColor(Color.BLACK);
        y1.setDrawGridLines(false);

        YAxis y12 = chart.getAxisRight();
        y12.setEnabled(false);
    }

    // crée un point avec les coordonnées reçues et l'ajoute dans le lineChart
    public static void ajouterDonnee(double xValue, double yValue, LineDataSet set, LineChart chart, LineData data){
        data = chart.getData();

        if(data != null){
            set = (LineDataSet) data.getDataSetByIndex(0);

            if(set == null){
                //creation if null
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry((float) xValue, (float) yValue), 0);

            chart.notifyDataSetChanged();

            chart.setVisibleXRangeMaximum(10f);
        }
    }

    // crée un lineDataSet pour la méthode ajouterDonnee si le LineData n'en possède pas
    private static LineDataSet createSet(){
        LineDataSet set = new LineDataSet(null, "texte");
        set.setCubicIntensity(0.2f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(2f);
        set.setCircleSize(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(10f);

        return set;
    }

    /*public static void creerData(List<Entry> entries, LineDataSet dataSet, String titre, Color color){
        dataSet = new LineDataSet(entries, titre);
        dataSet.setColor(color.toArgb());
    }*/


    /*public static void ajouterDataInChart(LineDataSet dataSet, LineData lineData, LineChart chart){
        lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); //refresh
        chart.notifyDataSetChanged();
    }*/

    /*private static LineGraphSeries<DataPoint> series = null;

    public static void creerSerie(){
        series = new LineGraphSeries<DataPoint>();
    }

    //ajouter un point dans le graphique (gérer les exceptions avec des if{} else{} plus tard
    public static void ajouterDonnee(double xValue, double yValue, LineGraphSeries<DataPoint> series, GraphView graphique){
        series.appendData(new DataPoint(xValue, yValue), true, 500);
    }

    //afficher la donnée du graphique (ses coordonnées x et y)
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

    //update les informations du graphique dont le titre, les titres des axes, la couleur de la série.
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
    }*/

    // *** méthodes pour controleur stats ***

    /*private LineChart chart = null;
    private LineDataSet set = null;
    private LineData data = null;*/

    /*protected void onCreate(Bundle savedInstanceState){
        chart = findViewById(R.id.chart);
        initierGraphique();
    }*/

    /*public void initierGraphique(){

        double random = (Math.random() *75);

        data = new LineData();

        onResume(random, data);
    }*/

    /*protected void onResume(double nbr, LineData data){
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i = 0; i < 20; i++){
                    int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Graphique.ajouterDonnee((float) finalI, (float) nbr + 60f, set, chart, data);
                        }
                    });

                    try{
                        Thread.sleep(600);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }*/
}
