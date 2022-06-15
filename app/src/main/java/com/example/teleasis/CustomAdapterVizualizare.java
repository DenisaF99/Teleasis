package com.example.teleasis;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomAdapterVizualizare extends BaseAdapter {

    Context context;
    ArrayList<String> valoare = new ArrayList<>();
    ArrayList<String> data = new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();
    ArrayList<String> preziceri_puls = new ArrayList<>();
    LayoutInflater inflter;

    public CustomAdapterVizualizare(Context context, ArrayList<String> valoare, ArrayList<String> data, ArrayList<String> id, ArrayList<String> preziceri_puls) {

        this.context = context;
        this.valoare = valoare;
        this.data = data;
        this.id = id;
        this.preziceri_puls = preziceri_puls;
        inflter = (LayoutInflater.from(context));
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        this.notifyDataSetChanged();
        view = inflter.inflate(R.layout.puls_list_item, null);
        TextView data_textview = (TextView) view.findViewById(R.id.data);
        TextView valoare_textview = (TextView) view.findViewById(R.id.valoare);
        TextView id_textview = (TextView) view.findViewById(R.id.id);
        TextView prezicere_puls = (TextView) view.findViewById(R.id.prezicere_puls);
        data_textview.setText(data.get(i));
        valoare_textview.setText(valoare.get(i));

        if(preziceri_puls.get(i).equals("1")){
            prezicere_puls.setText("normal");
        }else if(preziceri_puls.get(i).equals("0")){
            prezicere_puls.setText("bradicardie");
        }else if(preziceri_puls.get(i).equals("2")){
            prezicere_puls.setText("tahicardie");
        }else{
            prezicere_puls.setText("error");
        }


        id_textview.setText(id.get(i));
        this.notifyDataSetChanged();
        return view;
    }

    public void remove(int position){
        data.remove(data.get(position));
        valoare.remove(valoare.get(position));
        id.remove(id.get(position));
        preziceri_puls.remove(preziceri_puls.get(position));

    }
}


