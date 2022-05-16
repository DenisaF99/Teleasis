package com.example.teleasis;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PrimaPagina_Pacient extends AppCompatActivity {
    Button citireBtn;
    Button vizualizareBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prima_pagina);
        citireBtn=findViewById(R.id.CitireBtn);
        vizualizareBtn =  findViewById(R.id.vizualizareBtn);

        citireBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(PrimaPagina_Pacient.this, CitireDate.class);
                startActivity(myIntent);
            }
        });

        vizualizareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(PrimaPagina_Pacient.this, VizualizareDatePacient.class);
                startActivity(myIntent);
            }
        });
    }
}