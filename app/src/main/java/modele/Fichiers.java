package modele;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Fichiers {

    public static ArrayList<ArrayList<?>> lireFichier(File fichier, ArrayList<Double> tabLat, ArrayList<Double> tabLon, ArrayList<Double> tabEle, ArrayList<Instant> tabTime) {
        ArrayList<ArrayList<?>> tabAtt = null;

        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File("c:\\file.xml");

        if (fichier != null) {
            tabAtt = new ArrayList<ArrayList<?>>();

            try {

                Document document = (Document) builder.build(xmlFile);
                Element rootNode = document.getRootElement();
                List list = rootNode.getChildren("trkpt");

                for (int i = 0; i < list.size(); i++) {

                    Element node = (Element) list.get(i);


                    Double lat = Double.parseDouble(node.getAttributeValue("lat"));
                    tabLat.add(lat);

                    Double lon = Double.parseDouble(node.getAttributeValue("lon"));
                    tabLon.add(lon);


                    Double ele = Double.parseDouble(node.getAttributeValue("ele"));
                    tabEle.add(ele);

                    Instant time = Instant.parse(node.getAttributeValue("time"));
                    tabTime.add(time);

                }
                tabAtt.add(tabLat);
                tabAtt.add(tabLon);
                tabAtt.add(tabEle);
                tabAtt.add(tabTime);

            } catch (IOException | JDOMException io) {
                System.out.println(io.getMessage());
            }
        }


        return tabAtt;
    }


    public static void ecrireFichier(Activite activite, File fichier) {
        ArrayList<Double> tabLatitude = activite.getTabLatitude();
        ArrayList<Double> tabLongitude = activite.getTabLongitude();
        ArrayList<Double> tabElevation = activite.getTabElevationMetrique();
        ArrayList<Instant> tabTemps = activite.getTabTemps();

        try {
            Element gpx = new Element("gpx");
            Document doc = new Document(gpx);
            doc.setRootElement(gpx);


            Element trace = new Element("trk");
            trace.addContent(new Element("name").setText(activite.getNom()));

            Element segment = new Element("trkseg");

            for (int i = 0; i < tabLatitude.size(); i++) {
                Element point = new Element("trkpt");
                point.setAttribute(new Attribute("lat", tabLatitude.get(i).toString()));
                point.setAttribute(new Attribute("lon", tabLongitude.get(i).toString()));
                point.addContent(new Element("ele").setText(tabElevation.get(i).toString()));
                point.addContent(new Element("time").setText(tabTemps.get(i).toString()));

                segment.addContent(point);
            }

            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, new FileWriter(fichier));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
