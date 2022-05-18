package com.example.teleasis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PrimaPagina_Ingrijitor extends AppCompatActivity {
    Button introducereBtn;
    Button interventiiBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prima_pagina_ingrijitor);
        introducereBtn = findViewById(R.id.IntroducereDateBtn);
        interventiiBtn = findViewById(R.id.RezInterventiiBtn);

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
                startActivity(myIntent);
            }
        });
    }
}