package com.example.teleasis;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PrimaPagina_Pacient extends AppCompatActivity {
    Button citireBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prima_pagina);
        citireBtn=findViewById(R.id.CitireBtn);

        citireBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(PrimaPagina_Pacient.this, CitireDate.class);
                startActivity(myIntent);
            }
        });
    }
}