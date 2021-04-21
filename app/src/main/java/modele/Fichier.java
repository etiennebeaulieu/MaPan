package modele;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Fichier {

    private static ArrayList<Activite> listeActivites = new ArrayList<>();


    public static ArrayList<Activite> getListeActivites(){
        return listeActivites;
    }

    public static void enregistrer(Context context, Activite activite) {
        File directory = context.getFilesDir();
        File dossierActivite = new File(directory, "activites");
        if(!dossierActivite.exists())
            dossierActivite.mkdir();

        ArrayList<File> fichiers = new ArrayList<File>(Arrays.asList(dossierActivite.listFiles()));


        String nomFichier = activite.getNom() + ".mp";

        boolean isFini = false;

        int indice = 0;
        while(!isFini) {
            boolean isChange = false;
            for (int i = 0; i<fichiers.size() && !isChange; i++) {
                if (fichiers.get(i).getName().equalsIgnoreCase(nomFichier)) {
                    indice ++;
                    if(nomFichier.endsWith(").mp")){
                        nomFichier = nomFichier.replace("("+ (indice-1) + ")", "("+indice+")");
                        isChange = true;
                    }
                    else {
                        nomFichier = nomFichier.replace(".mp", " (1).mp");
                        isChange = true;
                    }
                }
            }
            if(!isChange)
                isFini = true;
        }
        activite.setNom(nomFichier.replace(".mp", ""));
        File fichier = new File(dossierActivite, nomFichier);





        try{
            FileOutputStream fos = new FileOutputStream(fichier);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(activite);
            oos.close();
            fos.close();
        }catch (IOException io){
            io.printStackTrace();
        }
    }

    public static void loadActivites(Context context){
        File directory = context.getFilesDir();
        File dossierActivite = new File(directory, "activites");
        if(!dossierActivite.exists())
            dossierActivite.mkdir();

        File[] fichiers = dossierActivite.listFiles();

        listeActivites.clear();
        for(int i = 0; i<fichiers.length; i++) {
            Activite activite = null;
            try {
                FileInputStream fis = new FileInputStream(fichiers[i]);
                ObjectInputStream ois = new ObjectInputStream(fis);
                activite = (Activite) ois.readObject();
                ois.close();
                fis.close();
                listeActivites.add(activite);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public static void supprimer(Context context, Activite activite){
        String nom = activite.getNom();
        File directory = context.getFilesDir();
        File dossierActivite = new File(directory, "activites");
        if(!dossierActivite.exists())
            dossierActivite.mkdir();

        new File(dossierActivite, nom + ".mp").delete();

    }

    public static File partager(Context context, Activite activite){
        File fichier = new File(context.getFilesDir(), activite.getNom()+".gpx");
        activite.ecrireFichier(fichier);

        return fichier;
    }

    public static void rafraichir(Context context){
        listeActivites.clear();
        loadActivites(context);

    }
}
