package com.example.teleasis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CitireDate extends AppCompatActivity {
    Button pulsBtn;
    Button connectBluetooth;
    Button tempBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citire_date);
        pulsBtn = findViewById(R.id.pulsBtn);
        connectBluetooth =  findViewById(R.id.connectBluetoothBtn);
        tempBtn = findViewById(R.id.temperaturaBtn);

        pulsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CitireDate.this, PreluarePuls.class);
                startActivity(myIntent);
            }
        });

       tempBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent myIntent = new Intent(CitireDate.this, PreluareTemperatura.class);
               startActivity(myIntent);
           }
       });
    }
}