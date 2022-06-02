package com.example.teleasis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class PreluareValoriMediu extends AppCompatActivity {
    private static final String TAG = "BlueTest5-Controlling";
    private int mMaxChars = 50000;
    private UUID mDeviceUUID;
    private BluetoothSocket mBTSocket;
    private PreluareValoriMediu.ReadInput mReadThread = null;
    private boolean mIsUserInitiatedDisconnect = false;
    private boolean mIsBluetoothConnected = false;
    private BluetoothDevice mDevice;
    private ProgressDialog progressDialog;
    private Button butonAdaugareValori;
    DatabaseReference reff = FirebaseDatabase.getInstance().getReference();
    BazaDateValoriMediu bdValori;
    private FirebaseAuth mAuth;
    TextView textViewTemp, textViewUmiditate, textViewPrezenta, textViewGaz;
    float temp_final = 0;
    float umid_final = 0;
    float gaz_final = 0;
    String prez_final;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preluare_temperatura);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        mDevice = b.getParcelable(CitireDate.DEVICE_EXTRA);
        mDeviceUUID = UUID.fromString(b.getString(CitireDate.DEVICE_UUID));
        mMaxChars = b.getInt(CitireDate.BUFFER_SIZE);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
        registerReceiver(mPairingRequestReceiver, filter);
        butonAdaugareValori = (Button) findViewById(R.id.addTempPacientBtn);
        textViewTemp = findViewById(R.id.textviewTemp);
        textViewUmiditate = findViewById(R.id.textviewUmiditate);
        textViewPrezenta = findViewById(R.id.textviewPrezenta);
        textViewGaz = findViewById(R.id.textviewGaz);
        bdValori = new BazaDateValoriMediu();

        butonAdaugareValori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                Random random = new Random();
                int id = random.nextInt(100000);
                bdValori.setValue_temperatura(String.valueOf(temp_final));
                bdValori.setValue_gaz(String.valueOf(gaz_final));
                bdValori.setValue_umiditate(String.valueOf(umid_final));
                bdValori.setValue_prezenta(String.valueOf(prez_final));
                bdValori.setData(date);
                bdValori.setId(id);
                reff.child("Conturi").child("Pacienti").child(userId).child("ValoriMediu").child(String.valueOf(id)).setValue(bdValori);

                Toast.makeText(PreluareValoriMediu.this, "Data inserted successfully", Toast.LENGTH_LONG).show();
            }
        });

    }
    private final BroadcastReceiver mPairingRequestReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_PAIRING_REQUEST)) {
                try {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    int pin = intent.getIntExtra("android.bluetooth.device.extra.PAIRING_KEY", 1234);
                    Log.d(TAG, "Start Auto Pairing. PIN = " + intent.getIntExtra("android.bluetooth.device.extra.PAIRING_KEY", 1234));
                    byte[] pinBytes;
                    pinBytes = ("" + pin).getBytes("UTF-8");
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    device.setPin(pinBytes);
                    device.setPairingConfirmation(true);
                } catch (Exception e) {
                    Log.e(TAG, "Error occurs when trying to auto pair");
                    e.printStackTrace();
                }
            }
        }
    };

    class ReadInput implements Runnable {

        private boolean bStop = false;
        private Thread t;

        public ReadInput() {
            t = new Thread(this, "Input Thread");
            t.start();
        }

        public boolean isRunning() {
            return t.isAlive();
        }

        @Override
        public void run() {
            InputStream inputStream;
            try {
                inputStream = mBTSocket.getInputStream();
                while (!bStop) {

                    byte[] buffer = new byte[256];
                    if ((inputStream.available() > 0)) {
                        inputStream.read(buffer);
                        int i = 0;
                        for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
                        }
                        final String strInput = new String(buffer, 0, i);
                        String[] despartit = strInput.trim().replace("\n", "").split(";");
                        Log.d("strInput", strInput);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                float temp,prezenta, umiditate, gaz;
                                if (despartit.length > 1 && despartit.length < 6) {
                                    if (despartit[2].length() > 0 && despartit[2].contains("T=")) {
                                        temp = Float.parseFloat(despartit[2].split("=")[1]);
                                        Log.d("string_temp", String.valueOf(despartit[2]));
                                        textViewTemp.setText("Temperatura dumneavoastra este: " + temp);
                                        temp_final = temp;
                                    }
                                    if(despartit[1].length() > 0 && despartit[1].contains("M=")){
                                        prezenta = Float.parseFloat(despartit[1].split("=")[1]);
                                        Log.d("string_prezenta", String.valueOf(despartit[1]));
                                        String pr = prezenta == 0 ? "Detectata" : "Nedetectata";
                                        textViewPrezenta.setText("Prezenta: " + pr);
                                        prez_final = pr;
                                    }
                                    if(despartit[3].length() > 0 && despartit[3].contains("H=")){
                                        umiditate =Float.parseFloat(despartit[3].split("=")[1]);
                                        Log.d("string_umiditate", String.valueOf(despartit[3]));

                                        textViewUmiditate.setText("Umiditatea mediului este: " + umiditate + " %");
                                        umid_final = umiditate;
                                    }
                                    if(despartit[4].length() > 0 && despartit[4].contains("G=")){
                                        gaz = Float.parseFloat(despartit[4].split("=")[1]);
                                        Log.d("string_gaz", String.valueOf(despartit[4]));
                                        textViewGaz.setText("Gaz: " + gaz);
                                        gaz_final = gaz;
                                    }
                                }
                            }
                        });

                    }

                    Thread.sleep(600);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }

        public void stop() {
            bStop = true;
        }

    }

    private class DisConnectBT extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (mReadThread != null) {
                mReadThread.stop();
                while (mReadThread.isRunning())
                    ;
                mReadThread = null;

            }

            try {
                mBTSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mIsBluetoothConnected = false;
            if (mIsUserInitiatedDisconnect) {
                finish();
            }
        }

    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        if (mBTSocket != null && mIsBluetoothConnected) {
            new PreluareValoriMediu.DisConnectBT().execute();
        }
        Log.d(TAG, "Paused");
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mBTSocket == null || !mIsBluetoothConnected) {
            new PreluareValoriMediu.ConnectBT().execute();
        }
        Log.d(TAG, "Resumed");
        super.onResume();

    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopped");
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean mConnectSuccessful = true;

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(PreluareValoriMediu.this, "Hold on", "Connecting");

        }

        @SuppressLint("MissingPermission")
        @Override
        protected Void doInBackground(Void... devices) {

            try {
                if (mBTSocket == null || !mIsBluetoothConnected) {
                    Log.d("mDeviceUUID", String.valueOf(mDeviceUUID));
                    mBTSocket = mDevice.createInsecureRfcommSocketToServiceRecord(mDeviceUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mBTSocket.connect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                mConnectSuccessful = false;

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!mConnectSuccessful) {
                Toast.makeText(getApplicationContext(), "Could not connect to device.Please turn on your Hardware", Toast.LENGTH_LONG).show();
                finish();
            } else {
                msg("Connected to device");
                mIsBluetoothConnected = true;
                mReadThread = new PreluareValoriMediu.ReadInput();
            }

            progressDialog.dismiss();
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {

            startActivity(new Intent(getApplicationContext(), PreluareValoriMediu.class));

            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(), PreluareValoriMediu.class));
        finish();


    } */


}