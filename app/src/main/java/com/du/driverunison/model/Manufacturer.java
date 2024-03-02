package com.du.driverunison.model;

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
    public int getLogo() {
        return logo;
    }
}
