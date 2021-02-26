package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mapan.R;

import controleur.ControleurHistorique;
import controleur.ControleurParametre;

public class Application extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void afficherParametre(View view){
        startActivity(new Intent(Application.this, ControleurParametre.class));
    }

    public void afficherHistorique(View view){
        startActivity(new Intent(Application.this, ControleurHistorique.class));

    }
}