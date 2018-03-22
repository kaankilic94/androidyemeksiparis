package com.example.emre.yemek;

import java.util.ArrayList;

/**
 * Created by lenovo on 14.2.2018.
 */

public class DurumProperty {
    private String id;//Kullanici id
    private String ad;//Kullanici ad
    private double fiyat = 0;
    private ArrayList<Urunler> urunAlinan;
    private Integer sayac;

    public Integer getSayac() {
        return sayac;
    }

    public void setSayac(Integer sayac) {
        this.sayac = sayac;
    }

    public ArrayList<Urunler> getUrunAlinan() {
        return urunAlinan;
    }

    public void setUrunAlinan(ArrayList<Urunler> urunAlinan) {
        this.urunAlinan = urunAlinan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public double getFiyat() {
        return fiyat;
    }

    public void setFiyat(double fiyat) {
        this.fiyat = fiyat;
    }

}
