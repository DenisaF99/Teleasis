package com.example.teleasis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CitireDate extends AppCompatActivity {
    Button pulsBtn;
    Button connectBluetooth;
    Button tempBtn;
    DrawerLayout drawerLayout;

    private ListView listView;
    private BluetoothAdapter mBTAdapter;
    private static final int BT_ENABLE_REQUEST = 10;
    private static final int SETTINGS = 20;
    private UUID mDeviceUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private int mBufferSize = 50000;
    public static final String DEVICE_EXTRA = "com.example.teleasis.SOCKET";
    public static final String DEVICE_UUID = "com.example.teleasis.uuid";
    private static final String DEVICE_LIST = "com.example.teleasis.devicelist";
    private static final String DEVICE_LIST_SELECTED = "com.example.teleasis.devicelistselected";
    public static final String BUFFER_SIZE = "com.example.teleasis.buffersize";
    private static final String TAG = "BlueTest5-MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_citire_date);
        pulsBtn = findViewById(R.id.pulsBtn);
        connectBluetooth = findViewById(R.id.connectBluetoothBtn);
        tempBtn = findViewById(R.id.temperaturaBtn);
        listView = (ListView) findViewById(R.id.lista_bluetooth);
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);
        if (savedInstanceState != null) {
            ArrayList<BluetoothDevice> list = savedInstanceState.getParcelableArrayList(DEVICE_LIST);
            if (list != null) {
                initList(list);
                MyAdapter adapter = (MyAdapter) listView.getAdapter();
                int selectedIndex = savedInstanceState.getInt(DEVICE_LIST_SELECTED);
                if (selectedIndex != -1) {
                    adapter.setSelectedIndex(selectedIndex);
                    tempBtn.setEnabled(true);
                    pulsBtn.setEnabled(true);
                }
            } else {
                initList(new ArrayList<BluetoothDevice>());
            }

        } else {
            initList(new ArrayList<BluetoothDevice>());
        }


        connectBluetooth.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View arg0) {

                mBTAdapter = BluetoothAdapter.getDefaultAdapter();

                if (mBTAdapter == null) {
                    Toast.makeText(getApplicationContext(), "Bluetooth not found", Toast.LENGTH_SHORT).show();
                } else if (!mBTAdapter.isEnabled()) {
                    Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    // startActivityForResult(enableBT, BT_ENABLE_REQUEST);
                    startActivityForResult(enableBT, BT_ENABLE_REQUEST);
                } else {

                    mBTAdapter.startDiscovery();

                }
            }
        });


        pulsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CitireDate.this, PreluarePuls.class);
                BluetoothDevice device = ((MyAdapter) (listView.getAdapter())).getSelectedItem();
                myIntent.putExtra(DEVICE_EXTRA, device);
                myIntent.putExtra(DEVICE_UUID, mDeviceUUID.toString());
                myIntent.putExtra(BUFFER_SIZE, mBufferSize);
                startActivity(myIntent);

            }
        });

        tempBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CitireDate.this, PreluareValoriMediu.class);
                BluetoothDevice device = ((MyAdapter) (listView.getAdapter())).getSelectedItem();
                myIntent.putExtra(DEVICE_EXTRA, device);
                myIntent.putExtra(DEVICE_UUID, mDeviceUUID.toString());
                myIntent.putExtra(BUFFER_SIZE, mBufferSize);
                startActivity(myIntent);
            }
        });


    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean found = false;
            String action = intent.getAction();
            List<BluetoothDevice> listDevices = new ArrayList<BluetoothDevice>();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null)
                    listDevices.add(device);
            }

            if (listDevices.size() > 0) {
                MyAdapter adapter = (MyAdapter) listView.getAdapter();
                adapter.replaceItems(listDevices);
                for (int i = 0; i < listDevices.size(); i++) {

                    if (adapter.getItem(i).getAddress().equals("98:D3:31:F6:25:44")) {
                        unregisterReceiver(mReceiver);
                        adapter.setSelectedIndex(i);
                        tempBtn.setEnabled(true);
                        pulsBtn.setEnabled(true);
                        tempBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.frame_btn_masurare));
                        pulsBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.frame_btn_masurare));
                        tempBtn.setTextColor(getResources().getColor(R.color.white));
                        pulsBtn.setTextColor(getResources().getColor(R.color.white));
                        tempBtn.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_temp), null, null, null);
                        pulsBtn.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_puls), null, null, null);
                        Toast.makeText(getApplicationContext(), "Conectat!", Toast.LENGTH_LONG).show();
                        found = true;
                    }

//                    } else {
//                        msg("Device-ul nu a fost gasit!");
//                    }
                }
                if (!found) {
                    msg("Device-ul nu a fost gasit!");
                }

            }

        }
    };

    /*@Override
    protected void onPause(){
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }*/
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BT_ENABLE_REQUEST:
                if (resultCode == RESULT_OK) {
                    msg("Bluetooth Enabled successfully");
                    new SearchDevices().execute();
                } else {
                    msg("Bluetooth couldn't be enabled");
                }

                break;
            case SETTINGS: //If the settings have been updated
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String uuid = prefs.getString("prefUuid", "Null");
                mDeviceUUID = UUID.fromString(uuid);
                Log.d(TAG, "UUID: " + uuid);
                String bufSize = prefs.getString("prefTextBuffer", "Null");
                mBufferSize = Integer.parseInt(bufSize);
                String orientation = prefs.getString("prefOrientation", "Null");
                Log.d(TAG, "Orientation: " + orientation);
                if (orientation.equals("Landscape")) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if (orientation.equals("Portrait")) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (orientation.equals("Auto")) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void msg(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }


    private void initList(List<BluetoothDevice> objects) {
        final MyAdapter adapter = new MyAdapter(getApplicationContext(), R.layout.list_item, R.id.lstContent, objects);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    private class SearchDevices extends AsyncTask<Void, Void, List<BluetoothDevice>> {

        @Override
        protected List<BluetoothDevice> doInBackground(Void... params) {
            @SuppressLint("MissingPermission") Set<BluetoothDevice> pairedDevices = mBTAdapter.getBondedDevices();
            List<BluetoothDevice> listDevices = new ArrayList<BluetoothDevice>();
            for (BluetoothDevice device : pairedDevices) {
                listDevices.add(device);
            }
            return listDevices;

        }

        @Override
        protected void onPostExecute(List<BluetoothDevice> listDevices) {
            super.onPostExecute(listDevices);
            boolean found = false;
            if (listDevices.size() > 0) {
                MyAdapter adapter = (MyAdapter) listView.getAdapter();
                adapter.replaceItems(listDevices);
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (adapter.getItem(i).getAddress().equals("98:D3:31:F6:25:44")) {
                        unregisterReceiver(mReceiver);
                        adapter.setSelectedIndex(i);
                        tempBtn.setEnabled(true);
                        pulsBtn.setEnabled(true);
                        tempBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.frame_btn_masurare));
                        pulsBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.frame_btn_masurare));
                        tempBtn.setTextColor(getResources().getColor(R.color.white));
                        pulsBtn.setTextColor(getResources().getColor(R.color.white));
                        tempBtn.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_temp), null, null, null);
                        pulsBtn.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_puls), null, null, null);
                        Toast.makeText(getApplicationContext(), "Conectat!", Toast.LENGTH_LONG).show();
                        found = true;
                    }

//                    else {
//                        Toast.makeText(getApplicationContext(), "Device-ul nu a fost gasit!",Toast.LENGTH_LONG).show();
//
//                    }
                }
                if (!found) {
                    msg("Device-ul nu a fost gasit!");
                }

            } else {
                msg("No paired devices found, please pair your serial BT device and try again");
            }
        }


    }

    private class MyAdapter extends ArrayAdapter<BluetoothDevice> {
        private int selectedIndex;
        private Context context;
        private int selectedColor = Color.parseColor("#abcdef");
        private List<BluetoothDevice> myList;

        public MyAdapter(Context ctx, int resource, int textViewResourceId, List<BluetoothDevice> objects) {
            super(ctx, resource, textViewResourceId, objects);
            context = ctx;
            myList = objects;
            selectedIndex = -1;
        }

        public void setSelectedIndex(int position) {
            selectedIndex = position;
            notifyDataSetChanged();
        }

        public BluetoothDevice getSelectedItem() {
            return myList.get(selectedIndex);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public BluetoothDevice getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            TextView tv;
        }

        public void replaceItems(List<BluetoothDevice> list) {
            myList = list;
            notifyDataSetChanged();
        }

        public List<BluetoothDevice> getEntireList() {
            return myList;
        }

        @SuppressLint("MissingPermission")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            ViewHolder holder;
            if (convertView == null) {
                vi = LayoutInflater.from(context).inflate(R.layout.list_item, null);
                holder = new ViewHolder();

                holder.tv = (TextView) vi.findViewById(R.id.lstContent);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            if (selectedIndex != -1 && position == selectedIndex) {
                holder.tv.setBackgroundColor(selectedColor);
            } else {
                holder.tv.setBackgroundColor(Color.GRAY);
            }
            BluetoothDevice device = myList.get(position);
            holder.tv.setText(device.getName() + "\n " + device.getAddress());
            holder.tv.setTextColor(Color.WHITE);
            return vi;
        }


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }


}