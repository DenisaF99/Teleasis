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
import android.graphics.Color;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class PreluarePuls extends AppCompatActivity {
    private static final String TAG = "BlueTest5-Controlling";
    private int mMaxChars = 50000;
    private UUID mDeviceUUID;
    private BluetoothSocket mBTSocket;
    private ReadInput mReadThread = null;
    private boolean mIsUserInitiatedDisconnect = false;
    private boolean mIsBluetoothConnected = false;
    private BluetoothDevice mDevice;
    private ProgressDialog progressDialog;
    private Button buttonbd;
    GraphView graph;
    public int coordonataX = 0;
    LineGraphSeries<DataPoint> series;
    DatabaseReference reff = FirebaseDatabase.getInstance().getReference();
    BazaDatePuls bdpuls;
    private FirebaseAuth mAuth;
    TextView textViewPuls;
    int contor5060, contor6070, contor7080, contor8090, contor90100, contor100110, contor110120, contor120130, contor130140;
    int puls_final = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preluare_puls);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b == null){
            Log.d("Err", "null");
        }
        else{
        mDevice = b.getParcelable(CitireDate.DEVICE_EXTRA);
        mDeviceUUID = UUID.fromString(b.getString(CitireDate.DEVICE_UUID));
        mMaxChars = b.getInt(CitireDate.BUFFER_SIZE); }
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
        registerReceiver(mPairingRequestReceiver, filter);
        buttonbd = (Button) findViewById(R.id.addPulsBtnPacientBtn);
        textViewPuls = findViewById(R.id.textviewPuls);
        bdpuls = new BazaDatePuls();
        graph = (GraphView) findViewById(R.id.grafPuls);
        series = new LineGraphSeries<>();
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setColor(Color.parseColor("#000000"));
        graph.addSeries(series);
        graph.setBackgroundColor(Color.BLACK);
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        glr.setPadding(32);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(7);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(50);
        graph.getViewport().setMaxY(300);
        graph.getViewport().setScrollable(true);
        graph.getGridLabelRenderer().setNumHorizontalLabels(7);
        graph.getGridLabelRenderer().setNumVerticalLabels(14);
        graph.setBackgroundColor(Color.parseColor("#ffffff"));
        graph.getGridLabelRenderer().setGridColor(Color.parseColor("#000000"));
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.parseColor("#000000"));
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#000000"));
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        String idParameter = getIntent().getStringExtra("idPacient");
        Log.d("idParam", idParameter);


        buttonbd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                Random random = new Random();
                int id = random.nextInt(100000);
                bdpuls.setValue(String.valueOf(puls_final));
                bdpuls.setData(date);
                bdpuls.setId(id);
                if(!idParameter.equals("-1")){
                    reff.child("Conturi").child("Pacienti").child(idParameter).child("ValoriPuls").child(String.valueOf(id)).setValue(bdpuls);
                    Toast.makeText(PreluarePuls.this, "Date introduse cu succes!", Toast.LENGTH_LONG).show();
                }
                else{
                Log.d("userID", userId);
                reff.child("Conturi").child("Pacienti").child(userId).child("ValoriPuls").child(String.valueOf(id)).setValue(bdpuls);
                    Toast.makeText(PreluarePuls.this, "Date introduse cu succes", Toast.LENGTH_LONG).show();
                }


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

    private class ReadInput implements Runnable {

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
                                int puls;
                                if (despartit.length > 1) {
                                    if (despartit[0].length() > 0 && despartit[0].contains("P=")) {
                                        puls = Integer.parseInt(despartit[0].split("=")[1]);
                                        Log.d("string_puls", String.valueOf(despartit[0]));
                                        textViewPuls.setText("Pulsul dumneavoastra este: " + puls);
                                        puls_final = puls;
                                        series.appendData(new DataPoint(coordonataX++, puls), true, 10);

                                        if (puls > 50 && puls < 60) {
                                            contor5060++;
                                            if (contor5060 == 10) {
                                                series.appendData(new DataPoint(coordonataX++, puls), true, 10);
                                                textViewPuls.setText("Pulsul final este: " + puls);
                                                puls_final = puls;
                                                t.interrupt();
                                            } else if (contor5060 < 10) {
                                                series.appendData(new DataPoint(coordonataX++, puls), true, 10);
                                            }
                                        } else if (puls > 60 && puls < 70) {
                                            contor6070++;
                                            if (contor6070 == 10) {
                                                series.appendData(new DataPoint(coordonataX++, puls), true, 10);
                                                textViewPuls.setText("Pulsul final este: " + puls);
                                                puls_final = puls;
                                                t.interrupt();
                                            } else if (contor6070 < 10) {
                                                series.appendData(new DataPoint(coordonataX++, puls), true, 10);
                                            }
                                        } else if (puls > 70 && puls < 80) {
                                            contor7080++;
                                            if (contor7080 == 10) {
                                                series.appendData(new DataPoint(coordonataX++, puls), true, 10);
                                                textViewPuls.setText("Pulsul final este: " + puls);
                                                puls_final = puls;
                                                t.interrupt();
                                            } else if (contor7080 < 10) {
                                                series.appendData(new DataPoint(coordonataX++, puls), true, 10);
                                            }
                                        } else if (puls > 80 && puls < 90) {
                                            contor8090++;
                                            if (contor8090 == 10) {
                                                series.appendData(new DataPoint(coordonataX++, puls), true, 10);
                                                textViewPuls.setText("Pulsul final este: " + puls);
                                                puls_final = puls;
                                                t.interrupt();
                                            } else if (contor8090 < 10) {
                                                series.appendData(new DataPoint(coordonataX++, puls), true, 10);
                                            }
                                        } else if (puls > 90 && puls < 100) {
                                            contor90100++;
                                            if (contor90100 == 10) {
                                                series.appendData(new DataPoint(coordonataX++, puls), true, 10);
                                                textViewPuls.setText("Pulsul final este: " + puls);
                                                puls_final = puls;
                                                t.interrupt();
                                            } else if (contor90100 < 10) {
                                                series.appendData(new DataPoint(coordonataX++, puls), true, 10);
                                            }
                                        } else if (puls > 100 && puls < 110) {
                                            contor100110++;
                                            if (contor100110 == 10) {
                                                series.appendData(new DataPoint(coordonataX++, puls), true, 10);
                                                textViewPuls.setText("Pulsul final este: " + puls);
                                                puls_final = puls;
                                                t.interrupt();
                                            } else if (contor100110 < 10) {
                                                series.appendData(new DataPoint(coordonataX++, puls), true, 10);
                                            }
                                        } else if (puls > 110 && puls < 120) {
                                            contor110120++;
                                            if (contor110120 == 10) {
                                                series.appendData(new DataPoint(coordonataX++, puls), true, 10);
                                                textViewPuls.setText("Pulsul final este: " + puls);
                                                puls_final = puls;
                                                t.interrupt();
                                            } else if (contor110120 < 10) {
                                                series.appendData(new DataPoint(coordonataX++, puls), true, 10);
                                            }
                                        } else if (puls > 120 && puls < 130) {
                                            contor120130++;
                                            if (contor120130 == 10) {
                                                series.appendData(new DataPoint(coordonataX++, puls), true, 10);
                                                textViewPuls.setText("Pulsul final este: " + puls);
                                                puls_final = puls;
                                                t.interrupt();
                                            } else if (contor120130 < 10) {
                                                series.appendData(new DataPoint(coordonataX++, puls), true, 10);
                                            }
                                        } else if (puls > 130 && puls < 140) {
                                            contor130140++;
                                            if (contor130140 == 10) {
                                                series.appendData(new DataPoint(coordonataX++, puls), true, 10);
                                                textViewPuls.setText("Pulsul final este: " + puls);
                                                puls_final = puls;
                                                t.interrupt();
                                            } else if (contor130140 < 10) {
                                                series.appendData(new DataPoint(coordonataX++, puls), true, 10);
                                            }
                                        }

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
            new DisConnectBT().execute();
        }
        Log.d(TAG, "Paused");
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mBTSocket == null || !mIsBluetoothConnected) {
            new ConnectBT().execute();
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

            progressDialog = ProgressDialog.show(PreluarePuls.this, "Asteptati", "Connecting");

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
                mReadThread = new ReadInput();
            }

            progressDialog.dismiss();
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {

            startActivity(new Intent(getApplicationContext(),CitireDate.class));

            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.d("exit","back");
        startActivity(new Intent(getApplicationContext(), CitireDate.class));
        finish();


    }
*/

}