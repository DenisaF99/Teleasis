package com.example.teleasis;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterListaInterventii extends BaseAdapter {
    ArrayList<String> descrieri = new ArrayList<String>();
    ArrayList<String> id_uri = new ArrayList<>();
    ArrayList<String> stadii = new ArrayList<>();
    ArrayList<String> tipuri = new ArrayList<>();
    ArrayList<String> date_rezolvate = new ArrayList<>();
    Context context;
    LayoutInflater inflter;

    public AdapterListaInterventii(ArrayList<String> descrieri,ArrayList<String> id_uri, ArrayList<String> stadii, ArrayList<String> tipuri, ArrayList<String> date_rezolvate, Context context) {
        this.descrieri = descrieri;
        this.id_uri=id_uri;
        this.stadii=stadii;
        this.tipuri=tipuri;
        this.date_rezolvate=date_rezolvate;
        this.context = context;
        inflter = (LayoutInflater.from(context));
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return descrieri.size();
    }

    @Override
    public Object getItem(int pos) {
        return descrieri.get(pos);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView( int position, View view, ViewGroup parent) {
        this.notifyDataSetChanged();
        view = inflter.inflate(R.layout.interventie_layout, null);


        //Handle TextView and display string from your list
        TextView tvContact= (TextView)view.findViewById(R.id.interventieTxt);
        tvContact.setText(descrieri.get(position));

        //Handle buttons and add onClickListeners
        Button callbtn= (Button)view.findViewById(R.id.rezolvaInterventieBtn);
        TextView rezolvatId = (TextView) view.findViewById(R.id.rezolvatId);

        if(stadii.get(position).equals("Necompletat"))
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
        });


        return view;
    }
}