package com.example.emre.yemek;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener {
    private SliderLayout sliderKampanya;
    ArrayList<Urunler> lstUrunler = new ArrayList<Urunler>();
    ListView liste;
    BaseAdapter adp;
    LayoutInflater linf;
    JSONArray larr;
    MenuItem mCartIconMenuItem;
    ImageButton mImageBtn;
    TextView mCountTv;
    Button btn;
    DurumProperty drm = new DurumProperty();
    SharedPreferences sha;
    SharedPreferences.Editor edit;


    TextView txtUserName;
    TextView txtUserEmail;

    SharedPreferences sha2;
    SharedPreferences.Editor edit2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sha = getSharedPreferences("durum", MODE_PRIVATE);
        edit = sha.edit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        sha2 = getSharedPreferences("xmlDosyaAdi", MODE_PRIVATE);
        ;
        edit2 = sha2.edit();
        String denetim = sha2.getString("kId", "");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        txtUserName = navigationView.getHeaderView(0).findViewById(R.id.txtUserName);
        txtUserEmail = navigationView.getHeaderView(0).findViewById(R.id.txtUserMail);
        if (!denetim.equals("")) {
            navigationView.inflateMenu(R.menu.activiy_main_kullanici);
            txtUserName.setText(sha2.getString("kAdi", "") + " " + sha2.getString("kSoyadi", ""));
            txtUserEmail.setText(sha2.getString("kEmail", ""));
        } else {
            navigationView.inflateMenu(R.menu.activity_main_drawer);
        }

        linf = LayoutInflater.from(this);
        sliderKampanya = (SliderLayout) findViewById(R.id.slider);
        liste = findViewById(R.id.lstYemek);
        String url = "http://jsonbulut.com/json/product.php";
        HashMap<String, String> hm = new HashMap<>();
        hm.put("ref", "0c1d6ff9bca3255d3ef5692b276f1c19");
        hm.put("start", "0");
        hm.put("count", "100");
        hm.put("order", "asc");
        new jData(url, hm, this).execute();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        ///
        getMenuInflater().inflate(R.menu.main, menu);
        mCartIconMenuItem = menu.findItem(R.id.cart_count_menu_item);

        View actionView = mCartIconMenuItem.getActionView();

        if (actionView != null) {

            mImageBtn = actionView.findViewById(R.id.image_btn_layout);
            mCountTv = actionView.findViewById(R.id.count_tv);

            Map<String, ?> all = sha.getAll();
            Set<String> keys = all.keySet();

            for (String i : keys) {

                if (i.equals("drm")) {
                    Gson gson = new Gson();
                    String json = sha.getString("drm", "");
                    drm = gson.fromJson(json, DurumProperty.class);
                    if (drm.getSayac() == null) {
                        drm.setSayac(0);
                    }
                }
            }

            if (drm.getSayac() == null) {
                drm.setSayac(0);
            }
            mCountTv.setText(drm.getSayac() + "");

            mImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, Siparis.class);
                    startActivity(i);

                }
            });

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_register) {
            Intent i = new Intent(MainActivity.this, KayitActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_login) {
            Intent i = new Intent(MainActivity.this, GirisActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_basket) {
            Intent i = new Intent(MainActivity.this, Siparis.class);
            startActivity(i);
        } else if (id == R.id.nav_paid) {
            Intent i = new Intent(MainActivity.this, YenilenYemekler.class);
            startActivity(i);
        } else if (id == R.id.nav_logout) {

            boolean silDurum = edit2.remove("kId").commit();
            if (silDurum) {
                edit2.clear().commit();
                Toast.makeText(this, "Çıkış Yapıldı", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                finish();
                startActivity(i);

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                boolean durum = obj.getJSONArray("Products").getJSONObject(0).getBoolean("durum");
                String mesaj = obj.getJSONArray("Products").getJSONObject(0).getString("mesaj");
                if (durum) {
                    yemekListesi = obj.getJSONArray("Products").getJSONObject(0).getJSONArray("bilgiler");
                    if (yemekListesi.length() > 0) {
                        //ürün var
                        dataDoldur();
                    } else {
                        //ürün yok
                        Toast.makeText(cnx, "Herhangi bir ürün yok", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            pro.dismiss();
        }


        public void dataDoldur() {
            for (int i = 1; i < yemekListesi.length(); i++) {
                Urunler urun = new Urunler();
                try {
                    urun.setProductId(yemekListesi.getJSONObject(i).getString("productId"));
                    urun.setProductName(yemekListesi.getJSONObject(i).getString("productName"));
                    urun.setBrief(yemekListesi.getJSONObject(i).getString("brief"));
                    urun.setPrice(yemekListesi.getJSONObject(i).getDouble("price"));
                    urun.setDescription(yemekListesi.getJSONObject(i).getString("description"));
                    JSONArray kategori = yemekListesi.getJSONObject(i).getJSONArray("categories");
                    if (kategori.length() > 0) {
                        urun.setCategoryId(kategori.getJSONObject(0).getString("categoryId"));
                        urun.setCategoryName(kategori.getJSONObject(0).getString("categoryName"));
                    }

                    urun.setImage(yemekListesi.getJSONObject(i).getString("image"));
                    if (Boolean.valueOf(urun.getImage())) {
                        JSONArray urunImages = yemekListesi.getJSONObject(i).getJSONArray("images");
                        urun.setImagenormal(urunImages.getJSONObject(0).getString("normal"));
                        urun.setImagethumb(urunImages.getJSONObject(0).getString("thumb"));
                    }

                    lstUrunler.add(urun);

                } catch (JSONException e) {
                    Log.e("Hata", "Ürün Bilgilerini Alırken Hata oluştu" + e);
                }
            }
            sliderDoldur();
            listeDoldur();
        }


    }//jData Sınıfı Sonu

    HashMap<String, Urunler> sliderUrunleri = new HashMap<>();

    public void sliderDoldur() {

        LinkedHashMap<String, String> urun_Resimleri = new LinkedHashMap<String, String>();

        for (int i = 0; i < lstUrunler.size(); i++) {
            if (lstUrunler.get(i).getCategoryName().equalsIgnoreCase("Eko Menüler")) {
                String sliderYazi = lstUrunler.get(i).getProductName() + " " + lstUrunler.get(i).getPrice() + " TL";
                urun_Resimleri.put(sliderYazi,
                        lstUrunler.get(i).getImagenormal());
                sliderUrunleri.put(sliderYazi, lstUrunler.get(i));
            }

        }
        for (String name : urun_Resimleri.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(urun_Resimleri.get(name))
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);
            sliderKampanya.setPresetTransformer("Tablet");
            sliderKampanya.addSlider(textSliderView);
        }

    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        sliderKampanya.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Intent i = new Intent(MainActivity.this, UrunDetay.class);
        i.putExtra("urun", sliderUrunleri.get(slider.getBundle().get("extra")));
        startActivity(i);

    }


    public void listeDoldur() {
        adp = new BaseAdapter() {
            @Override
            public int getCount() {   // Ne kadar döneceğini burda belirt Eleman sayısı
                return lstUrunler.size();
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
                    view = linf.inflate(R.layout.listsatirlari, null);//inflate view nesnesini row a bağlamak için kullanılıyor
                }

                try {
                    ImageView img = view.findViewById(R.id.imgLstResim);
                    TextView baslik = view.findViewById(R.id.txtLstUrunBilgisi);
                    TextView fiyat = view.findViewById(R.id.txtLstUrunFiyati);

                    Urunler urun = lstUrunler.get(position);

                    baslik.setText(urun.getProductName());
                    fiyat.setText(urun.getPrice() + " TL");

                    //resim
                    boolean isResim = Boolean.valueOf(urun.getImage());
                    if (isResim) {
                        String url = urun.getImagethumb();
                        Picasso.with(MainActivity.this)
                                .load(url)
                                .resize(80, 80)
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
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent i = new Intent(MainActivity.this, UrunDetay.class);
                    i.putExtra("urun", lstUrunler.get(position));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
