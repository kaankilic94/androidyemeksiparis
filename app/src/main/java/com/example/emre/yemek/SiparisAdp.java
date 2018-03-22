package com.example.emre.yemek;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SiparisAdp extends BaseAdapter {
    private DurumProperty durumProperty;
    private ArrayList<String> urunbaslik;
    private ArrayList<String> urunfiyat;
    private LayoutInflater ınflater;
    private Context context;


    public SiparisAdp() {

    }

    public SiparisAdp(Context c, DurumProperty durumProperty) {
        this.durumProperty = durumProperty;
        context = c;
    }

    @Override
    public int getCount() {
        return durumProperty.getSayac();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;


        if (view == null) {
            ınflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = ınflater.inflate(R.layout.siparis_row, null);
        }


        ImageView icon = v.findViewById(R.id.sire);
        TextView baslik = v.findViewById(R.id.siparisBaslikTV);
        TextView fiyat = v.findViewById(R.id.siparisFiyatTV);


        baslik.setText(durumProperty.getUrunAlinan().get(i).getProductName());
        fiyat.setText(""+durumProperty.getUrunAlinan().get(i).getPrice());
        String url = durumProperty.getUrunAlinan().get(i).getImagethumb();
        Picasso.with(context).load(url).fit().centerCrop().into(icon);


        return v;
    }


}
