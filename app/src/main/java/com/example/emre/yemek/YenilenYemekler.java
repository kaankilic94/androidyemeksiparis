package com.example.emre.yemek;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class YenilenYemekler extends AppCompatActivity {
    ArrayList<Urunler> lstUrunler = new ArrayList<Urunler>();
    ListView liste;
    BaseAdapter adp;
    LayoutInflater linf;
    JSONArray larr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yenilen_yemekler);
        linf = LayoutInflater.from(this);

        liste = findViewById(R.id.lstYediklerim);
        String url = "http://jsonbulut.com/json/orderList.php";
        HashMap<String, String> hm = new HashMap<>();
        hm.put("ref", "0c1d6ff9bca3255d3ef5692b276f1c19");
        hm.put("musterilerID", "1285");
        new jData(url, hm, this).execute();
    }


    class jData extends AsyncTask<Void, Void, Void> {
        String url = null;
        HashMap<String, String> hm = null;
        ProgressDialog pro;
        String data = null;
        Context cnx;
        JSONArray yemekListesi;

        public jData(String url, HashMap<String, String> hm, Context cnx) {
            this.hm = hm;
            this.url = url;
            this.cnx = cnx;
            pro = ProgressDialog.show(cnx, "Yükleniyor", "Lütfen Bekleyiniz");
        }

        // 1
        @Override
        protected void onPreExecute() {
            super.onPreExecute();//İlk Çalışan Method
        }

        // 2 Dataların yanıtını bekler
        @Override
        protected Void doInBackground(Void... voids) {
            // Jsoup
            // url HashMap
            try {
                data = Jsoup.connect(url).data(hm).timeout(30000).ignoreContentType(true).execute().body();
            } catch (Exception ex) {
                System.err.println("Data Getirme Hatası : " + ex);
            }
            return null;
        }

        // 3 Datalar geldi artık işlemleri yap
        @Override
        protected void onPostExecute(Void aVoid) {
            /** Grafiksel işlemler burda yapılır
             * Yeni Sayfa aç  yükleyiciyi durdur.
             */

            super.onPostExecute(aVoid);
            try {
                JSONObject obj = new JSONObject(data);
                yemekListesi = obj.getJSONArray("orderList").getJSONArray(0);

                if (yemekListesi.length() > 0) {
                    //ürün var
                    adp = new BaseAdapter() {
                        @Override
                        public int getCount() {   // Ne kadar döneceğini burda belirt Eleman sayısı
                            return yemekListesi.length();
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
                        public View getView(int position, View view, ViewGroup parent) {
                            if (view == null) {
                                view = linf.inflate(R.layout.yediklerim_row, null);//inflate view nesnesini row a bağlamak için kullanılıyor
                            }

                            try {
                                ImageView img = view.findViewById(R.id.imgYResim);
                                TextView baslik = view.findViewById(R.id.txtYUrun);
                                TextView fiyat = view.findViewById(R.id.txtYFiyat);
                                TextView adet = view.findViewById(R.id.txtYAdet);
                                TextView tarih = view.findViewById(R.id.txtYeTarih);

                                baslik.setText(yemekListesi.getJSONObject(position).getString("urun_adi"));
                                fiyat.setText(yemekListesi.getJSONObject(position).getString("fiyat")+" TL");
                                adet.setText(yemekListesi.getJSONObject(position).getString("siparis_bilgisi"));
                                String url = yemekListesi.getJSONObject(position).getString("thumb");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                                Date sTarih =format.parse(yemekListesi.getJSONObject(position).getString("eklenme_tarihi"));
                                tarih.setText(format2.format(sTarih));

                                if (url!=null) {
                                    Picasso.with(YenilenYemekler.this)
                                            .load(url)
                                            .fit()
                                            .centerCrop()
                                            .into(img);
                                }


                            } catch (Exception ex) {
                                Log.e("Hata : ", ex.toString());
                            }
                            return view;
                        }
                    };
                    liste.setAdapter(adp);
                } else {
                    //ürün yok
                    Toast.makeText(cnx, "Herhangi bir ürün yok", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            pro.dismiss();
        }

    }//jData Sınıfı Sonu

}