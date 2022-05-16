package com.example.teleasis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RezolvareInterventii_ingrijitor extends AppCompatActivity {
    Button rezolvareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezolvare_interventii_ingrijitor);
        rezolvareBtn = findViewById(R.id.rezolvaInterventieBtn);

        rezolvareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(RezolvareInterventii_ingrijitor.this, Interventie.class);
                startActivity(myIntent);
            }
        });
    }
}