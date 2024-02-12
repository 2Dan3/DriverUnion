package com.du.driverunison.model;

import android.graphics.Bitmap;

public class Manufacturer {
    private String name;
    private int logo;

    public Manufacturer() {
    }

    public Manufacturer(String name, int logo) {
        this.name = name;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }
//    public void setName(String name) {
//        this.name = name;
//    }
    public int getLogo() {
        return logo;
    }
//    public void setLogo(Bitmap logo) {
//        this.logo = logo;
//    }
}
