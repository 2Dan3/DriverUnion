package com.du.driverunison.model;

import com.du.driverunison.R;

public class Motorization {
    private String name;
    private int photo;
    public Motorization(){}
    public Motorization(String name, int photo) {
        this.name = name;
        this.photo = photo;
    }
    public Motorization(String name, String layout) {
        this.name = name;
        //    todo temporary predefined custom engine layouts photo display
        switch (layout){
            case "i3":
                this.photo = R.mipmap.engine_i3;
                break;
            case "i4":
                this.photo = R.mipmap.engine_i4;
                break;
            case "b4":
                this.photo = R.mipmap.engine_b4;
                break;
            case "i5":
                this.photo = R.mipmap.engine_i5;
                break;
            case "i6":
                this.photo = R.mipmap.engine_i6;
                break;
            case "v6":
                this.photo = R.mipmap.engine_v6;
                break;
            case "v8":
                this.photo = R.mipmap.engine_v8;
                break;
            case "v10":
                this.photo = R.mipmap.engine_v10;
                break;
            case "v12":
                this.photo = R.mipmap.engine_v12;
                break;
            case "w16":
                this.photo = R.mipmap.engine_w16;
                break;
            default:
//              todo change default img to : 1) engine block generic pic or 2) pic n/a
                this.photo = R.mipmap.engine_i4;
        }
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getPhoto() {
        return photo;
    }
    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
