package com.forbit.sultanr.models;

import java.io.Serializable;

/**
 * Created by sohel on 5/20/2018.
 */

public class Device implements Serializable {
    private String id;
    private String uid;
    private String driver_name;
    private String driver_phone;
    private String driver_photo;
    private String registration_number;
    private int vehicle_type;
    private String device_sim_number;
    private String center_number;
    private String device_model;
    private double mileage;
    private double congestion_consumption;
    private boolean is_inactive;

    private Geo geo;


    public Device() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_phone() {
        return driver_phone;
    }

    public void setDriver_phone(String driver_phone) {
        this.driver_phone = driver_phone;
    }

    public String getDriver_photo() {
        return driver_photo;
    }

    public void setDriver_photo(String driver_photo) {
        this.driver_photo = driver_photo;
    }

    public int getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(int vehicle_type) {
        this.vehicle_type = vehicle_type;
    }



    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    @Override
    public String toString() {
        return this.registration_number;
    }

    public String getDevice_sim_number() {
        return device_sim_number;
    }

    public void setDevice_sim_number(String device_sim_number) {
        this.device_sim_number = device_sim_number;
    }

    public double getCongestion_consumption() {
        return congestion_consumption;
    }

    public void setCongestion_consumption(double congestion_consumption) {
        this.congestion_consumption = congestion_consumption;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public boolean isIs_inactive() {
        return is_inactive;
    }

    public void setIs_inactive(boolean is_inactive) {
        this.is_inactive = is_inactive;
    }

    public String getCenter_number() {
        return center_number;
    }

    public void setCenter_number(String center_number) {
        this.center_number = center_number;
    }

    public String getDevice_model() {
        return device_model;
    }

    public void setDevice_model(String device_model) {
        this.device_model = device_model;
    }
}
