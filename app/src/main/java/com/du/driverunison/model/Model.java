package com.du.driverunison.model;

import com.du.driverunison.R;

public class Model {
    private String manufacturerName;
    private String name;
    private int photo;

    public Model(){}

    public Model(String manufacturerName, String name, int photo) {
        this.manufacturerName = manufacturerName;
        this.name = name;
        this.photo = photo;
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
    public String getManufacturerName() {
        return manufacturerName;
    }
//    public void setManufacturerName(String manufacturerName) {
//        this.manufacturerName = manufacturerName;
//    }
    @Override
    public String toString() {
        return manufacturerName + " " + name + " " + photo;
    }
}
