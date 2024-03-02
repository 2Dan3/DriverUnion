package com.du.driverunison.model;

public class CarSafetySpecs {
    private int evalYear;
    private int stars;
    private int adultOccupant;
    private int childOccupant;
    private int vulnerableRoadUsers;
    private int safetyAssist;
    public CarSafetySpecs(){}
    public CarSafetySpecs(int evalYear, int stars, int adultOccupant, int childOccupant, int vulnerableRoadUsers, int safetyAssist) {
        this.evalYear = evalYear;
        this.stars = stars;
        this.adultOccupant = adultOccupant;
        this.childOccupant = childOccupant;
        this.vulnerableRoadUsers = vulnerableRoadUsers;
        this.safetyAssist = safetyAssist;
    }
    public int getEvalYear() {
        return evalYear;
    }
    public void setEvalYear(int evalYear) {
        this.evalYear = evalYear;
    }
    public int getStars() {
        return stars;
    }
    public void setStars(int stars) {
        this.stars = stars;
    }
    public int getAdultOccupant() {
        return adultOccupant;
    }
    public void setAdultOccupant(int adultOccupant) {
        this.adultOccupant = adultOccupant;
    }
    public int getChildOccupant() {
        return childOccupant;
    }
    public void setChildOccupant(int childOccupant) {
        this.childOccupant = childOccupant;
    }
    public int getVulnerableRoadUsers() {
        return vulnerableRoadUsers;
    }
    public void setVulnerableRoadUsers(int vulnerableRoadUsers) {
        this.vulnerableRoadUsers = vulnerableRoadUsers;
    }
    public int getSafetyAssist() {
        return safetyAssist;
    }
    public void setSafetyAssist(int safetyAssist) {
        this.safetyAssist = safetyAssist;
    }
}
