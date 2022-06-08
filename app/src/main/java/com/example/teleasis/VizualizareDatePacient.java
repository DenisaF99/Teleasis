package com.example.teleasis;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VizualizareDatePacient extends AppCompatActivity {
    Button valoriPuls, valoriMediu;
    ListView listValoriPuls, listValoriMediu;
    TextView no_data;
    DatabaseReference reff;
    DatabaseReference reff2;
    LinearLayout layoutPuls, layoutMediu;
    private FirebaseAuth mAuth;

    ArrayList<String> lista_puls = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> valori = new ArrayList<>();
    ArrayList<String> iduri = new ArrayList<>();

    ArrayList<String> lista_mediu = new ArrayList<String>();
    ArrayList<String> date_mediu = new ArrayList<>();
    ArrayList<String> valori_gaz = new ArrayList<>();
    ArrayList<String> valori_temperatura = new ArrayList<>();
    ArrayList<String> valori_prezenta = new ArrayList<>();
    ArrayList<String> valori_umiditate = new ArrayList<>();
    ArrayList<String> iduri_mediu = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vizualizare_date_pacient);

        valoriPuls = findViewById(R.id.valoriPuls);
        valoriMediu = findViewById(R.id.valoriMediu);
        listValoriPuls = findViewById(R.id.listValoriPuls);
        listValoriMediu = findViewById(R.id.listValoriMediu);
        layoutMediu = findViewById(R.id.linearLayoutMediu);
        layoutPuls = findViewById(R.id.linearLayoutPuls);
        no_data = findViewById(R.id.no_data);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();

        reff = FirebaseDatabase.getInstance().getReference().child("Conturi/Pacienti/").child(userId).child("/ValoriPuls");
        reff2 = FirebaseDatabase.getInstance().getReference().child("Conturi/Pacienti/").child(userId).child("/ValoriMediu");

        ValueEventListener roomsValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter = 0;
                String data_luata = "",valoare_luata= "";

                for (DataSnapshot pulsuri : dataSnapshot.getChildren()) {
                    if (pulsuri != null) {
                        for ( DataSnapshot val : pulsuri.getChildren() ) {
                            if(val.getKey().equals("data")){
                                data_luata = val.getValue().toString();
                            }
                            if(val.getKey().equals("value")){
                                valoare_luata = val.getValue().toString();
                            }
                            if(!data_luata.equals("") && !valoare_luata.equals("")){
                                lista_puls.add(data_luata+","+valoare_luata + "," + pulsuri.getKey());
                                counter++;
                                data_luata="";
                                valoare_luata="";

                            }
                        }
                    }
                }
                if (counter == 0) {
                    no_data.setVisibility(View.VISIBLE);
                }
                Collections.sort(lista_puls);
                for (String puls_curent:lista_puls) {
                    String[] sp = puls_curent.split(",");
                    String valoare = sp[1];
                    String[] data = sp[0].split("-");
                    String id = sp[2];
                    String an = data[2];
                    String luna = data[1];
                    String zi = data[0];
                    String data_invers = an + "-" + luna + "-" + zi;
                    date.add(data_invers);
                    valori.add(valoare);
                    iduri.add(id);
                    CustomAdapterVizualizare customAdapter = new CustomAdapterVizualizare(getApplicationContext(), valori, date,iduri);
                    customAdapter.notifyDataSetChanged();
                    listValoriPuls.setAdapter(customAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        reff.addListenerForSingleValueEvent(roomsValueEventListener);


        ValueEventListener roomsValueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter = 0;
                String data_luata = "",valoare_gaz_luata= "",valoare_temperatura_luata= "",valoare_prezenta_luata= "",valoare_umiditate_luata= "";

                for (DataSnapshot mediu : dataSnapshot.getChildren()) {
                    if (mediu != null) {
                        for ( DataSnapshot val : mediu.getChildren() ) {
                            if(val.getKey().equals("data")){
                                data_luata = val.getValue().toString();
                            }
                            if(val.getKey().equals("value_gaz")){
                                valoare_gaz_luata = val.getValue().toString();
                            }
                            if(val.getKey().equals("value_temperatura")){
                                valoare_temperatura_luata = val.getValue().toString();
                            }
                            if(val.getKey().equals("value_prezenta")){
                                valoare_prezenta_luata = val.getValue().toString();
                            }
                            if(val.getKey().equals("value_umiditate")){
                                valoare_umiditate_luata = val.getValue().toString();
                            }
                            if(!data_luata.equals("") && !valoare_gaz_luata.equals("")&& !valoare_temperatura_luata.equals("")&& !valoare_prezenta_luata.equals("")&& !valoare_umiditate_luata.equals("")){
                                lista_mediu.add(data_luata+","+valoare_gaz_luata +","+valoare_temperatura_luata+","+valoare_prezenta_luata+","+valoare_umiditate_luata+ "," + mediu.getKey());
                                counter++;
                                data_luata="";
                                valoare_gaz_luata="";
                                valoare_temperatura_luata="";
                                valoare_prezenta_luata="";
                                valoare_umiditate_luata="";
                            }
                        }
                    }
                }
                if (counter == 0) {
                    no_data.setVisibility(View.VISIBLE);
                }
                Collections.sort(lista_mediu);
                for (String mediu_curent:lista_mediu) {
                    String[] sp = mediu_curent.split(",");
                    String valoare_gaz = sp[1];
                    String valoare_temperatura = sp[2];
                    String valoare_prezenta = sp[3];
                    String valoare_umiditate = sp[4];
                    String[] data = sp[0].split("-");
                    String id = sp[2];
                    String an = data[2];
                    String luna = data[1];
                    String zi = data[0];
                    String data_invers = an + "-" + luna + "-" + zi;
                    date_mediu.add(data_invers);
                    valori_gaz.add(valoare_gaz);
                    valori_temperatura.add(valoare_temperatura);
                    valori_umiditate.add(valoare_umiditate);
                    valori_prezenta.add(valoare_prezenta);
                    iduri_mediu.add(id);
                    Log.d("caca", valoare_gaz);
                    CustomAdapterVizualizareMediu customAdapter2 = new CustomAdapterVizualizareMediu(getApplicationContext(), valori_gaz,valori_temperatura,valori_umiditate,valori_prezenta, date_mediu,iduri_mediu);
                    customAdapter2.notifyDataSetChanged();
                    listValoriMediu.setAdapter(customAdapter2);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        reff2.addListenerForSingleValueEvent(roomsValueEventListener2);



        valoriPuls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutPuls.setVisibility(View.VISIBLE);
                layoutMediu.setVisibility(View.GONE);
            }
        });

        valoriMediu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutPuls.setVisibility(View.GONE);
                layoutMediu.setVisibility(View.VISIBLE);
            }
        });

    }

}
