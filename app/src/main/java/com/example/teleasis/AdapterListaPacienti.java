package com.example.teleasis;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterListaPacienti extends BaseAdapter {
    ArrayList<String> lista_interventii = new ArrayList<String>();
    ArrayList<String> id_uri = new ArrayList<>();
    Context context;
    LayoutInflater inflter;

    public AdapterListaPacienti(ArrayList<String> id_uri, Context context) {
        this.id_uri=id_uri;
        inflter = (LayoutInflater.from(context));
        this.notifyDataSetChanged();
    }


    /*@Override
    public Object getItem(int pos) {
        return descrieri.get(pos);
    } */

    @Override
    public int getCount() {
        return id_uri.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        this.notifyDataSetChanged();
        view = inflter.inflate(R.layout.activity_adapter_alege_pacient, parent, false);
        Log.d("View","1653");
        //Handle TextView and display string from your list
        TextView tvContact= (TextView)view.findViewById(R.id.numePacient);
        tvContact.setText(id_uri.get(position));

        //Handle buttons and add onClickListeners
       // TextView interventie = (TextView) view.findViewById(R.id.numePacientt);


      /*  if(stadii.get(position).equals("Necompletat"))
        {
            callbtn.setVisibility(View.VISIBLE);
            rezolvatId.setVisibility(View.GONE);
        }
        else
        {
            callbtn.setVisibility(View.GONE);
            rezolvatId.setVisibility(View.VISIBLE);
            rezolvatId.setText(rezolvatId.getText()+date_rezolvate.get(position));
        }
        callbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Interventie.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.putExtra("stadiu", stadii.get(position));
                myIntent.putExtra("tip",  tipuri.get(position));
                myIntent.putExtra("descriere",  descrieri.get(position));
                myIntent.putExtra("idInterventie",  id_uri.get(position));
                context.startActivity(myIntent);

            }
        }); */


        return view;
    }
}
