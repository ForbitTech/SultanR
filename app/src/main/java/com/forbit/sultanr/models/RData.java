package com.forbit.sultanr.models;






import com.forbit.sultanr.utils.MyUtil;

import java.io.Serializable;

public class RData implements Serializable {

    private String device_time;
    private double lat;
    private double lng;
    private double speed;
    private String status;

    public RData() {
    }


    public String getDevice_time() {
        return device_time;
    }

    public void setDevice_time(String device_time) {
        this.device_time = device_time;
    }

    public long getServerTime(){
        return MyUtil.getServerTime(device_time);
    }

    public void setServerTime(long time){
        this.device_time = MyUtil.getServerTimeStr(time);
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public int getSpanNo(){
        return Integer.parseInt(device_time.split(" ")[1].split(":")[0]);
    }
}
