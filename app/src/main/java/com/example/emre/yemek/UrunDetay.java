package com.example.emre.yemek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class UrunDetay extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageView imgResim;
    TextView txtAciklama;
    TextView txtTutar;
    TextView txtUrunAdi;
    MenuItem mCartIconMenuItem;
    ImageButton mImageBtn;
    TextView mCountTv;
    Button sepetAt;
    DurumProperty obj;
    SharedPreferences sha;
    SharedPreferences.Editor edit;
    public static ArrayList <Urunler> ul=new ArrayList<>();
    public static final String [] sayilar = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};
    ArrayAdapter<String> aa;
    Urunler urun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_detay);

        sha = getSharedPreferences("durum",MODE_PRIVATE);
        edit = sha.edit();
        Gson gson = new Gson();
        String json = sha.getString("drm","");
        obj = gson.fromJson(json, DurumProperty.class);
        Log.e("Json içinde :" , ""+json);

        final Spinner spnAdet =(Spinner)findViewById(R.id.spnAdet);
        spnAdet.setOnItemSelectedListener(this);

        aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sayilar);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAdet.setAdapter(aa);
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spnAdet);

            // Set popupWindow height to 500px
            popupWindow.setHeight(300);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

       urun = (Urunler) getIntent().getExtras().get("urun");
        sepetAt=findViewById(R.id.sepetButon);
        imgResim = findViewById(R.id.imgUrunDetayResim);
        txtAciklama=findViewById(R.id.txtUrunDetayAciklama);
        txtTutar=findViewById(R.id.txtUrunFiyat);
        txtUrunAdi=findViewById(R.id.txtUrunAdi);
        txtAciklama.setText(urun.getDescription());
        txtTutar.setText(""+urun.getPrice());
        txtUrunAdi.setText(urun.getProductName());
        //resim
        boolean isResim = Boolean.valueOf(urun.getImage());
        if (isResim) {
            String url = urun.getImagenormal();
            Picasso.with(UrunDetay.this)
                    .load(url)
                    .fit().centerCrop()
                    .into(imgResim);
        }
          sepetAt.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if (obj.getSayac()==null){
                      obj.setSayac(0);
                  }
                  if(obj.getUrunAlinan()!=null) {
                      ul = obj.getUrunAlinan();
                  }
                  ul.add(urun);
                  obj.setSayac(obj.getSayac()+1);
                  obj.setUrunAlinan(ul);
                  obj.setFiyat(obj.getFiyat()+urun.getPrice());
                  Gson gson = new Gson();
                  String json = gson.toJson(obj);
                  edit.putString("drm", json);
                  edit.commit();
                  Intent i = new Intent(UrunDetay.this,MainActivity.class);
                  startActivity(i);

              }
          });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, aa.getItem(i) , Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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


            if (obj.getSayac()==null){
                obj.setSayac(0);
            }

            mCountTv.setText(obj.getSayac()+"");

            mImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(UrunDetay.this,Siparis.class);
                    startActivity(i);
                }
            });

        }

        return true;
    }

}
