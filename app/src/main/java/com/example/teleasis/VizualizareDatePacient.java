package com.example.teleasis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.jarjarred.org.stringtemplate.v4.Interpreter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;

public class VizualizareDatePacient extends AppCompatActivity {


    DrawerLayout drawerLayout;
    String nume_extras;
    ImageView no_data;
    ImageView poza_meniu;
    LinearLayout currentLayout;
    ListView listView;
    DatabaseReference ref;
    TextView pulsTxt,tempTxt,umiditateTxt,gazTxt,prezentaTxt;
    DatabaseReference reff;
    Interpreter interpreter;


    private FirebaseAuth mAuth;
    ArrayList<String> lista_puls = new ArrayList<String>();
    ArrayList<String> puls = new ArrayList<>();
    ArrayList<String> temperatura = new ArrayList<>();
    ArrayList<String> umiditate = new ArrayList<>();
    ArrayList<String> gaz = new ArrayList<>();
    ArrayList<String> prezenta = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vizualizare_date_pacient);

        try {
            interpreter = new Interpreter(loadModelFile(),null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        listView = findViewById(R.id.listaAfisare);
        no_data = findViewById(R.id.no_data);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        String userId = currentUser.getUid();

        ref = FirebaseDatabase.getInstance().getReference().child(userId).child("Puls");

        drawerLayout = findViewById(R.id.drawer_layout);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        pulsTxt = findViewById(R.id.vizualizarePuls);
        tempTxt = findViewById(R.id.vizualizareTemp);
        umiditateTxt = findViewById(R.id.vizualizareUmiditate);
        gazTxt = findViewById(R.id.vizualizareGaz);
        prezentaTxt = findViewById(R.id.vizualizarePrezenta);

//        DatabaseReference mDatabase;
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                nume_extras = dataSnapshot.child(currentFirebaseUser.getUid()).child("Nume").getValue(String.class);
//                Picasso.with(getApplication()).load(dataSnapshot.child(currentFirebaseUser.getUid()).child("Imagine").child("imageUrl").getValue(String.class)).into(poza_meniu);
//                tv.setText(nume_extras);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) { }
//        };

//        mDatabase.addValueEventListener(postListener);
    }

    private MappedByteBuffer loadModelFile() throws  IOException{
        AssetFileDescriptor assetFileDescriptor = this.getAssets().openFd("linear3.tflite");
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long length = assetFileDescriptor.getLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,length);
    }

    public float doInference(String val){
        float[] input = new float[1];
        input[0] = Float.parseFloat(val);
        float[][] output = new float[1][1];
        interpreter.run(input,output);
        return output[0][0];
    }


//    @Override
//    protected void onPause(){
//        super.onPause();
//        MainActivity.closeDrawer(drawerLayout);
//
//    }
//    @Override
//    public void onBackPressed() {
//
//
//        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        finish();
//
//
//    }


    @Override
    protected void onStart() {
        super.onStart();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                int counter = 0;
                int f = 0;
                String puls_luat = "", temp_luat = "", umiditate_luata = "",  gaz_luat = "",  prezenta_luata = "";

                for (DataSnapshot pulsuri : dataSnapshot.getChildren()) {
                    if (pulsuri != null) {
                        for (DataSnapshot val : pulsuri.getChildren()) {
                            if (val.getKey().equals("puls")) {
                                puls_luat = val.getValue().toString();
                            }
                            if (val.getKey().equals("temperatura")) {
                                temp_luat = val.getValue().toString();
                                f = (int) doInference(String.valueOf(temp_luat));
                            }
                            if (val.getKey().equals("umiditate")) {
                                umiditate_luata = val.getValue().toString();
                                f = (int) doInference(String.valueOf(umiditate_luata));
                            }
                            if (val.getKey().equals("gaz")) {
                                gaz_luat = val.getValue().toString();
                                f = (int) doInference(String.valueOf(gaz_luat));
                            }
                            if (val.getKey().equals("prezenta")) {
                                prezenta_luata = val.getValue().toString();
                                f = (int) doInference(String.valueOf(prezenta_luata));
                            }

                            if (!puls_luat.equals("") && !valoare_luata.equals("")) {
                                lista_puls.add(data_luata + "," + valoare_luata + "," + f);
                                counter++;
                                data_luata = "";
                                valoare_luata = "";
                            }
                        }
                    }
                }
                if (counter == 0) {
                    no_data.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    tv_data.setVisibility(View.GONE);
                    tv_valoare.setVisibility(View.GONE);
                    tv_prezicere.setVisibility(View.GONE);

                }
                Collections.sort(lista_puls);
                for (String puls_curent : lista_puls) {
                    String[] sp = puls_curent.split(",");
                    String valoare = sp[1];
                    String[] data = sp[0].split("-");
                    String an = data[2];
                    String luna = data[1];
                    String zi = data[0];
                    String data_invers = an + "-" + luna + "-" + zi;
                    String ff = sp[2];
                    date.add(data_invers);
                    valori.add(valoare);
                    preziceri.add(ff);
                    i++;
                    CustomAdapterAnalizare customAdapter = new CustomAdapterAnalizare(getApplicationContext(), valori, date, preziceri);
                    customAdapter.notifyDataSetChanged();
                    listView.setAdapter(customAdapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}