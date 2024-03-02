package com.du.driverunison.model;

public class Model {
    private String manufacturerName;
    private String name;
    public Model(){}
    public Model(String manufacturerName, String name) {
        this.manufacturerName = manufacturerName;
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String getManufacturerName() {
        return manufacturerName;
    }
}
