package com.example.teleasis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class RezolvareInterventii_ingrijitor extends AppCompatActivity {
    DatabaseReference reff;
    private FirebaseAuth mAuth;
    TextView no_data;
    ListView listaInterventii;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezolvare_interventii_ingrijitor);
        ArrayList<String> lista_interventii = new ArrayList<String>();
        ArrayList<String> descrieri = new ArrayList<>();
        ArrayList<String> id_uri = new ArrayList<>();


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        listaInterventii = findViewById(R.id.listInterventii);


        reff = FirebaseDatabase.getInstance().getReference().child("Conturi/Ingrijitori/").child(userId).child("/Interventii");

        ValueEventListener roomsValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter = 0;
                String descriere = "",id= "";

                for (DataSnapshot interventii : dataSnapshot.getChildren()) {
                    if (interventii != null) {
                        for ( DataSnapshot val : interventii.getChildren() ) {
                            if(val.getKey().equals("descriere")){
                                descriere = val.getValue().toString();
                            }
                            if(val.getKey().equals("id_interventie")){
                                id = val.getValue().toString();
                            }
                            if(!descriere.equals("") && !id.equals("")){
                                lista_interventii.add(descriere+","+id);
                                counter++;
                                descriere="";
                                id="";

                            }
                        }
                    }
                }
                if (counter == 0) {
                    no_data.setVisibility(View.VISIBLE);
                }
                Collections.sort(lista_interventii);
                for (String interventie_curenta:lista_interventii) {
                    String[] sp = interventie_curenta.split(",");
                    String descriere_int = sp[0];
                    String id_int = sp[1];

                    descrieri.add(descriere_int);
                    id_uri.add(id_int);

                    AdapterListaInterventii customAdapter = new AdapterListaInterventii(descrieri, id_uri, getApplicationContext());
                    customAdapter.notifyDataSetChanged();
                    listaInterventii.setAdapter(customAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        reff.addListenerForSingleValueEvent(roomsValueEventListener);


    }
}