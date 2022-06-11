package com.example.teleasis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class AlegePacient extends AppCompatActivity {
    DatabaseReference reff;
    private FirebaseAuth mAuth;
    TextView no_data;
    ListView listaPacienti;
    String nume="", prenume="";
    ArrayList<String> lista_nume = new ArrayList<String>();
    ArrayList<String> lista_NumePacient = new ArrayList<String>();
    ArrayList<String> lista_PrenumePacient = new ArrayList<String>();
    ArrayList<String> lista_IdPacient = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alege_pacient);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        no_data = findViewById(R.id.no_data);
        listaPacienti = findViewById(R.id.listInterventii);

        reff = FirebaseDatabase.getInstance().getReference().child("Conturi/Pacienti/");
        ValueEventListener roomsValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean flag = false;
                for (DataSnapshot pacienti : dataSnapshot.getChildren()) {
                    flag = false;
                    if (pacienti != null) {
                        for ( DataSnapshot val : pacienti.getChildren() ) {
                            if(val.getKey().equals("DateDemografice")){
                                for(DataSnapshot d : val.getChildren()){
                                    if(d.getKey().equals("id_ingrijitor")){
                                        if(d.getValue().equals(userId)){
                                            flag = true;
                                        }
                                    }
                                    if(d.getKey().equals("nume_pacient") && flag){
                                        nume = d.getValue(String.class);

                                    }
                                    if(d.getKey().equals("prenume_pacient") && flag){
                                        prenume = d.getValue(String.class);

                                    }
                                    if(!nume.equals("") && !prenume.equals(""))
                                    {
                                        lista_nume.add(nume+","+prenume+","+pacienti.getKey());
                                        nume="";
                                        prenume="";
                                    }
                                }
                            }
                        }
                    }
                }


                Collections.sort(lista_nume);

                for(String c : lista_nume){
                    String[] sp = c.split(",");
                    String nume = sp[0];
                    String prenume = sp[1];
                    String id_pacient = sp[2];
                    lista_NumePacient.add(nume);
                    lista_PrenumePacient.add(prenume);
                    lista_IdPacient.add(id_pacient);

                    AdapterListaPacienti customAdapter = new AdapterListaPacienti(lista_NumePacient,lista_PrenumePacient,lista_IdPacient, getApplicationContext());
                    customAdapter.notifyDataSetChanged();
                    listaPacienti.setAdapter(customAdapter);
                }


                listaPacienti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent myIntent = new Intent(AlegePacient.this, PrimaPagina_Ingrijitor.class);
                        myIntent.putExtra("numePacient",lista_NumePacient.get(position));
                        myIntent.putExtra("prenumePacient",lista_PrenumePacient.get(position));
                        myIntent.putExtra("idPacient",lista_IdPacient.get(position));
                        startActivity(myIntent);
                    }
                });

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };



        reff.addListenerForSingleValueEvent(roomsValueEventListener);






    }
}
