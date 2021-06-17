package com.forbit.sultanr.models;

import java.io.Serializable;

public class Geo implements Serializable {

    private String acc;
    private double lat;
    private double lng;
    private double speed;
    private String fuel_line;
    private String voltage_level;
    private String charging;




    public Geo() {
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getFuel_line() {
        return fuel_line;
    }

    public void setFuel_line(String fuel_line) {
        this.fuel_line = fuel_line;
    }

    public String getVoltage_level() {
        return voltage_level;
    }

    public void setVoltage_level(String voltage_level) {
        this.voltage_level = voltage_level;
    }

    public String getCharging() {
        return charging;
    }

    public void setCharging(String charging) {
        this.charging = charging;
    }
}
