package com.example.emre.yemek;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.HashMap;

public class KayitActivity extends AppCompatActivity {

    EditText adi, soyadi, telefon, mail, sifre;
    Button btnKayit;
    SharedPreferences sha;
    SharedPreferences.Editor edit;
    DurumProperty drm=new DurumProperty();

    SharedPreferences sha2;
    SharedPreferences.Editor edit2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);
        sha = getSharedPreferences("durum",MODE_PRIVATE);
        edit = sha.edit();
        sha2 = getSharedPreferences("xmlDosyaAdi", MODE_PRIVATE);;
        edit2 = sha2.edit();

        adi = findViewById(R.id.txtAd);
        soyadi = findViewById(R.id.txtSoyad);
        telefon = findViewById(R.id.txtTel);
        mail = findViewById(R.id.txtMail);
        sifre = findViewById(R.id.txtPass);

        btnKayit = findViewById(R.id.btnKayit);

        btnKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("ref", "0c1d6ff9bca3255d3ef5692b276f1c19");
                hm.put("userName", adi.getText().toString());
                hm.put("userSurname", soyadi.getText().toString());
                hm.put("userPhone", telefon.getText().toString());
                hm.put("userMail", mail.getText().toString());
                hm.put("userPass", sifre.getText().toString());
                String url = "http://jsonbulut.com/json/userRegister.php";
                new jData(url, hm, KayitActivity.this).execute();


                drm.setSayac(0);

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
            pro = ProgressDialog.show(cnx, "Yükleniyor", "Lütfen Bekleyin",true);
        }


        //1
        @Override
        protected void onPreExecute() {
            super.onPreExecute();//ilk çalışan metod
        }

        //2-dataların yanıtlarını bekler
        @Override
        protected Void doInBackground(Void... voids) {
            //jsoup
            //url, HashMap
            try {
                data = Jsoup.connect(url).data(hm).ignoreContentType(true).timeout(30000).execute().body();
            } catch (Exception e) {
                System.err.println("Data getirme Hatası: " + e);
            }
            return null;
        }

        //3- datalar geldi artik ıslemleri yap
        @Override
        protected void onPostExecute(Void aVoid) {
            pro.dismiss();
            //grafiklsel işlemler yapılır
            //yeni sayfa aç yükleciyi durdur.
            super.onPostExecute(aVoid);
            try {
                JSONObject obj = new JSONObject(data);
                JSONObject kobj = obj.getJSONArray("user").getJSONObject(0);
                boolean durum = kobj.getBoolean("durum");
                String mesaj = kobj.getString("mesaj");
                if (durum) {
                    String kid = kobj.getString("kullaniciId");
                    Toast.makeText(KayitActivity.this, mesaj + " " + kid, Toast.LENGTH_SHORT).show();
                    edit2.putString("kId", kid);
                    edit2.putString("kAdi", hm.get("userName"));
                    edit2.putString("kSoyadi", hm.get("userSurname"));
                    edit2.putString("kEmail", hm.get("userMail"));
                    edit2.putString("kTelefon", hm.get("userPhone"));
                    edit2.commit();


                    drm.setAd(adi.getText().toString());
                    drm.setId(kid);
                    Gson gson = new Gson();
                    String json = gson.toJson(drm);
                    edit.putString("drm", json);
                    edit.commit();
                } else {
                    Toast.makeText(KayitActivity.this, mesaj, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {

                System.err.println("Json Hata: " + e);

            }
        }
    }
}
