package com.example.mohamed.mypharmapp.Adapter;

/**
 * Created by Mohamed on 14/12/2016.
 */

public class Pharmacy {

    private long id;
    private String name;
    private String adress;
    private boolean openNow;
    private String openingHours;
    private String phone;
    private Float lat;
    private Float lng;

    public Pharmacy(long id, String name, String adress, String phone, boolean openNow, String openingHours, Float lat, Float lng){
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.phone = phone;
        this.openNow = openNow;
        this.openingHours = openingHours;
        this.lat = lat;
        this.lng = lng;
    }

    public Pharmacy(String name, String adress, String phone, boolean openNow, String openingHours, Float lat, Float lng){
        this.name = name;
        this.adress = adress;
        this.phone = phone;
        this.openNow = openNow;
        this.openingHours = openingHours;
        this.phone = phone;
        this.lat = lat;
        this.lng = lng;

    }
    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }
}
