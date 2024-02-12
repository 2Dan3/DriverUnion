package com.du.driverunison.model;

public class CarGeneralSpecs {
    public String length;
    public String width;
    public String height;
    public String wheelbase;

    public CarGeneralSpecs(){}

    public CarGeneralSpecs(String length, String width, String height, String wheelbase){
        this.length = length;
        this.height = height;
        this.wheelbase = wheelbase;
        this.width = width;
    }
}
