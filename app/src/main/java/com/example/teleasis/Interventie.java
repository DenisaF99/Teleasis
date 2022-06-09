package com.example.teleasis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Interventie extends AppCompatActivity {
    DatabaseReference reff = FirebaseDatabase.getInstance().getReference();;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interventie);

        String stadiu = getIntent().getStringExtra("stadiu");
        String tip = getIntent().getStringExtra("tip");
        String descriere = getIntent().getStringExtra("descriere");
        String idInterventie = getIntent().getStringExtra("idInterventie");
        Button rezolvareInterventieBtn;
        TextView descriereId, stadiuId, tipId;
        EditText editTextId;

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        descriereId = findViewById(R.id.descriereId);
        stadiuId = findViewById(R.id.stadiuId);
        tipId = findViewById(R.id.tipId);
        rezolvareInterventieBtn = findViewById(R.id.rezolvareInterventieBtn);
        editTextId = findViewById(R.id.editTextId);


        descriereId.setText(descriereId.getText() + " " +  descriere);
        stadiuId.setText(stadiuId.getText() + " " +  stadiu);
        tipId.setText(tipId.getText() + " " +  tip);

        rezolvareInterventieBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());

                reff.child("Conturi").child("Ingrijitori").child(userId).child("Interventii").child(idInterventie).child("observatii").setValue(editTextId.getText().toString());
                reff.child("Conturi").child("Ingrijitori").child(userId).child("Interventii").child(idInterventie).child("data_rezolvata").setValue(timeStamp);
                reff.child("Conturi").child("Ingrijitori").child(userId).child("Interventii").child(idInterventie).child("stadiu").setValue("Completat");

                Intent myIntent = new Intent(Interventie.this, RezolvareInterventii_ingrijitor.class);
                startActivity(myIntent);
            }
        });
    }
}