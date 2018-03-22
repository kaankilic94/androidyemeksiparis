package com.example.emre.yemek;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class GirisActivity extends AppCompatActivity {

    EditText mail, parola;
    Button btnGiris;

    SharedPreferences sha;
    SharedPreferences.Editor edit;

    SharedPreferences sha2;
    SharedPreferences.Editor edit2;

    DurumProperty drm=new DurumProperty();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        sha = getSharedPreferences("durum",MODE_PRIVATE);
        edit = sha.edit();

        mail = findViewById(R.id.txtgmail);
        parola = findViewById(R.id.txtgParola);

        btnGiris = findViewById(R.id.btnGiris);

        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("ref", "0c1d6ff9bca3255d3ef5692b276f1c19");
                hm.put("userEmail", mail.getText().toString());
                hm.put("userPass", parola.getText().toString());
                hm.put("face", "no");
                String url = "http://jsonbulut.com/json/userLogin.php";
                new jData(url, hm, GirisActivity.this).execute();


            }
        });

        sha2 = getSharedPreferences("xmlDosyaAdi", MODE_PRIVATE);;
        edit2 = sha2.edit();

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
                    JSONObject kullaniciObj = kobj.getJSONObject("bilgiler");

                    String kId = kullaniciObj.getString("userId");
                    String kAdi = kullaniciObj.getString("userName");
                    String kSoyadi = kullaniciObj.getString("userSurname");
                    String kEmail = kullaniciObj.getString("userEmail");
                    String kTelefon = kullaniciObj.getString("userPhone");

                    drm.setAd(kAdi);
                    drm.setId(kId);
                    Gson gson = new Gson();
                    String json = gson.toJson(drm);
                    edit.putString("drm", json);
                    edit.commit();


                    //MainActivity.finish();
                    edit2.putString("kId", kId);
                    edit2.putString("kAdi", kAdi);
                    edit2.putString("kSoyadi", kSoyadi);
                    edit2.putString("kEmail", kEmail);
                    edit2.putString("kTelefon", kTelefon);
                    edit2.commit();
                    Intent i = new Intent(GirisActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(GirisActivity.this, mesaj, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {

                System.err.println("Json Hata: " + e);

            }
        }
    }
}
