package com.du.driverunison.model;

import com.du.driverunison.R;

public class Model {
    private String name;
    private int photo;

    public Model(){}

    public Model(String name, int photo) {
        this.name = name;
//        todo : fix when real photo server fetch is implemented
        if ("6".equals(name))
            this.photo = R.mipmap.car_default_filler;
        else if ("M3".equals(name))
            this.photo = R.mipmap.car_default_filler2;
        else
            this.photo = R.mipmap.car_coupe_shape;
    }

    public String getName() {
        return name;
    }
//    public void setName(String name) {
//        this.name = name;
//    }
    public int getPhoto() {
        return photo;
    }
//    public void setPhoto(int photo) {
//        this.photo = photo;
//    }

    @Override
    public String toString() {
        return name + " " + photo;
    }
}
