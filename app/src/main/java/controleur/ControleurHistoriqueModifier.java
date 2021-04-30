package controleur;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.mapan.R;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

import java.io.File;
import java.util.ArrayList;

import modele.Activite;
import modele.ActiviteAdapter;
import modele.Fichier;
import modele.Sport;

public class ControleurHistoriqueModifier extends AppCompatActivity implements PickiTCallbacks {

    //Affichage des activité
    private ListView modifier_list;

    //Affichage personalisé pour ListView
    private ActiviteAdapter adapter;

    //L'activité qui est sélectionné dans la liste
    private Activite activiteSelect;
    private static final int CHOISIR_GPX = 2;
    private static final int   PARTAGER_GPX = 3;
    PickiT pickiT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historique_modifier);

        modifier_list = findViewById(R.id.modifier_list);
        activiteSelect = null;
        pickiT = new PickiT(this, this, this);

        adapter = new ActiviteAdapter(this, R.layout.list_row, Fichier.getListeActivites());
        modifier_list.setAdapter(adapter);

        modifier_list.setOnItemClickListener((parent, view, position, id) -> activiteSelect = (Activite) modifier_list.getItemAtPosition(position));

        Fichier.rafraichir(this.getApplicationContext());
        adapter.notifyDataSetChanged();



        //Action a faire si une autre application envoie un fichier GPX à MaPan
        Intent intent = getIntent();
        if(intent != null){
            String action = intent.getAction();
            String type = intent.getType();

            if((Intent.ACTION_SEND.equals(action)|| Intent.ACTION_VIEW.equals(action))&& type != null){
                if(type.equalsIgnoreCase("application/gpx+xml")){
                    Uri fichierGPX = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                    if(fichierGPX != null){
                        pickiT.getPath(fichierGPX, Build.VERSION.SDK_INT);
                    }
                }
            }
            else if(Intent.ACTION_SEND_MULTIPLE.equals(action)&&type!=null){
                if(type.equalsIgnoreCase("application/gpx+xml")){
                    ArrayList<Uri> listGPX = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                    if(listGPX.get(0) != null){
                        pickiT.getPath(listGPX.get(0), Build.VERSION.SDK_INT);
                    }
                }
            }
        }
    }


    public void ouvrirAccueil(View view) {
        startActivity(new Intent(ControleurHistoriqueModifier.this, ControleurAccueil.class));
    }

    public void ouvrirHistorique(View view) {
        startActivity(new Intent(ControleurHistoriqueModifier.this, ControleurHistorique.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ControleurHistoriqueModifier.this, ControleurHistorique.class));
    }

    public void exporterGPX(View view) {

        if(activiteSelect != null) {
            //Vérifie si l'activité a des données GPS
            if(activiteSelect.getTabLatitude() != null) {

                File fichier = Fichier.partager(this.getApplicationContext(), activiteSelect);

                Uri path = FileProvider.getUriForFile(this, "controleur", fichier);

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, path);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.setType("application/gpx+xml");
                startActivityForResult(Intent.createChooser(shareIntent, "Partager..."), PARTAGER_GPX);
            }
            else{
                Toast.makeText(this, "Le fichier doit avoir des données de localisation", Toast.LENGTH_SHORT).show();
            }
            modifier_list.clearChoices();
            adapter.notifyDataSetChanged();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void importerGPX(View view) {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent loadIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            loadIntent.setType("application/*");
            startActivityForResult(loadIntent, CHOISIR_GPX);
        } else {
            demanderPermissionStockage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            Context context = this.getApplicationContext();
        if (requestCode == CHOISIR_GPX && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                pickiT.getPath(uri, Build.VERSION.SDK_INT);
            }
        }
        if(requestCode == PARTAGER_GPX){
            new File(this.getFilesDir(), activiteSelect.getNom()+".gpx").delete();
        }
    }

    private void demanderPermissionStockage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this).setTitle("Permission demandée").setMessage("La permission est nécessaire pour avoir accès aux fichiers")
                    .setPositiveButton("Ok", (dialog, which) -> ActivityCompat.requestPermissions(ControleurHistoriqueModifier.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1))
                    .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission autorisée", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void deleteActivity(View view) {
        if (activiteSelect != null) {
            Fichier.supprimer(this.getApplicationContext(), activiteSelect);
            Fichier.rafraichir(this.getApplicationContext());
            modifier_list.clearChoices();
            adapter.notifyDataSetChanged();
        }
    }

    public void renommer(View view){
        if(activiteSelect != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setTitle("Renommer l'activité").setMessage("Entrez le nouveau nom").setPositiveButton("Confirmer", (dialog, which) ->
            {
                Fichier.supprimer(ControleurHistoriqueModifier.this, activiteSelect);
                activiteSelect.setNom(input.getText().toString());
                Fichier.enregistrer(ControleurHistoriqueModifier.this, activiteSelect);
                Fichier.rafraichir(ControleurHistoriqueModifier.this);
                modifier_list.clearChoices();
                adapter.notifyDataSetChanged();
            }).setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss()).show();
        }
    }

    public void changerSport(View view){
        if(activiteSelect != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            Spinner spinner = new Spinner(this);
            ArrayAdapter aa2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Sport.values());
            aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(aa2);
            builder.setTitle("Changer le type d'activité").setMessage("Sélectionner le type d'activité")
                    .setPositiveButton("Confirmer", (dialog, which) ->
                    {
                        Fichier.supprimer(ControleurHistoriqueModifier.this, activiteSelect);
                        activiteSelect.setSport(Sport.valueOf(spinner.getSelectedItem().toString()));
                        Fichier.enregistrer(ControleurHistoriqueModifier.this, activiteSelect);
                        Fichier.rafraichir(ControleurHistoriqueModifier.this);
                        modifier_list.clearChoices();
                        adapter.notifyDataSetChanged();
                    }).setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss()).setView(spinner).show();
        }
    }


    @Override
    public void PickiTonUriReturned() {

    }

    @Override
    public void PickiTonStartListener() {

    }

    @Override
    public void PickiTonProgressUpdate(int progress) {

    }

    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {
      Context context= this.getApplicationContext();
        File fichier = new File(path);
        if(fichier.exists()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            Spinner spinner = new Spinner(this);
            ArrayAdapter aa2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Sport.values());
            aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(aa2);
            LinearLayout vBox = new LinearLayout(this);
            vBox.setOrientation(LinearLayout.VERTICAL);
            vBox.setDividerPadding(15);
            vBox.addView(input);
            vBox.addView(spinner);
            builder.setView(vBox);
            builder.setTitle("Définir l'activité").setMessage("Entrez le nouveau nom et le sport").setPositiveButton("Confirmer", (dialog, which) ->
            {
                if(!input.getText().toString().isEmpty()) {
                    Activite importation = new Activite(input.getText().toString(), Sport.valueOf(spinner.getSelectedItem().toString()), fichier);
                    Fichier.enregistrer(context, importation);
                    Fichier.rafraichir(context);
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(context, "L'activité doit avoir un nom", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss()).show();
        }
    }
}
