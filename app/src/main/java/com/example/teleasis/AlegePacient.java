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
    DatabaseReference reffName;
    private FirebaseAuth mAuth;
    TextView no_data;
    ListView listaPacienti;
    Button alegePacient;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alege_pacient);
        ArrayList<String> lista_Id = new ArrayList<String>();
        ArrayList<String> lista_pacienti = new ArrayList<String>();
        ArrayList<String> id_uri = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        no_data = findViewById(R.id.no_data);
        listaPacienti = findViewById(R.id.listInterventii);
        String numePacient="";


        reff = FirebaseDatabase.getInstance().getReference().child("Conturi/Ingrijitori/").child(userId).child("/Interventii");
        ValueEventListener roomsValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter = 0;
                String  id="";
                for (DataSnapshot interventii : dataSnapshot.getChildren()) {
                    if (interventii != null) {
                        for ( DataSnapshot val : interventii.getChildren() ) {
                            if(val.getKey().equals("id_pacient")){
                                id = val.getValue().toString();
                            }
                            if(!id.equals("") ){
                                if (!lista_Id.contains(id))
                                {
                                   reffName = FirebaseDatabase.getInstance().getReference().child("Conturi/Pacient/").child(id).child("/DateDemografice");

                                    Log.d("reff", String.valueOf(reffName));
                                   /* for (DataSnapshot numaPacineti : dataSnapshot.getChildren()) {
                                    for ( DataSnapshot nume : numaPacineti.getChildren() ) {
                                        if(nume.getKey().equals("nume_pacient")){
                                            numePacient = val.getValue().toString();
                                        }; */
                                    lista_Id.add(id);
                                    //lista_pacienti.add();
                                //Log.d("id", "added");
                                //Log.d("id", id);
                                counter++;
                                id=""; }

                            }
                        }
                    }
                }
                Collections.sort(lista_Id);


                AdapterListaPacienti customAdapter = new AdapterListaPacienti(lista_Id, getApplicationContext());
                customAdapter.notifyDataSetChanged();
                listaPacienti.setAdapter(customAdapter);

                listaPacienti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("lista",lista_Id.get(position));
                        Intent myIntent = new Intent(AlegePacient.this, PrimaPagina_Ingrijitor.class);
                        myIntent.putExtra("idPacient",lista_Id.get(position));
                        startActivity(myIntent);
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        reff.addListenerForSingleValueEvent(roomsValueEventListener);



       /* alegePacient = findViewById(R.id.alegePacientBtn);

        alegePacient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(AlegePacient.this, PrimaPagina_Ingrijitor.class);
                startActivity(myIntent);
            }
        }); */


    }
}
