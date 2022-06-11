package com.example.teleasis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PrimaPagina_Ingrijitor extends AppCompatActivity {
    Button introducereBtn;
    Button interventiiBtn;
    TextView numeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prima_pagina_ingrijitor);
        introducereBtn = findViewById(R.id.IntroducereDateBtn);
        interventiiBtn = findViewById(R.id.RezInterventiiBtn);
        numeTextView = findViewById(R.id.numePacient);
        String nume = getIntent().getStringExtra("numePacient");
        String prenume = getIntent().getStringExtra("prenumePacient");
        String id_uri = getIntent().getStringExtra("idPacient");
        numeTextView.setText(numeTextView.getText()+nume+" "+prenume);

        introducereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(PrimaPagina_Ingrijitor.this, IntroducereDate_ingrijitor.class);
                startActivity(myIntent);
            }
        });

        interventiiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(PrimaPagina_Ingrijitor.this, RezolvareInterventii_ingrijitor.class);
                myIntent.putExtra("idPacient",id_uri);
                startActivity(myIntent);
            }
        });
    }
}