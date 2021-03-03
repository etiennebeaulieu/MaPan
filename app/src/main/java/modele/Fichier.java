package modele;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

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

        String nomFichier = activite.getNom() + ".mp";
        File fichier = new File(dossierActivite, nomFichier);




        try{
            //FileOutputStream fos = this.getApplicationContext().openFileOutput(nomFichier, Context.MODE_PRIVATE);
            FileOutputStream fos = new FileOutputStream(fichier);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(activite);
            oos.close();
            fos.close();
            Toast.makeText(context, activite.getNom() + " enregistré", Toast.LENGTH_SHORT).show();
        }catch (IOException io){
            io.printStackTrace();
        }
    }

    public static void loadActivites(Context context){
        //ArrayList<Activite> listeActivites = new ArrayList<>();
        File directory = context.getFilesDir();
        File dossierActivite = new File(directory, "activites");
        if(!dossierActivite.exists())
            dossierActivite.mkdir();

        File[] fichiers = dossierActivite.listFiles();
        //String[] fichiers = this.getApplicationContext().fileList();

        listeActivites.clear();
        for(int i = 0; i<fichiers.length; i++) {
            Activite activite = null;
            try {
                //FileInputStream fis = this.getApplicationContext().openFileInput(fichiers[i]);
                //FileInputStream fis = new FileInputStream(new File(this.getApplicationContext().getFilesDir(),fichiers[i]));
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
        if(new File(dossierActivite, nom + ".mp").delete()){
            Toast.makeText(context, nom +" supprimée", Toast.LENGTH_SHORT).show();
        }
    }

    public static void partager(Context context, Activite activite){
        File fichier = new File(context.getFilesDir(), activite.getNom()+".gpx");
        activite.ecrireFichier(fichier);
    }

    public static void rafraichir(Context context){
        listeActivites.clear();
        loadActivites(context);

    }
}
