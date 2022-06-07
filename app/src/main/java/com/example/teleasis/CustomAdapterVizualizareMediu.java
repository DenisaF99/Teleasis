package com.example.teleasis;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomAdapterVizualizareMediu extends BaseAdapter {

    Context context;
    ArrayList<String> valoare_gaz = new ArrayList<>();
    ArrayList<String> valoare_temperatura = new ArrayList<>();
    ArrayList<String> valoare_umiditate = new ArrayList<>();
    ArrayList<String> valoare_prezenta = new ArrayList<>();
    ArrayList<String> data = new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();
    LayoutInflater inflter;

    public CustomAdapterVizualizareMediu(Context context, ArrayList<String> valoare_gaz,ArrayList<String> valoare_temperatura,
                                         ArrayList<String> valoare_umiditate,ArrayList<String> valoare_prezenta, ArrayList<String> data, ArrayList<String> id) {
        this.context = context;
        this.valoare_gaz = valoare_gaz;
        this.valoare_temperatura = valoare_temperatura;
        this.valoare_umiditate = valoare_umiditate;
        this.valoare_prezenta = valoare_prezenta;
        this.data = data;
        this.id = id;
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
        view = inflter.inflate(R.layout.mediu_list_item, null);
        TextView data_textview = (TextView) view.findViewById(R.id.data);
        TextView valoare_gaz2 = (TextView) view.findViewById(R.id.valoare_gaz);
        TextView valoare_prezenta2 = (TextView) view.findViewById(R.id.valoare_prezenta);
        TextView valoare_temperatura2 = (TextView) view.findViewById(R.id.valoare_temperatura);
        TextView valoare_umiditate2 = (TextView) view.findViewById(R.id.valoare_umiditate);
        TextView id_textview = (TextView) view.findViewById(R.id.id);
        data_textview.setText(data.get(i));
        valoare_gaz2.setText(valoare_gaz.get(i));
        valoare_prezenta2.setText(valoare_prezenta.get(i));
        valoare_temperatura2.setText(valoare_temperatura.get(i));
        valoare_umiditate2.setText(valoare_umiditate.get(i));
        id_textview.setText(id.get(i));
        this.notifyDataSetChanged();
        return view;
    }

}