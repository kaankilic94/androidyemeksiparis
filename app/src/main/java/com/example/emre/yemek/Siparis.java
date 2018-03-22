package com.example.emre.yemek;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.HashMap;

public class Siparis extends AppCompatActivity {
    ListView lv;
    SharedPreferences sha;
    SharedPreferences.Editor edit;
    TextView tv;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siparis);

        sha = getSharedPreferences("durum", MODE_PRIVATE);
        edit = sha.edit();
        Gson gson = new Gson();
        String json = sha.getString("drm", "");
        final DurumProperty obj = gson.fromJson(json, DurumProperty.class);
        lv = findViewById(R.id.lvsiparis);

        SiparisAdp adapter = new SiparisAdp(Siparis.this, obj);
        lv.setAdapter(adapter);

        tv = findViewById(R.id.TVFiyat);
        btn = findViewById(R.id.btnSatinAlim);
        tv.setText(obj.getFiyat()+"");


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> hm;

                for (int i = 0; i < obj.getUrunAlinan().size(); i++) {
                    hm = new HashMap<>();
                    String s = obj.getUrunAlinan().get(i).getProductId();
                    hm.put("productId", s);
                    hm.put("html", s);
                    hm.put("ref", "0c1d6ff9bca3255d3ef5692b276f1c19");
                    hm.put("customerId", obj.getId());
                    String url = "http://jsonbulut.com/json/orderForm.php";

                    new jData(url, hm, Siparis.this).execute();



/*

                    Gson gson = new Gson();
                    String json = sha.getString("drm", "");
                    DurumProperty dobj = gson.fromJson(json, DurumProperty.class);
                    dobj.setSayac(0);
                    dobj.setUrunAlinan(null);
                    dobj.setFiyat(0);
                    json = gson.toJson(dobj);
                    edit.putString("drm", json);
                    edit.commit();
                    */
                }
            }
        });
    }

    class jData extends AsyncTask<Void, Void, Void> {

        String url = "";
        HashMap<String, String> hm = null;
        ProgressDialog pro = null;
        String data = "";

        Context cnx;

        public jData(String url, HashMap<String, String> hm, Context cnx) {
            this.url = url;
            this.hm = hm;
            this.cnx = cnx;
            pro = ProgressDialog.show(cnx, "Sipariş veriliyor", "Lütfen Bekleyin", true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pro.dismiss();
            super.onPostExecute(aVoid);


            try {
                JSONObject obj = new JSONObject(data);
                JSONObject kobj = obj.getJSONArray("order").getJSONObject(0);
                boolean durum = kobj.getBoolean("durum");
                if (durum) {
                    Toast.makeText(Siparis.this, "Siparis Verildi", Toast.LENGTH_SHORT).show();

                    Gson gson = new Gson();
                    String json = sha.getString("drm", "");
                    DurumProperty dobj = gson.fromJson(json, DurumProperty.class);
                    dobj.setSayac(0);
                    dobj.setUrunAlinan(null);
                    dobj.setFiyat(0);
                    json = gson.toJson(dobj);
                    edit.putString("drm", json);
                    edit.commit();

                    Intent i = new Intent(Siparis.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                data = Jsoup.connect(url).data(hm).ignoreContentType(true).timeout(30000).execute().body();
            } catch (Exception e) {
                System.err.println("Data getirme Hatası: " + e);
            }

            return null;
        }
    }


}
