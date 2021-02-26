package modele;

import org.jdom2.*;

import java.util.ArrayList;

import modele.Activite;

public class Fichiers {

    public void lireFichier() {

    }

    public void ecrireFichier(Activite activite) {
        ArrayList<Double> tabLatitude = activite.getTabLatitude();
        ArrayList<Double> tabLongitude = activite.getTabLongitude();
        ArrayList<Double> tabElevation = activite.getTabElevationMetrique();
        ArrayList<Date> tabTemps = activite.getTabTemps();

        try {
            Element gpx = new Element("gpx");
            Document doc = new Document(gpx);
            doc.setRootElement(gpx);


            Element trace = new Element("trk");
            trace.addContent(new Element("name").setText(activite.getNom()));

            Element segment = new Element("trkseg");

            for (int i = 0; i < tabLatitude.size(); i++) {
                Element point = new Element("trkpt")
                point.setAttribute(new Attribute("lat", tabLatitude.get(i).toString()));
                point.setAttribute(new Attribute("lon", tabLongitude.get(i).toString()));
                point.addContent(new Element("ele").setText(tabElevation.get(i).toString()));
                point.addContent(new Element("time").setText(tabTemps.get(i).toString));

                segment.addContent(point);
            }

        }
    }

}
