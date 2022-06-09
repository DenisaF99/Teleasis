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

public class AdapterListaInterventii extends BaseAdapter implements ListAdapter {
    private ArrayList<String> descrieri = new ArrayList<String>();
    ArrayList<String> id_uri = new ArrayList<>();
    private Context context;

    public AdapterListaInterventii(ArrayList<String> descrieri,ArrayList<String> id_uri, Context context) {
        this.descrieri = descrieri;
        this.id_uri=id_uri;
        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_rezolvare_interventii_ingrijitor, null);
        }

        //Handle TextView and display string from your list
        TextView tvContact= (TextView)view.findViewById(R.id.interventieTxt);
        tvContact.setText(descrieri.get(position));

        //Handle buttons and add onClickListeners
        Button callbtn= (Button)view.findViewById(R.id.rezolvaInterventieBtn);

        callbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Interventie.class);
                context.startActivity(myIntent);

            }
        });


        return view;
    }
}